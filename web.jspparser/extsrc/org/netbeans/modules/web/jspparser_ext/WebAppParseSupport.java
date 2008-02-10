/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2008 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.netbeans.modules.web.jspparser_ext;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagLibraryInfo;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.ErrorManager;

import org.netbeans.modules.web.jsps.parserapi.JspParserAPI;
import org.netbeans.modules.web.jsps.parserapi.Node;
import org.netbeans.modules.web.jsps.parserapi.PageInfo;


import org.apache.jasper.Options;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.ExtractPageData;
import org.apache.jasper.compiler.GetParseData;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.TldLocationsCache;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.modules.web.jspparser.ContextUtil;
import org.netbeans.modules.web.jspparser.ParserServletContext;
import org.netbeans.modules.web.jspparser.WebAppParseProxy;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.URLMapper;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 * Class that provides JSP parsing support for one web application. It caches
 * some useful data on a per-webapp basis.<br>
 *
 * Among other things, it does the following to correctly manage the development cycle:
 * <ul>
 *   <li>Creates the correct classloader for loading JavaBeans, tag handlers and other classes managed
 *      by the application.</li>
 *   <li>Caches the ServletContext (needed by the parser) corresponding to the application.</li>
 *   <li>Listens on changes in the application and releases caches as needed.</li>
 * </ul>
 * <p>UPDATE
 * <p>
 * Caching was changed - this class listens to the changes on FS and in libraries. The logic should be:
 * <ul>
 *   <li>for tld files: listen to FS changes and if it the change is related to "our" tld file, then release the cache
 *   <li>for web.xml: same as for tld files
 *   <li>for jar files: listen to the class path changes and behave similarly as for web.xml and tld files
 * </ul>
 * <p>
 * The cache behaviour could be probably improved (it means remove, update, add just one item, do not release
 * the whole cache).
 * @author Petr Jiricka, Tomas Mysik
 */
public class WebAppParseSupport implements WebAppParseProxy, PropertyChangeListener {
    
    private static final Logger LOG = Logger.getLogger(WebAppParseSupport.class.getName());
    private static final JspParserAPI.JspOpenInfo DEFAULT_JSP_OPEN_INFO = new JspParserAPI.JspOpenInfo(false, "8859_1");
    private static final Pattern RE_PATTERN_COMMONS_LOGGING = Pattern.compile(".*commons-logging.*\\.jar.*"); // NOI18N
    
    private final JspParserAPI.WebModule wm;
    final FileObject wmRoot;
    private final FileSystemListener fileSystemListener;
    
    OptionsImpl editorOptions;
    OptionsImpl diskOptions;
    ServletContext editorContext;
    ServletContext diskContext;
    private JspRuntimeContext rctxt;
    private URLClassLoader waClassLoader;
    private URLClassLoader waContextClassLoader;
    /** Maps File -> Long, holds timestamps for files used during classloading,
     * namely: all jar files containing classes, and directories+subdirectories containing unpackaged classes. */
    private final HashMap<File, Long> clRootsTimeStamps;
    
    /**
     * The mappings are cashed here.
     */
    private Map<String, String[]> mappings;
    
    /** This is flag, whether the execute and compilation classpath for the web  project is actual.
     *  The flag is set to false, when there is event, which notifies about change in the classpath.
     *  The value is obtained before constructing the cache and classloaders.
     */
    private boolean isClassPathCurrent;
    
    /** This is hashcode of the execution classpath, which is used for building classloader.
     * In checkClassesAreCurrent is used for fast check, whether the classpath was not changed.
     * The main reason is the web freeform, because the web freeform doesn't fire the change property.
     */
    private int lastCheckedClasspath;
    
    /** Creates a new instance of WebAppParseSupport */
    public WebAppParseSupport(JspParserAPI.WebModule wm) {
        this.wm = wm;
        this.wmRoot = wm.getDocumentBase();
        fileSystemListener = new FileSystemListener();
        wm.addPropertyChangeListener(this);
        clRootsTimeStamps = new HashMap<File, Long>();
        reinitOptions();
        // register listener (listen to changes of tld files, web.xml)
        try {
            wm.getDocumentBase().getFileSystem().addFileChangeListener(fileSystemListener);
        } catch (FileStateInvalidException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public JspParserAPI.JspOpenInfo getJspOpenInfo(FileObject jspFile, boolean useEditor
            /*, URLClassLoader waClassLoader*/) {
        // PENDING - do caching for individual JSPs
        //  in fact should not be needed - see FastOpenInfoParser.java
        JspCompilationContext ctxt = createCompilationContext(jspFile, useEditor);
        ExtractPageData epd = new ExtractPageData(ctxt);
        try {
            return new JspParserAPI.JspOpenInfo(epd.isXMLSyntax(), epd.getEncoding());
        } catch (Exception e) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(e.getMessage());
                Exceptions.printStackTrace(e);
            }
        }
        return DEFAULT_JSP_OPEN_INFO;
    }
    
    synchronized void reinitOptions() {
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JSP parser reinitialized for WM " + FileUtil.toFile(wmRoot));
        }
        editorContext = new ParserServletContext(wmRoot, wm, true);
        diskContext = new ParserServletContext(wmRoot, wm, false);
        editorOptions = new OptionsImpl(editorContext);
        diskOptions = new OptionsImpl(diskContext);
        rctxt = null;
        // try null, but test with tag files
        //new JspRuntimeContext(context, options);
        isClassPathCurrent = true;
        createClassLoaders();
    }
    
    private boolean isUnexpectedLibrary(URL url) {
        Matcher m = RE_PATTERN_COMMONS_LOGGING.matcher(url.getFile());
        return m.matches();
    }
    
    private void createClassLoaders() {
        clRootsTimeStamps.clear();
        
        // libraries
        
        // WEB-INF/lib
        // Looking for jars in WEB-INF/lib is mainly for tests. Can a user create a lib dir in the document base
        // and put here a jar?
        
        FileObject webInf = org.netbeans.modules.web.api.webmodule.WebModule.getWebModule(wmRoot).getWebInf();
        Hashtable<URL, URL> tomcatTable = new Hashtable<URL, URL>();
        Hashtable<URL, URL> loadingTable = new Hashtable<URL, URL>();
        FileObject libDir = webInf.getFileObject("lib"); // NOI18N
        URL helpurl;
        
        if (libDir != null) {
            Enumeration<? extends FileObject> libDirKids = libDir.getChildren(false);
            while (libDirKids.hasMoreElements()) {
                FileObject elem = libDirKids.nextElement();
                if (elem.getExt().equals("jar")) { // NOI18N
                    helpurl = findInternalURL(elem);
                    if (!isUnexpectedLibrary(helpurl)) {
                        tomcatTable.put(helpurl, helpurl);
                        loadingTable.put(helpurl, helpurl);
                    }
                }
            }
        }
        
        // issue 54845. On the class loader we must put the java sources as well. It's in the case, when there are a
        // tag hendler, which is added in a tld, which is used in the jsp file.
        ClassPath cp = ClassPath.getClassPath(wmRoot, ClassPath.COMPILE);
        if (cp != null) {
            FileObject[] roots = cp.getRoots();
            for (int i = 0; i < roots.length; i++) {
                helpurl = findInternalURL(roots[i]);
                if (loadingTable.get(helpurl) == null && !isUnexpectedLibrary(helpurl)) {
                    loadingTable.put(helpurl, helpurl);
                    tomcatTable.put(helpurl, findExternalURL(roots[i]));
                }
            }
        }
        // libraries and built classes are on the execution classpath
        cp = ClassPath.getClassPath(wmRoot, ClassPath.EXECUTE);
        // remember the hashCode of this classpath
        lastCheckedClasspath = cp.hashCode();
        
        if (cp != null) {
            FileObject [] roots = cp.getRoots();
            for (int i = 0; i < roots.length; i++){
                helpurl = findInternalURL(roots[i]);
                if (loadingTable.get(helpurl) == null && !isUnexpectedLibrary(helpurl)) {
                    loadingTable.put(helpurl, helpurl);
                    tomcatTable.put(helpurl, findExternalURL(roots[i]));
                }
            }
        }
        FileObject classesDir = webInf.getFileObject("classes");  //NOI18N
        if (classesDir != null && loadingTable.get(helpurl = findInternalURL(classesDir)) == null){
            loadingTable.put(helpurl, helpurl);
            tomcatTable.put(helpurl, helpurl);
        }
        
        URL loadingURLs[] = loadingTable.values().toArray(new URL[0]);
        URL tomcatURLs[] = tomcatTable.values().toArray(new URL[0]);
        
        waClassLoader = new ParserClassLoader(loadingURLs, tomcatURLs, getClass().getClassLoader());
        waContextClassLoader = new ParserClassLoader(loadingURLs, tomcatURLs, getClass().getClassLoader());
        
    }
    
    private URL findInternalURL(FileObject fo) {
        URL url = URLMapper.findURL(fo, URLMapper.INTERNAL);
        return url;
    }
    
    private URL findExternalURL(FileObject fo) {
        // PENDING - URLMapper.EXTERNAL does not seem to be working now, so using this workaround
        File f = FileUtil.toFile(fo);
        if ((f != null)/* && (f.isDirectory())*/) {
            try {
                return f.toURI().toURL();
            } catch (MalformedURLException e) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
            }
        }
        // fallback
        URL u = URLMapper.findURL(fo,  URLMapper.EXTERNAL);
        URL archiveFile = FileUtil.getArchiveFile(u);
        if (archiveFile != null) {
            return archiveFile;
        }
        return u;
    }
    
    private synchronized JspCompilationContext createCompilationContext(FileObject jspFile, boolean useEditor) {
        boolean isTagFile = determineIsTagFile(jspFile);
        String jspUri = getJSPUri(jspFile);
        Options options = useEditor ? editorOptions : diskOptions;
        ServletContext context = useEditor ? editorContext : diskContext;
        JspCompilationContext clctxt = null;
        try {
            if (isTagFile) {
                clctxt = new JspCompilationContext(jspUri, null,  options, context, null, rctxt, null);
            } else {
                clctxt = new JspCompilationContext(jspUri, false,  options, context, null, rctxt);
            }
        } catch (JasperException ex) {
            ErrorManager.getDefault().annotate(ex, "JSP Parser");
        }
        clctxt.setClassLoader(getWAClassLoader());
        return clctxt;
    }
    
    private boolean determineIsTagFile(FileObject fo) {
        if (fo.getExt().startsWith("tag")) { // NOI18N - all tag, tagx and even tagf are considered tag files
            return true;
        }
        if (JspParserAPI.TAG_MIME_TYPE.equals(fo.getMIMEType())) {
            return true;
        }
        return false;
    }
    
    private String getJSPUri(FileObject jsp) {
        return ContextUtil.findRelativeContextPath(wmRoot, jsp);
    }
    
    // from JspCompileUtil
    public JspParserAPI.ParseResult analyzePage(FileObject jspFile, /*String compilationURI, */
            int errorReportingMode) {
        // PENDING - do caching for individual JSPs
        JspCompilationContext ctxt = createCompilationContext(jspFile, true);
        
        return callTomcatParser(jspFile, ctxt, waContextClassLoader, errorReportingMode);
    }
    
    /**
     * Returns the mapping of the 'global' tag library URI to the location (resource
     * path) of the TLD associated with that tag library. The location is
     * returned as a String array:
     *    [0] The location
     *    [1] If the location is a jar file, this is the location of the tld.
     */
    public synchronized Map<String, String[]> getTaglibMap(boolean useEditor) throws IOException {
        // useEditor not needed, both caches are the same
        if (mappings == null) {
            mappings = new  HashMap<String, String[]>();
            fileSystemListener.reinitCaches();
        }
        return new HashMap<String, String[]>(mappings);
    }
    
    private Map<String, String[]> getMappingsByReflection(TldLocationsCache lc) throws IOException {
        if (!isClassPathCurrent) {
            // if the classpath was changed, create new classloaders
            if (!isClassPathCurrent) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("class path has changed");
                }
                reinitOptions();
            }
        }
        return mappings;
    }
    
    
    /**
     * Returns the classloader to be used by the JSP parser.
     * This classloader loads the classes belonging to the application
     * from both expanded directory structures and jar files.
     */
    public URLClassLoader getWAClassLoader() {
        if (!checkClassesAreCurrent()) {
            reinitOptions();
        }
        return waClassLoader;
    }
    
    /**
     * Checks whether the classes used by this web module have not changed since
     * the last time the classloader was initialized.
     * @return true if the classes are still the same (have not changed).
     */
    private boolean checkClassesAreCurrent() {
        if (!isClassPathCurrent) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("checkClassesAreCurrent(): class path has changed");
            }
            return false;
        }
        long timeStamp = 0;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("checkClassesAreCurrent(): check started for WM " + FileUtil.toFile(wmRoot));
            timeStamp = System.currentTimeMillis();
        }
        if (clRootsTimeStamps == null) {
            return false;
        }
        Iterator it = clRootsTimeStamps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next();
            File f = (File)e.getKey();
            if (LOG.isLoggable(Level.FINER)) {
                LOG.finer(" -> checking file " + f);
            }
            if (!f.exists()) {
                return false;
            }
            if (f.lastModified() != ((Long)e.getValue()).longValue()) {
                return false;
            }
        }
        ClassPath cp = ClassPath.getClassPath(wmRoot, ClassPath.EXECUTE);
        // check whether the execution classpath was not changed
        if (lastCheckedClasspath != cp.hashCode()){
            return false;
        }
        // check whether the files on the execution classpath were not changed.
        FileObject[] roots = cp.getRoots();
        FileObject fo;
        File file;
        for (int i = 0 ; i < roots.length; i++){
            URL url = findInternalURL(roots[i]);
            if (!isUnexpectedLibrary(url)) {
                fo = roots[i]; 
                file =  null;
                try {
                    if (roots[i].getURL().getProtocol().equals("jar"))
                        fo = FileUtil.getArchiveFile(roots[i]);
                } catch (FileStateInvalidException ex) {
                    ErrorManager.getDefault().notify(ex);
                }
                if (fo != null)
                    file = FileUtil.toFile(fo);
                if (!clRootsTimeStamps.containsKey(file)){
                    return false;
                }
            }
        }
        if (LOG.isLoggable(Level.FINE)) {
            long timeStamp2 = System.currentTimeMillis();
            LOG.fine("checkClassesAreCurrent(): completed with result 'true', time " + (timeStamp2 - timeStamp));
        }
        return true;
    }
    
    public class RRef {
        JspParserAPI.ParseResult result;
    }
    
    private JspParserAPI.ParseResult callTomcatParser(final FileObject jspFile,
            final JspCompilationContext ctxt, final ClassLoader contextClassLoader, final int errorReportingMode) {
        
        final RRef resultRef = new RRef();
        
        // calling the parser in a new thread, as per the spec, we need to set the context classloader
        // to contain the web application classes. Jasper really relies on this
        Thread compThread = new Thread("JSP Parsing") { // NOI18N
            
            private void setResult(GetParseData gpd){
                PageInfo nbPageInfo = gpd.getNbPageInfo();
                Node.Nodes nbNodes = gpd.getNbNodes();
                Throwable e = gpd.getParseException();
                if (e == null) {
                    resultRef.result = new JspParserAPI.ParseResult(nbPageInfo, nbNodes);
                } else {
                    // the exceptions we may see here:
                    // JasperException - usual
                    // ArrayIndexOutOfBoundsException - see issue 20919
                    // Throwable - see issue 21169, related to Tomcat bug 7124
                    // XXX has to be returned back to track all errors
                    ErrorManager.getDefault().annotate(e, NbBundle.getMessage(WebAppParseSupport.class, "MSG_errorDuringJspParsing"));
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine(e.getMessage());
                        Exceptions.printStackTrace(e);
                    }
                    JspParserAPI.ErrorDescriptor error = constructErrorDescriptor(e, wmRoot, jspFile);
                    resultRef.result = new JspParserAPI.ParseResult(nbPageInfo, nbNodes, new JspParserAPI.ErrorDescriptor[] {error});
                }
            }
            
            public void run() {
                GetParseData gpd = null;
                try {
                    gpd = new GetParseData(ctxt, errorReportingMode);
                    gpd.parse();
                    setResult(gpd);
                    
                } catch (ThreadDeath td) {
                    ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, td);
                    throw td;
                } catch (Throwable t) {
                    if (gpd != null) {
                        setResult(gpd);
                    }
                    else {
                        ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, t);
                    }
                }
            }
        };
        compThread.setContextClassLoader(contextClassLoader);
        compThread.start();
        try {
            compThread.join();
            return resultRef.result;
        } catch (InterruptedException e) {
            JspParserAPI.ErrorDescriptor error = constructErrorDescriptor(e, wmRoot, jspFile);
            return new JspParserAPI.ParseResult(new JspParserAPI.ErrorDescriptor[] {error});
        }
    }
    
    private static JspParserAPI.ErrorDescriptor constructErrorDescriptor(Throwable e, FileObject wmRoot, FileObject jspPage) {
        JspParserAPI.ErrorDescriptor error = null;
        try {
            error = constructJakartaErrorDescriptor(wmRoot, jspPage, e);
        } catch (FileStateInvalidException e2) {
            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e2);
            // do nothing, error will just remain to be null
        } catch (IOException e2) {
            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e2);
            // do nothing, error will just remain to be null
        }
        if (error == null) {
            error = new JspParserAPI.ErrorDescriptor(wmRoot, jspPage, -1, -1,
                    ContextUtil.getThrowableMessage(e, !(e instanceof JasperException)), "");
        }
        return error;
    }
    
    /** Returns an ErrorDescriptor for a compilation error if the throwable was thrown by Jakarta,
     * otherwise returns null. */
    private static JspParserAPI.ErrorDescriptor constructJakartaErrorDescriptor(
            FileObject wmRoot, FileObject jspPage, Throwable ex) throws IOException {
        
        // PENDING: maybe we should check all nested exceptions
        StringBuffer allStack = new StringBuffer();
        Throwable last = ex;
        allStack.append(ContextUtil.getThrowableMessage(ex, true));
        while (ex instanceof JasperException) {
            last = ex;
            ex = ((JasperException)ex).getRootCause();
            if (ex != null) {
                ErrorManager.getDefault().annotate(last, ex);
                allStack.append(ContextUtil.getThrowableMessage(ex, true));
            }
        }
        
        if (ex == null)
            ex = last;
        
/*System.out.println("--------STACK------");
System.out.println(allStack.toString());
System.out.println("--------ENDSTACK------");        */
        // now it can be JasperException, which starts with error location description
        String m1 = ex.getMessage();
        if (m1 == null) return null;
        int lpar = m1.indexOf('(');
        if (lpar == -1) return null;
        int comma = m1.indexOf(',', lpar);
        if (comma == -1) return null;
        int rpar = m1.indexOf(')', comma);
        if (rpar == -1) return null;
        String line = m1.substring(lpar + 1, comma).trim();
        String col = m1.substring(comma + 1, rpar).trim();
        String fileName = m1.substring(0, lpar);
        
        // now cnstruct the FileObject using this file name and the web module root
        File file = FileUtil.toFile(wmRoot);
        FileObject errorFile = jspPage; // a sensible default
        
        fileName = new File(fileName).getCanonicalPath();
        String wmFileName = file.getCanonicalPath();
        if (fileName.startsWith(wmFileName)) {
            String errorRes = fileName.substring(wmFileName.length());
            errorRes = errorRes.replace(File.separatorChar, '/');
            if (errorRes.startsWith("/")) // NOI18N
                errorRes = errorRes.substring(1);
            FileObject errorTemp = wmRoot.getFileObject(errorRes);
            if (errorTemp != null)
                errorFile = errorTemp;
        }
        
        // now construct the ErrorDescriptor
        try {
            String errContextPath = ContextUtil.findRelativeContextPath(wmRoot, errorFile);
            // some information in the message are unnecessary
            int index = m1.indexOf("PWC");  //NOI18N
            String errorMessage;
            if (index > -1) {
                index = m1.indexOf(':', index);
                errorMessage = m1.substring(index + 2).trim();
            }
            else {
                errorMessage = errContextPath + " [" + line + ";" + col + "] " + m1.substring(rpar + 1).trim();
            }
            return new JspParserAPI.ErrorDescriptor(
                    wmRoot, errorFile, Integer.parseInt(line), Integer.parseInt(col),
                    errorMessage, ""); // NOI18N
            // pending - should also include a line of code
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    // IMPLEMENTATION OF PropertyChangeListener
    
    /** Handes the event of property change of libraries used by the web module.
     * Reinitializes the classloader and other stuff.
     */
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if (JspParserAPI.WebModule.PROP_LIBRARIES.equals(propName) ||
                JspParserAPI.WebModule.PROP_PACKAGE_ROOTS.equals(propName)) {
            // the classpath was changed, need to be done reinitOptions()
            isClassPathCurrent = false;
        }
    }
    
    public static class JasperSystemClassLoader extends URLClassLoader{
        private static final java.security.AllPermission ALL_PERM = new java.security.AllPermission();
        
        public JasperSystemClassLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }
        
        protected PermissionCollection getPermissions(CodeSource codesource) {
            PermissionCollection perms = super.getPermissions(codesource);
            perms.add(ALL_PERM);
            return perms;
        }
    }
    
    /** This classloader does some security stuff, but equally importantly, it does one horrible hack,
     * explained in the constructor Javadoc.
     */
    public static class ParserClassLoader extends URLClassLoader {
        
        private static final java.security.AllPermission ALL_PERM = new java.security.AllPermission();
        
        private URL[] tomcatURLs;
        
        /** This constructor and the getURLs() method is one horrible hack. On the one hand, we want to give
         * Tomcat a JarURLConnection when it attempts to load classes from jars, on the other hand
         * we don't want this classloader to load classes using JarURLConnection, as that's buggy.
         * We want it to load classes using internal nb: protocols. So the getURLs() method
         * returns an "external" list of URLs for Tomcat, while internally, the classloader uses
         * an "internal" list of URLs.
         */
        public ParserClassLoader(URL[] classLoadingURLs, URL[] tomcatURLs, ClassLoader parent) {
            super(classLoadingURLs, parent);
            this.tomcatURLs = tomcatURLs;
        }
        
        /** See the constructor for explanation of what thie method does.
         */
        public URL[] getURLs() {
            return tomcatURLs;
        }
        
        protected PermissionCollection getPermissions(CodeSource codesource) {
            PermissionCollection perms = super.getPermissions(codesource);
            perms.add(ALL_PERM);
            return perms;
        }
        
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(super.toString());
            sb.append(", parent : "); // NOI18N
            sb.append(getParent().toString());
            return sb.toString();
        }
        
    }
    
    private static class InitTldLocationCacheThread extends Thread{
        
        private TldLocationsCache cache;
        
        InitTldLocationCacheThread(TldLocationsCache lc){
            super("Init TldLocationCache"); // NOI18N
            cache = lc;
        }
        
        public void run() {
            try {
                Field initialized= TldLocationsCache.class.getDeclaredField("initialized"); // NOI18N
                initialized.setAccessible(true);
                initialized.setBoolean(cache, false);
                cache.getLocation("");
            } catch (JasperException e) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
            } catch (NoSuchFieldException e) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
            } catch (IllegalAccessException e) {
                ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
            }
        }
        
    }
    
    final class FileSystemListener extends FileChangeAdapter {

        @Override
        public void fileChanged(FileEvent fe) {
            processFileChange(fe);
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
            processFileChange(fe);
        }

        @Override
        public void fileDeleted(FileEvent fe) {
            processFileChange(fe);
        }

        @Override
        public void fileRenamed(FileRenameEvent fe) {
            processFileChange(fe);
        }

        private void processFileChange(final FileEvent fe) {
            // check the file type/name/extension and then
            // check if the file belongs to this parse proxy
            String name = fe.getFile().getNameExt();
            String ext = fe.getFile().getExt();
            if (ext.equals("tld") || name.equals("web.xml")) { // NOI18N
                FileObject fo = fe.getFile();
                if (FileUtil.isParentOf(wmRoot, fo)) {
                    // our file => process caches
                    synchronized (WebAppParseSupport.this) {
                        reinitCaches();
                    }
                }
            }
        }
        
        void reinitCaches() {
            assert Thread.holdsLock(WebAppParseSupport.this);
            clearTagLibraryInfoCache();
            reinitTagLibMappings();
        }
        
        private void clearTagLibraryInfoCache() {
            assert Thread.holdsLock(WebAppParseSupport.this);
            // clear the cache of tagLibrary map
            Map<String, TagLibraryInfo> map = (ConcurrentHashMap<String, TagLibraryInfo>) editorContext
                    .getAttribute("com.sun.jsp.taglibraryCache"); // NOI18N
            if (map != null) {
                map.clear();
            }
            map = (ConcurrentHashMap<String, TagLibraryInfo>) diskContext.getAttribute("com.sun.jsp.taglibraryCache"); // NOI18N
            if (map != null) {
                map.clear();
            }
        }
        
        private void reinitTagLibMappings() {
            assert Thread.holdsLock(WebAppParseSupport.this);
            try {
                // editor options
                TldLocationsCache lc = editorOptions.getTldLocationsCache();
                
                Field mappingsField = TldLocationsCache.class.getDeclaredField("mappings"); //NOI18N
                mappingsField.setAccessible(true);
                // Before new parsing, the old mappings in the TldLocationCache has to be cleared. Else there are
                // stored the old mappings.
                Map<String, String[]> tmpMappings = (Map<String, String[]>) mappingsField.get(lc);
                // the mapping doesn't have to be initialized yet
                if(tmpMappings != null) {
                    tmpMappings.clear();
                }

                Thread compThread = new WebAppParseSupport.InitTldLocationCacheThread(lc);
                compThread.setContextClassLoader(waContextClassLoader);
                long start = 0;
                if (LOG.isLoggable(Level.FINE)) {
                    start = System.currentTimeMillis();
                    LOG.fine("InitTldLocationCacheThread start");
                }
                compThread.start();

                try {
                    compThread.join();
                    if (LOG.isLoggable(Level.FINE)) {
                        long end = System.currentTimeMillis();
                        LOG.fine("InitTldLocationCacheThread finished in " + (end - start) + " ms");
                    }
                } catch (java.lang.InterruptedException e){
                    ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
                }

                // obtain the current mappings after parsing
                tmpMappings = (Map<String, String[]>) mappingsField.get(lc);
                
                // disk options
                lc = diskOptions.getTldLocationsCache();
                
                mappingsField = TldLocationsCache.class.getDeclaredField("mappings"); //NOI18N
                mappingsField.setAccessible(true);
                // store the same tld cache into disk options as well
                mappingsField.set(lc, tmpMappings);
                
                // update cache
                mappings.clear();
                mappings.putAll(tmpMappings);
                // cache tld files under WEB-INF directory as well
                mappings.putAll(getImplicitLocation());
            } catch (NoSuchFieldException e) {
                Exceptions.printStackTrace(e);
            } catch (IllegalAccessException e) {
                Exceptions.printStackTrace(e);
            }
        }
        
        /**
         * Returns map with tlds, which doesn't have defined <uri>.
         */
        private Map<String, String[]> getImplicitLocation() {
            assert Thread.holdsLock(WebAppParseSupport.this);
            Map<String, String[]> returnMap = new HashMap<String, String[]>();
            // Obtain all tld files under WEB-INF folder
            FileObject webInf = wm.getWebInf();
            FileObject fo;
            if (webInf != null && webInf.isFolder()) {
                Enumeration<? extends FileObject> en = webInf.getChildren(true);
                while (en.hasMoreElements()) {
                    fo = en.nextElement();
                    if (fo.getExt().equals("tld")) { // NOI18N
                        String path;
                        if (ContextUtil.isInSubTree(wmRoot, fo)) {
                            path = "/" + ContextUtil.findRelativePath(wmRoot, fo); // NOI18N
                        } else {
                            // the web-inf folder is mapped somewhere else
                            path = "/WEB-INF/" + ContextUtil.findRelativePath(webInf, fo); //NOI18N
                        }
                        returnMap.put(path, new String[] {path, null});
                    }
                }
            }
            return returnMap;
        }
    }
}
