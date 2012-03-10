/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2012 Sun Microsystems, Inc.
 */
package org.netbeans.modules.java.hints.analysis;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JPanel;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.modules.analysis.spi.Analyzer;
import org.netbeans.modules.java.hints.providers.spi.HintDescription;
import org.netbeans.modules.java.hints.providers.spi.HintMetadata;
import org.netbeans.modules.java.hints.providers.spi.HintMetadata.Options;
import org.netbeans.modules.java.hints.spiimpl.MessageImpl;
import org.netbeans.modules.java.hints.spiimpl.RulesManager;
import org.netbeans.modules.java.hints.spiimpl.batch.BatchSearch;
import org.netbeans.modules.java.hints.spiimpl.batch.BatchSearch.BatchResult;
import org.netbeans.modules.java.hints.spiimpl.batch.BatchSearch.Folder;
import org.netbeans.modules.java.hints.spiimpl.batch.BatchSearch.Resource;
import org.netbeans.modules.java.hints.spiimpl.batch.ProgressHandleWrapper;
import org.netbeans.modules.java.hints.spiimpl.batch.Scopes;
import org.netbeans.modules.java.hints.spiimpl.options.HintsSettings;
import org.netbeans.modules.java.hints.spiimpl.refactoring.Utilities.ClassPathBasedHintWrapper;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.java.hints.Hint.Kind;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author lahvac
 */
@ServiceProvider(service=Analyzer.class)
public class AnalyzerImpl implements Analyzer {

    @Override
    public Iterable<? extends ErrorDescription> analyze(Context ctx) {
        final List<ErrorDescription> result = new ArrayList<ErrorDescription>();
        ProgressHandleWrapper w = new ProgressHandleWrapper(ctx, 10, 90);
        Collection<HintDescription> hints = new ArrayList<HintDescription>();

        for (Entry<HintMetadata, ? extends Collection<? extends HintDescription>> e : RulesManager.getInstance().readHints(null, null, new AtomicBoolean()).entrySet()) {
            //XXX: should check settings, whether this hint is enabled or not
            if (e.getKey().kind != Kind.INSPECTION) continue;
            if (e.getKey().options.contains(Options.NO_BATCH)) continue;
            if (!HintsSettings.isEnabled(e.getKey())) continue;

            hints.addAll(e.getValue());
        }

        BatchResult candidates = BatchSearch.findOccurrences(hints, Scopes.specifiedFoldersScope(Folder.convert(ctx.getScope().getSourceRoots()/*XXX: other content!!!*/)), w);
        List<MessageImpl> problems = new LinkedList<MessageImpl>(candidates.problems);

        BatchSearch.getVerifiedSpans(candidates, w, new BatchSearch.VerifiedSpansCallBack() {
            public void groupStarted() {}
            public boolean spansVerified(CompilationController wc, Resource r, Collection<? extends ErrorDescription> inHints) throws Exception {
                result.addAll(inHints);
                return true;
            }
            public void groupFinished() {}
            public void cannotVerifySpan(Resource r) {
            }
        }, problems, new AtomicBoolean());

        w.finish();

        return result;
    }

    @Override
    public String getDisplayName() {
        return "NetBeans Java Hints";
    }

    private  static final String HINTS_FOLDER = "org-netbeans-modules-java-hints/rules/hints/";  // NOI18N

    @Override
    public Iterable<? extends WarningDescription> getWarnings() {
        List<WarningDescription> result = new ArrayList<WarningDescription>();

        for (Entry<HintMetadata, ? extends Collection<? extends HintDescription>> e : RulesManager.getInstance().readHints(null, null, new AtomicBoolean()).entrySet()) {
            String displayName = e.getKey().displayName;
            String category = e.getKey().category;
            FileObject catFO = FileUtil.getConfigFile(HINTS_FOLDER + category);
            String categoryDisplayName = catFO != null ? getFileObjectLocalizedName(catFO) : "Unknown";

            result.add(WarningDescription.create("text/x-java:" + e.getKey().id, displayName, category, categoryDisplayName));
        }

        return result;
    }

    static String getFileObjectLocalizedName( FileObject fo ) {
        Object o = fo.getAttribute("SystemFileSystem.localizingBundle"); // NOI18N
        if ( o instanceof String ) {
            String bundleName = (String)o;
            try {
                ResourceBundle rb = NbBundle.getBundle(bundleName);
                String localizedName = rb.getString(fo.getPath());
                return localizedName;
            }
            catch(MissingResourceException ex ) {
                // Do nothing return file path;
            }
        }
        return fo.getPath();
    }

    @Override
    public Image getIcon() {
        return ImageUtilities.loadImage("org/netbeans/modules/java/hints/analyzer/ui/warning-glyph.gif");
    }

    @Override
    public Collection<? extends MissingPlugin> requiredPlugins(Context context) {
        return Collections.emptyList();
    }

    @Override
    public CustomizerProvider<ClassPathBasedHintWrapper, JPanel> getCustomizerProvider() {
        return new CustomizerProvider<ClassPathBasedHintWrapper, JPanel>() {
            @Override public ClassPathBasedHintWrapper initialize() {
                ClassPathBasedHintWrapper w = new ClassPathBasedHintWrapper();
                
                w.compute();
                return w;
            }
            @Override public JPanel createComponent(CustomizerContext context) {
                return new JPanel();
            }
        };
    }

    @Override
    public String getId() {
        return "java.hints";
    }

}
