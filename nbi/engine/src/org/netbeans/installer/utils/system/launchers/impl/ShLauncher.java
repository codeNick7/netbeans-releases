/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 * $Id$
 */

package org.netbeans.installer.utils.system.launchers.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.PropertyResourceBundle;
import org.netbeans.installer.utils.FileUtils;
import org.netbeans.installer.utils.LogManager;
import org.netbeans.installer.utils.ResourceUtils;
import org.netbeans.installer.utils.StreamUtils;
import org.netbeans.installer.utils.StringUtils;
import org.netbeans.installer.utils.helper.JavaCompatibleProperties;
import org.netbeans.installer.utils.system.launchers.LauncherProperties;
import org.netbeans.installer.utils.system.launchers.LauncherResource;
import org.netbeans.installer.utils.progress.Progress;
import org.netbeans.installer.utils.system.NativeUtils;

/**
 *
 * @author Dmitry Lipin
 */
public class ShLauncher extends CommonLauncher {
    public static final String SH_LAUNCHER_STUB_NAME = "launcher.sh"; //NOI18N
    public static final String DEFAULT_UNIX_RESOURCE_SUFFIX =
            NativeUtils.NATIVE_LAUNCHER_RESOURCE_SUFFIX +
            "unix/"; //NOI18N
    public static final String I18N = "i18n"; //NOI18N
    public static final String SH_LAUNCHER_STUB = 
            DEFAULT_UNIX_RESOURCE_SUFFIX + SH_LAUNCHER_STUB_NAME;
    
    private static final String SH_EXT = ".sh"; //NOI18N
    private static final int SH_BLOCK = 1024;
    private static final String SH_INDENT = "        "; //NOI18N
    private static final String SH_LINE_SEPARATOR = StringUtils.LF;
    private static final String SH_COMMENT = "#";
    public static final String MIN_JAVA_VERSION_UNIX = "1.5.0_01";
    
    // if the location ends with "/" then all its children will be checked as well
    private static final String [] JAVA_COMMON_LOCATIONS = {
        "/usr/java/",
        "/usr/jdk/",
        "/usr/j2se/",
        "/usr/j2sdk/",
        
        "/usr/java/jdk/",
        "/usr/jdk/instances/",
        
        "/usr/local/java/",
        "/usr/local/jdk/",
        "/usr/local/j2se/",
        "/usr/local/j2sdk/",
        
        "/opt/java/",
        "/opt/jdk/",
        "/opt/j2sdk/",
        "/opt/j2se/",
        
        "/usr/lib/",
        
        "/export/jdk/",
        "/export/java/",
        "/export/j2se/",
        "/export/j2sdk/"
    };
    
    public ShLauncher(LauncherProperties props) {
        super(props);
    }
    
    public void initialize() throws IOException {
        LogManager.log("Checking SH launcher parameters..."); // NOI18N
        checkAllParameters();
    }
    
    public File create( Progress progress) throws IOException {
        
        FileOutputStream fos = null;
        try {
            
            progress.setPercentage(Progress.START);
            long total = getBundledFilesSize();
            fos = new FileOutputStream(outputFile,false);
            
            StringBuilder sb = new StringBuilder(getStubString());
            
            addShInitialComment(sb);
            addPossibleJavaLocations(sb);
            addI18NStrings(sb);
            addTestJVMFile(sb);
            addClasspathJars(sb);
            addJavaCompatible(sb);
            
            LogManager.log("Main Class : " + mainClass);
            addStringVariable(sb, "MAIN_CLASS", mainClass);
            
            LogManager.log("TestJVM Class : " + testJVMClass);
            addStringVariable(sb, "TEST_JVM_CLASS", testJVMClass);
            
            String jvmArgs = ((jvmArguments!=null) ?
                escapeSlashes(StringUtils.asString(jvmArguments,StringUtils.SPACE)):
                StringUtils.EMPTY_STRING);
            LogManager.log("JVM args : " + jvmArgs);
            addStringVariable(sb, "JVM_ARGUMENTS", jvmArgs);
            
            
            String appArgs = ((jvmArguments!=null) ?
                escapeSlashes(StringUtils.asString(appArguments,StringUtils.SPACE)):
                StringUtils.EMPTY_STRING);
            LogManager.log("Application args : " + appArgs);
            addStringVariable(sb, "APP_ARGUMENTS", appArgs);
            
            String token = "_^_^_^_^_^_^_^_^"; // max size: (10^16-1) bytes
            
            sb.append("LAUNCHER_STUB_SIZE=" + token + SH_LINE_SEPARATOR);
            
            sb.append("entryPoint \"$@\"" + SH_LINE_SEPARATOR);
            nextLine(sb);
            
            long size = sb.length();
            
            long fullBlocks = (size - (size % SH_BLOCK)) / SH_BLOCK + 1;
            
            String str = Long.toString(fullBlocks);
            int spaces = token.length() - str.length();
            
            for(int j=0;j < spaces; j++) {
                str+= StringUtils.SPACE;
            }
            
            sb.replace(sb.indexOf(token), sb.indexOf(token) + token.length(), str);
            
            long pads = fullBlocks * SH_BLOCK - size;
            
            for ( long i=0; i < pads;i++) {
                sb.append(SH_COMMENT);
            }
            addStringBuilder(fos,sb,false);
            addBundledData(fos, progress, total);
        } catch (IOException ex) {
            LogManager.log(ex);
            try {
                fos.close();
            } catch (IOException e) {
                LogManager.log(e);
            }
            
            try {
                FileUtils.deleteFile(outputFile);
            } catch (IOException e) {
                LogManager.log(e);
            }
            fos = null;
        } finally {
            
            if(fos!=null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    LogManager.log(ex);
                    throw ex;
                }
            }
            progress.setPercentage(Progress.COMPLETE);
        }
        return outputFile;
    }
    
    public String[] getExecutionCommand() throws IOException {
        return new String [] {outputFile.getAbsolutePath()};
    }
    
    public List <JavaCompatibleProperties> getDefaultCompatibleJava() {
        List <JavaCompatibleProperties> list = new ArrayList <JavaCompatibleProperties>();
        list.add(new JavaCompatibleProperties(
                MIN_JAVA_VERSION_UNIX, null, null, null, null));
        return list;
    }
    
    private long getBundledFilesSize() {
        long total = 0;
        
        for (LauncherResource jvmFile : jvms) {
            if ( jvmFile.isBundled()) {
                total += FileUtils.getSize(new File(jvmFile.getPath()));
            }
        }
        if(testJVMFile!=null && testJVMFile.isBundled()) {
            total += FileUtils.getSize(new File(testJVMFile.getPath()));
        }
        
        for (LauncherResource jarFile : jars) {
            if ( jarFile.isBundled()) {
                File file = new File(jarFile.getPath());
                total += FileUtils.getSize(file);
            }
        }
        return total;
    }
    
    protected String getI18NResourcePrefix() {
        return DEFAULT_UNIX_RESOURCE_SUFFIX;
    }
    
    public String getExtension() {
        return SH_EXT;
    }
    
    private String escapeChars(String str) {
        return (str==null) ? StringUtils.EMPTY_STRING :
            str.replace("\n","\\n").
                replace("\t","\\\\t").
                replace("\r","\\\\r").
                replaceAll("`","\\`").
                replaceAll("\"","\\\\\"");
        
    }
    private String escapeSlashesAndChars(String str) {
        return escapeSlashes(escapeChars(str));
    }
    private String escapeSlashes(String str) {
        return (str==null) ? StringUtils.EMPTY_STRING :
            str.replace(StringUtils.BACK_SLASH,
                StringUtils.DOUBLE_BACK_SLASH);
    }
    
    private String getUTF8(String str, boolean changePropertyCounterStyle) throws UnsupportedEncodingException{
        if(!changePropertyCounterStyle) {
            return getUTF8(str);
        } else {
            String string = StringUtils.EMPTY_STRING;
            int maxCounter=0;
            while(str.indexOf(getJavaCounter(maxCounter))!=-1) {
                maxCounter++;
            }
            boolean con;
            String jc;
            for(int i=0;i<str.length();i++) {
                con = false;
                for(int j=0;j<maxCounter;j++) {
                    jc = getJavaCounter(j);
                    if(str.indexOf(jc)== i) {
                        string += "$" + (j + 1);
                        i+=jc.length();
                        con = true;
                        break;
                    }
                }
                if(!con) {
                    string+=getUTF8(str.substring(i,i+1));
                }
            }
            
            return string;
        }
    }
    
    private String getStubString() throws IOException {
        InputStream stubStream;
        
        if(stubFile!=null)  {
            checkParameter("stub file", stubFile);
            stubStream = new FileInputStream(stubFile);
        } else {
            stubStream = ResourceUtils.getResource(SH_LAUNCHER_STUB);
        }
        CharSequence cs = StreamUtils.readStream(stubStream);
        stubStream.close();
        
        String [] strings = cs.toString().split(StringUtils.NEW_LINE_PATTERN);
        String stubString = StringUtils.asString(strings, SH_LINE_SEPARATOR);
        return stubString;
    }
    private String getUTF8(String str) throws UnsupportedEncodingException{
        String repr = StringUtils.EMPTY_STRING;
        for(byte oneByte : str.getBytes(StringUtils.ENCODING_UTF8)) {
            repr+= StringUtils.BACK_SLASH + Integer.toOctalString(256 + oneByte);
        }
        return repr;
    }
    
    private String changePropertyCounterStyle(String string)  {
        int counter = 0;
        String jp;
        String str = string;
        do {
            jp = getJavaCounter(counter);
            if(str.indexOf(jp)!=-1) {
                str = str.replace(jp, "$" + (counter + 1) );
            } else {
                break;
            }
            counter++;
        }
        while (true);
        return str;
    }
    
    private void addStringVariable(StringBuilder sb, String name, String value)  {
        String str = (value != null) ? value : StringUtils.EMPTY_STRING;
        sb.append(name + StringUtils.EQUAL + StringUtils.QUOTE +
                str + StringUtils.QUOTE + SH_LINE_SEPARATOR);
    }
    
    private void addNumberVariable(StringBuilder sb, String name, long value) {
        sb.append(name + StringUtils.EQUAL + value +  SH_LINE_SEPARATOR);
    }
    private void nextLine(StringBuilder sb) {
        sb.append(SH_LINE_SEPARATOR);
    }
    private void addJavaCompatible(StringBuilder sb) throws IOException {
        // add java compatibility properties number
        nextLine(sb);
        
        LogManager.log("Total compatible java properties : " + compatibleJava.size()); //NOI18N
        addNumberVariable(sb, "JAVA_COMPATIBLE_PROPERTIES_NUMBER", compatibleJava.size());
        
        for(int i=0;i<compatibleJava.size();i++) {
            nextLine(sb);
            sb.append("setJavaCompatibilityProperties_" + i + "() {" + SH_LINE_SEPARATOR);
            
            JavaCompatibleProperties prop = compatibleJava.get(i);
            LogManager.log("... adding compatible jvm [" + i + "] : " + prop.toString()); //NOI18N
            addStringVariable(sb, "JAVA_COMP_VERSION_MIN", prop.getMinVersion());
            addStringVariable(sb, "JAVA_COMP_VERSION_MAX", prop.getMaxVersion());
            addStringVariable(sb, "JAVA_COMP_VENDOR", prop.getVendor());
            addStringVariable(sb, "JAVA_COMP_OSNAME", prop.getOsName());
            addStringVariable(sb, "JAVA_COMP_OSARCH", prop.getOsArch());
            sb.append("}");
            nextLine(sb);
        }
    }
    private void addTestJVMFile(StringBuilder sb) throws IOException {
        nextLine(sb);
        long type = (testJVMFile!=null) ? testJVMFile.getPathType().toLong() : 0;
        
        addNumberVariable(sb, "TEST_JVM_FILE_TYPE", type); //NOI18N
        
        if(testJVMFile == null || testJVMFile.isBundled()) {
            long size = 0;
            String name = StringUtils.EMPTY_STRING;
            if(testJVMFile!=null) {
                String path = testJVMFile.getPath();
                File  testFile = new File(path);
                size = FileUtils.getSize(testFile);
                name = testFile.getName();
            } else {
                size = ResourceUtils.getResourceSize(TEST_JVM_RESOURCE);
                name = ResourceUtils.getResourceFileName(TEST_JVM_RESOURCE);
            }
            addNumberVariable(sb, "TEST_JVM_FILE_SIZE", size);      //NOI18N
            addStringVariable(sb, "TEST_JVM_FILE_PATH",   //NOI18N
                    escapeSlashesAndChars(name));
        } else {
            addStringVariable(sb, "TEST_JVM_FILE_PATH",        //NOI18N
                    escapeSlashesAndChars(testJVMFile.getPath()));
        }
    }
    
    private void addClasspathJars(StringBuilder sb) throws IOException {
        nextLine(sb);
        
        addNumberVariable(sb, "JARS_NUMBER",  jars.size()); //NOI18N
        
        int counter = 0;
        for(LauncherResource jarFile : jars) {
            addNumberVariable(sb, "JAR_TYPE_" + counter, jarFile.getPathType().toLong());
            if ( jarFile.isBundled()) {
                File file = new File(jarFile.getPath());
                addNumberVariable(sb, "JAR_SIZE_" + counter, FileUtils.getSize(file));
                addStringVariable(sb,"JAR_PATH_" + counter, escapeSlashesAndChars(file.getName()));
            } else{
                addStringVariable(sb, "JAR_PATH_"+ counter, escapeSlashesAndChars(jarFile.getPath()));
            }
            counter++;
        }
        nextLine(sb);
    }
    
    private void addI18NStrings(StringBuilder sb) throws IOException {
        Object [] locales = i18nMap.keySet().toArray();
        addNumberVariable(sb,"LAUNCHER_LOCALES_NUMBER",locales.length); //NOI18N
        
        for(int i=0;i<locales.length;i++) {
            addStringVariable(sb,"LAUNCHER_LOCALE_NAME_" + i, //NOI18N
                    locales[i].toString());
        }
        
        nextLine(sb);
        
        for(int i=0;i<locales.length;i++) {
            String locale = locales[i].toString();
            sb.append("getLocalizedMessage_" + locale + "() {" + SH_LINE_SEPARATOR );
            sb.append(SH_INDENT + "arg=$1" + SH_LINE_SEPARATOR );
            sb.append(SH_INDENT + "shift" + SH_LINE_SEPARATOR );
            sb.append(SH_INDENT + "case $arg in" + SH_LINE_SEPARATOR );
            PropertyResourceBundle rb = i18nMap.get(locales[i]);
            Enumeration <String>en = rb.getKeys();
            while(en.hasMoreElements()) {
                String name  = en.nextElement();
                String value =  rb.getString(name);
                sb.append(SH_INDENT + "\"" + name + "\")" + SH_LINE_SEPARATOR);
                String printString = value;
                if(locale==null || locale.equals("")) {
                    printString = escapeChars(changePropertyCounterStyle(printString));
                } else {
                    printString = getUTF8(printString, true);
                }
                sb.append(SH_INDENT + SH_INDENT + "printf \"" + printString + "\\n" + "\"" + SH_LINE_SEPARATOR);
                sb.append(SH_INDENT + SH_INDENT + ";;" + SH_LINE_SEPARATOR);
                
            }
            sb.append(SH_INDENT + "*)" + SH_LINE_SEPARATOR);
            sb.append(SH_INDENT + SH_INDENT + "printf \"$arg\\n\"" + SH_LINE_SEPARATOR);
            sb.append(SH_INDENT + SH_INDENT + ";;" + SH_LINE_SEPARATOR);
            sb.append(SH_INDENT + "esac" + SH_LINE_SEPARATOR);
            sb.append("}" + SH_LINE_SEPARATOR);
            nextLine(sb);
        }
        
    }
    private void addShInitialComment(StringBuilder sb) throws IOException {
        nextLine(sb);
        nextLine(sb);
        for(int i=0;i<80;i++) {
            sb.append(SH_COMMENT);
        }
        nextLine(sb);
        sb.append(SH_COMMENT + " Added by the bundle builder" + //NOI18N
                SH_LINE_SEPARATOR);
        addNumberVariable(sb,"FILE_BLOCK_SIZE", SH_BLOCK);//NOI18N
        nextLine(sb);
    }
    
    private int addJavaPaths(int count, StringBuilder sb, List<LauncherResource> list) {
        int counter = count;
        for(LauncherResource location : list) {
            addNumberVariable(sb, "JAVA_LOCATION_TYPE_" + counter, //NOI18N
                    location.getPathType().toLong());
            String path = location.getPath();
            if(location.isBundled()) {
                addNumberVariable(sb, "JAVA_LOCATION_SIZE_" + counter, //NOI18N
                        FileUtils.getSize(new File(path)));
                addStringVariable(sb, "JAVA_LOCATION_PATH_" + counter,//NOI18N
                        escapeSlashesAndChars(new File(path).getName()));
            } else {
                addStringVariable(sb, "JAVA_LOCATION_PATH_" + counter, //NOI18N
                        escapeSlashesAndChars(path));
            }
            
            counter ++;
        }
        return counter;
    }
    private int addJavaPaths(int count, StringBuilder sb, String  [] paths) {
        List <LauncherResource> list = new ArrayList <LauncherResource> ();
        for(String path : paths) {
            list.add(new LauncherResource(LauncherResource.Type.ABSOLUTE, path));
        }
        return addJavaPaths(count, sb, list);
    }
    protected String [] getCommonSystemJavaLocations() {
        return JAVA_COMMON_LOCATIONS;
    }
    private void addPossibleJavaLocations(StringBuilder sb) {
        int total = 0;
        total = addJavaPaths(total, sb, jvms);
        total = addJavaPaths(total, sb, getCommonSystemJavaLocations());
        addNumberVariable(sb, "JAVA_LOCATION_NUMBER", total); //NOI18N
        nextLine(sb);
    }
    
    private void fillWithPads(FileOutputStream fos, long sz) throws IOException {
        long d = (SH_BLOCK - (sz % SH_BLOCK)) % SH_BLOCK;
        for ( int i=0; i < d; i++) {
            addString(fos, SH_LINE_SEPARATOR, false);
        }
    }
    private void addBundledData(FileOutputStream fos, Progress progress, long total) throws IOException {
        
        if(testJVMFile==null || testJVMFile.isBundled()) { // if bundle TestJVM
            LogManager.log("Bundle testJVM file..."); //NOI18N
            if(testJVMFile!=null) {
                addData(fos, new File(testJVMFile.getPath()), progress, total);
            } else {
                addData(fos, ResourceUtils.getResource(TEST_JVM_RESOURCE), progress, total);
            }
            long sz = (testJVMFile!=null) ?
                FileUtils.getSize(new File(testJVMFile.getPath())) :
                ResourceUtils.getResourceSize(TEST_JVM_RESOURCE);
            fillWithPads(fos, sz);
            
            LogManager.log("... done bundle testJVM file");//NOI18N
        }
        for(LauncherResource file : jars) {
            if(file.isBundled()) {
                File jarFile = new File(file.getPath());
                LogManager.log("Bundle jar " + jarFile);     //NOI18N
                addData(fos, jarFile, progress, total);
                LogManager.log("... done bundle jar");      //NOI18N
                long sz = FileUtils.getSize(jarFile);
                fillWithPads(fos, sz);
            }
        }
        
        for(LauncherResource jvm : jvms) {
            if(jvm.isBundled()) {
                File jvmFile = new File(jvm.getPath());
                LogManager.log("Bundle jvm " + jvmFile);        //NOI18N
                addData(fos, jvmFile, progress, total);
                LogManager.log("... done bundle jvm");          //NOI18N
                long sz = FileUtils.getSize(jvmFile);
                fillWithPads(fos, sz);
            }
        }
    }
}
