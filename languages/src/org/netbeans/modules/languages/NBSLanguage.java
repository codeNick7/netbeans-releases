/*
 * LanguageReader.java
 *
 * Created on February 10, 2006, 11:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.netbeans.modules.languages;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.netbeans.modules.languages.parser.LLSyntaxAnalyser;
import org.netbeans.modules.languages.parser.ParseException;
import org.netbeans.modules.languages.parser.Parser;
import org.netbeans.modules.languages.parser.Parser.Rule;
import org.netbeans.modules.languages.parser.Pattern;
import org.netbeans.modules.languages.parser.SToken;

    
    
/**
 *
 * @author Jan Jancura
 */
public class NBSLanguage {
    
    static final String NBS = "text/x-nbs";
    static final SToken STRING = SToken.create (NBS, "string", null);
    static final SToken OPERATOR = SToken.create (NBS, "operator", null);
    static final SToken KEYWORD = SToken.create (NBS, "keyword", null);
    static final SToken IDENTIFIER = SToken.create (NBS, "identifier", null);
    static final SToken COMMENT = SToken.create (NBS, "comment", null);
    static final SToken WHITESPACE = SToken.create (NBS, "whitespace", null);
    static final SToken BRACE = SToken.create (NBS, "operator", "{");
    static final SToken BRACE2 = SToken.create (NBS, "operator", "}");
    static final SToken BRACKET = SToken.create (NBS, "operator", "[");
    static final SToken BRACKET2 = SToken.create (NBS, "operator", "]");
    static final SToken KEYWORD_TOKEN = SToken.create (NBS, "keyword", "TOKEN");
    static final SToken COLON = SToken.create (NBS, "operator", ":");
    static final SToken SEMICOLON = SToken.create (NBS, "operator", ";");
    static final SToken EQUAL = SToken.create (NBS, "operator", "=");
    static final SToken MINUS = SToken.create (NBS, "operator", "-");
    static final SToken DOLLAR = SToken.create (NBS, "operator", "$");
    static final SToken UPP = SToken.create (NBS, "operator", "^");
    static final SToken OR = SToken.create (NBS, "operator", "|");
    static final SToken COMMA = SToken.create (NBS, "operator", ",");
    static final SToken DOT = SToken.create (NBS, "operator", ".");
    static final SToken PLUS = SToken.create (NBS, "operator", "+");
    static final SToken MULTIPLY = SToken.create (NBS, "operator", "*");
    static final SToken QUESTION = SToken.create (NBS, "operator", "?");
    static final SToken DOUBLE_QUOTES = SToken.create (NBS, "operator", "\"");
    static final SToken LT = SToken.create (NBS, "operator", "<");
    static final SToken GT = SToken.create (NBS, "operator", ">");
    static final SToken PARENTHESIS = SToken.create (NBS, "operator", "(");
    static final SToken PARENTHESIS2 = SToken.create (NBS, "operator", ")");
    
    private static Parser nbsParser;
    
    static Parser getNBSParser () throws ParseException {
        if (nbsParser == null) {
            nbsParser = Parser.create (Arrays.asList (new Rule[] {
                Parser.create (
                    null,
                    Pattern.create (
                        "'ACTION' |" +
                        "'COLOR' |" +
                        "'COMPLETE' |" +
                        "'COMPLETION' |" +
                        "'FOLD' |" +
                        "'HYPERLINK' |" +
                        "'IMPORT' |" +
                        "'INDENT' |" +
                        "'MARK' | " +
                        "'NAVIGATOR' |" +
                        "'PARSE' |" +
                        "'PROPERTIES' |" +
                        "'REFORMAT' |" +
                        "'SKIP' |" +
                        "'STORE' |" +
                        "'TOKEN' |" +
                        "'TOOLTIP'",
                        NBS
                    ),
                    KEYWORD,
                    null
                ),
                Parser.create (
                    null,
                    Pattern.create (
                        "['a'-'z' 'A'-'Z'] ['a'-'z' 'A'-'Z' '0'-'9' '_' '-']*",
                        NBS
                    ),
                    IDENTIFIER,
                    null
                ),
                Parser.create (
                    null,
                    Pattern.create (
                        "':' | '*' | '?' | '+' | '-' | '[' | ']' | '<' | " +
                        "'>' | '^' | '|' | '{' | '}' | '(' | ')' | ',' | " +
                        "'=' | ';' | '.' | '$'",
                        NBS
                    ),
                    OPERATOR,
                    null
                ),
                Parser.create (
                    null,
                    Pattern.create (
                        "'\\\"' ([^'\\\"' '\\\\' '\\r' '\\n'] |" +
                        "('\\\\' ['r' 'n' 't' '\\\\' '\\\'' '\\\"']) )* '\\\"'",
                        NBS
                    ),
                    STRING,
                    null
                ),
                Parser.create (
                    null,
                    Pattern.create (
                        "'\\\'' ([^'\\\'' '\\\\' '\\r' '\\n'] |" +
                        "('\\\\' ['r' 'n' 't' '\\\\' '\\\'' '\\\"']) )* '\\\''",
                        NBS
                    ),
                    STRING,
                    null
                ),
                Parser.create (
                    null,
                    Pattern.create ("'#' [^'\\n' '\\r']* ['\\n' '\\r']+", NBS),
                    COMMENT,
                    null
                ),
                Parser.create (
                    null,
                    Pattern.create ("['\\n' '\\r' ' ' '\\t']+", NBS),
                    WHITESPACE,
                    null
                    )
            }));
        }
        return nbsParser;
    }
    
    private static LLSyntaxAnalyser nbsAnalyser;
    
    static LLSyntaxAnalyser getNBSAnalyser () throws ParseException {
        if (nbsAnalyser == null)
            nbsAnalyser = LLSyntaxAnalyser.create (
                Arrays.asList (new LLSyntaxAnalyser.Rule[] {
                    rule ("S", new Object[] {"token", "S"}),
                    rule ("S", new Object[] {"tokenState", "S"}),
                    rule ("S", new Object[] {"grammarRule", "S"}),
                    rule ("S", new Object[] {"command", "S"}),
                    rule ("S", new Object[] {}),
                    
                    rule ("tokenState", new Object[] {"state", "tokenState1"}),
                    rule ("tokenState1", new Object[] {COLON, "token"}),
                    rule ("tokenState1", new Object[] {BRACE, "tokenGroup"}),
                    rule ("token", new Object[] {KEYWORD_TOKEN, COLON, IDENTIFIER, COLON, PARENTHESIS, "regularExpression", PARENTHESIS2, "token2"}),
                    rule ("token2", new Object[] {COLON, "state"}),
                    rule ("token2", new Object[] {}),
                    rule ("state", new Object[] {LT, IDENTIFIER, GT}),
                    rule ("tokenGroup", new Object[] {"tokensInGroup", BRACE2}),
                    rule ("tokensInGroup", new Object[] {"tokenInGroup", "tokensInGroup"}),
                    rule ("tokensInGroup", new Object[] {}),
                    rule ("tokenInGroup", new Object[] {KEYWORD_TOKEN, COLON, IDENTIFIER, COLON, PARENTHESIS, "regularExpression", PARENTHESIS2, "token2"}),
                    
                    rule ("regularExpression", new Object[] {"reChoice", "regularExpression1"}),
                    rule ("regularExpression1", new Object[] {OR, "reChoice", "regularExpression1"}),
                    rule ("regularExpression1", new Object[] {}),
                    rule ("reChoice", new Object[] {"rePart", "reChoice1"}),
                    rule ("reChoice1", new Object[] {"rePart", "reChoice1"}),
                    rule ("reChoice1", new Object[] {}),
                    rule ("rePart", new Object[] {STRING, "rePartOperatorOrMinus"}),
                    rule ("rePart", new Object[] {DOT, "rePartOperator"}),
                    rule ("rePart", new Object[] {"reClass", "rePartOperator"}),
                    rule ("rePart", new Object[] {PARENTHESIS, "regularExpression", PARENTHESIS2, "rePartOperator"}),
                    rule ("rePartOperator", new Object[] {}),
                    rule ("rePartOperator", new Object[] {PLUS}),
                    rule ("rePartOperator", new Object[] {QUESTION}),
                    rule ("rePartOperator", new Object[] {MULTIPLY}),
                    rule ("rePartOperatorOrMinus", new Object[] {MINUS, STRING}),
                    rule ("rePartOperatorOrMinus", new Object[] {"rePartOperator"}),
                    rule ("reClass", new Object[] {BRACKET, "reInClassNegation", "reInClass", BRACKET2}),
                    rule ("reInClassNegation", new Object[] {UPP}),
                    rule ("reInClassNegation", new Object[] {}),
                    rule ("reInClass", new Object[] {STRING, "reInClassMinus", "reInClass1"}),
                    rule ("reInClass1", new Object[] {STRING, "reInClassMinus", "reInClass1"}),
                    rule ("reInClass1", new Object[] {}),
                    rule ("reInClassMinus", new Object[] {MINUS, STRING}),
                    rule ("reInClassMinus", new Object[] {}),
                    
                    rule ("grammarRule", new Object[] {IDENTIFIER, EQUAL, "grRightSide", SEMICOLON}),
                    rule ("grRightSide", new Object[] {"grChoice", "grRightSide1"}),
                    rule ("grRightSide1", new Object[] {OR, "grChoice", "grRightSide1"}),
                    rule ("grRightSide1", new Object[] {}),
                    rule ("grChoice", new Object[] {"grPart", "grChoice"}),
                    rule ("grChoice", new Object[] {}),
                    rule ("grPart", new Object[] {IDENTIFIER, "grOperator"}),
                    rule ("grPart", new Object[] {"tokenDef", "grOperator"}),
                    rule ("grPart", new Object[] {STRING, "grOperator"}),
                    rule ("grPart", new Object[] {BRACKET, "grRightSide", BRACKET2}),
                    rule ("grPart", new Object[] {PARENTHESIS, "grRightSide", PARENTHESIS2, "grOperator"}),
                    rule ("grOperator", new Object[] {PLUS}),
                    rule ("grOperator", new Object[] {MULTIPLY}),
                    rule ("grOperator", new Object[] {QUESTION}),
                    rule ("grOperator", new Object[] {}),
                    rule ("tokenDef", new Object[] {LT, IDENTIFIER, "tokenDef1", GT}),
                    rule ("tokenDef1", new Object[] {COMMA, STRING}),
                    rule ("tokenDef1", new Object[] {}),
                    
                    rule ("command", new Object[] {KEYWORD, "command0"}),
                    rule ("command0", new Object[] {BRACE, "properties", BRACE2}),
                    rule ("command0", new Object[] {COLON, "class", "command1"}),
                    rule ("command0", new Object[] {"command2"}),
                    rule ("command1", new Object[] {BRACE, "properties", BRACE2}),
                    rule ("command1", new Object[] {COLON, "command2"}),
                    rule ("command1", new Object[] {}),
                    rule ("command2", new Object[] {"class"}),
                    rule ("command2", new Object[] {STRING}),
                    rule ("properties", new Object[] {"property", "properties"}),
                    rule ("properties", new Object[] {}),
                    rule ("property", new Object[] {IDENTIFIER, COLON, "propertyValue", SEMICOLON}),
                    rule ("propertyValue", new Object[] {STRING}),
                    rule ("propertyValue", new Object[] {"class"}),
                    
                    rule ("class", new Object[] {IDENTIFIER, "class1"}),
                    rule ("class1", new Object[] {DOT, IDENTIFIER, "class1"}),
                    rule ("class1", new Object[] {}),
                }),
                new HashSet (Arrays.asList (new Object[] {
                    "whitespace",
                    "comment"
                }))
            );
        return nbsAnalyser;
    }
    
    private static LLSyntaxAnalyser.Rule rule (String nt, Object[] right) {
        return LLSyntaxAnalyser.Rule.create (
            "text/x-nbs", 
            nt, 
            Arrays.asList (right)
        );
    }
}
