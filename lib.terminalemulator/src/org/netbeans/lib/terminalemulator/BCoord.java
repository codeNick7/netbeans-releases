/*
 *	The contents of this file are subject to the terms of the Common Development
 *	and Distribution License (the License). You may not use this file except in
 *	compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 *	or http://www.netbeans.org/cddl.txt.
 *	
 *	When distributing Covered Code, include this CDDL Header Notice in each file
 *	and include the License file at http://www.netbeans.org/cddl.txt.
 *	If applicable, add the following below the CDDL Header, with the fields
 *	enclosed by brackets [] replaced by your own identifying information:
 *	"Portions Copyrighted [year] [name of copyright owner]"
 * 
 * The Original Software is Terminal Emulator.
 * The Initial Developer of the Original Software is Sun Microsystems, Inc..
 * Portions created by Sun Microsystems, Inc. are Copyright (C) 2001-2004.
 * All Rights Reserved.
 *
 * Contributor(s): Ivan Soleimanipour.
 */

package org.netbeans.lib.terminalemulator;

/**
 * A cartesian coordinate class, similar to Point.
 * The equivalent of 'offset' in swing.text.Document.
 * <br>
 * Rows are 0-origin, columns are 0-origin.
 * <p>
 * Why not the regular Java Point? Because ...
 * <ul>
 * <li>Point with 'x' and 'y' is not as clear.
 * <li>Point doesn't implement Comparable, which we depend on a lot.
 * </ul>
 */

class BCoord implements Comparable {
    public int row;
    public int col;

    /**
     * Create a BCoord at the origin (top-left)
     */
    public BCoord() {
	this.row = 0;
	this.col = 0;
    } 

    public BCoord(int row, int col) {
	// Note this is flipped from Points(x, y) sense
	this.row = row;
	this.col = col;
    } 

    public BCoord(BCoord coord) {
	this.row = coord.row;
	this.col = coord.col;
    } 

    public void copyFrom(BCoord src) {
	this.row = src.row;
	this.col = src.col;
    } 

    // Overrides of Object:

    public Object clone() {
	return new BCoord(row, col);
    } 

    public boolean equals(BCoord target) { // XXX param should be Object and also hashCode should be overriden
	if (row != target.row)
	    return false;
	return col == target.col;
    } 

    public String toString() {
	return "(r=" + row + ",c=" + col + ")";	// NOI18N
    } 

    /**
     * Examples:
     * To satisfy Comparable.
     * <p>
     * <pre>
     * a &lt b	=== a.compareTo(b) &lt 0
     * a &gt= b	=== a.compareTo(b) &gt= 0
     * </pre>
     */
    public int compareTo(Object o) throws ClassCastException {
	BCoord target = (BCoord) o;

	// -1 or negative  -> this < o
	//  0              -> this == o
	// +1 or positive  -> this > o

	if (this.row < target.row)
	    return -1;
	else if (this.row > target.row)
	    return +1;
	else {
	    return this.col - target.col;
	}
    } 

    public void clip(int rows, int cols) {
	// BE CAREFUL and clip view BCoords with a view box and buffer
	// BCoords with a buffer box!
	if (row < 0)
	    row = 0;
	else if (row > rows)
	    row = rows;
	if (col < 0)
	    col = 0;
	else if (col > cols)
	    col = cols;
    }
} 
