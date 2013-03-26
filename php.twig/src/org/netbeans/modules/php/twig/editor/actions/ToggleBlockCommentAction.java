/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2013 Oracle and/or its affiliates. All rights reserved.
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
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2013 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.twig.editor.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseAction;
import org.netbeans.editor.BaseDocument;
import org.netbeans.editor.Utilities;
import org.netbeans.editor.ext.ExtKit;
import org.netbeans.modules.csl.api.CslActions;
import org.netbeans.modules.php.twig.editor.lexer.TwigLexerUtils;
import org.netbeans.modules.php.twig.editor.lexer.TwigTokenId;
import org.netbeans.modules.php.twig.editor.lexer.TwigTopLexer;
import org.netbeans.modules.php.twig.editor.ui.options.TwigOptions;

/**
 *
 * @author Ondrej Brejla <obrejla@netbeans.org>
 */
public class ToggleBlockCommentAction extends BaseAction {
    static final long serialVersionUID = -1L;
    private static final Logger LOGGER = Logger.getLogger(ToggleBlockCommentAction.class.getName());
    private static final String FORCE_COMMENT = "force-comment"; //NOI18N
    private static final String FORCE_UNCOMMENT = "force-uncomment"; //NOI18N

    public ToggleBlockCommentAction() {
        super(ExtKit.toggleCommentAction);
    }

    @Override
    public void actionPerformed(ActionEvent evt, JTextComponent target) {
        final AtomicBoolean processedHere = new AtomicBoolean(false);
        if (target != null) {
            if (!target.isEditable() || !target.isEnabled() || !(target.getDocument() instanceof BaseDocument)) {
                target.getToolkit().beep();
                return;
            }
            final int caretOffset = Utilities.isSelectionShowing(target) ? target.getSelectionStart() : target.getCaretPosition();
            final BaseDocument doc = (BaseDocument) target.getDocument();
            doc.runAtomic(new Runnable() {

                @Override
                public void run() {
                    performCustomAction(doc, caretOffset, processedHere);
                }
            });
            if (!processedHere.get()) {
                performDefaultAction(evt, target);
            }
        }
    }

    private void performCustomAction(BaseDocument baseDocument, int caretOffset, AtomicBoolean processedHere) {
        ToggleCommentType toggleCommentType = TwigOptions.getInstance().getToggleCommentType();
        try {
            toggleCommentType.comment(baseDocument, caretOffset, processedHere);
        } catch (BadLocationException ex) {
            LOGGER.log(Level.WARNING, null, ex);
        }
    }

    private void performDefaultAction(ActionEvent evt, JTextComponent target) {
        BaseAction action = (BaseAction) CslActions.createToggleBlockCommentAction();
        if (getValue(FORCE_COMMENT) != null) {
            action.putValue(FORCE_COMMENT, getValue(FORCE_COMMENT));
        }
        if (getValue(FORCE_UNCOMMENT) != null) {
            action.putValue(FORCE_UNCOMMENT, getValue(FORCE_UNCOMMENT));
        }
        action.actionPerformed(evt, target);
    }

    public static enum ToggleCommentType {
        LINE_BY_TWIG {

            @Override
            void comment(BaseDocument baseDocument, int caretOffset, AtomicBoolean processedHere) throws BadLocationException {
                TokenSequence<? extends TwigTokenId> ts = TwigLexerUtils.getTwigMarkupTokenSequence(baseDocument, caretOffset);
                Token<? extends TwigTokenId> token = null;
                if (ts != null) {
                    ts.move(caretOffset);
                    ts.moveNext();
                    token = ts.token();
                }
                if (token != null && isInComment(token.id())) {
                    uncommentToken(ts, baseDocument);
                } else {
                    baseDocument.insertString(Utilities.getRowStart(baseDocument, caretOffset), TwigTopLexer.OPEN_COMMENT, null);
                    baseDocument.insertString(Utilities.getRowEnd(baseDocument, caretOffset), TwigTopLexer.CLOSE_COMMENT, null);
                }
                processedHere.set(true);
            }
        },

        FOCUSED_TWIG_PART {

            @Override
            void comment(BaseDocument baseDocument, int caretOffset, AtomicBoolean processedHere) throws BadLocationException {
                TokenSequence<? extends TwigTokenId> ts = TwigLexerUtils.getTwigMarkupTokenSequence(baseDocument, caretOffset);
                if (ts == null) {
                    processedHere.set(false);
                    return;
                }
                ts.move(caretOffset);
                ts.moveNext();
                Token<? extends TwigTokenId> token = ts.token();
                if (token != null && isInComment(token.id())) {
                    uncommentToken(ts, baseDocument);
                } else {
                    TokenInsertWrapper startTokenWraper = findBackward(ts, Arrays.asList(TwigTokenId.T_TWIG_BLOCK_START, TwigTokenId.T_TWIG_VAR_START));
                    TokenInsertWrapper endTokenWrapper = findForward(ts, Arrays.asList(TwigTokenId.T_TWIG_BLOCK_END, TwigTokenId.T_TWIG_VAR_END));
                    endTokenWrapper.insertAfter(baseDocument);
                    startTokenWraper.insertBefore(baseDocument);
                }
                processedHere.set(true);
            }
        };

        abstract void comment(BaseDocument baseDocument, int caretOffset, AtomicBoolean processedHere) throws BadLocationException;

        protected void uncommentToken(TokenSequence<? extends TwigTokenId> ts, BaseDocument baseDocument) throws BadLocationException {
            int start = ts.offset();
            int end = ts.offset() + ts.token().text().length() - TwigTopLexer.OPEN_COMMENT.length() - TwigTopLexer.CLOSE_COMMENT.length();
            baseDocument.remove(start, TwigTopLexer.OPEN_COMMENT.length());
            baseDocument.remove(end, TwigTopLexer.CLOSE_COMMENT.length());
        }

        private static boolean isInComment(TwigTokenId tokenId) {
            return tokenId == TwigTokenId.T_TWIG_COMMENT;
        }

        private static TokenInsertWrapper findBackward(TokenSequence<? extends TwigTokenId> ts, List<TwigTokenId> tokenIds) {
            assert ts != null;
            assert tokenIds != null;
            TokenInsertWrapper result = TokenInsertWrapper.NONE;
            ts.moveNext();
            int originalOffset = ts.offset();
            while (ts.movePrevious()) {
                Token<? extends TwigTokenId> token = ts.token();
                if (token != null && tokenIds.contains(token.id())) {
                    result = new TokenInsertWrapperImpl(token, ts.offset());
                    break;
                }
            }
            ts.move(originalOffset);
            return result;
        }

        private static TokenInsertWrapper findForward(TokenSequence<? extends TwigTokenId> ts, List<TwigTokenId> tokenIds) {
            assert ts != null;
            assert tokenIds != null;
            TokenInsertWrapper result = TokenInsertWrapper.NONE;
            ts.moveNext();
            int originalOffset = ts.offset();
            while (ts.moveNext()) {
                Token<? extends TwigTokenId> token = ts.token();
                if (token != null && tokenIds.contains(token.id())) {
                    result = new TokenInsertWrapperImpl(token, ts.offset());
                    break;
                }
            }
            ts.move(originalOffset);
            return result;
        }
    }

    private interface TokenInsertWrapper {
        TokenInsertWrapper NONE = new TokenInsertWrapper() {

            @Override
            public void insertBefore(BaseDocument baseDocument) throws BadLocationException {
            }

            @Override
            public void insertAfter(BaseDocument baseDocument) throws BadLocationException {
            }
        };

        void insertBefore(BaseDocument baseDocument) throws BadLocationException;
        void insertAfter(BaseDocument baseDocument) throws BadLocationException;
    }

    private static final class TokenInsertWrapperImpl implements TokenInsertWrapper {
        private final Token<? extends TwigTokenId> token;
        private final int offset;

        private TokenInsertWrapperImpl(Token<? extends TwigTokenId> token, int offset) {
            this.token = token;
            this.offset = offset;
        }

        @Override
        public void insertBefore(BaseDocument baseDocument) throws BadLocationException {
            baseDocument.insertString(offset, TwigTopLexer.OPEN_COMMENT, null);
        }

        @Override
        public void insertAfter(BaseDocument baseDocument) throws BadLocationException {
            baseDocument.insertString(offset + token.text().length(), TwigTopLexer.CLOSE_COMMENT, null);
        }

    }

}
