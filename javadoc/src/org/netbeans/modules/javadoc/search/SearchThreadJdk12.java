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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
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

package org.netbeans.modules.javadoc.search;

import java.util.StringTokenizer;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;

import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.MutableAttributeSet;

import org.openide.ErrorManager;
import org.openide.util.NbBundle;

/** This class implements the index search through documenation
 * generated by Jdk 1.2 standard doclet
 */

class SearchThreadJdk12 extends IndexSearchThread {

    private Reader in;
    private URL contextURL;

    private boolean stopSearch = false;

    private boolean splitedIndex = false;
    private int currentIndexNumber;
    private URL folder = null;
    private final Object LOCK = new Object();
    
    public SearchThreadJdk12 ( String toFind,
                               URL fo,
                               IndexSearchThread.DocIndexItemConsumer diiConsumer, boolean caseSensitive ) {

        super( toFind, fo, diiConsumer, caseSensitive);
        //this.caseSensitive = caseSensitive;
        
        if (fo.toString().endsWith("/")) {
            // Documentation uses splited index - resolve the right file            
            
            // This is just a try in most cases the fileNumber should be
            // the right one but when some index files are missing we have
            // to find the right one
            folder = fo;
            currentIndexNumber = (int)(Character.toUpperCase( lastField.charAt(0) ))  - 'A' + 1;//toFind.charAt(0) ))  - 'A' + 1;

            if ( currentIndexNumber < 1 ) {
                currentIndexNumber = 1;
            }
            else if ( currentIndexNumber > 26 ) {
                currentIndexNumber = 27;
            }
                
            /*
            if ( currentIndexNumber < 1 || currentIndexNumber > 26 ) {
                currentIndexNumber = 27;
            }
            */
            findFileObject( 0 );
            
            splitedIndex = true;
        }
        else {
            contextURL = indexRoot;
            splitedIndex = false;
        }
    }

    public @Override void stopSearch() {
        Reader br;
        synchronized (LOCK) {
            stopSearch = true;
            br = in;
        }
        
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            ErrorManager.getDefault().notify(e);
        }
    }
    
    public @Override void run() {

        ParserDelegator pd = new ParserDelegator();
        
        if ( indexRoot == null || lastField == null || lastField.length() == 0) {
            taskFinished();
            return;
        }

        
        SearchCallbackJdk12 sc = null;

        int theDirection = 0;
        
        do {
            if ( sc != null ) {
                
                if (sc.badFile != theDirection ) {
                    break;
                }
                
                findFileObject( sc.badFile );
                if ( indexRoot == null ) {
                    // No other file to search
                    break;
                }
            }

            try {
                synchronized (LOCK) {
                    if (stopSearch) {
                        break;
                    }
                    in = new BufferedReader(new InputStreamReader(URLUtils.openStream(indexRoot)));
                }
                pd.parse( in, sc = new SearchCallbackJdk12( splitedIndex, caseSensitive ), true );
            }
            catch ( java.io.IOException e ) {
               // Do nothing
            }
            
            if ( sc.badFile != 0 && theDirection == 0 ) {
                theDirection = sc.badFile;
            }            
        }
        while ( sc.badFile != 0 );

        try {
            if (in != null) {
                in.close();
            }
        }
        catch ( java.io.IOException e ) {
            // Do nothing
        }
        //is.searchEnded();
        taskFinished();
    }
    
    private void findFileObject(int direction) {

        
        if ( direction < 0 ) {
            currentIndexNumber--;
        }
        else if ( direction > 0 ) {
            currentIndexNumber++;
        }
        
        do {
            
            // Assure the only one direction of looking for Files
            
            
            if ( currentIndexNumber < 0 || currentIndexNumber > 27 ) {
                indexRoot = null;
                return;
            }

            if ( folder == null ) {
                indexRoot = null;
                return;
            }

            indexRoot = URLUtils.findOpenable(folder, "index-" + currentIndexNumber + ".html"); // NOI18N

            if ( indexRoot != null ) {
                contextURL = indexRoot;
            }
            else {
                
                currentIndexNumber += direction > 0 ? 1 : -1;
            }
        }
        while ( indexRoot == null );
        
    }

    // Inner classes ------------------------------------------------------------------------------------


    /* These are constants for the inner class */
    
    static private final String STR_CLASS = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_CLASS" );       //NOI18N
    static private final String STR_INTERFACE = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_INTERFACE" );   //NOI18N
    static private final String STR_EXCEPTION = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_EXCEPTION" );   //NOI18N
    static private final String STR_CONSTRUCTOR = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_CONSTRUCTOR" );   //NOI18N
    static private final String STR_METHOD = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_METHOD" );   //NOI18N
    static private final String STR_ERROR = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_ERROR" );   //NOI18N
    static private final String STR_VARIABLE = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_VARIABLE" );   //NOI18N
    static private final String STR_STATIC = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_STATIC" );   //NOI18N
    static private final String STR_DASH = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_DASH" );   //NOI18N
    static private final String STR_PACKAGE = NbBundle.getMessage(SearchThreadJdk12.class, "JDK12_PACKAGE" );   //NOI18N
    private static final String STR_ENUM = NbBundle.getMessage(SearchThreadJdk12.class, "JDK15_ENUM"); //NOI18N
    private static final String STR_ANNTYPE = NbBundle.getMessage(SearchThreadJdk12.class, "JDK15_ANNOTATION_TYPE"); //NOI18N

    static private final int IN_BALAST = 0;
    static private final int IN_DT = 1;
    static private final int IN_AREF = 2;
//    static private final int IN_B = 3;
    static private final int IN_DESCRIPTION = 4;
    static private final int IN_DESCRIPTION_SUFFIX = 5;
    
    /** This inner class parses the JDK 1.2 Documentation index and returns
     *  found indexItems. 
     */

    private class SearchCallbackJdk12 extends HTMLEditorKit.ParserCallback {

        private String              hrefVal;
        private DocIndexItem        currentDii = null;
        private int                 where = IN_BALAST;

        private boolean             splited;
        private boolean             stopOnNext = false;
        
        private int                 badFile = 0;         
        
        int printText = 0;
        
        SearchCallbackJdk12( boolean splited, boolean caseSensitive ) {
            super();
            this.splited = splited;
        }
        
        public @Override void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {

            if ( t == HTML.Tag.DT ) {
                where = IN_DT;
                currentDii = null;
            }
            else if ( t == HTML.Tag.A && where == IN_DT ) {
                where = IN_AREF;
                Object val = a.getAttribute( HTML.Attribute.HREF );
                if ( val != null ) {
                    hrefVal = val.toString();
                    currentDii = new DocIndexItem( null, null, contextURL, hrefVal );
                }
            }
            else if ( t == HTML.Tag.A && (where == IN_DESCRIPTION_SUFFIX || where == IN_DESCRIPTION) ) {
                // Just ignore
            }
            else if ( (t == HTML.Tag.B || t == HTML.Tag.SPAN)/* && where == IN_AREF */) {
                /*where = IN_AREF;*/
                // Ignore formatting
            }
            else {
                where = IN_BALAST;
            }
        }

        public @Override void handleEndTag(HTML.Tag t, int pos) {
            if (t == HTML.Tag.DT && where != IN_BALAST) {
                where = IN_BALAST;
            }
        }

        public @Override void handleText(char[] data, int pos) {
            
            if ( where == IN_AREF ) {
                
                if ( stopOnNext ) {
                    try {
                        in.close();
                        where = IN_BALAST;
                        return;
                    }
                    catch ( java.io.IOException e ) {
                        ErrorManager.getDefault().notify(e);
                    }
                }
                
                String text = new String( data );
                
                if ( splited ) {
                    // it is possible that we search wrong file
                    char first = Character.toUpperCase( lastField.charAt( 0 ) );//toFind.charAt( 0 ) );
                    char curr = Character.toUpperCase( data[0] );
                    if ( first != curr ) {
                        
                        badFile = first < curr ? -1 : 1;
                        try {
                           in.close();
                           where = IN_BALAST;
                           return;
                        }
                        catch ( java.io.IOException e ) {
                            ErrorManager.getDefault().notify(e);
                        }
                    }
                    
                }
                currentDii.setField( text.trim() );
                where = IN_DESCRIPTION;                
            }
            else if ( where == IN_DESCRIPTION  ) {
                String text = new String( data );
                
                /*
                // Stop suffering if we are behind the searched words
                if ( text.substring( 0, Math.min(toFind.length(), text.length()) ).compareTo( toFind ) > 0 ) {
                    try {
                        System.out.println("Stoping suffering");
                        in.close();
                    }
                    catch ( java.io.IOException e ) {
                        ErrorManager.getDefault().notify(e);
                    }
                }
                */
                
                //text = text.toUpperCase();

                int dashIdx = text.indexOf(STR_DASH);
                if (dashIdx < 0) {
                    return;
                }
                text = text.substring(dashIdx - 1);
                currentDii.setRemark( text );

                StringTokenizer st = new StringTokenizer( text );
                String token = st.nextToken();
                if (token.equals(STR_DASH) && st.hasMoreTokens()) {
                    token = st.nextToken();
                }

                boolean isStatic = false;

                if ( token.equalsIgnoreCase( STR_STATIC ) ) {
                    isStatic = true;
                    token = st.nextToken();
                }

                if (token.equalsIgnoreCase(STR_CLASS)) {
                    currentDii.setIconIndex(DocSearchIcons.ICON_CLASS);
                } else if (token.equalsIgnoreCase(STR_INTERFACE)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_INTERFACE );
                } else if (token.equalsIgnoreCase(STR_ENUM)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_ENUM );
                } else if (token.equalsIgnoreCase(STR_ANNTYPE)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_ANNTYPE );
                } else if (token.equalsIgnoreCase(STR_EXCEPTION)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_EXCEPTION );
                } else  if (token.equalsIgnoreCase(STR_ERROR)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_ERROR );
                } else  if (token.equalsIgnoreCase(STR_PACKAGE)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_PACKAGE );
                } else if (token.equalsIgnoreCase(STR_CONSTRUCTOR)) {
                    currentDii.setIconIndex( DocSearchIcons.ICON_CONSTRUCTOR );
                } else if (token.equalsIgnoreCase(STR_METHOD)) {
                    currentDii.setIconIndex( isStatic ? DocSearchIcons.ICON_METHOD_ST : DocSearchIcons.ICON_METHOD );
                } else if (token.equalsIgnoreCase(STR_VARIABLE)) {
                    currentDii.setIconIndex( isStatic ? DocSearchIcons.ICON_VARIABLE_ST : DocSearchIcons.ICON_VARIABLE );
                }

                // Add the item when all information is available
                //insertDocIndexItem( currentDii );

                if (currentDii.getPackage() != null) {
                    where = IN_DESCRIPTION_SUFFIX;
                } else if ( text.endsWith( "." ) ) { // NOI18N
                    where = IN_DESCRIPTION_SUFFIX;
                    currentDii.setPackage( text.substring( text.lastIndexOf( ' ' ) ).trim() );
                } else {
                    where = IN_BALAST;
                }
            }
            else if ( where == IN_DESCRIPTION_SUFFIX ) {
                String remark = String.valueOf(data);
                currentDii.setRemark( currentDii.getRemark() + remark);
                String declaringClass = remark.trim();
                if( !(".".equals(declaringClass))){    //NOI18N
                    currentDii.setDeclaringClass(declaringClass);
                    insertDocIndexItem( currentDii );
                }
            } else {
                where = IN_BALAST;
            }

        }
        /*
        private boolean getContainsText(String text){//, boolean caseSensitive){
            if( lastField.length() != 0 && text.indexOf( lastField ) != -1 )
                return true;
            else if ( middleField.length() != 0 && text.indexOf( middleField ) != -1 )
                return true;
            else if ( reminder.length() != 0 && text.indexOf( reminder ) != -1 )
                return true;
            else 
                return false;            
        }
        */
    }
}
