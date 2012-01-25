/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2008 Sun Microsystems, Inc.
 */
package org.netbeans.modules.php.editor;

import javax.swing.text.Document;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.swing.text.JTextComponent;
import org.netbeans.lib.editor.codetemplates.api.CodeTemplate;
import org.netbeans.lib.editor.codetemplates.spi.CodeTemplateFilter;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Embedding;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.php.api.util.FileUtils;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import static org.netbeans.modules.php.editor.CompletionContextFinder.CompletionContext;

/**
 *
 * @author Tomasz.Slota@Sun.COM
 */
public class PHPCodeTemplateFilter extends UserTask implements CodeTemplateFilter {

    private volatile boolean accept = false;
    private int caretOffset;
    private CompletionContext context;
    private static final RequestProcessor requestProcessor = new RequestProcessor("PHPCodeTemplateFilter");//NOI18N
    private final Future<Future<Void>> future;

    public PHPCodeTemplateFilter(final Document document, final int offset) {
        this.caretOffset = offset;
        future = requestProcessor.submit(new Callable<Future<Void>>() {
            @Override
            public Future<Void> call() {
                try {
                    return parseDocument(document);
                } catch (ParseException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return null;
            }
        });
    }

    @Override
    public boolean accept(CodeTemplate template) {
        try {
            future.get(300, TimeUnit.MILLISECONDS).get(300, TimeUnit.MILLISECONDS);
            if (template.getContexts() != null && !template.getContexts().isEmpty() && context == CompletionContext.CLASS_CONTEXT_KEYWORDS) {
                String abbrev = template.getAbbreviation();
                return "fnc".equals(abbrev) || "fcom".equals(abbrev); //NOI18N
            }
            return accept;
            
        } catch (TimeoutException ex) {
        } catch (InterruptedException ex) {
        } catch (ExecutionException ee) {
        }
        return false;
    }

    @Override
    public  void run(ResultIterator resultIterator) throws Exception {
        ParserResult parameter = null;
        String mimeType = resultIterator.getSnapshot().getMimeType();
        if (!mimeType.equals(FileUtils.PHP_MIME_TYPE)) {
            for (Embedding e : resultIterator.getEmbeddings()) {
                if (e.getMimeType().equals(FileUtils.PHP_MIME_TYPE)) {
                    resultIterator = resultIterator.getResultIterator(e);
                    break;
                }
            }
            mimeType = resultIterator.getSnapshot().getMimeType();
        }
        if (mimeType.equals(FileUtils.PHP_MIME_TYPE)) {
            parameter = (ParserResult) resultIterator.getParserResult();
            if (parameter != null) {
                context = CompletionContextFinder.findCompletionContext(parameter, caretOffset);
                switch (context) {
                    case EXPRESSION:
                        accept = true;
                        break;
                    case CLASS_CONTEXT_KEYWORDS:
                        accept = true;
                        break;
                }
            }
        }
    }

    public static final class Factory implements CodeTemplateFilter.Factory {

        @Override
        public CodeTemplateFilter createFilter(JTextComponent component, int offset) {
            return new PHPCodeTemplateFilter(component.getDocument(), offset);
        }
    }

    private Future<Void> parseDocument(final Document document) throws ParseException {
        return ParserManager.parseWhenScanFinished(Collections.singleton(Source.create(document)), this);
    }
}
