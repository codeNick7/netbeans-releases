/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2015 Oracle and/or its affiliates. All rights reserved.
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
 * Portions Copyrighted 2015 Sun Microsystems, Inc.
 */
package org.netbeans.modules.docker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.event.ChangeListener;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.openide.util.ChangeSupport;
import org.openide.util.NbPreferences;
import org.openide.util.Parameters;

/**
 *
 * @author Petr Hejl
 */
public class DockerInstance {

    static final String INSTANCES_KEY = "instances";

    private static final Logger LOGGER = Logger.getLogger(DockerInstance.class.getName());

    private static final String DISPLAY_NAME_KEY = "display_name";

    private static final String URL_KEY = "url";

    private static final String CERTIFICATE_PATH_KEY = "certificate";

    private static final String KEY_PATH_KEY = "key";

    private final ChangeSupport changeSupport = new ChangeSupport(this);

    private final InstanceListener listener = new InstanceListener();

    private final Preferences prefs;

    private final ContainerFactory containerFactory = new ContainerFactory(this);

    private DockerInstance(Preferences prefs) {
        this.prefs = prefs;
    }

    @NonNull
    public static DockerInstance create(@NonNull String displayName, @NonNull String url,
            @NullAllowed File certificate, @NullAllowed File key) {
        Parameters.notNull("displayName", displayName);
        Parameters.notNull("url", url);

        Preferences global = NbPreferences.forModule(DockerInstance.class).node(INSTANCES_KEY);
        // XXX better id?
        // XXX synchronization ?
        Preferences prefs = global.node(displayName);
        prefs.put(DISPLAY_NAME_KEY, displayName);
        prefs.put(URL_KEY, url);
        if (certificate != null) {
            prefs.put(CERTIFICATE_PATH_KEY, certificate.getPath());
        }
        if (key != null) {
            prefs.put(KEY_PATH_KEY, key.getPath());
        }
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            // XXX better solution?
            throw new IllegalStateException(ex);
        }
        DockerInstance instance = new DockerInstance(prefs);
        instance.init();
        return instance;
    }

    public static Collection<? extends DockerInstance> findAll() {
        Preferences global = NbPreferences.forModule(DockerInstance.class).node(INSTANCES_KEY);
        List<DockerInstance> instances = new ArrayList<>();
        try {
            for (String name : global.childrenNames()) {
                Preferences p = global.node(name);
                String displayName = p.get(DISPLAY_NAME_KEY, null);
                String url = p.get(URL_KEY, null);
                if (displayName != null && url != null) {
                    DockerInstance instance = new DockerInstance(p);
                    instance.init();
                    instances.add(instance);
                } else {
                    LOGGER.log(Level.INFO, "Invalid Docker instance {0}", name);
                }
            }
        } catch (BackingStoreException ex) {
            LOGGER.log(Level.WARNING, null, ex);
        }
        return instances;
    }

    public ContainerFactory getContainerFactory() {
        return containerFactory;
    }

    public String getDisplayName() {
        return prefs.get(DISPLAY_NAME_KEY, null);
    }

    public String getUrl() {
        return prefs.get(URL_KEY, null);
    }

    public File getCertificateFile() {
        String path = prefs.get(CERTIFICATE_PATH_KEY, null);
        return path == null ? null : new File(path);
    }

    public File getKeyFile() {
        String path = prefs.get(KEY_PATH_KEY, null);
        return path == null ? null : new File(path);
    }

    public void remove() {
        try {
            prefs.removeNode();
        } catch (BackingStoreException ex) {
            LOGGER.log(Level.WARNING, null, ex);
        }
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    private void init() {
        prefs.addPreferenceChangeListener(listener);
    }

    private class InstanceListener implements PreferenceChangeListener {

        @Override
        public void preferenceChange(PreferenceChangeEvent evt) {
            changeSupport.fireChange();
        }
    }

}
