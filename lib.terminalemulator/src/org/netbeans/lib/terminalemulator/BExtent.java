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
 * Portions created by Sun Microsystems, Inc. are Copyright (C) 2001.
 * All Rights Reserved.
 *
 * Contributor(s): Ivan Soleimanipour.
 */

/*
 * "BExtent.java"
 * BExtent.java 1.5 01/07/26
 */

package org.netbeans.lib.terminalemulator;

class BExtent {
    public BCoord begin;
    public BCoord end;

    public BExtent(BCoord begin, BCoord end) {
	this.begin = (BCoord) begin.clone();
	this.end = (BCoord) end.clone();
    } 

    public Extent toExtent(int bias) {
	return new Extent(new Coord(begin, bias), new Coord(end, bias));
    }

    /**
     * Override Object.toString
     */
    public String toString() {
	return "BExtent[" + begin + " " + end + "]";	// NOI18N
    } 
}
