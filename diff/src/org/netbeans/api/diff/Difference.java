/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2002 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.api.diff;

import java.io.Serializable;

/**
 * This class represents a single difference between two files.
 *
 * @author  Martin Entlicher
 */
public class Difference extends Object implements Serializable {

    /** Delete type of difference - a portion of a file was removed in the other */
    public static final int DELETE = 0;

    /** Add type of difference - a portion of a file was added in the other */
    public static final int ADD = 1;

    /** Change type of difference - a portion of a file was changed in the other */
    public static final int CHANGE = 2;
    
    private int type = 0;
    private int firstStart = 0;
    private int firstEnd = 0;
    private int secondStart = 0;
    private int secondEnd = 0;
    private Difference.Part[] firstLineDiffs;
    private Difference.Part[] secondLineDiffs;
    
    /** The text of the difference in the first file. */
    private String firstText;
    /** The text of the difference in the second file. */
    private String secondText;
    
    private static final long serialVersionUID = 7638201981188907148L;
    
    /**
     * Creates a new instance of Difference
     * @param type The type of the difference. Must be one of the <a href="#DELETE">DELETE</a>,
     *             <a href="#ADD">ADD</a> or <a href="#CHANGE">CHANGE</a>
     * @param firstStart The line number on which the difference starts in the first file.
     * @param firstEnd The line number on which the difference ends in the first file.
     * @param secondStart The line number on which the difference starts in the second file.
     * @param secondEnd The line number on which the difference ends in the second file.
     */
    public Difference(int type, int firstStart, int firstEnd, int secondStart, int secondEnd) {
        this(type, firstStart, firstEnd, secondStart, secondEnd, null, null, null, null);
    }
    
    /**
     * Creates a new instance of Difference
     * @param type The type of the difference. Must be one of the <a href="#DELETE">DELETE</a>,
     *             <a href="#ADD">ADD</a> or <a href="#CHANGE">CHANGE</a>
     * @param firstStart The line number on which the difference starts in the first file.
     * @param firstEnd The line number on which the difference ends in the first file.
     * @param secondStart The line number on which the difference starts in the second file.
     * @param secondEnd The line number on which the difference ends in the second file.
     * @param firstText The text content of the difference in the first file.
     * @param secondText The text content of the difference in the second file.
     */
    public Difference(int type, int firstStart, int firstEnd, int secondStart, int secondEnd,
                      String firstText, String secondText) {
        this(type, firstStart, firstEnd, secondStart, secondEnd, firstText, secondText, null, null);
    }
    
    /**
     * Creates a new instance of Difference
     * @param type The type of the difference. Must be one of the <a href="#DELETE">DELETE</a>,
     *             <a href="#ADD">ADD</a> or <a href="#CHANGE">CHANGE</a>
     * @param firstStart The line number on which the difference starts in the first file.
     * @param firstEnd The line number on which the difference ends in the first file.
     * @param secondStart The line number on which the difference starts in the second file.
     * @param secondEnd The line number on which the difference ends in the second file.
     * @param firstText The text content of the difference in the first file.
     * @param secondText The text content of the difference in the second file.
     * @param firstLineDiffs The list of differences on lines in the first file.
     *                    The list contains instances of {@link Difference.Part}.
     *                    Can be <code>null</code> when there are no line differences.
     * @param secondLineDiffs The list of differences on lines in the second file.
     *                    The list contains instances of {@link Difference.Part}.
     *                    Can be <code>null</code> when there are no line differences.
     */
    public Difference(int type, int firstStart, int firstEnd, int secondStart, int secondEnd,
                      String firstText, String secondText, Difference.Part[] firstLineDiffs, Difference.Part[] secondLineDiffs) {
        if (type > 2 || type < 0) {
            throw new IllegalArgumentException("Bad Difference type = "+type);
        }
        this.type = type;
        this.firstStart = firstStart;
        this.firstEnd = firstEnd;
        this.secondStart = secondStart;
        this.secondEnd = secondEnd;
        this.firstText = firstText;
        this.secondText = secondText;
        this.firstLineDiffs = firstLineDiffs;
        this.secondLineDiffs = secondLineDiffs;
    }

    /**
     * Get the difference type. It's one of <a href="#DELETE">DELETE</a>,
     * <a href="#ADD">ADD</a> or <a href="#CHANGE">CHANGE</a>.
     */
    public int getType() {
        return this.type;
    }
    
    /**
     * Get the line number on which the difference starts in the first file.
     */
    public int getFirstStart() {
        return this.firstStart;
    }

    /**
     * Get the line number on which the difference ends in the first file.
     */
    public int getFirstEnd() {
        return this.firstEnd;
    }
    
    /**
     * Get the line number on which the difference starts in the second file.
     */
    public int getSecondStart() {
        return this.secondStart;
    }
    
    /**
     * Get the line number on which the difference ends in the second file.
     */
    public int getSecondEnd() {
        return this.secondEnd;
    }
    
    /**
     * The list of differences on lines in the first file.
     * The list contains instances of {@link Difference.Part}.
     * Can be <code>null</code> when there are no line differences.
     */
    public Difference.Part[] getFirstLineDiffs() {
        return firstLineDiffs;
    }
    
    /**
     * The list of differences on lines in the second file.
     * The list contains instances of {@link Difference.Part}.
     * Can be <code>null</code> when there are no line differences.
     */
    public Difference.Part[] getSecondLineDiffs() {
        return secondLineDiffs;
    }
    
    /**
     * Get the text content of the difference in the first file.
     */
    public String getFirstText() {
        return firstText;
    }
    
    /**
     * Get the text content of the difference in the second file.
     */
    public String getSecondText() {
        return secondText;
    }
    
    public String toString() {
        return "Difference("+((type == ADD) ? "ADD" : (type == DELETE) ? "DELETE" : "CHANGE")+", "+
               firstStart+", "+firstEnd+", "+secondStart+", "+secondEnd+")";
    }
    
    /**
     * This class represents a difference on a single line.
     */
    public static final class Part extends Object implements Serializable {
        
        private int type;
        private int line;
        private int pos1;
        private int pos2;
        private String text;
        
        private static final long serialVersionUID = 7638201981188907149L;
    
        /**
          * Creates a new instance of LineDiff
          * @param type The type of the difference. Must be one of the {<a href="#DELETE">DELETE</a>,
          *             <a href="#ADD">ADD</a> or <a href="#CHANGE">CHANGE</a>
          * @param line The line number
          * @param pos1 The position on which the difference starts on this line.
          * @param pos2 The position on which the difference ends on this line.
          */
        public Part(int type, int line, int pos1, int pos2) {
            if (type > 2 || type < 0) {
                throw new IllegalArgumentException("Bad Difference type = "+type);
            }
            this.type = type;
            this.line = line;
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
        
        /**
          * Get the difference type. It's one of <a href="#DELETE">DELETE</a>,
          * <a href="#ADD">ADD</a> or <a href="#CHANGE">CHANGE</a>.
          */
        public int getType() {
            return this.type;
        }
    
        /**
          * Get the line number.
          */
        public int getLine() {
            return this.line;
        }
        
        /**
          * Get the position on which the difference starts on this line.
          */
        public int getStartPosition() {
            return this.pos1;
        }
        
        /**
          * Get the position on which the difference ends on this line.
          */
        public int getEndPosition() {
            return this.pos2;
        }
        
        /**
         * Set the text content of the difference.
         *
        public void setText(String text) {
            this.text = text;
        }
        
        /**
         * Get the text content of the difference.
         *
        public String getText() {
            return text;
        }
         */
        
    }
    
}
