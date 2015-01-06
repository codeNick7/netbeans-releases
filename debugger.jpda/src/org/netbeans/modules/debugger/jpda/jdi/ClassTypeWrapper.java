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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2009 Sun
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

package org.netbeans.modules.debugger.jpda.jdi;

// DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
// Generated by org.netbeans.modules.debugger.jpda.jdi.Generate class located in 'gensrc' folder,
// perform the desired modifications there and re-generate by "ant generate".

/**
 * Wrapper for ClassType JDI class.
 * Use methods of this class instead of direct calls on JDI objects.
 * These methods assure that exceptions thrown from JDI calls are handled appropriately.
 *
 * @author Martin Entlicher
 */
public final class ClassTypeWrapper {

    private ClassTypeWrapper() {}

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static java.util.List<com.sun.jdi.InterfaceType> allInterfaces0(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "allInterfaces",
                    "JDI CALL: com.sun.jdi.ClassType({0}).allInterfaces()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            java.util.List<com.sun.jdi.InterfaceType> ret;
            ret = a.allInterfaces();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            return java.util.Collections.emptyList();
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            return java.util.Collections.emptyList();
        } catch (com.sun.jdi.ClassNotPreparedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "allInterfaces",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static java.util.List<com.sun.jdi.InterfaceType> allInterfaces(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "allInterfaces",
                    "JDI CALL: com.sun.jdi.ClassType({0}).allInterfaces()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            java.util.List<com.sun.jdi.InterfaceType> ret;
            ret = a.allInterfaces();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (com.sun.jdi.ClassNotPreparedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "allInterfaces",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static com.sun.jdi.Method concreteMethodByName(com.sun.jdi.ClassType a, java.lang.String b, java.lang.String c) throws org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "concreteMethodByName",
                    "JDI CALL: com.sun.jdi.ClassType({0}).concreteMethodByName({1}, {2})",
                    new Object[] {a, b, c});
        }
        Object retValue = null;
        try {
            com.sun.jdi.Method ret;
            ret = a.concreteMethodByName(b, c);
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (com.sun.jdi.ClassNotPreparedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "concreteMethodByName",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static java.util.List<com.sun.jdi.InterfaceType> interfaces0(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "interfaces",
                    "JDI CALL: com.sun.jdi.ClassType({0}).interfaces()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            java.util.List<com.sun.jdi.InterfaceType> ret;
            ret = a.interfaces();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            return java.util.Collections.emptyList();
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            return java.util.Collections.emptyList();
        } catch (com.sun.jdi.ClassNotPreparedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "interfaces",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static java.util.List<com.sun.jdi.InterfaceType> interfaces(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "interfaces",
                    "JDI CALL: com.sun.jdi.ClassType({0}).interfaces()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            java.util.List<com.sun.jdi.InterfaceType> ret;
            ret = a.interfaces();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (com.sun.jdi.ClassNotPreparedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "interfaces",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static com.sun.jdi.Value invokeMethod(com.sun.jdi.ClassType a, com.sun.jdi.ThreadReference b, com.sun.jdi.Method c, java.util.List<? extends com.sun.jdi.Value> d, int e) throws com.sun.jdi.InvalidTypeException, com.sun.jdi.ClassNotLoadedException, com.sun.jdi.IncompatibleThreadStateException, com.sun.jdi.InvocationException, org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.ObjectCollectedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "invokeMethod",
                    "JDI CALL: com.sun.jdi.ClassType({0}).invokeMethod({1}, {2}, {3}, {4})",
                    new Object[] {a, b, c, d, e});
        }
        Object retValue = null;
        try {
            try {
                com.sun.jdi.Value ret;
            ret = a.invokeMethod(b, c, d, e);
            retValue = ret;
            return ret;
            } catch (com.sun.jdi.InternalException iex) {
                if (iex.errorCode() == 502) { // ALREADY_INVOKING
                    iex = (com.sun.jdi.InternalException) org.openide.util.Exceptions.attachLocalizedMessage(iex, org.openide.util.NbBundle.getMessage(org.netbeans.modules.debugger.jpda.JPDADebuggerImpl.class, "JDWPError502"));
                    org.openide.util.Exceptions.printStackTrace(iex);
                    return null;
                } else {
                    throw iex; // re-throw the original
                }
            }
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (com.sun.jdi.ObjectCollectedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ObjectCollectedExceptionWrapper(ex);
        } catch (com.sun.jdi.InvalidTypeException ex) {
            retValue = ex;
            throw ex;
        } catch (com.sun.jdi.ClassNotLoadedException ex) {
            retValue = ex;
            throw ex;
        } catch (com.sun.jdi.IncompatibleThreadStateException ex) {
            retValue = ex;
            throw ex;
        } catch (com.sun.jdi.InvocationException ex) {
            retValue = ex;
            throw ex;
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "invokeMethod",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static boolean isEnum0(com.sun.jdi.ClassType a) {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "isEnum",
                    "JDI CALL: com.sun.jdi.ClassType({0}).isEnum()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            boolean ret;
            ret = a.isEnum();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            return false;
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            return false;
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "isEnum",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static boolean isEnum(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "isEnum",
                    "JDI CALL: com.sun.jdi.ClassType({0}).isEnum()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            boolean ret;
            ret = a.isEnum();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "isEnum",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static com.sun.jdi.ObjectReference newInstance(com.sun.jdi.ClassType a, com.sun.jdi.ThreadReference b, com.sun.jdi.Method c, java.util.List<? extends com.sun.jdi.Value> d, int e) throws com.sun.jdi.InvalidTypeException, com.sun.jdi.ClassNotLoadedException, com.sun.jdi.IncompatibleThreadStateException, com.sun.jdi.InvocationException, org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.ObjectCollectedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "newInstance",
                    "JDI CALL: com.sun.jdi.ClassType({0}).newInstance({1}, {2}, {3}, {4})",
                    new Object[] {a, b, c, d, e});
        }
        Object retValue = null;
        try {
            com.sun.jdi.ObjectReference ret;
            ret = a.newInstance(b, c, d, e);
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (com.sun.jdi.ObjectCollectedException ex) {
            retValue = ex;
            throw new org.netbeans.modules.debugger.jpda.jdi.ObjectCollectedExceptionWrapper(ex);
        } catch (com.sun.jdi.InvalidTypeException ex) {
            retValue = ex;
            throw ex;
        } catch (com.sun.jdi.ClassNotLoadedException ex) {
            retValue = ex;
            throw ex;
        } catch (com.sun.jdi.IncompatibleThreadStateException ex) {
            retValue = ex;
            throw ex;
        } catch (com.sun.jdi.InvocationException ex) {
            retValue = ex;
            throw ex;
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "newInstance",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static void setValue(com.sun.jdi.ClassType a, com.sun.jdi.Field b, com.sun.jdi.Value c) throws com.sun.jdi.InvalidTypeException, com.sun.jdi.ClassNotLoadedException, org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "setValue",
                    "JDI CALL: com.sun.jdi.ClassType({0}).setValue({1}, {2})",
                    new Object[] {a, b, c});
        }
        try {
            a.setValue(b, c);
        } catch (com.sun.jdi.InternalException ex) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (com.sun.jdi.ClassNotPreparedException ex) {
            throw new org.netbeans.modules.debugger.jpda.jdi.ClassNotPreparedExceptionWrapper(ex);
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "setValue",
                        org.netbeans.modules.debugger.jpda.JDIExceptionReporter.RET_VOID);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static java.util.List<com.sun.jdi.ClassType> subclasses0(com.sun.jdi.ClassType a) {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "subclasses",
                    "JDI CALL: com.sun.jdi.ClassType({0}).subclasses()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            java.util.List<com.sun.jdi.ClassType> ret;
            ret = a.subclasses();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            return java.util.Collections.emptyList();
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            return java.util.Collections.emptyList();
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "subclasses",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static java.util.List<com.sun.jdi.ClassType> subclasses(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "subclasses",
                    "JDI CALL: com.sun.jdi.ClassType({0}).subclasses()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            java.util.List<com.sun.jdi.ClassType> ret;
            ret = a.subclasses();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "subclasses",
                        retValue);
            }
        }
    }

    // DO NOT MODIFY THIS CODE, GENERATED AUTOMATICALLY
    public static com.sun.jdi.ClassType superclass(com.sun.jdi.ClassType a) throws org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper, org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper {
        if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallStart(
                    "com.sun.jdi.ClassType",
                    "superclass",
                    "JDI CALL: com.sun.jdi.ClassType({0}).superclass()",
                    new Object[] {a});
        }
        Object retValue = null;
        try {
            com.sun.jdi.ClassType ret;
            ret = a.superclass();
            retValue = ret;
            return ret;
        } catch (com.sun.jdi.InternalException ex) {
            retValue = ex;
            org.netbeans.modules.debugger.jpda.JDIExceptionReporter.report(ex);
            throw new org.netbeans.modules.debugger.jpda.jdi.InternalExceptionWrapper(ex);
        } catch (com.sun.jdi.VMDisconnectedException ex) {
            retValue = ex;
            if (a instanceof com.sun.jdi.Mirror) {
                com.sun.jdi.VirtualMachine vm = ((com.sun.jdi.Mirror) a).virtualMachine();
                vm.dispose();
            }
            throw new org.netbeans.modules.debugger.jpda.jdi.VMDisconnectedExceptionWrapper(ex);
        } catch (Error err) {
            retValue = err;
            throw err;
        } catch (RuntimeException rex) {
            retValue = rex;
            throw rex;
        } finally {
            if (org.netbeans.modules.debugger.jpda.JDIExceptionReporter.isLoggable()) {
                org.netbeans.modules.debugger.jpda.JDIExceptionReporter.logCallEnd(
                        "com.sun.jdi.ClassType",
                        "superclass",
                        retValue);
            }
        }
    }

}
