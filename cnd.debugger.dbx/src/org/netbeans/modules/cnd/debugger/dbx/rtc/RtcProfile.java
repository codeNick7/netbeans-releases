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

package org.netbeans.modules.cnd.debugger.dbx.rtc;

import org.netbeans.modules.cnd.debugger.common2.utils.options.OptionSet;
import org.netbeans.modules.cnd.debugger.common2.utils.options.OptionSetOwner;
import java.beans.PropertyChangeSupport;

import org.openide.nodes.Sheet;

import org.netbeans.modules.cnd.makeproject.api.configurations.ConfigurationAuxObject;
import org.netbeans.modules.cnd.api.xml.*;

import org.netbeans.modules.cnd.debugger.common2.debugger.NativeDebuggerManager;
import org.netbeans.modules.cnd.makeproject.api.configurations.Configuration;


public final class RtcProfile implements ConfigurationAuxObject, OptionSetOwner {

    public static final String ID = "dbxrtc"; // NOI18N

    public static final String PROP_RTC_OPTIONS = "rtc_options";// NOI18N
    public static final String PROP_RTC_LOADOBJS = "rtc_loadobjs"; // NOI18N

    private PropertyChangeSupport pcs = null;

    private String baseDir;
    private Loadobjs loadobjs;

    private boolean needSave = false;

    public RtcProfile(String baseDir) {
	this.baseDir = baseDir;
    }

    protected RtcProfile(String baseDir, PropertyChangeSupport pcs) {
	this.baseDir = baseDir;
	this.pcs = pcs;

	// Ensure that dbx always gets a value of /dev/null for this setting.
	// Thus, when AutoContinue is chosen, no extraneous files get
	// created in /tmp.
	// See also CR 6511959

	RtcOption.RTC_ERROR_LOG_FILENAME.setCurrValue(options, "/dev/null"); // NOI18N
    }

    /**
     * Returns an unique id (String) used to retrive this object from the
     * pool of aux objects and for storing the object in xml form and
     * parsing the xml code to restore the object.
     */

    // interface ConfigurationAuxObject
    public String getId() {
	return ID;
    }

    public boolean shared() {
	return false;
    }

    /**
     * Validatable property records whether there is an entity that can
     * validate UI changes, specifically dbx, listening on 'pcs'.
     */

    private boolean validatable;
    public void setValidatable(boolean validatable) {
	this.validatable = validatable;
    } 
    public boolean isValidatable() {
	return validatable;
    } 


    // interface OptionSetOwner
    public OptionSet getOptions() {
	return options;
    } 

    private void notifyOptionsChange() {
	// clones don't have a pcs
	if (pcs != null)
	    pcs.firePropertyChange(PROP_RTC_OPTIONS, null, null);
	needSave = true;
    }

    private OptionSet options = new RtcOptionSet();

    /**
     * Initializes the object to default values
     */
    // interface ???
    public void initialize() {
    }

    //
    // Convenience accessors
    //
    public String getBaseDir() {
	return baseDir;
    }

    public String getExperimentDir() {
	return RtcOption.RTC_EXPERIMENT_DIR.getCurrValue(options);
    }

    public String getExperimentName() {
	return RtcOption.RTC_EXPERIMENT_NAME.getCurrValue(options);
    }

    public Loadobj[] createLoadobjs( String[] los) {
	int size = los.length;
	Loadobj[] loadobjsArray = new Loadobj[size];

	for (int i = 0; i < size; i++) {
	    loadobjsArray[i] = new Loadobj();
	    loadobjsArray[i].lo = los[i];
	    loadobjsArray[i].skip = false;
	}
	return loadobjsArray;
    }

    public void setLoadobjs( String[] los) {

	int size = los.length;

	if (loadobjs == null) {
	    loadobjs = new Loadobjs(this);
	    loadobjs.los = new Loadobj[size];
	} else if (loadobjs.los == null) {
	    loadobjs.los = new Loadobj[size];
	} else {
	    loadobjs.mergeLoadobjs(createLoadobjs(los));
	    return;
	}

	for (int i = 0; i < size; i++) {
	    loadobjs.los[i] = new Loadobj();
	    loadobjs.los[i].lo = los[i];
	    loadobjs.los[i].skip = false;
	}

	loadobjs.adjustLoadobjs();
    }
    
    public Loadobjs getLoadobjs() {

	if (loadobjs == null)
	    return loadobjs = new Loadobjs(this);
	else
	    return loadobjs;
    }

    public static abstract class ProfileCategory {
	protected RtcProfile owner;
	protected String propertyName;

	ProfileCategory(RtcProfile owner, String propertyName) {
	    this.owner = owner;
	    this.propertyName = propertyName;
	}

	/**
	 * We've been mutated
	 */
	protected void delta(Object o, Object n) {
	    // clones don't have an owner
	    if (owner == null)
		return;

	    PropertyChangeSupport pcs = owner.pcs;
	    // clones don't have a pcs
	    if (pcs != null) {
		// SHOULD do some kind of comparison here
		// e.g. if pathmap == newvars we know there is nothing to do...
		pcs.firePropertyChange(propertyName, o, n);
	    }
	    owner.needSave = true;
	}

        @Override
	public abstract boolean equals(Object o);
        @Override
        public abstract int hashCode();
        @Override
	public abstract Object clone();
	public abstract void assign(Object that);
    }

    //
    // XML codec support
    // This stuff ends up in <projectdir>/nbproject/private/profiles.xml
    // 

    // interface ConfigurationAuxObject
    public XMLDecoder getXMLDecoder() {
	return new RtcProfileXMLCodec(this);
    }

    // interface ConfigurationAuxObject
    public XMLEncoder getXMLEncoder() {
	return new RtcProfileXMLCodec(this);
    }

    // interface ConfigurationAuxObject
    public boolean hasChanged() {
	return needSave;
    }

    // interface ConfigurationAuxObject
    public void clearChanged() {
	needSave = false;
    }


    /**
     * Assign all values from a profileAuxObject to this object (reverse
     * of clone)
     */

    // interface ConfigurationAuxObject
    public void assign(ConfigurationAuxObject auxObject) {
	if (!(auxObject instanceof RtcProfile)) {
	    // SHOULD throw exception or log?
	    System.err.print("RtcProfile - assign: RtcProfile object type expected - got " + auxObject); // NOI18N
	    return;
	}
	RtcProfile that = (RtcProfile) auxObject;

	this.baseDir = new String(that.baseDir);
	this.validatable = that.validatable;
	this.options.assign(that.options);
	getLoadobjs().assign(that.loadobjs);
	notifyOptionsChange();
    }
    

    /**
     * Clone itself to an identical (deep) copy.
     */

    // interface ConfigurationAuxObject
    public ConfigurationAuxObject clone(Configuration conf) {
	RtcProfile clone = new RtcProfile(new String(baseDir), null);

	// don't clone pcs ... we'll end up notifying listeners prematurely
	// they will get notified on 'assign()'.

	clone.validatable = this.validatable;
	clone.options = this.options.makeCopy();
	clone.loadobjs = ( Loadobjs ) this.getLoadobjs().clone();

	return clone;
    }

    public Sheet getSheet() {
	Sheet sheet = new Sheet();
	Sheet.Set set;

	if (!NativeDebuggerManager.isStandalone()) {
	    set = new Sheet.Set();
	    set.setName(Catalog.get("Experiment"));
	    set.setDisplayName(Catalog.get("Experiment"));
	    set.setShortDescription(Catalog.get("Experiment"));
	    set.put(RtcOption.RTC_EXPERIMENT_NAME.createNodeProp(this));
	    set.put(RtcOption.RTC_EXPERIMENT_DIR.createBasedNodeProp(this,baseDir));
	    sheet.put(set);

	    set = new Sheet.Set();
	    set.setName("General"); // NOI18N
	    set.setDisplayName("General"); // FIXUP I18N // NOI18N
	    set.put(RtcOption.RTC_ENABLE_AT_DEBUG.createNodeProp(this));
	    sheet.put(set);
	}

	set = new Sheet.Set();
	set.setName(Catalog.get("AccessChecking"));
	set.setDisplayName(Catalog.get("AccessChecking"));
	set.setShortDescription(Catalog.get("AccessChecking"));
	set.put(RtcOption.RTC_ACCESS_ENABLE.createNodeProp(this));
	set.put(RtcOption.RTC_AUTO_CONTINUE.createNodeProp(this));
	set.put(RtcOption.RTC_AUTO_SUPPRESS.createNodeProp(this));
	set.put(RtcOption.RTC_INHERIT.createNodeProp(this));
//	set.put(RtcOption.RTC_USE_TRAPS.createNodeProp(this));
	set.put(RtcOption.RTC_ERROR_LIMIT.createNodeProp(this));
	set.put(new LoadobjNodeProp(this));
	sheet.put(set);

	set = new Sheet.Set();
	set.setName(Catalog.get("MemoryUsage"));
	set.setDisplayName(Catalog.get("MemoryUsage"));
	set.setShortDescription(Catalog.get("MemoryUsage"));
	/* OLD
	set.put(RtcOption.RTC_MEMUSE_ENABLE.createNodeProp(this));
	set.put(RtcOption.RTC_LEAKS_ENABLE.createNodeProp(this));
	*/
	set.put(RtcOption.RTC_LEAKS_MEMUSE_ENABLE.createNodeProp(this));
	set.put(RtcOption.RTC_CUSTOM_STACK_MATCH2.createNodeProp(this));
	/* OLD
	set.put(RtcOption.RTC_CUSTOM_STACK_MATCH.createNodeProp(this));
	set.put(RtcOption.RTC_CUSTOM_STACK_MATCH_VALUE.createNodeProp(this));
	*/
	set.put(RtcOption.RTC_CUSTOM_STACK_FRAMES2.createNodeProp(this));
	/* OLD
	set.put(RtcOption.RTC_CUSTOM_STACK_FRAMES.createNodeProp(this));
	set.put(RtcOption.RTC_CUSTOM_STACK_FRAMES_VALUE.createNodeProp(this));
	*/
	set.put(RtcOption.RTC_BIU_AT_EXIT.createNodeProp(this));
	set.put(RtcOption.RTC_MEL_AT_EXIT.createNodeProp(this));
	sheet.put(set);

	return sheet;
    }
}
