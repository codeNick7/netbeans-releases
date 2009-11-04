/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
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

package org.netbeans.modules.cnd.api.compilers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.BaseFolder;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.CMakeDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.CompilerDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.DebuggerDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.LinkerDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.MakeDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.QMakeDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.ScannerDescriptor;
import org.netbeans.modules.cnd.api.compilers.ToolchainManager.ToolchainDescriptor;
import org.netbeans.modules.cnd.utils.CndUtils;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironmentFactory;
import org.netbeans.modules.nativeexecution.api.ExecutionEnvironment;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * A container for information about a set of related compilers, typicaly from a vendor or
 * redistributor.
 */
public class CompilerSet {

    private static final String UNKNOWN = "Unknown"; // NOI18N

    public boolean isAutoGenerated() {
        return autoGenerated;
    }

    public void setAutoGenerated(boolean autoGenerated) {
        this.autoGenerated = autoGenerated;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isUrlPointer(){
        if (getDirectory() == null || getDirectory().length() == 0){
            return flavor.descriptor.getUpdateCenterUrl() != null && flavor.descriptor.getModuleID() != null;
        }
        return false;
    }

    void setAsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void unsetDefault() {
        this.isDefault = false;  // to set to true use CompilerSetManager.setDefault()
    }

    /** Recognized (and prioritized) types of compiler sets */
    public static final class CompilerFlavor {
        private static final List<CompilerFlavor> flavors = new ArrayList<CompilerFlavor>();
        private static final Map<Integer, CompilerFlavor> unknown = new HashMap<Integer, CompilerFlavor>();
        static {
            for(ToolchainDescriptor descriptor : ToolchainManager.getImpl().getAllToolchains()){
                flavors.add(new CompilerFlavor(descriptor.getName(), descriptor));
            }
        }
    
        private String sval;
        private ToolchainDescriptor descriptor;
        
        CompilerFlavor(String sval, ToolchainDescriptor descriptor) {
            this.sval = sval;
            this.descriptor = descriptor;
        }
        
        public ToolchainDescriptor getToolchainDescriptor(){
            return descriptor;
        }

        public boolean isGnuCompiler() {
            ToolchainDescriptor d = getToolchainDescriptor();
            if (d != null) {
                for(String f : d.getFamily()){
                    if ("GNU".equals(f)) { // NOI18N
                        return true;
                    }
                }
            }
            return false;
        }
        
        public boolean isSunStudioCompiler() {
            ToolchainDescriptor d = getToolchainDescriptor();
            if (d != null) {
                for(String f : d.getFamily()){
                    if ("SunStudio".equals(f)) { // NOI18N
                        return true;
                    }
                }
            }
            return false;
        }
        
        public boolean isMinGWCompiler(){
            return "MinGW".equals(sval); // NOI18N
        }

        public boolean isCygwinCompiler(){
            return "Cygwin".equals(sval); // NOI18N
        }

        public String getCommandFolder(int platform){
            ToolchainDescriptor d = getToolchainDescriptor();
            if (d != null) {
                return ToolchainManager.getImpl().getCommandFolder(d, platform);
            }
            return null;
        }
        
        public static CompilerFlavor getUnknown(int platform){
            CompilerFlavor unknownFlavor = unknown.get(platform);
            if (unknownFlavor == null) {
                unknownFlavor = _getUnknown(platform);
            }
            return unknownFlavor;
        }

        private static CompilerFlavor _getUnknown(int platform){
            CompilerFlavor unknownFlavor = null;
            synchronized(unknown) {
                unknownFlavor = unknown.get(platform);
                if (unknownFlavor == null) {
                    ToolchainDescriptor d = ToolchainManager.getImpl().getToolchain("GNU", platform); // NOI18N
                    if (d == null) {
                        List<ToolchainDescriptor> list = ToolchainManager.getImpl().getToolchains(platform);
                        if (list.size()>0){
                            d = list.get(0);
                        }
                    }
                    if (d == null) {
                        d = new UnknownToolchainDescriptor();
                    }
                    unknownFlavor = new CompilerFlavor(UNKNOWN, d);
                    unknown.put(platform, unknownFlavor);
                }
            }
            return unknownFlavor;
        }

        public static CompilerFlavor toFlavor(String name, int platform) {
            if (UNKNOWN.equals(name)) {
                return getUnknown(platform);
            }
            for (CompilerFlavor flavor : flavors) {
                if (name.equals(flavor.sval) && ToolchainManager.getImpl().isPlatforSupported(platform, flavor.getToolchainDescriptor())) {
                    return flavor;
                }
            }
            return null;
        }
        
        public static String mapOldToNew(String flavor, int version) {
            if (version <=43) {
                if (flavor.equals("Sun")) { // NOI18N
                    return "SunStudio"; // NOI18N
                }
                else if (flavor.equals("SunExpress")) { // NOI18N
                    return "SunStudioExpress"; // NOI18N
                }
                else if (flavor.equals("Sun12")) { // NOI18N
                    return "SunStudio_12"; // NOI18N
                }
                else if (flavor.equals("Sun11")) { // NOI18N
                    return "SunStudio_11"; // NOI18N
                }
                else if (flavor.equals("Sun10")) { // NOI18N
                    return "SunStudio_10"; // NOI18N
                }
                else if (flavor.equals("Sun9")) { // NOI18N
                    return "SunStudio_9"; // NOI18N
                }
                else if (flavor.equals("Sun8")) { // NOI18N
                    return "SunStudio_8"; // NOI18N
                }
                else if (flavor.equals("DJGPP")) { // NOI18N
                    return "GNU"; // NOI18N
                }
                else if (flavor.equals("Interix")) { // NOI18N
                    return "GNU"; // NOI18N
                }
                else if (flavor.equals(UNKNOWN)) {
                    return "GNU"; // NOI18N
                }
            }
            return flavor;
        }
     
        private static boolean isPlatforSupported(CompilerFlavor flavor, int platform){
            ToolchainDescriptor d = flavor.getToolchainDescriptor();
            if (d != null){
                return ToolchainManager.getImpl().isPlatforSupported(platform, d);
            }
            return true;
        }

        public static List<CompilerFlavor> getFlavors(int platform) {
            ArrayList<CompilerFlavor> list = new ArrayList<CompilerFlavor>();
            for (CompilerFlavor flavor : flavors){
                if (isPlatforSupported(flavor, platform)) {
                    list.add(flavor);
                }
            }
            return list;
        }
    
        @Override
        public String toString() {
            return sval;
        }
    }
    
    public static final String None = "None"; // NOI18N
    
    private CompilerFlavor flavor;
    private String name;
    private String displayName;
    private boolean autoGenerated;
    private boolean isDefault;
    private StringBuilder directory = new StringBuilder(256);
    private final ArrayList<Tool> tools = new ArrayList<Tool>();
    private String librarySearchOption;
    private String dynamicLibrarySearchOption;
    private String libraryOption;
    private CompilerProvider compilerProvider;
    private String driveLetterPrefix = "/"; // NOI18N
    private Map<Integer,String> pathSearch;
    
    /** Creates a new instance of CompilerSet */
    protected CompilerSet(CompilerFlavor flavor, String directory, String name) {
        addDirectory(directory);
        
        compilerProvider = CompilerProvider.getInstance();
        driveLetterPrefix = flavor.getToolchainDescriptor().getDriveLetterPrefix();
        
        if (name != null) {
            this.name = name;
        } else {
            this.name = flavor.toString();
        }
        //displayName = mapNameToDisplayName(flavor);
        displayName = flavor.getToolchainDescriptor().getDisplayName();
        librarySearchOption = flavor.getToolchainDescriptor().getLinker().getLibrarySearchFlag();
        dynamicLibrarySearchOption = flavor.getToolchainDescriptor().getLinker().getDynamicLibrarySearchFlag();
        libraryOption = flavor.getToolchainDescriptor().getLinker().getLibraryFlag();
        this.flavor = flavor;
        setAutoGenerated(true);
        setAsDefault(false);
    }
    
    protected CompilerSet(int platform) {
        this.name = None;
        this.flavor = CompilerFlavor.getUnknown(platform);
        this.displayName = NbBundle.getMessage(CompilerSet.class, "LBL_EmptyCompilerSetDisplayName"); // NOI18N
        
        compilerProvider = CompilerProvider.getInstance();
        setAutoGenerated(true);
        setAsDefault(false);
    }
    
    public CompilerSet createCopy() {
        CompilerSet copy = new CompilerSet(flavor, getDirectory(), name);
        copy.setAutoGenerated(isAutoGenerated());
        copy.setAsDefault(isDefault());
        
        for (Tool tool : getTools()) {
            copy.addTool(tool.createCopy());
        }
        
        return copy;
    }
    
    /**
     * Get an existing compiler set. If it doesn't exist, get an empty one based on the requested name.
     *
     * @param name The name of the compiler set we want
     * @returns The best fitting compiler set (may be an empty CompilerSet)
     */
    public static CompilerSet getCompilerSet(ExecutionEnvironment env, String name, int platform) {
        CompilerSet cs = CompilerSetManager.getDefault(env).getCompilerSet(CompilerFlavor.toFlavor(name, platform));
        if (cs == null) {
            CompilerFlavor flavor = CompilerFlavor.toFlavor(name, platform);
            flavor = flavor == null ? CompilerFlavor.getUnknown(platform) : flavor;
            cs = new CompilerSet(flavor, "", null); // NOI18N
        }
        return cs;
    }
    
    public static List<CompilerFlavor> getCompilerSetFlavor(String directory, int platform) {
        List<CompilerFlavor> list = new ArrayList<CompilerFlavor>();
        for(ToolchainDescriptor d : ToolchainManager.getImpl().getToolchains(platform)) {
            if (ToolchainManager.getImpl().isMyFolder(directory, d, platform, false)){
                CompilerFlavor f = CompilerFlavor.toFlavor(d.getName(), platform);
                if (f != null) {
                    list.add(f);
                }
            }
        }
        return list;
    }
    
    public static CompilerSet getCustomCompilerSet(String directory, CompilerFlavor flavor, String name) {
        CompilerSet cs = new CompilerSet(flavor, directory, name);
        cs.setAutoGenerated(false);
        return cs;
    }
    
    public static CompilerSet getCompilerSet(String directory, int platform) {
        List<CompilerFlavor> flavors = getCompilerSetFlavor(directory, platform);
        if (flavors.size()>0) {
            return new CompilerSet(flavors.get(0), directory, null);
        }
        return new CompilerSet(CompilerFlavor.getUnknown(platform), directory, null);
    }
    
    /**
     * If no compilers are found an empty compiler set is created so we don't have an empty list.
     * Too many places in CND expect a non-empty list and throw NPEs if it is empty!
     */
    protected static CompilerSet createEmptyCompilerSet(int platform) {
        return new CompilerSet(platform);
    }
    
    public boolean isGnuCompiler() {
        return flavor.isGnuCompiler();
    }

    public boolean isSunCompiler() {
        return flavor.isSunStudioCompiler();
    }

    public CompilerFlavor getCompilerFlavor() {
        return flavor;
    }
    
    public void setFlavor(CompilerFlavor flavor) {
        this.flavor = flavor;
    }
    
    public void addDirectory(String path) {
        if (path != null) {
            if (directory.length() == 0) {
                directory.append(path);
            } else {
                directory.append(File.pathSeparator);
                directory.append(path);
            }
        }
    }
    
    public String getDirectory() {
        return directory.toString();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDisplayName() {
        // TODO: this thing is never used although it's being set to informative values by personality
        return displayName;
    }
    
    public Tool addTool(ExecutionEnvironment env, String name, String path, int kind) {
        if (findTool(kind) != null) {
            return null;
        }
        Tool tool = compilerProvider.createCompiler(env, flavor, kind, name, Tool.getToolDisplayName(kind), path);
        if (!tools.contains(tool)) {
            tools.add(tool);
        }
        tool.setCompilerSet(this);
        return tool;
    }
    
    public void addTool(Tool tool) {
        tools.add(tool);
        tool.setCompilerSet(this);
    }
    
    public Tool addNewTool(ExecutionEnvironment env, String name, String path, int kind) {
        Tool tool = compilerProvider.createCompiler(env, flavor, kind, name, Tool.getToolDisplayName(kind), path);
        tools.add(tool);
        tool.setCompilerSet(this);
        return tool;
    }
    
    public void removeTool(String name, String path, int kind) {
        for (Tool tool : tools) {
            if (tool.getName().equals(name) && tool.getPath().equals(path) && tool.getKind() == kind) {
                tools.remove(tool);
                tool.setCompilerSet(null);
                return;
            }
        }
    }
    
    public void reparent(String newPath) {
        directory = new StringBuilder(256);
        addDirectory(newPath);
        tools.clear();
    }
    
    /**
     * Get a tool by name
     *
     * @param name The name of the desired tool
     * @return The Tool or null
     */
    public Tool getTool(String name) {
        String exename = null;
        
        if (Utilities.isWindows()) {
            exename = name + ".exe"; // NOI18N
        }
        for (Tool tool : tools) {
            if (tool.getDisplayName().equals(name) || tool.getName().equals(name) ||
                    (exename != null && tool.getName().equals(exename))) {
                return tool;
            }
        }
        return null;
    }
    
    /**
     * Get a tool by name
     *
     * @param name The name of the desired tool
     * @return The Tool or null
     */
    public Tool getTool(String name, int kind) {
        String exename = null;
        
        if (Utilities.isWindows()) {
            exename = name + ".exe"; // NOI18N
        }
        for (Tool tool : tools) {
            if ((tool.getDisplayName().equals(name) || tool.getName().equals(name) ||
                    (exename != null && tool.getName().equals(exename))) && kind == tool.getKind()) {
                return tool;
            }
        }
        CndUtils.assertFalse(true, "Should not be here, cuz we should create empty tools in CompilerSetManager");  //NOI18N
        //TODO: remove this code, empty tools should be created in CompilerSetManager
        return compilerProvider.createCompiler(ExecutionEnvironmentFactory.getLocal(),
                CompilerFlavor.getUnknown(PlatformTypes.getDefaultPlatform()), kind, "", Tool.getToolDisplayName(kind), ""); // NOI18N
    }
    
    /**
     * Get the first tool of its kind.
     *
     * @param kind The type of tool to get
     * @return The Tool or null
     */
    public Tool getTool(int kind) {
        for (Tool tool : tools) {
            if (tool.getKind() == kind) {
                return tool;
            }
        }
        CndUtils.assertFalse(true, "Should not be here, cuz we should create empty tools in CompilerSetManager"); //NOI18N
        //TODO: remove this code, empty tools should be created in CompilerSetManager
        Tool t;
        // Fixup: all tools should go here ....
        t = compilerProvider.createCompiler(ExecutionEnvironmentFactory.getLocal(),
                getCompilerFlavor(), kind, "", Tool.getToolDisplayName(kind), ""); // NOI18N
        t.setCompilerSet(this);
        synchronized( tools ) { // synchronize this only unpredictable tools modification
            tools.add(t);
        }
        return t;
    }
    
    
    /**
     * Get the first tool of its kind.
     *
     * @param kind The type of tool to get
     * @return The Tool or null
     */
    public Tool findTool(int kind) {
        for (Tool tool : tools) {
            if (tool.getKind() == kind) {
                return tool;
            }
        }
        return null;
    }

    /*
     * Is this one being used?
     */
    @Deprecated
    public boolean isValid() {
        Tool cCompiler = getTool(Tool.CCompiler);
        Tool cppCompiler = getTool(Tool.CCCompiler);
        Tool fortranCompiler = getTool(Tool.FortranCompiler);
        Tool assembler = getTool(Tool.Assembler);
        
        return cCompiler != null && cppCompiler != null && fortranCompiler != null && assembler != null;
    }
    
    @SuppressWarnings("unchecked")
    public List<Tool> getTools() {
        synchronized (tools) {
            return (List<Tool>)tools.clone();
        }
    }
    
    public String getDynamicLibrarySearchOption() {
        return dynamicLibrarySearchOption;
    }

    public void setDynamicLibrarySearchOption(String dynamicLibrarySearchOption) {
        this.dynamicLibrarySearchOption = dynamicLibrarySearchOption;
    }

    public String getLibrarySearchOption() {
        return librarySearchOption;
    }

    public void setLibrarySearchOption(String librarySearchOption) {
        this.librarySearchOption = librarySearchOption;
    }

    public String getLibraryOption() {
        return libraryOption;
    }

    public void setLibraryOption(String libraryOption) {
        this.libraryOption = libraryOption;
    }
    
    public String getDriveLetterPrefix() {
        return driveLetterPrefix;
    }

    public String getQmakeSpec(int platform) {
        String qmakespec = flavor.getToolchainDescriptor().getQmakeSpec();
        if (qmakespec != null && 0 <= qmakespec.indexOf("${os}")) { // NOI18N
            String os = null;
            switch (platform) {
                case PlatformTypes.PLATFORM_LINUX:
                    os = "linux"; // NOI18N
                    break;
                case PlatformTypes.PLATFORM_MACOSX:
                    os = "macx"; // NOI18N
                    break;
                case PlatformTypes.PLATFORM_SOLARIS_INTEL:
                case PlatformTypes.PLATFORM_SOLARIS_SPARC:
                    os = "solaris"; // NOI18N
                    break;
                case PlatformTypes.PLATFORM_WINDOWS:
                    os = "win32"; // NOI18N
                    break;
            }
            if (os == null) {
                qmakespec = null;
            } else {
                qmakespec = qmakespec.replaceAll("\\$\\{os\\}", os); // NOI18N
            }
        }
        return qmakespec;
    }

    /**
     * Converts absolute Windows paths to paths without the ':'.
     * Example: C:/abc/def.c -> /cygdrive/c/def/c
     */
    public String normalizeDriveLetter(String path) {
        if (path.length() > 1 && path.charAt(1) == ':') { // NOI18N
            return getDriveLetterPrefix() + path.charAt(0) + path.substring(2); // NOI18N
        }
        return path;
    }
    
    void addPathCandidate(int tool, String path) {
        if (pathSearch == null){
            pathSearch = new HashMap<Integer, String>();
        }
        pathSearch.put(tool, path);
    }

    String getPathCandidate(int tool){
        if (pathSearch == null){
            return null;
        }
        return pathSearch.get(tool);
    }

    @Override
    public String toString() {
        return name;
    }

    private static class UnknownToolchainDescriptor implements ToolchainDescriptor {

        public String getFileName() {
            return ""; // NOI18N
        }

        public String getName() {
            return ""; // NOI18N
        }

        public String getDisplayName() {
            return ""; // NOI18N
        }

        public String[] getFamily() {
            return new String[]{};
        }

        public String[] getPlatforms() {
            return new String[]{};
        }

        public String getUpdateCenterUrl() {
            return null;
        }

        public String getUpdateCenterDisplayName() {
            return null;
        }

        public String getUpgradeUrl() {
            return null;
        }

        public String getModuleID() {
            return null;
        }

        public String getDriveLetterPrefix() {
            return ""; // NOI18N
        }

        public List<BaseFolder> getBaseFolders() {
            return Collections.<BaseFolder>emptyList();
        }

        public List<BaseFolder> getCommandFolders() {
            return Collections.<BaseFolder>emptyList();
        }

        public String getQmakeSpec() {
            return ""; // NOI18N
        }

        public CompilerDescriptor getC() {
            return null;
        }

        public CompilerDescriptor getCpp() {
            return null;
        }

        public CompilerDescriptor getFortran() {
            return null;
        }

        public CompilerDescriptor getAssembler() {
            return null;
        }

        public ScannerDescriptor getScanner() {
            return null;
        }

        public LinkerDescriptor getLinker() {
            return new LinkerDescriptor(){

                public String getLibraryPrefix() {
                    return ""; // NOI18N
                }

                public String getLibrarySearchFlag() {
                    return ""; // NOI18N
                }

                public String getDynamicLibrarySearchFlag() {
                    return ""; // NOI18N
                }

                public String getLibraryFlag() {
                    return ""; // NOI18N
                }

                public String getPICFlag() {
                    return ""; // NOI18N
                }

                public String getStaticLibraryFlag() {
                    return ""; // NOI18N
                }

                public String getDynamicLibraryFlag() {
                    return ""; // NOI18N
                }

                public String getDynamicLibraryBasicFlag() {
                    return ""; // NOI18N
                }

                public String getOutputFileFlag() {
                    return ""; // NOI18N
                }
            };
        }

        public MakeDescriptor getMake() {
            return null;
        }

        public Map<String, List<String>> getDefaultLocations() {
            return Collections.<String, List<String>>emptyMap();
        }

        public DebuggerDescriptor getDebugger() {
            return null;
        }

        public String getMakefileWriter() {
            return null;
        }

        public QMakeDescriptor getQMake() {
            return null;
        }

        public CMakeDescriptor getCMake() {
            return null;
        }

    }
}
