/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 */


package org.netbeans.modules.i18n.regexp;

import java.util.Map;

/**
 * Translator of Apache's Regexp regular expressions to JDK's regular
 * expressions.
 *
 * @author  Marian Petras
 */
public final class Translator {

    /**
     * Translates the given Apache Regexp regular expression
     * into a JDK's regular expression.
     *
     * @param  regexp  regular expression according to Apache's Regexp library
     *                 syntax rules
     * @return  regular expression according to syntax rules of JDK's class
     *          {@link java.util.regex.Pattern Pattern}.
     * @exception  java.lang.IllegalArgumentException
     *             if the argument was <code>null</code>
     * @exception  ParseException
     *             if the given expression contained a syntax error
     */
    public static String translateRegexp(String regexp)
            throws IllegalArgumentException, ParseException {
        TreeNodeRoot tree = Parser.parse(regexp);
        return Generator.generateRegexp(tree);
    }

    /**
     * Translates the given Apache Regexp regular expression
     * into a JDK's regular expression.
     *
     * @param  regexp  regular expression according to Apache's Regexp library
     *                 syntax rules
     * @param  tokenReplacements  maps token names to strings to be put in place
     *                            of them, or <code>null</code> to ignore tokens
     *                            (leave them unchanged)
     * @return  regular expression according to syntax rules of JDK's class
     *          {@link java.util.regex.Pattern Pattern}.
     * @exception  java.lang.IllegalArgumentException
     *             if the regular expression is <code>null</code>
     * @exception  java.lang.ClassCastException
     *             if not all keys in the map were strings
     * @exception  ParseException
     *             if the given expression contained a syntax error
     */
    public static String translateRegexp(String regexp,
                                         Map tokenReplacements)
            throws IllegalArgumentException, ParseException {

        if ((tokenReplacements == null) || (tokenReplacements.isEmpty())) {
            return translateRegexp(regexp);
        }

        String[] tokenNames = new String[tokenReplacements.size()];
        try {
            tokenReplacements.keySet().toArray(tokenNames);
        } catch (ArrayStoreException ex) {
            throw new ClassCastException();
        }
        TreeNodeRoot tree = Parser.parse(regexp, tokenNames);
        return Generator.generateRegexp(tree, tokenReplacements);
    }

}
