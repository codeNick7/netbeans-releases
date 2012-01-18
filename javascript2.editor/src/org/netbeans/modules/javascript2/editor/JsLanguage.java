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
package org.netbeans.modules.javascript2.editor;

import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.netbeans.modules.csl.api.*;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import org.netbeans.modules.javascript2.editor.lexer.JsTokenId;
import org.netbeans.modules.javascript2.editor.navigation.DeclarationFinderImpl;
import org.netbeans.modules.javascript2.editor.navigation.OccurrencesFinderImpl;
import org.netbeans.modules.javascript2.editor.parser.JsParser;
import org.netbeans.modules.parsing.spi.Parser;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

/**
 *
 * @author Petr Pisl, Tor Norbye
 */

@LanguageRegistration(mimeType="text/javascript", useMultiview = true) //NOI18N
public class JsLanguage extends DefaultLanguageConfig {
    
    @MultiViewElement.Registration(displayName = "#LBL_JSEditorTab",
        iconBase = "org/netbeans/modules/javascript2/editor/resources/javascript.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
        preferredID = "css.source",
        mimeType = "text/javascript",
        position = 1)
    public static MultiViewEditorElement createMultiViewEditorElement(Lookup context) {
        return new MultiViewEditorElement(context);
    }
    
    public JsLanguage() {
        super();
    }

    @Override
    public org.netbeans.api.lexer.Language getLexerLanguage() {
        return JsTokenId.language();
    }

    @Override
    public String getDisplayName() {
        return "JavaScript"; //NOI18N
    }

    @Override
    public Parser getParser() {
        return new JsParser();
    }

    @Override
    public boolean hasStructureScanner() {
        return true;
    }

    @Override
    public StructureScanner getStructureScanner() {
        return new JsStructureScanner();
    }

    @Override
    public SemanticAnalyzer getSemanticAnalyzer() {
        return new JsSemanticAnalyzer();
    }
    
    @Override
    public DeclarationFinder getDeclarationFinder() {
        return new DeclarationFinderImpl();
    }

    @Override
    public boolean hasOccurrencesFinder() {
        return true;
    }

    @Override
    public OccurrencesFinder getOccurrencesFinder() {
        return new OccurrencesFinderImpl();
    }

    @Override
    public CodeCompletionHandler getCompletionHandler() {
        return new JsCodeCompletion();
    }
    
    
}
