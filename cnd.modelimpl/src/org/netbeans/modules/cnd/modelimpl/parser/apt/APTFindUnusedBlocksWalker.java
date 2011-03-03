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
 * Portions Copyrighted 2007 Sun Microsystems, Inc.
 */
package org.netbeans.modules.cnd.modelimpl.parser.apt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.cnd.api.model.CsmFile;
import org.netbeans.modules.cnd.api.model.CsmOffsetable;
import org.netbeans.modules.cnd.apt.structure.APT;
import org.netbeans.modules.cnd.apt.structure.APTFile;
import org.netbeans.modules.cnd.apt.support.APTFileCacheEntry;
import org.netbeans.modules.cnd.apt.support.APTPreprocHandler;
import org.netbeans.modules.cnd.modelimpl.csm.core.FileImpl;
import org.netbeans.modules.cnd.modelimpl.csm.core.Utils;
import org.openide.util.Exceptions;

/**
 * This walker gathers information about code blocks being hidden by preprocessor
 * commands
 * 
 * @author Sergey Grinev
 */
public class APTFindUnusedBlocksWalker extends APTSelfWalker {

    private final List<CsmOffsetable> blocks = new ArrayList<CsmOffsetable>();
    private final CsmFile csmFile;
    public List<CsmOffsetable> getBlocks() {
        return blocks;
    }
    
    protected void addBlock(int startOffset, int endOffset) {
        blocks.add(Utils.createOffsetable(csmFile, startOffset, endOffset));
    }
    
    public APTFindUnusedBlocksWalker(APTFile apt, CsmFile csmFile, APTPreprocHandler preprocHandler, APTFileCacheEntry cacheEntry) {
        super(apt, preprocHandler, cacheEntry);
        this.csmFile = csmFile;
    }
    
    protected @Override boolean onIfdef(APT apt) {
        boolean val = super.onIfdef(apt);
        handleIf(apt, val);
        return val;
    }
    
    protected @Override boolean onIfndef(APT apt) {
        boolean val = super.onIfndef(apt);
        handleIf(apt, val);
        return val;
    }

    protected @Override boolean onIf(APT apt) {
        boolean val = super.onIf(apt);
        handleIf(apt, val);
        return val;
    }

    protected @Override boolean onElif(APT apt, boolean wasInPrevBranch) {
        boolean val = super.onElif(apt, wasInPrevBranch);
        handleIf(apt, val);
        return val;
    }

    protected @Override boolean onElse(APT apt, boolean wasInPrevBranch) {
        boolean val = super.onElse(apt, wasInPrevBranch);
        handleIf(apt, val);
        return val;
    }

    protected @Override void onErrorNode(APT apt) {
        super.onErrorNode(apt);
        int startOffset = apt.getEndOffset();
        int endOffset;
        try {
            endOffset = ((FileImpl) csmFile).getBuffer().getCharBuffer().length;
        } catch (IOException ex) {
            endOffset = 0;
        }
        addBlock(startOffset, endOffset);
    }

    @Override
    protected void onPragmaNode(APT apt) {
        super.onPragmaNode(apt);
        if (isStopped()) {
            int startOffset = apt.getEndOffset();
            int endOffset;
            try {
                endOffset = ((FileImpl) csmFile).getBuffer().getCharBuffer().length;
            } catch (IOException ex) {
                endOffset = 0;
            }
            addBlock(startOffset, endOffset);
        }
    }

    private void handleIf(APT opener, boolean value) {
        APT closer = opener.getNextSibling();
        if (closer != null && !value) {
            addBlock(opener.getEndOffset(), closer.getOffset()-1);
        } 
    }
}
