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
package org.netbeans.test.codegen;

import java.io.IOException;

/**
 * This class is used as a source of code-generator method test.
 *
 * @author  Pavel Flaska
 */
public class MethodTest1 {

    public void firstMethod() {
    }

    public int secondMethod() {
        return 0;
    }

    /**
     * JavaDoc in method.
     *
     * @param  a  integral value
     * @param  c  sequence of chars
     *
     * @return value of something
     */
    public long thirdMethod(int a, String c) {
        int e = a++;
        String d = c;
        
        return e - d.length();
    }
    
    protected static void fourthMethod(int x) throws IOException {
        // nothing to do.
        return;
    }
    
    private void fifthMethod(String d) throws IOException, IllegalAccessError, IllegalArgumentException {
        // three exceptions thrown
    }
    
    protected Object sixthMethod() {
        return "";
    }
    
    public abstract void seventhMethod();
    
    public void eighthMethod() {
    }
    
    public interface TestInterface {
        void interfaceMethod();
    }
}
