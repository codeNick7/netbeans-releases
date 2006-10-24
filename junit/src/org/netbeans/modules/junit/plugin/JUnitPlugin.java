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
 * Software is Sun Microsystems, Inc. Portions Copyright 2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.junit.plugin;

import java.util.Map;
//XXX: retouche
//import org.netbeans.jmi.javamodel.Feature;
//import org.netbeans.jmi.javamodel.JavaClass;
//import org.netbeans.jmi.javamodel.Method;
//import org.netbeans.modules.javacore.api.JavaModel;
import org.netbeans.modules.junit.JUnitPluginTrampoline;
import org.openide.filesystems.FileObject;

/**
 * SPI for custom implementations of support for JUnit.
 * It declares methods for:
 * <ul>
 *     <li>navigation between source classes and corresponding test classes
 *         ({@link #getTestLocation getTestLocation},
 *          {@link #getTestedLocation getTestedLocation})</li>
 *     <li>creation of test class skeletons
 *         ({@link #createTests createTests})</li>
 * </ul>
 *
 * @author  Marian Petras
 */
public abstract class JUnitPlugin {
    
    static {
        JUnitPluginTrampoline.DEFAULT = new JUnitPluginTrampoline() {
            public FileObject[] createTests(
                    JUnitPlugin plugin,
                    FileObject[] filesToTest,
                    FileObject targetRoot,
                    Map<CreateTestParam,Object> params) {
                return plugin.createTests(filesToTest, targetRoot, params);
            }
            public Location getTestLocation(
                    JUnitPlugin plugin,
                    Location sourceLocation) {
                return plugin.getTestLocation(sourceLocation);
            }
            public Location getTestedLocation(
                    JUnitPlugin plugin,
                    Location testLocation) {
                return plugin.getTestedLocation(testLocation);
            }
        };
    }
    
    /**
     * Default constructor for use by subclasses.
     */
    protected JUnitPlugin() {}

    /**
     * Enumeration of test creation parameters.
     */
    public enum CreateTestParam {
        
        /**
         * key for the map of test creation parameters
         * - name of the test class
         */
        CLASS_NAME(99310),
        /**
         * key for the map of test creation parameters
         * - include tests for public methods?
         */
        INC_PUBLIC(99311),
        /**
         * key for the map of test creation parameters
         * - include tests for protected methods?
         */
        INC_PROTECTED(99312),
        /**
         * key for the map of test creation parameters
         * - include tests for package-private methods?
         */
        INC_PKG_PRIVATE(99313),
        /**
         * key for the map of test creation parameters
         * - generate method {@code setup()}?
         */
        INC_SETUP(99314),
        /**
         * key for the map of test creation parameters
         * - generate method {@code tearDown()}?
         */
        INC_TEAR_DOWN(99315),
        /**
         * key for the map of test creation parameters
         * - generate default test method bodies?
         */
        INC_METHOD_BODIES(99316),
        /**
         * key for the map of test creation parameters
         * - generate Javadoc comments for test methods?
         */
        INC_JAVADOC(99317),
        /**
         * key for the map of test creation parameters
         * - generate source code hints?
         */
        INC_CODE_HINT(99318),
        /**
         * key for the map of test creation parameters
         * - generate test classes for package-private classes?
         */
        INC_PKG_PRIVATE_CLASS(99319),
        /**
         * key for the map of test creation parameters
         * - generate test classes for abstract classes?
         */
        INC_ABSTRACT_CLASS(99320),
        /**
         * key for the map of test creation parameters
         * - generate test classes for exception classes?
         */
        INC_EXCEPTION_CLASS(99321),
        /**
         * key for the map of test creation parameters
         * - generate test suites for packages?
         */
        INC_GENERATE_SUITE(99322);
        
        private final int idNumber;
        
        CreateTestParam(int idNumber) {
            this.idNumber = idNumber;
        }
        
        /**
         * Return a unique number of this enum element.
         *
         * @return  unique number of this enum element
         */
        public int getIdNumber() {
            return idNumber;
        }
        
    }
    
    /**
     * Data structure for storage of specification of a Java element or
     * a Java file.
     */
    public static final class Location {
        /**
         * holds specification of a Java file
         */
        private final FileObject fileObject;
//XXX: retouche
//        /**
//         * holds specification of a Java element within the Java file;
//         * may be {@code null}
//         */
//        private final Feature javaElement;
        /**
         * Creates a new instance.
         *
         * @param  fileObject  the {@code FileObject}
         * @param  javaElement  instance of {@code JavaMethod}
         *                      or {@code JavaClass}, or {@code null};
         *                      if non-{@code null}, it must be contained
         *                      in the given {@code FileObject}
         * @exception  java.lang.IllegalArgumentException
         *             if the passed {@code FileObject} is {@code null};
         *             or if the Java element is specified but it is neither
         *             {@code JavaClass} nor {@code Method};
         *             or if the Java element is specified but it is not
         *             contained in the given {@code FileObject}
         * @see  Method
         * @see  JavaClass
         */
//XXX: retouche
        public Location(FileObject fileObject/*XXX: retouche, Feature javaElement*/) {
            if (fileObject == null) {
               throw new IllegalArgumentException("fileObject is null");//NOI18N
            }
//XXX: retouche
//            if (javaElement != null) {
//                if (!(javaElement instanceof JavaClass)
//                        && !(javaElement instanceof Method)) {
//                    throw new IllegalArgumentException(
//               "The Java element is neither JavaClass nor JavaElement");//NOI18N
//                }
//                if (JavaModel.getFileObject(javaElement.getResource())
//                        != fileObject) {
//                    throw new IllegalArgumentException(
//          "The Java element is not contained in the given FileObject.");//NOI18N
//                }
//            }
            this.fileObject = fileObject;
//XXX: retouche
//            this.javaElement = javaElement;
        }
        
        /**
         * Returns the {@code FileObject}.
         *
         * @return  the {@code FileObject} held in this instance
         */
        public FileObject getFileObject() {
            return fileObject;
        }
        
//XXX: retouche
//        /**
//         * Returns the Java element.
//         *
//         * @return  {@link Method} or {@link JavaClass} held in this instance,
//         *          or {@code null} if no Java element is specified
//         */
//        public Feature getJavaElement() {
//            return javaElement;
//        }
    }
    
    /**
     * Returns a specification of a Java element or file representing test
     * for the given source Java element or file.
     *
     * @param  sourceLocation  specification of a Java element or file
     * @return  specification of a corresponding test Java element or file,
     *          or {@code null} if no corresponding test Java file is available
     */
    protected abstract Location getTestLocation(Location sourceLocation);
    
    /**
     * Returns a specification of a Java element or file that is tested
     * by the given test Java element or test file.
     *
     * @param  testLocation  specification of a Java element or file
     * @return  specification of a Java element or file that is tested
     *          by the given Java element or file.
     */
    protected abstract Location getTestedLocation(Location testLocation);
    
    /**
     * Creates test classes for given source classes.
     *
     * @param  filesToTest  source files for which test classes should be
     *                      created
     * @param  targetRoot   root folder of the target source root
     * @param  params  parameters of creating test class
     *                 - each key is an {@code Integer} whose value is equal
     *                 to some of the constants defined in the class;
     *                 the value is either
     *                 a {@code String} (for key with value {@code CLASS_NAME})
     *                 or a {@code Boolean} (for other keys)
     * @return  created test files, or {@code null} if no test classes were
     *          created and/or updated
     */
    protected abstract FileObject[] createTests(
            FileObject[] filesToTest,
            FileObject targetRoot,
            Map<CreateTestParam, Object> params);

}
