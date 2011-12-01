/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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
package org.netbeans.modules.refactoring.java;

import com.sun.source.tree.*;
import com.sun.source.util.TreePath;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.netbeans.api.annotations.common.NullUnknown;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.ClassPath.Entry;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.java.queries.SourceForBinaryQuery;
import org.netbeans.api.java.queries.SourceForBinaryQuery.Result;
import org.netbeans.api.java.source.*;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.ui.OpenProjects;
import org.netbeans.modules.refactoring.java.plugins.LocalVarScanner;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.URLMapper;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Utilities;

/**
 *
 * @author Jan Becicka
 */
public class RefactoringUtils {

    private static final String JAVA_MIME_TYPE = "text/x-java"; // NOI18N
    public static volatile boolean cancel = false;
    private static final Logger LOG = Logger.getLogger(RefactoringUtils.class.getName());
    private static final RequestProcessor RP = new RequestProcessor(RefactoringUtils.class.getName(), 1, false, false);

    /**
     * Get all overriding methods for given ExecutableElement
     *
     * @param e
     * @param info
     * @return
     */
    public static Collection<ExecutableElement> getOverridenMethods(ExecutableElement e, CompilationInfo info) {
        return getOverridenMethods(e, info.getElementUtilities().enclosingTypeElement(e), info);
    }

    private static Collection<ExecutableElement> getOverridenMethods(ExecutableElement e, TypeElement parent, CompilationInfo info) {
        ArrayList<ExecutableElement> result = new ArrayList<ExecutableElement>();

        TypeMirror sup = parent.getSuperclass();
        if (sup.getKind() == TypeKind.DECLARED) {
            TypeElement next = (TypeElement) ((DeclaredType) sup).asElement();
            ExecutableElement overriden = getMethod(e, next, info);
            result.addAll(getOverridenMethods(e, next, info));
            if (overriden != null) {
                result.add(overriden);
            }
        }
        for (TypeMirror tm : parent.getInterfaces()) {
            TypeElement next = (TypeElement) ((DeclaredType) tm).asElement();
            ExecutableElement overriden2 = getMethod(e, next, info);
            result.addAll(getOverridenMethods(e, next, info));
            if (overriden2 != null) {
                result.add(overriden2);
            }
        }
        return result;
    }

    private static ExecutableElement getMethod(ExecutableElement method, TypeElement type, CompilationInfo info) {
        for (ExecutableElement met : ElementFilter.methodsIn(type.getEnclosedElements())) {
            if (info.getElements().overrides(method, met, type)) {
                return met;
            }
        }
        return null;
    }

    public static Set<ElementHandle<TypeElement>> getImplementorsAsHandles(ClassIndex idx, ClasspathInfo cpInfo, TypeElement el) {
        cancel = false;
        LinkedList<ElementHandle<TypeElement>> elements = new LinkedList<ElementHandle<TypeElement>>(
                implementorsQuery(idx, ElementHandle.create(el)));
        Set<ElementHandle<TypeElement>> result = new HashSet<ElementHandle<TypeElement>>();
        while (!elements.isEmpty()) {
            if (cancel) {
                cancel = false;
                return Collections.emptySet();
            }
            ElementHandle<TypeElement> next = elements.removeFirst();
            if (!result.add(next)) {
                // it is a duplicate; do not query again
                continue;
            }
            Set<ElementHandle<TypeElement>> foundElements = implementorsQuery(idx, next);
            elements.addAll(foundElements);
        }
        return result;
    }

    private static Set<ElementHandle<TypeElement>> implementorsQuery(ClassIndex idx, ElementHandle<TypeElement> next) {
        return idx.getElements(next,
                EnumSet.of(ClassIndex.SearchKind.IMPLEMENTORS),
                EnumSet.of(ClassIndex.SearchScope.SOURCE, ClassIndex.SearchScope.DEPENDENCIES));
    }

    public static Collection<ExecutableElement> getOverridingMethods(ExecutableElement e, CompilationInfo info) {
        Collection<ExecutableElement> result = new ArrayList();
        TypeElement parentType = (TypeElement) e.getEnclosingElement();
        Set<ElementHandle<TypeElement>> subTypes = getImplementorsAsHandles(info.getClasspathInfo().getClassIndex(), info.getClasspathInfo(), parentType);
        for (ElementHandle<TypeElement> subTypeHandle : subTypes) {
            TypeElement type = subTypeHandle.resolve(info);
            if (type == null) {
                // #120577: log info to find out what is going wrong
                FileObject file = SourceUtils.getFile(subTypeHandle, info.getClasspathInfo());
                if (file == null) {
                    //Deleted file
                    continue;
                } else {
                    throw new NullPointerException("#120577: Cannot resolve " + subTypeHandle + "; file: " + file);
                }
            }
            for (ExecutableElement method : ElementFilter.methodsIn(type.getEnclosedElements())) {
                if (info.getElements().overrides(method, e, type)) {
                    result.add(method);
                }
            }
        }
        return result;
    }

    /**
     *
     * @param f
     * @return true if f is java
     */
    public static boolean isJavaFile(FileObject f) {
        return JAVA_MIME_TYPE.equals(FileUtil.getMIMEType(f, JAVA_MIME_TYPE));
    }

    /**
     * @param element
     * @param info
     * @return true if given element comes from library
     */
    public static boolean isFromLibrary(Element element, ClasspathInfo info) {
        FileObject file = SourceUtils.getFile(element, info);
        if (file == null) {
            //no source for given element. Element is from library
            return true;
        }
        return FileUtil.getArchiveFile(file) != null;
    }

    /**
     * is given name valid package name
     *
     * @param name
     * @return
     */
    public static boolean isValidPackageName(String name) {
        if (name.endsWith(".")) //NOI18N
        {
            return false;
        }
        if (name.startsWith(".")) //NOI18N
        {
            return false;
        }
        if (name.contains("..")) //NOI18N
        {
            return false;
        }
        StringTokenizer tokenizer = new StringTokenizer(name, "."); // NOI18N
        while (tokenizer.hasMoreTokens()) {
            if (!Utilities.isJavaIdentifier(tokenizer.nextToken())) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param f
     * @return true if given file is in open project
     */
    public static boolean isFileInOpenProject(FileObject file) {
        assert file != null;
        Project p = FileOwnerQuery.getOwner(file);
        if (p == null) {
            return false;
        }
        return isOpenProject(p);
    }

    /**
     * Is given file on any source classpath?
     *
     * @param fo
     * @return
     */
    public static boolean isOnSourceClasspath(FileObject fo) {
        Project pr = FileOwnerQuery.getOwner(fo);
        if (pr == null) {
            return false;
        }

        //workaround for 143542
        for (String type : new String[]{JavaProjectConstants.SOURCES_TYPE_JAVA, JavaProjectConstants.SOURCES_TYPE_RESOURCES}) {
            for (SourceGroup sg : ProjectUtils.getSources(pr).getSourceGroups(type)) {
                if (fo == sg.getRootFolder() || (FileUtil.isParentOf(sg.getRootFolder(), fo) && sg.contains(fo))) {
                    return ClassPath.getClassPath(fo, ClassPath.SOURCE) != null;
                }
            }
        }
        return false;
        //end of workaround
        //return ClassPath.getClassPath(fo, ClassPath.SOURCE)!=null;
    }

    /**
     * Is given file a root of source classpath?
     *
     * @param fo
     * @return
     */
    public static boolean isClasspathRoot(FileObject fo) {
        ClassPath cp = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        return cp != null ? fo.equals(cp.findOwnerRoot(fo)) : false;
    }

    /**
     * Is the given file "java" && in open projects && on source classpath?
     *
     * @param file
     * @return
     */
    public static boolean isRefactorable(FileObject file) {
        return file != null && isJavaFile(file) && isFileInOpenProject(file) && isOnSourceClasspath(file);
    }

    /**
     * returns package name for given folder. Folder must be on source classpath
     *
     * @param folder
     * @return
     */
    public static String getPackageName(FileObject folder) {
        assert folder.isFolder() : "argument must be folder";
        ClassPath cp = ClassPath.getClassPath(folder, ClassPath.SOURCE);
        if (cp == null) {
            // see http://www.netbeans.org/issues/show_bug.cgi?id=159228
            throw new IllegalStateException(String.format("No classpath for %s.", folder.getPath())); // NOI18N
        }
        return cp.getResourceName(folder, '.', false);
    }

    /**
     * get package name for given CompilationUnitTree
     *
     * @param unit
     * @return
     */
    public static String getPackageName(CompilationUnitTree unit) {
        assert unit != null;
        ExpressionTree name = unit.getPackageName();
        if (name == null) {
            //default package
            return "";
        }
        return name.toString();
    }

    /**
     * get package name for given url.
     *
     * @param url
     * @return
     */
    public static String getPackageName(URL url) {
        File f = null;
        try {
            f = FileUtil.normalizeFile(new File(url.toURI()));
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        String suffix = "";

        do {
            FileObject fo = FileUtil.toFileObject(f);
            if (fo != null) {
                if ("".equals(suffix)) {
                    return getPackageName(fo);
                }
                String prefix = getPackageName(fo);
                return prefix + ("".equals(prefix) ? "" : ".") + suffix; // NOI18N
            }
            if (!"".equals(suffix)) {
                suffix = "." + suffix; // NOI18N
            }
            suffix = f.getPath().substring(f.getPath().lastIndexOf(File.separatorChar) + 1) + suffix; // NOI18N
            f = f.getParentFile();
        } while (f != null);
        throw new IllegalArgumentException("Cannot create package name for url " + url); // NOI18N
    }

    /**
     * creates or finds FileObject according to
     *
     * @param url
     * @return FileObject
     */
    public static FileObject getOrCreateFolder(URL url) throws IOException {
        try {
            FileObject result = URLMapper.findFileObject(url);
            if (result != null) {
                return result;
            }
            File f = new File(url.toURI());

            result = FileUtil.createFolder(f);
            return result;
        } catch (URISyntaxException ex) {
            throw (IOException) new IOException().initCause(ex);
        }
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static FileObject getClassPathRoot(URL url) throws IOException {
        FileObject result = URLMapper.findFileObject(url);
        File f;
        try {
            f = result != null ? null : FileUtil.normalizeFile(new File(url.toURI())); //NOI18N
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
        while (result == null) {
            result = FileUtil.toFileObject(f);
            f = f.getParentFile();
        }
        return ClassPath.getClassPath(result, ClassPath.SOURCE).findOwnerRoot(result);
    }

    /**
     * Get all supertypes for given type
     *
     * @param type
     * @param info
     * @return
     */
    public static Collection<TypeElement> getSuperTypes(TypeElement type, CompilationInfo info) {
        Collection<TypeElement> result = new HashSet<TypeElement>();
        LinkedList<TypeElement> l = new LinkedList<TypeElement>();
        l.add(type);
        while (!l.isEmpty()) {
            TypeElement t = l.removeFirst();
            TypeElement superClass = typeToElement(t.getSuperclass(), info);
            if (superClass != null) {
                result.add(superClass);
                l.addLast((TypeElement) superClass);
            }
            Collection<TypeElement> interfaces = typesToElements(t.getInterfaces(), info);
            result.addAll(interfaces);
            l.addAll(interfaces);
        }
        return result;
    }

    /**
     * TODO: should be removed
     *
     * @param handle
     * @return
     * @deprecated
     */
    @Deprecated
    public static Collection<FileObject> getSuperTypesFiles(TreePathHandle handle) {
        try {
            SuperTypesTask ff;
            JavaSource source = JavaSource.forFileObject(handle.getFileObject());
            source.runUserActionTask(ff = new SuperTypesTask(handle), true);
            return ff.getFileObjects();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * get supertypes of given types
     *
     * @param type
     * @param info
     * @param sourceOnly true if only types defined in open project should be
     * searched
     * @return
     */
    public static Collection<TypeElement> getSuperTypes(TypeElement type, CompilationInfo info, boolean sourceOnly) {
        if (!sourceOnly) {
            return getSuperTypes(type, info);
        }
        Collection<TypeElement> result = new HashSet<TypeElement>();
        for (TypeElement el : getSuperTypes(type, info)) {
            FileObject file = SourceUtils.getFile(el, info.getClasspathInfo());
            if (file != null && isFileInOpenProject(file) && !isFromLibrary(el, info.getClasspathInfo())) {
                result.add(el);
            }
        }
        return result;
    }

    public static TypeElement typeToElement(TypeMirror type, CompilationInfo info) {
        return (TypeElement) info.getTypes().asElement(type);
    }

    private static boolean isOpenProject(Project p) {
        return OpenProjects.getDefault().isProjectOpen(p);
    }

    private static Collection<TypeElement> typesToElements(Collection<? extends TypeMirror> types, CompilationInfo info) {
        Collection<TypeElement> result = new HashSet();
        for (TypeMirror tm : types) {
            result.add(typeToElement(tm, info));
        }
        return result;
    }

    public static Collection<FileObject> elementsToFile(Collection<? extends Element> elements, ClasspathInfo cpInfo) {
        Collection<FileObject> result = new HashSet();
        for (Element handle : elements) {
            result.add(SourceUtils.getFile(handle, cpInfo));
        }
        return result;
    }

    public static boolean elementExistsIn(TypeElement target, Element member, CompilationInfo info) {
        for (Element currentMember : target.getEnclosedElements()) {
            if (info.getElements().hides(member, currentMember) || info.getElements().hides(currentMember, member)) {
                return true;
            }
            if (member instanceof ExecutableElement
                    && currentMember instanceof ExecutableElement
                    && (info.getElements().overrides((ExecutableElement) member, (ExecutableElement) currentMember, target)
                    || (info.getElements().overrides((ExecutableElement) currentMember, (ExecutableElement) member, target)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param fqn
     * @param info
     * @return
     */
    public static boolean typeExists(String fqn, CompilationInfo info) {
        return info.getElements().getTypeElement(fqn) != null;
    }

    /**
     * create ClasspathInfo for specified files includes dependencies
     *
     * @param files
     * @return
     */
    public static ClasspathInfo getClasspathInfoFor(FileObject... files) {
        return getClasspathInfoFor(true, files);
    }

    /**
     * create ClasspathInfo for specified files
     *
     * @param dependencies
     * @param files
     * @return
     */
    public static ClasspathInfo getClasspathInfoFor(boolean dependencies, FileObject... files) {
        return getClasspathInfoFor(dependencies, false, files);
    }

    /**
     * create ClasspathInfo for specified files
     *
     * @param dependencies include dependencies
     * @param backSource libraries replaces by sources using
     * SourceForBinaryQuery
     * @param files
     * @return
     */
    public static ClasspathInfo getClasspathInfoFor(boolean dependencies, boolean backSource, FileObject... files) {
        assert files.length > 0;
        Set<URL> dependentRoots = new HashSet();
        for (FileObject fo : files) {
            ClassPath cp = null;
            FileObject ownerRoot = null;
            if (fo != null) {
                cp = ClassPath.getClassPath(fo, ClassPath.SOURCE);
                if (cp != null) {
                    ownerRoot = cp.findOwnerRoot(fo);
                }
            }
            if (cp != null && ownerRoot != null && FileUtil.getArchiveFile(ownerRoot) == null) {
                URL sourceRoot = URLMapper.findURL(ownerRoot, URLMapper.INTERNAL);
                if (dependencies) {
                    dependentRoots.addAll(SourceUtils.getDependentRoots(sourceRoot));
                } else {
                    dependentRoots.add(sourceRoot);
                }
                if (FileOwnerQuery.getOwner(fo) != null) {
                    for (FileObject f : cp.getRoots()) {
                        dependentRoots.add(URLMapper.findURL(f, URLMapper.INTERNAL));
                    }
                }
            } else {
                for (ClassPath scp : GlobalPathRegistry.getDefault().getPaths(ClassPath.SOURCE)) {
                    for (FileObject root : scp.getRoots()) {
                        dependentRoots.add(URLMapper.findURL(root, URLMapper.INTERNAL));
                    }
                }
            }
        }

        if (backSource) {
            for (FileObject file : files) {
                if (file != null) {
                    ClassPath source = ClassPath.getClassPath(file, ClassPath.COMPILE);
                    for (Entry root : source.entries()) {
                        Result r = SourceForBinaryQuery.findSourceRoots(root.getURL());
                        for (FileObject root2 : r.getRoots()) {
                            dependentRoots.add(URLMapper.findURL(root2, URLMapper.INTERNAL));
                        }
                    }
                }
            }
        }

        ClassPath rcp = ClassPathSupport.createClassPath(dependentRoots.toArray(new URL[dependentRoots.size()]));
        ClassPath nullPath = ClassPathSupport.createClassPath(new FileObject[0]);
        ClassPath boot = files[0] != null ? ClassPath.getClassPath(files[0], ClassPath.BOOT) : nullPath;
        ClassPath compile = files[0] != null ? ClassPath.getClassPath(files[0], ClassPath.COMPILE) : nullPath;
        //When file[0] is a class file, there is no compile cp but execute cp
        //try to get it
        if (compile == null) {
            compile = ClassPath.getClassPath(files[0], ClassPath.EXECUTE);
        }
        //If no cp found at all log the file and use nullPath since the ClasspathInfo.create
        //doesn't accept null compile or boot cp.
        if (compile == null) {
            LOG.warning("No classpath for: " + FileUtil.getFileDisplayName(files[0]) + " " + FileOwnerQuery.getOwner(files[0]));
            compile = nullPath;
        }
        ClasspathInfo cpInfo = ClasspathInfo.create(boot, compile, rcp);
        return cpInfo;
    }


    /**
     * @param handle
     */
    public static FileObject getFileObject(TreePathHandle handle) {
       ClasspathInfo info = getClasspathInfoFor(false, handle.getFileObject()); 
       return SourceUtils.getFile(handle.getElementHandle(), info);
    }    
    /**
     * create ClasspathInfo for specified handles
     *
     * @param handles
     * @return
     */
    public static ClasspathInfo getClasspathInfoFor(TreePathHandle... handles) {
        FileObject[] result = new FileObject[handles.length];
        int i = 0;
        for (TreePathHandle handle : handles) {
            FileObject fo = getFileObject(handle);
            if (i == 0 && fo == null) {
                result = new FileObject[handles.length + 1];
                result[i++] = handle.getFileObject();
            }
            result[i++] = fo;
        }
        return getClasspathInfoFor(result);
    }

    /**
     * Finds type parameters from
     * <code>typeArgs</code> list that are referenced by
     * <code>tm</code> type.
     *
     * @param utils compilation type utils
     * @param typeArgs modifiable list of type parameters to search; found types
     * will be removed (performance reasons).
     * @param result modifiable list that will contain referenced type
     * parameters
     * @param tm parametrized type to analyze
     */
    public static void findUsedGenericTypes(Types utils, List<TypeMirror> typeArgs, Set<TypeMirror> result, TypeMirror tm) {
        if (typeArgs.isEmpty()) {
            return;
        } else if (tm.getKind() == TypeKind.TYPEVAR) {
            TypeVariable type = (TypeVariable) tm;
            TypeMirror low = type.getLowerBound();
            if (low != null && low.getKind() != TypeKind.NULL) {
                findUsedGenericTypes(utils, typeArgs, result, low);
            }
            TypeMirror up = type.getUpperBound();
            if (up != null) {
                findUsedGenericTypes(utils, typeArgs, result, up);
            }
            int index = findTypeIndex(utils, typeArgs, type);
            if (index >= 0) {
                result.add(typeArgs.get(index));
            }
        } else if (tm.getKind() == TypeKind.DECLARED) {
            DeclaredType type = (DeclaredType) tm;
            for (TypeMirror tp : type.getTypeArguments()) {
                findUsedGenericTypes(utils, typeArgs, result, tp);
            }
        } else if (tm.getKind() == TypeKind.WILDCARD) {
            WildcardType type = (WildcardType) tm;
            TypeMirror ex = type.getExtendsBound();
            if (ex != null) {
                findUsedGenericTypes(utils, typeArgs, result, ex);
            }
            TypeMirror su = type.getSuperBound();
            if (su != null) {
                findUsedGenericTypes(utils, typeArgs, result, su);
            }
        }
    }

    private static int findTypeIndex(Types utils, List<TypeMirror> typeArgs, TypeMirror type) {
        int i = -1;
        for (TypeMirror typeArg : typeArgs) {
            i++;
            if (utils.isSameType(type, typeArg)) {
                return i;
            }
        }
        return -1;
    }

    public static List<TypeMirror> filterTypes(List<TypeMirror> source, Set<TypeMirror> used) {
        List<TypeMirror> result = new ArrayList<TypeMirror>(source.size());

        for (TypeMirror tm : source) {
            if (used.contains(tm)) {
                result.add(tm);
            }
        }

        return result;
    }

    /**
     * translates list of elements to list of types
     *
     * @param typeParams elements
     * @return types
     */
    public static List<TypeMirror> resolveTypeParamsAsTypes(List<? extends Element> typeParams) {
        if (typeParams.isEmpty()) {
            return Collections.<TypeMirror>emptyList();
        }
        List<TypeMirror> typeArgs = new ArrayList<TypeMirror>(typeParams.size());
        for (Element elm : typeParams) {
            typeArgs.add(elm.asType());
        }
        return typeArgs;
    }

    /**
     * finds the nearest enclosing ClassTree on
     * <code>path</code> that is class or interface or enum or annotation type
     * and is or is not annonymous. In case no ClassTree is found the first top
     * level ClassTree is returned.
     *
     * Especially useful for selecting proper tree to refactor.
     *
     * @param javac javac
     * @param path path to search
     * @param isClass stop on class
     * @param isInterface stop on interface
     * @param isEnum stop on enum
     * @param isAnnotation stop on annotation type
     * @param isAnonymous check if class or interface is annonymous
     * @return path to the enclosing ClassTree
     */
    public static @NullUnknown
    TreePath findEnclosingClass(CompilationInfo javac, TreePath path, boolean isClass, boolean isInterface, boolean isEnum, boolean isAnnotation, boolean isAnonymous) {
        Tree selectedTree = path.getLeaf();
        TreeUtilities utils = javac.getTreeUtilities();
        while (true) {
            if (TreeUtilities.CLASS_TREE_KINDS.contains(selectedTree.getKind())) {
                ClassTree classTree = (ClassTree) selectedTree;
                if (isEnum && utils.isEnum(classTree)
                        || isInterface && utils.isInterface(classTree)
                        || isAnnotation && utils.isAnnotation(classTree)
                        || isClass && !(utils.isInterface(classTree) || utils.isEnum(classTree) || utils.isAnnotation(classTree))) {

                    Tree.Kind parentKind = path.getParentPath().getLeaf().getKind();
                    if (isAnonymous || Tree.Kind.NEW_CLASS != parentKind) {
                        break;
                    }
                }
            }

            path = path.getParentPath();
            if (path == null) {
                List<? extends Tree> typeDecls = javac.getCompilationUnit().getTypeDecls();
                if (typeDecls.isEmpty()) {
                    return null;
                }
                selectedTree = typeDecls.get(0);
                if (selectedTree.getKind().asInterface() == ClassTree.class) {
                    return javac.getTrees().getPath(javac.getCompilationUnit(), selectedTree);
                } else {
                    return null;
                }
            }
            selectedTree = path.getLeaf();
        }
        return path;
    }

    /**
     * Copies javadoc from
     * <code>elm</code> to newly created
     * <code>tree</code>.
     *
     * @param elm element containing some javadoc
     * @param tree newly created tree where the javadoc should be copied to
     * @param wc working copy where the tree belongs to
     */
    public static void copyJavadoc(Element elm, Tree tree, WorkingCopy wc) {
        TreeMaker make = wc.getTreeMaker();
        String jdtxt = wc.getElements().getDocComment(elm);
        if (jdtxt != null) {
            make.addComment(tree, Comment.create(Comment.Style.JAVADOC, -1, -1, -1, jdtxt), true);
        }
    }

    private static class SuperTypesTask implements CancellableTask<CompilationController> {

        private Collection<FileObject> files;
        TreePathHandle handle;

        SuperTypesTask(TreePathHandle handle) {
            this.handle = handle;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void run(CompilationController cc) {
            try {
                cc.toPhase(JavaSource.Phase.RESOLVED);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            Element el = handle.resolveElement(cc);
            files = elementsToFile(getSuperTypes((TypeElement) el, cc, true), cc.getClasspathInfo());
        }

        public Collection<FileObject> getFileObjects() {
            return files;
        }
    }

    /**
     * This is a helper method to provide support for delaying invocations of
     * actions depending on java model. See <a
     * href="http://java.netbeans.org/ui/waitscanfinished.html">UI
     * Specification</a>. <br>Behavior of this method is following:<br> If
     * classpath scanning is not in progress, runnable's run() is called. <br>
     * If classpath scanning is in progress, modal cancellable notification
     * dialog with specified tile is opened. </ul> As soon as classpath scanning
     * finishes, this dialog is closed and runnable's run() is called. This
     * method must be called in AWT EventQueue. Runnable is performed in AWT
     * thread.
     *
     * @param runnable Runnable instance which will be called.
     * @param actionName Title of wait dialog.
     * @return true action was cancelled <br> false action was performed
     */
    public static boolean invokeAfterScanFinished(final Runnable runnable, final String actionName) {
        assert SwingUtilities.isEventDispatchThread();
        if (SourceUtils.isScanInProgress()) {
            final ActionPerformer ap = new ActionPerformer(runnable);
            ActionListener listener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ap.cancel();
                    waitTask.cancel();
                }
            };
            JLabel label = new JLabel(getString("MSG_WaitScan"), javax.swing.UIManager.getIcon("OptionPane.informationIcon"), SwingConstants.LEFT);
            label.setBorder(new EmptyBorder(12, 12, 11, 11));
            DialogDescriptor dd = new DialogDescriptor(label, actionName, true, new Object[]{getString("LBL_CancelAction", new Object[]{actionName})}, null, 0, null, listener);
            waitDialog = DialogDisplayer.getDefault().createDialog(dd);
            waitDialog.pack();
            //100ms is workaround for 127536
            waitTask = RP.post(ap, 100);
            waitDialog.setVisible(true);
            waitTask = null;
            waitDialog = null;
            return ap.hasBeenCancelled();
        } else {
            runnable.run();
            return false;
        }
    }
    private static Dialog waitDialog = null;
    private static RequestProcessor.Task waitTask = null;

    private static String getString(String key) {
        return NbBundle.getMessage(RefactoringUtils.class, key);
    }

    private static String getString(String key, Object values) {
        return new MessageFormat(getString(key)).format(values);
    }

    private static class ActionPerformer implements Runnable {

        private Runnable action;
        private boolean cancel = false;

        ActionPerformer(Runnable a) {
            this.action = a;
        }

        public boolean hasBeenCancelled() {
            return cancel;
        }

        @Override
        public void run() {
            try {
                SourceUtils.waitScanFinished();
            } catch (InterruptedException ie) {
                Exceptions.printStackTrace(ie);
            }
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (!cancel) {
                        if (waitDialog != null) {
                            waitDialog.setVisible(false);
                            waitDialog.dispose();
                        }
                        action.run();
                    }
                }
            });
        }

        public void cancel() {
            assert SwingUtilities.isEventDispatchThread();
            // check if the scanning did not finish during cancel
            // invocation - in such case do not set cancel to true
            // and do not try to hide waitDialog window
            if (waitDialog != null) {
                cancel = true;
                waitDialog.setVisible(false);
                waitDialog.dispose();
            }
        }
    }

    //XXX: copied from SourceUtils.addImports. Ideally, should be on one place only:
    public static CompilationUnitTree addImports(CompilationUnitTree cut, List<String> toImport, TreeMaker make)
            throws IOException {
        // do not modify the list given by the caller (may be reused or immutable).
        toImport = new ArrayList<String>(toImport);
        Collections.sort(toImport);

        List<ImportTree> imports = new ArrayList<ImportTree>(cut.getImports());
        int currentToImport = toImport.size() - 1;
        int currentExisting = imports.size() - 1;

        while (currentToImport >= 0 && currentExisting >= 0) {
            String currentToImportText = toImport.get(currentToImport);

            while (currentExisting >= 0 && (imports.get(currentExisting).isStatic() || imports.get(currentExisting).getQualifiedIdentifier().toString().compareTo(currentToImportText) > 0)) {
                currentExisting--;
            }

            if (currentExisting >= 0) {
                imports.add(currentExisting + 1, make.Import(make.Identifier(currentToImportText), false));
                currentToImport--;
            }
        }
        // we are at the head of import section and we still have some imports
        // to add, put them to the very beginning
        while (currentToImport >= 0) {
            String importText = toImport.get(currentToImport);
            imports.add(0, make.Import(make.Identifier(importText), false));
            currentToImport--;
        }
        // return a copy of the unit with changed imports section
        return make.CompilationUnit(cut.getPackageAnnotations(), cut.getPackageName(), imports, cut.getTypeDecls(), cut.getSourceFile());
    }

    /**
     * transforms passed modifiers to abstract form
     *
     * @param make a tree maker
     * @param oldMods modifiers of method or class
     * @return the abstract form of ModifiersTree
     */
    public static ModifiersTree makeAbstract(TreeMaker make, ModifiersTree oldMods) {
        if (oldMods.getFlags().contains(Modifier.ABSTRACT)) {
            return oldMods;
        }
        Set<Modifier> flags = new HashSet<Modifier>(oldMods.getFlags());
        flags.add(Modifier.ABSTRACT);
        flags.remove(Modifier.FINAL);
        return make.Modifiers(flags, oldMods.getAnnotations());
    }

    public static String variableClashes(String newName, TreePath tp, CompilationInfo info) {
        LocalVarScanner lookup = new LocalVarScanner(info, newName);
        TreePath scopeBlok = tp;
        EnumSet set = EnumSet.of(Tree.Kind.BLOCK, Tree.Kind.FOR_LOOP, Tree.Kind.METHOD);
        while (!set.contains(scopeBlok.getLeaf().getKind())) {
            scopeBlok = scopeBlok.getParentPath();
        }
        Element var = info.getTrees().getElement(tp);
        lookup.scan(scopeBlok, var);

        if (lookup.hasRefernces()) {
            return new MessageFormat(getString("MSG_LocVariableClash")).format(
                    new Object[]{newName});
        }

        TreePath temp = tp;
        while (temp != null && temp.getLeaf().getKind() != Tree.Kind.METHOD) {
            Scope scope = info.getTrees().getScope(temp);
            for (Element el : scope.getLocalElements()) {
                if (el.getSimpleName().toString().equals(newName)) {
                    return new MessageFormat(getString("MSG_LocVariableClash")).format(
                            new Object[]{newName});
                }
            }
            temp = temp.getParentPath();
        }
        return null;
    }

    public static boolean isSetter(ExecutableElement el, Element propertyElement) {
        String setterName = getSetterName(propertyElement.getSimpleName().toString());

        return el.getSimpleName().contentEquals(setterName)
                && el.getReturnType().getKind() == TypeKind.VOID
                && el.getParameters().size() == 1
                && el.getParameters().iterator().next().asType().equals(propertyElement.asType());
    }

    public static boolean isGetter(ExecutableElement el, Element propertyElement) {
        String getterName = getGetterName(propertyElement.getSimpleName().toString());
        return el.getSimpleName().contentEquals(getterName)
                && el.getReturnType().equals(propertyElement.asType())
                && el.getParameters().isEmpty();
    }

    public static String getGetterName(String propertyName) {
        return "get" + capitalizeFirstLetter(propertyName); //NOI18N
    }

    public static String getSetterName(String propertyName) {
        return "set" + capitalizeFirstLetter(propertyName); //NOI18N
    }

    /**
     * Utility method capitalizes the first letter of string, used to generate
     * method names for patterns
     *
     * @param str The string for capitalization.
     * @return String with the first letter capitalized.
     */
    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.length() <= 0) {
            return str;
        }

        char chars[] = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static boolean isWeakerAccess(Set<Modifier> modifiers, Set<Modifier> modifiers0) {
        return accessLevel(modifiers) < accessLevel(modifiers0);
    }

    private static int accessLevel(Set<Modifier> modifiers) {
        if (modifiers.contains(Modifier.PRIVATE)) {
            return 0;
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            return 2;
        }
        if (modifiers.contains(Modifier.PUBLIC)) {
            return 3;
        }
        return 1;
    }

    public static String getAccess(Set<Modifier> modifiers) {
        if (modifiers.contains(Modifier.PRIVATE)) {
            return "private"; //NOI18N
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            return "protected"; //NOI18N
        }
        if (modifiers.contains(Modifier.PUBLIC)) {
            return "public"; //NOI18N
        }
        return "<default>"; //NOI18N
    }
}
