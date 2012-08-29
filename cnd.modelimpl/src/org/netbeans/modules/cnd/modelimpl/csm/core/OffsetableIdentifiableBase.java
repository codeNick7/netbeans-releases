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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
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
package org.netbeans.modules.cnd.modelimpl.csm.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.cnd.api.model.CsmFile;
import org.netbeans.modules.cnd.api.model.CsmUID;
import org.netbeans.modules.cnd.modelimpl.csm.CsmObjectBuilder;
import org.netbeans.modules.cnd.modelimpl.repository.RepositoryUtils;
import org.netbeans.modules.cnd.modelimpl.textcache.NameCache;
import org.netbeans.modules.cnd.modelimpl.uid.UIDObjectFactory;
import org.netbeans.modules.cnd.modelimpl.uid.UIDProviderIml;
import org.netbeans.modules.cnd.repository.spi.Persistent;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataInput;
import org.netbeans.modules.cnd.repository.spi.RepositoryDataOutput;
import org.netbeans.modules.cnd.repository.support.SelfPersistent;

/**
 * class to present object that has unique ID and is offsetable
 * unique ID is used to long-time stored references on Csm Objects
 * 
 * @see CsmUID
 * @author Vladimir Voskresensky
 */
public abstract class OffsetableIdentifiableBase<T> extends OffsetableBase implements CsmIdentifiable, Persistent, SelfPersistent {
    
    private CsmUID<?> uid = null;

    protected OffsetableIdentifiableBase(CsmFile containingFile, int startOffset, int endOffset) {
        super(containingFile, startOffset, endOffset);
    }

    protected abstract CsmUID<?> createUID();

    @Override
    public void dispose() {
        RepositoryUtils.disposeUID(uid, this);
        super.dispose();
    }

    @SuppressWarnings("unchecked")
    @Override
    public CsmUID<T> getUID() {
        if (uid == null) {
            uid = createUID();
        }
        return (CsmUID<T>) uid;
    }

    protected final void setSelfUID() {
        if (uid != null) {
            if (UIDProviderIml.isSelfUID(uid)) {
                return;
            }
            new Exception("replacing " + uid + " to self UID").printStackTrace(); // NOI18N
        }
        uid = UIDProviderIml.createSelfUID(this);
    }

    protected void cleanUID() {
        // this.uid = null;
    }

    public static class NameBuilder implements CsmObjectBuilder {
        
        boolean global = false;
        List<CharSequence> nameParts = new ArrayList<CharSequence>();
        
        public void addNamePart(CharSequence part) {
            nameParts.add(part);
        }

        public void setGlobal() {
            global = true;
        }

        public List<CharSequence> getNameParts() {
            return nameParts;
        }
        
        public CharSequence getName() {
            StringBuilder sb = new StringBuilder();
            boolean firstScope = !global;
            for (CharSequence part : nameParts) {                
                if(!firstScope) {
                    sb.append("::"); // NOI18N
                }
                sb.append(part);
                firstScope = false;
            }
            return NameCache.getManager().getString(sb.toString());
        }
        
        public CharSequence getLastNamePart() {
            return nameParts.get(nameParts.size() - 1);
        }        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // impl of SelfPersistent
    @Override
    public void write(RepositoryDataOutput output) throws IOException {
        super.write(output);
    }

    protected OffsetableIdentifiableBase(RepositoryDataInput input) throws IOException {
        super(input);
    }

    protected final void writeUID(RepositoryDataOutput output) throws IOException {
        UIDObjectFactory factory = UIDObjectFactory.getDefaultFactory();
        factory.writeUID(uid, output);
    }

    protected final void readUID(RepositoryDataInput input) throws IOException {
        UIDObjectFactory factory = UIDObjectFactory.getDefaultFactory();
        this.uid = factory.readUID(input);
    }
}
