/* The following code was generated by JFlex 1.4.3 on 2/8/12 9:12 AM */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development and
 * Distribution License("CDDL") (collectively, the "License"). You may not use
 * this file except in compliance with the License. You can obtain a copy of
 * the License at http://www.netbeans.org/cddl-gplv2.html or
 * nbbuild/licenses/CDDL-GPL-2-CP. See the License for the specific language
 * governing permissions and limitations under the License. When distributing
 * the software, include this License Header Notice in each file and include
 * the License file at nbbuild/licenses/CDDL-GPL-2-CP. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided by
 * Oracle in the GPL Version 2 section of the License file that accompanied
 * this code. If applicable, add the following below the License Header, with
 * the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license." If you do not indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to its
 * licensees as provided above. However, if you add GPL Version 2 code and
 * therefore, elected the GPL Version 2 license, then the option applies only
 * if the new code is made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */

package org.netbeans.modules.javascript2.editor.doclets;

import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 2/8/12 9:12 AM from the specification file
 * <tt>/space/repos/web-main/javascript2.editor/tools/jsDocColoringScanner.flex</tt>
 */
public final class JsDocColoringLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int AT = 6;
  public static final int STAR = 4;
  public static final int JSDOC = 2;
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3, 3
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\0\1\2\1\0\1\0\1\1\22\0\1\0\2\0\1\6"+
    "\6\0\1\4\1\14\1\0\1\15\1\0\1\3\12\5\6\0\1\16"+
    "\2\5\1\11\1\12\1\13\10\5\1\7\1\10\13\5\6\0\2\5"+
    "\1\11\1\12\1\13\10\5\1\7\1\10\13\5\57\0\1\5\12\0"+
    "\1\5\4\0\1\5\5\0\27\5\1\0\37\5\1\0\u013f\5\31\0"+
    "\162\5\4\0\14\5\16\0\5\5\11\0\1\5\213\0\1\5\13\0"+
    "\1\5\1\0\3\5\1\0\1\5\1\0\24\5\1\0\54\5\1\0"+
    "\46\5\1\0\5\5\4\0\202\5\10\0\105\5\1\0\46\5\2\0"+
    "\2\5\6\0\20\5\41\0\46\5\2\0\1\5\7\0\47\5\110\0"+
    "\33\5\5\0\3\5\56\0\32\5\5\0\13\5\25\0\12\5\4\0"+
    "\2\5\1\0\143\5\1\0\1\5\17\0\2\5\7\0\17\5\2\0"+
    "\1\5\20\0\1\5\1\0\36\5\35\0\3\5\60\0\46\5\13\0"+
    "\1\5\u0152\0\66\5\3\0\1\5\22\0\1\5\7\0\12\5\4\0"+
    "\12\5\25\0\10\5\2\0\2\5\2\0\26\5\1\0\7\5\1\0"+
    "\1\5\3\0\4\5\3\0\1\5\36\0\2\5\1\0\3\5\4\0"+
    "\14\5\23\0\6\5\4\0\2\5\2\0\26\5\1\0\7\5\1\0"+
    "\2\5\1\0\2\5\1\0\2\5\37\0\4\5\1\0\1\5\7\0"+
    "\12\5\2\0\3\5\20\0\11\5\1\0\3\5\1\0\26\5\1\0"+
    "\7\5\1\0\2\5\1\0\5\5\3\0\1\5\22\0\1\5\17\0"+
    "\2\5\4\0\12\5\25\0\10\5\2\0\2\5\2\0\26\5\1\0"+
    "\7\5\1\0\2\5\1\0\5\5\3\0\1\5\36\0\2\5\1\0"+
    "\3\5\4\0\12\5\1\0\1\5\21\0\1\5\1\0\6\5\3\0"+
    "\3\5\1\0\4\5\3\0\2\5\1\0\1\5\1\0\2\5\3\0"+
    "\2\5\3\0\3\5\3\0\10\5\1\0\3\5\55\0\11\5\25\0"+
    "\10\5\1\0\3\5\1\0\27\5\1\0\12\5\1\0\5\5\46\0"+
    "\2\5\4\0\12\5\25\0\10\5\1\0\3\5\1\0\27\5\1\0"+
    "\12\5\1\0\5\5\3\0\1\5\40\0\1\5\1\0\2\5\4\0"+
    "\12\5\25\0\10\5\1\0\3\5\1\0\27\5\1\0\20\5\46\0"+
    "\2\5\4\0\12\5\25\0\22\5\3\0\30\5\1\0\11\5\1\0"+
    "\1\5\2\0\7\5\72\0\60\5\1\0\2\5\14\0\7\5\11\0"+
    "\12\5\47\0\2\5\1\0\1\5\2\0\2\5\1\0\1\5\2\0"+
    "\1\5\6\0\4\5\1\0\7\5\1\0\3\5\1\0\1\5\1\0"+
    "\1\5\2\0\2\5\1\0\4\5\1\0\2\5\11\0\1\5\2\0"+
    "\5\5\1\0\1\5\11\0\12\5\2\0\2\5\42\0\1\5\37\0"+
    "\12\5\26\0\10\5\1\0\42\5\35\0\4\5\164\0\42\5\1\0"+
    "\5\5\1\0\2\5\25\0\12\5\6\0\6\5\112\0\46\5\12\0"+
    "\51\5\7\0\132\5\5\0\104\5\5\0\122\5\6\0\7\5\1\0"+
    "\77\5\1\0\1\5\1\0\4\5\2\0\7\5\1\0\1\5\1\0"+
    "\4\5\2\0\47\5\1\0\1\5\1\0\4\5\2\0\37\5\1\0"+
    "\1\5\1\0\4\5\2\0\7\5\1\0\1\5\1\0\4\5\2\0"+
    "\7\5\1\0\7\5\1\0\27\5\1\0\37\5\1\0\1\5\1\0"+
    "\4\5\2\0\7\5\1\0\47\5\1\0\23\5\16\0\11\5\56\0"+
    "\125\5\14\0\u026c\5\2\0\10\5\12\0\32\5\5\0\113\5\25\0"+
    "\15\5\1\0\4\5\16\0\22\5\16\0\22\5\16\0\15\5\1\0"+
    "\3\5\17\0\64\5\43\0\1\5\4\0\1\5\3\0\12\5\46\0"+
    "\12\5\6\0\130\5\10\0\51\5\127\0\35\5\51\0\50\5\2\0"+
    "\5\5\u038b\0\154\5\224\0\234\5\4\0\132\5\6\0\26\5\2\0"+
    "\6\5\2\0\46\5\2\0\6\5\2\0\10\5\1\0\1\5\1\0"+
    "\1\5\1\0\1\5\1\0\37\5\2\0\65\5\1\0\7\5\1\0"+
    "\1\5\3\0\3\5\1\0\7\5\3\0\4\5\2\0\6\5\4\0"+
    "\15\5\5\0\3\5\1\0\7\5\164\0\1\5\15\0\1\5\202\0"+
    "\1\5\4\0\1\5\2\0\12\5\1\0\1\5\3\0\5\5\6\0"+
    "\1\5\1\0\1\5\1\0\1\5\1\0\4\5\1\0\3\5\1\0"+
    "\7\5\3\0\3\5\5\0\5\5\u0ebb\0\2\5\52\0\5\5\5\0"+
    "\2\5\4\0\126\5\6\0\3\5\1\0\132\5\1\0\4\5\5\0"+
    "\50\5\4\0\136\5\21\0\30\5\70\0\20\5\u0200\0\u19b6\5\112\0"+
    "\u51a6\5\132\0\u048d\5\u0773\0\u2ba4\5\u215c\0\u012e\5\2\0\73\5\225\0"+
    "\7\5\14\0\5\5\5\0\1\5\1\0\12\5\1\0\15\5\1\0"+
    "\5\5\1\0\1\5\1\0\2\5\1\0\2\5\1\0\154\5\41\0"+
    "\u016b\5\22\0\100\5\2\0\66\5\50\0\14\5\164\0\5\5\1\0"+
    "\207\5\23\0\12\5\7\0\32\5\6\0\32\5\13\0\131\5\3\0"+
    "\6\5\2\0\6\5\2\0\6\5\2\0\3\5\43\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\0\3\2\1\1\1\3\1\4\1\5"+
    "\1\6\1\5\1\7\1\5\2\0\1\10\1\0\1\11"+
    "\2\12\1\0\1\13\1\0\1\14\5\0\1\15\4\0"+
    "\1\16\6\0\1\17\1\20";

  private static int [] zzUnpackAction() {
    int [] result = new int[45];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\17\0\36\0\55\0\74\0\113\0\132\0\151"+
    "\0\74\0\74\0\74\0\74\0\170\0\74\0\207\0\226"+
    "\0\245\0\74\0\170\0\207\0\264\0\74\0\303\0\322"+
    "\0\341\0\74\0\360\0\377\0\u010e\0\u011d\0\u012c\0\74"+
    "\0\u013b\0\u014a\0\u0159\0\u0168\0\74\0\u0177\0\u0186\0\u0195"+
    "\0\u01a4\0\u01b3\0\u01c2\0\74\0\74";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[45];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\3\5\1\6\1\7\12\5\4\10\1\11\11\10\1\12"+
    "\3\13\1\14\1\15\11\13\1\16\16\13\1\17\22\0"+
    "\1\20\1\21\15\0\1\22\13\0\4\10\1\0\11\10"+
    "\4\0\1\14\1\23\17\0\1\24\1\0\5\24\3\0"+
    "\1\20\1\25\1\26\14\20\4\27\1\30\12\27\2\0"+
    "\1\26\14\0\4\27\1\31\12\27\3\0\1\32\1\33"+
    "\1\0\1\34\10\0\3\27\1\32\1\31\12\27\3\0"+
    "\1\32\1\33\21\0\1\35\6\0\1\36\10\0\1\37"+
    "\22\0\1\40\1\41\12\0\1\42\11\0\1\43\22\0"+
    "\1\44\11\0\1\45\25\0\1\46\17\0\1\47\17\0"+
    "\1\50\1\51\5\0\1\52\16\0\1\53\15\0\1\54"+
    "\16\0\1\55\13\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[465];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\1\2\0\1\11\3\1\4\11\1\1\1\11"+
    "\1\1\2\0\1\11\1\0\2\1\1\11\1\0\1\1"+
    "\1\0\1\11\5\0\1\11\4\0\1\11\6\0\2\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[45];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
    private LexerInput input;

    public JsDocColoringLexer(LexerRestartInfo info) {
        this.input = info.input();

        if(info.state() != null) {
            //reset state
            setState((LexerState)info.state());
        } else {
            //initial state
            zzState = zzLexicalState = YYINITIAL;
        }
    }

    public LexerState getState() {
        if (zzState == YYINITIAL && zzLexicalState == YYINITIAL) {
            return null;
        }
        return new LexerState(zzState, zzLexicalState);
    }

    public void setState(LexerState state) {
        this.zzState = state.zzState;
        this.zzLexicalState = state.zzLexicalState;
    }

    public JsDocTokenId nextToken() throws java.io.IOException {
        JsDocTokenId token = yylex();
        return token;
    }

    public static final class LexerState  {
        /** the current state of the DFA */
        final int zzState;
        /** the current lexical state */
        final int zzLexicalState;

        LexerState (int zzState, int zzLexicalState) {
            this.zzState = zzState;
            this.zzLexicalState = zzLexicalState;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LexerState other = (LexerState) obj;
            if (this.zzState != other.zzState) {
                return false;
            }
            if (this.zzLexicalState != other.zzLexicalState) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + this.zzState;
            hash = 31 * hash + this.zzLexicalState;
            return hash;
        }

        @Override
        public String toString() {
            return "LexerState{" + "zzState=" + zzState + ", zzLexicalState=" + zzLexicalState + '}';
        }
    }

 // End user code



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public JsDocColoringLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public JsDocColoringLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1236) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return input.readText().toString();
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return input.readText().charAt(pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return input.readLength();
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    input.backup(number);
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public JsDocTokenId yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzMarkedPosL;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      yychar+= zzMarkedPosL-zzStartRead;

      zzAction = -1;

      int tokenLength = 0;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
            zzInput = input.read();
            if(zzInput == LexerInput.EOF) {
            zzInput = YYEOF;
            break zzForAction;
          }

          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            tokenLength = input.readLength();
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      if(zzInput != YYEOF) {
          input.backup(input.readLength() - tokenLength);
      }

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 5: 
          { yybegin(JSDOC); return JsDocTokenId.COMMENT_BLOCK;
          }
        case 17: break;
        case 1: 
          { return JsDocTokenId.COMMENT_BLOCK;
          }
        case 18: break;
        case 7: 
          { yybegin(JSDOC); yypushback(1); return JsDocTokenId.COMMENT_BLOCK;
          }
        case 19: break;
        case 14: 
          { return JsDocTokenId.COMMENT_SHARED_END;
          }
        case 20: break;
        case 8: 
          { return JsDocTokenId.COMMENT_END;
          }
        case 21: break;
        case 15: 
          { return JsDocTokenId.COMMENT_NOCODE_BEGIN;
          }
        case 22: break;
        case 9: 
          { yybegin(JSDOC); return JsDocTokenId.KEYWORD;
          }
        case 23: break;
        case 3: 
          { yybegin(STAR); yypushback(1);
          }
        case 24: break;
        case 11: 
          { yybegin(JSDOC); return JsDocTokenId.COMMENT_START;
          }
        case 25: break;
        case 10: 
          { return JsDocTokenId.COMMENT_LINE;
          }
        case 26: break;
        case 12: 
          { return JsDocTokenId.COMMENT_CODE;
          }
        case 27: break;
        case 16: 
          { return JsDocTokenId.COMMENT_NOCODE_END;
          }
        case 28: break;
        case 6: 
          { yybegin(YYINITIAL); return JsDocTokenId.COMMENT_END;
          }
        case 29: break;
        case 4: 
          { yybegin(AT); yypushback(1);
          }
        case 30: break;
        case 13: 
          { yybegin(JSDOC); return JsDocTokenId.COMMENT_SHARED_BEGIN;
          }
        case 31: break;
        case 2: 
          { 
          }
        case 32: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case YYINITIAL: {
              if (input.readLength() > 0) {
            // backup eof
            input.backup(1);
            //and return the text as error token
            //System.err.println("Illegal character <"+ yytext()+">");
            return JsDocTokenId.UNKNOWN;
        } else {
            return null;
        }
            }
            case 46: break;
            default:
            return null;
            }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
