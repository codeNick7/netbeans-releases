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

package org.netbeans.modules.keyring.jps;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.spi.keyring.KeyringProvider;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=KeyringProvider.class, position=80)
public class KeyringProviderImpl implements KeyringProvider {

    private static final Logger LOG = Logger.getLogger(KeyringProviderImpl.class.getName());

    private CredentialStoreProxy store;

    @Override public boolean enabled() {
        if (NbPreferences.forModule(KeyringProviderImpl.class).get(JPSConfig.PROP_CONFIG_FILE, null) == null) {
            LOG.fine("not initialized");
            return false; // shortcut - do not load other classes
        }
        try {
            initStore();
            LOG.fine("successfully loaded JPS");
            return true;
        } catch (Exception x) {
            LOG.log(Level.FINE, "failed to load JPS", x);
            return false;
        }
    }

    private void initStore() throws Exception {
        JPSConfig cfg = new JPSConfig();
        store = new CredentialStoreProxy(cfg.getConfigFile(), cfg.getImpl(), cfg.getContext(), cfg.getMap());
    }

    @Override public char[] read(String key) {
        try {
            return store.read(key);
        } catch (Exception x) {
            LOG.log(Level.FINE, "could not read " + key, x);
            return null;
        }
    }

    @Override public void save(String key, char[] password, String description) {
        try {
            store.save(key, password, description);
        } catch (Exception x) {
            LOG.log(Level.FINE, "could not save " + key, x);
        }
    }

    @Override public void delete(String key) {
        try {
            store.delete(key);
        } catch (Exception x) {
            LOG.log(Level.FINE, "could not delete " + key, x);
        }
    }

}
