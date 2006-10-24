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
 */
package org.netbeans.api.java.source;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.junit.NbTestCase;
import org.netbeans.spi.java.classpath.support.ClassPathSupport;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.LocalFileSystem;
import org.openide.filesystems.Repository;
import org.openide.filesystems.URLMapper;

/**
 *
 * @author Jan Lahoda
 */
public class APIIsSelfContainedTest extends NbTestCase {

    private List<String> violations = new ArrayList<String>();
    
    /**
     * Creates a new instance of APIIsSelfContained
     */

    public APIIsSelfContainedTest(String name) {
        super(name);
    }
    
    protected void setUp() throws Exception {
        SourceUtilsTestUtil.prepareTest(new String[0], new Object[0]);
    }
    
    private boolean shouldCheck(Set<Modifier> mods) {
        if (mods.contains(Modifier.PUBLIC))
            return true;
        if (mods.contains(Modifier.PROTECTED))
            return true;
        
        return false;
    }
    
    private void writeIntoFile(FileObject file, String what) throws Exception {
        FileLock lock = file.lock();
        OutputStream out = file.getOutputStream(lock);
        
        try {
            out.write(what.getBytes());
        } finally {
            out.close();
            lock.releaseLock();
        }
    }
    
    private static final List<String> API_PACKAGES = Arrays.asList(new String[] {
        "java",
        "org.netbeans.api",
        "org.openide",
        "com.sun.source.tree",
        "com.sun.source.util",
        "com.sun.javadoc",
    });
    
    private boolean isAPIClass(TypeElement clazz) {
        String nameS = /*!!!!*/clazz.toString();
        
        for (String s : API_PACKAGES) {
            if (nameS.startsWith(s))
                return true;
        }
        
        return false;
    }
    
    private void verifyAPIClass(TypeElement clazz, TypeElement in) {
        if (!isAPIClass(clazz)) {
            violations.add("Use of non-API class: " + clazz + " in " + in.toString()/*!!!*/);
        }
    }
    
    private void verifyAPIClass(TypeMirror tm, TypeElement in) {
        if (tm.getKind() == TypeKind.DECLARED) {
            verifyAPIClass((TypeElement) ((DeclaredType) tm).asElement(), in);
        }
    }
    
    private void verifySelfContainedAPI(VariableElement ve, TypeElement in) {
        if (!shouldCheck(ve.getModifiers()))
            return; //do not check non-public things
        
        verifyAPIClass(ve.asType(), in);
    }
    
    private void verifySelfContainedAPI(ExecutableElement ee, TypeElement in) {
        if (!shouldCheck(ee.getModifiers()))
            return; //do not check non-public things
        
        verifyAPIClass(ee.getReturnType(), in);
        for (VariableElement ve : ee.getParameters()) {
            verifyAPIClass(ve.asType(), in);
        }
    }
    
    private void verifySelfContainedAPI(TypeElement tel) {
        if (!shouldCheck(tel.getModifiers()))
            return; //do not check non-public things
        
        verifyAPIClass(tel.getSuperclass(), tel);
        
        for (TypeMirror intf : tel.getInterfaces()) {
            verifyAPIClass(intf, tel);
        }
        
        for (Element e : tel.getEnclosedElements()) {
            verifySelfContainedAPI(e, tel);
        }
    }
    
    private void verifySelfContainedAPI(PackageElement pel) {
        for (Element e : pel.getEnclosedElements()) {
            verifySelfContainedAPI(e, null);
        }
    }
    
    private void verifySelfContainedAPI(Element e, TypeElement in) {
        switch (e.getKind()) {
            case ANNOTATION_TYPE:
            case CLASS:
            case INTERFACE:
            case ENUM:
                verifySelfContainedAPI((TypeElement) e);
                break;
            case CONSTRUCTOR:
            case METHOD:
                verifySelfContainedAPI((ExecutableElement) e, in);
                break;
            case FIELD:
                verifySelfContainedAPI((VariableElement) e, in);
                break;
            case PACKAGE:
                verifySelfContainedAPI((PackageElement) e);
                break;
        }
    }
    
    private FileObject[] prepareClasspath() {
        FileObject javaSourceJar = URLMapper.findFileObject(JavaSource.class.getProtectionDomain().getCodeSource().getLocation());
        FileObject root = javaSourceJar.getParent().getParent().getParent();
        
        return new FileObject[] {
            FileUtil.getArchiveRoot(root.getFileObject("ide8/modules/org-netbeans-modules-java-source.jar")),
            FileUtil.getArchiveRoot(root.getFileObject("ide8/modules/ext/javac-api.jar")),
            FileUtil.getArchiveRoot(root.getFileObject("ide8/modules/ext/javac-impl.jar")),
        };
    }
    
    public void testAPIIsSelfContained() throws Exception {
        FileObject root = makeScratchDir(this);
        
        final ClassPath bootPath = ClassPathSupport.createClassPath(SourceUtilsTestUtil.getBootClassPath().toArray(new URL[0]));//ClassPath.getClassPath(source, ClassPath.BOOT);
        final ClassPath compilePath = ClassPathSupport.createClassPath(prepareClasspath());
        final ClassPath srcPath = ClassPathSupport.createClassPath(new URL[0]);
        
        JavaSource js = JavaSource.create(ClasspathInfo.create(bootPath, compilePath, srcPath)/*, source*/);
        
        js.runUserActionTask(new CancellableTask<CompilationController>() {
            public void cancel() {}
            public void run(CompilationController copy) throws Exception {
                PackageElement apiPackage = copy.getElements().getPackageElement("org.netbeans.api.java.source");
                verifySelfContainedAPI(apiPackage);
                
                apiPackage = copy.getElements().getPackageElement("org.netbeans.api.java.source.support");
                verifySelfContainedAPI(apiPackage);
            }
        },true);
        
        assertTrue(violations.toString(), violations.isEmpty());
    }
    
    /**Copied from org.netbeans.api.project.
     * Create a scratch directory for tests.
     * Will be in /tmp or whatever, and will be empty.
     * If you just need a java.io.File use clearWorkDir + getWorkDir.
     */
    public static FileObject makeScratchDir(NbTestCase test) throws IOException {
        test.clearWorkDir();
        File root = test.getWorkDir();
        assert root.isDirectory() && root.list().length == 0;
        FileObject fo = FileUtil.toFileObject(root);
        if (fo != null) {
            // Presumably using masterfs.
            return fo;
        } else {
            // For the benefit of those not using masterfs.
            LocalFileSystem lfs = new LocalFileSystem();
            try {
                lfs.setRootDirectory(root);
            } catch (PropertyVetoException e) {
                assert false : e;
            }
            Repository.getDefault().addFileSystem(lfs);
            return lfs.getRoot();
        }
    }
    
}
