/*
2 * {START_JAVA_COPYRIGHT_NOTICE
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL.
 * Use is subject to license terms.
 * END_COPYRIGHT_NOTICE}
 */


package org.netbeans.modules.visualweb.api.portlet.dd;
import java.io.File;
import java.io.IOException;
import org.openide.ErrorManager;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author David Botterill
 */
public class PortletDDHelper {

    private File ddFile;
    private PortletApp portletApp;

    public PortletDDHelper(File inDDFile) {
        ddFile = inDDFile;
        /**
         * Create the portlet application
         */

        try {
            portletApp = PortletApp.read(ddFile);
        } catch(IOException ioe) {
            ErrorManager.getDefault().notify(ioe);
        } catch(ParserConfigurationException pce) {
            ErrorManager.getDefault().notify(pce);
        } catch(SAXException se) {
            ErrorManager.getDefault().notify(se);
        }
    }
    /**
     * This method will set the portlet name
     * @param oldName the current name of the portlet to change.
     * @param newName the new name to give to the portlet.
     */
    public void setPortletName(String oldName, String newName) {
        try {
            if(null!=portletApp) {
                /**
                 * Now get the Portlets in the portlet application.
                 * NOTE- we only expect there to be one portlet per project so
                 * we will only deal with the first portlet.
                 */
                PortletType [] portletType = portletApp.getPortlet();
                for(int ii=0; null != portletType && ii < portletType.length;ii++) {
                    String currentPortletName = portletType[ii].getPortletName();
                    if(currentPortletName.equals(oldName)) {
                        portletType[ii].setPortletName(newName);
                        break;
                    }
                }
                portletApp.write(ddFile);
            }
        } catch(IOException ioe) {
            ErrorManager.getDefault().notify(ioe);
        }
        
    }    
    
    /**
     * This method will set the initial page in the portlet.xml file.
     * @param inMode The MODE to set for the initial page.
     * @param inRelativePathPageName This is page to be set as the initial page for the mode.
     * The page must include the relative path.  This will usually be a "/" but could
     * also include a subfolder.  For example, if the absolute path of the page is
     * /home/david/Creator/Projects/Portlet1/web/edit/EditPage.jsp, this parameter should
     * be "/edit/EditPage.jsp".
     */
    public void setInitialPage(PortletModeType inMode, String inRelativePathPageName) {
        /**
         * First unset the given page so we don't have leftover
         */
        this.unsetInitialPage(inRelativePathPageName);
        
        try {
            
            if(null!=portletApp) {
                /**
                 * Now get the Portlets in the portlet application.
                 * NOTE- we only expect there to be one portlet per project so
                 * we will only deal with the first portlet.
                 */
                PortletType [] portletType = portletApp.getPortlet();
                if(null != portletType) {
                    
                    InitParamType [] initParams = portletType[0].getInitParam();
                    
                    
                    /**
                     * Now look for the init-param currently set with the initial page.
                     */
                    boolean foundMode = false;
                    for(int ii=0; null != initParams && ii < initParams.length;ii++) {
                        
                        if(inMode.equals(PortletModeType.VIEW)) {
                            if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_VIEW)) {
                                foundMode = true;
                                initParams[ii].setValue(inRelativePathPageName);
                            }
                        } else if(inMode.equals(PortletModeType.EDIT)) {
                            if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_EDIT)) {
                                foundMode = true;
                                initParams[ii].setValue(inRelativePathPageName);
                            }
                        } else if(inMode.equals(PortletModeType.HELP)) {
                            if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_HELP)) {
                                foundMode = true;
                                initParams[ii].setValue(inRelativePathPageName);
                            }
                        }
                        
                    }
                                        
                    
                    if(!foundMode) {
                        /**
                         * The INIT for the page didn't exist so we need to add it.
                         */
                        InitParamType initParam = new InitParamType();
                        if(inMode.equals(PortletModeType.VIEW)) {
                            initParam.setName(JsfPortletDDProperties.INIT_VIEW);
                        } else if(inMode.equals(PortletModeType.EDIT)) {
                            initParam.setName(JsfPortletDDProperties.INIT_EDIT);
                        } else if(inMode.equals(PortletModeType.HELP)) {
                            initParam.setName(JsfPortletDDProperties.INIT_HELP);
                        }
                        initParam.setValue(inRelativePathPageName);
                        portletType[0].addInitParam(initParam);
                        
                    }
                    
                    /**
                     * If the the supported mode is not listed, list it.
                     */
                   SupportsType [] supportsTypes = portletType[0].getSupports();
                    
                    boolean foundSupports = false;
                    boolean foundPortletMode = false;
                    if(null != supportsTypes && supportsTypes.length > 0) {
                        foundSupports = true;
                    }
                    
                    for(int ii=0; null != supportsTypes && ii < supportsTypes.length;ii++) {
                       String [] modes = supportsTypes[ii].getPortletMode();
                       for(int jj=0; null != modes && jj < modes.length; jj++) {
                            if(modes[jj].equalsIgnoreCase(PortletModeType.VIEW.toString()) &&
                                    inMode.equals(PortletModeType.VIEW)) {
                                foundPortletMode=true;
                            } else if(modes[jj].equalsIgnoreCase(PortletModeType.EDIT.toString()) &&
                                    inMode.equals(PortletModeType.EDIT)) {
                                foundPortletMode=true;
                            } else if(modes[jj].equalsIgnoreCase(PortletModeType.HELP.toString()) &&
                                    inMode.equals(PortletModeType.HELP)) {
                                foundPortletMode=true;
                            }
                       }
                    }
                    
                    if(!foundPortletMode) {
                        SupportsType supportsType = null;
                        /**
                         * If we found a SupportsType use it, otherwise create one.
                         */
                        if(!foundSupports) {
                            supportsType = new SupportsType();
                        } else {
                            supportsType = supportsTypes[0];                            
                        }
                        
                        supportsType.addPortletMode(inMode.toString());
                    }

                    
                    
                    
                }
                portletApp.write(ddFile);
            }
        } catch(IOException ioe) {
            ErrorManager.getDefault().notify(ioe);
        }
        
    }
    /**
     * This method will unset the initial page in the portlet.xml file.
     * @param inPageName This is page to be unset as any and all initial page.
     * The page must include the relative path.  This will usually be a "/" but could
     * also include a subfolder.  For example, if the absolute path of the page is
     * /home/david/Creator/Projects/Portlet1/web/edit/EditPage.jsp, this parameter should
     * be "/edit/EditPage.jsp".
     */    
    public void unsetInitialPage(String inPageName) {
        try {
            
            if(null!=portletApp) {
                /**
                 * Now get the Portlets in the portlet application.
                 * NOTE- we only expect there to be one portlet per project so
                 * we will only deal with the first portlet.
                 */
                PortletType [] portletType = portletApp.getPortlet();
                if(null != portletType) {
                    
                    InitParamType [] initParams = portletType[0].getInitParam();
                    
                    boolean foundMode = false;
                    String modeToRemove = null;
                    for(int ii=0; null != initParams && ii < initParams.length;ii++) {
                        if(initParams[ii].getValue().equals(inPageName)) {
                            portletType[0].removeInitParam(initParams[ii]);
                            modeToRemove = initParams[ii].getName();
                        }
                    }
                    /**
                     * Now remove the supported mode
                     */
                    SupportsType [] supportsTypes = portletType[0].getSupports();
                    for(int ii=0; null != supportsTypes && null != modeToRemove && ii < supportsTypes.length;ii++) {
                        
                        if(modeToRemove.equals(JsfPortletDDProperties.INIT_VIEW)) {
                            supportsTypes[ii].removePortletMode(PortletModeType.VIEW.toString());
                        } else if(modeToRemove.equals(JsfPortletDDProperties.INIT_EDIT)) {
                            supportsTypes[ii].removePortletMode(PortletModeType.EDIT.toString());
                        } else if(modeToRemove.equals(JsfPortletDDProperties.INIT_HELP)) {
                            supportsTypes[ii].removePortletMode(PortletModeType.HELP.toString());
                        }
                    }
                
                }
            }
            portletApp.write(ddFile);
            
        } catch(IOException ioe) {
            ErrorManager.getDefault().notify(ioe);
        }
        
    }
    /**
     * This method will determine if the given page is the initial page for the given mode.
     * @param inMode This is the mode to check against.
     * @param inPageName This is page to be check as the initial page for the given mode.
     * The page must include the relative path.  This will usually be a "/" but could
     * also include a subfolder.  For example, if the absolute path of the page is
     * /home/david/Creator/Projects/Portlet1/web/edit/EditPage.jsp, this parameter should
     * be "/edit/EditPage.jsp".
     */        
    public boolean isInitialPage(PortletModeType inMode, String inPageName) {
        boolean isPage = false;
        if(null!=portletApp) {
            /**
             * Now get the Portlets in the portlet application.
             * NOTE- we only expect there to be one portlet per project so
             * we will only deal with the first portlet.
             */
            PortletType [] portletType = portletApp.getPortlet();
            if(null != portletType) {
                InitParamType [] initParams = portletType[0].getInitParam();
                for(int ii=0; null != initParams && ii < initParams.length;ii++) {
                    if(inMode.equals(PortletModeType.VIEW)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_VIEW)) {
                            String pageValue = initParams[ii].getValue();
                            if(null != pageValue) {

                                if(pageValue.equals(inPageName)) {
                                    isPage=true;
                                }
                            }
                        }
                    } else if(inMode.equals(PortletModeType.EDIT)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_EDIT)) {
                            String pageValue = initParams[ii].getValue();
                            if(null != pageValue) {

                                if(pageValue.equals(inPageName)) {
                                    isPage=true;
                                }
                            }
                        }
                    } else if(inMode.equals(PortletModeType.HELP)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_HELP)) {
                            String pageValue = initParams[ii].getValue();
                            if(null != pageValue) {

                                if(pageValue.equals(inPageName)) {
                                    isPage=true;
                                }
                            }
                        }
                    }
                }
            }
            
        }
        return isPage;
    }
    /**
     * This method will check whether a given page is an initial page for any mode.
     * @param inFileObject The FileObject of the file to set check as the initial page.
     * @return the PortletModeType the page is the initial page for.  If the page is not an initial page, 
     * null is returned.
     */
    public PortletModeType isInitialPage(String inPageName)  {
        PortletModeType returnMode = null;
        if(null!=portletApp) {
            /**
             * Now get the Portlets in the portlet application.
             * NOTE- we only expect there to be one portlet per project so
             * we will only deal with the first portlet.
             */
            PortletType [] portletType = portletApp.getPortlet();
            if(null != portletType) {
                InitParamType [] initParams = portletType[0].getInitParam();
                for(int ii=0; null != initParams && ii < initParams.length;ii++) {
                    String pageValue = initParams[ii].getValue();
                    if(null != pageValue && pageValue.equals(inPageName)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_VIEW)) {
                            returnMode = PortletModeType.VIEW;
                        } else if (initParams[ii].getName().equals(JsfPortletDDProperties.INIT_EDIT)) {
                            returnMode = PortletModeType.EDIT;
                        } else if (initParams[ii].getName().equals(JsfPortletDDProperties.INIT_HELP)) {
                            returnMode = PortletModeType.HELP;
                        }
                    }                                        
                }                
            }
        }
        return returnMode;
    }
        
    /**
     * This method will determine get the initial page for the given mode.
     * @param inMode This is the mode to check against.
     */        
    
    public String getInitialPage(PortletModeType inMode) {
        String returnPage = null;
        
        if(null!=portletApp) {
            /**
             * Now get the Portlets in the portlet application.
             * NOTE- we only expect there to be one portlet per project so
             * we will only deal with the first portlet.
             */
            PortletType [] portletType = portletApp.getPortlet();
            if(null != portletType) {
                InitParamType [] initParams = portletType[0].getInitParam();
                for(int ii=0; null != initParams && ii < initParams.length;ii++) {
                    if(inMode.equals(PortletModeType.VIEW)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_VIEW)) {
                            returnPage = initParams[ii].getValue();
                        }
                    } else if(inMode.equals(PortletModeType.EDIT)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_EDIT)) {
                            returnPage = initParams[ii].getValue();
                        }
                    } else if(inMode.equals(PortletModeType.HELP)) {
                        if(initParams[ii].getName().equals(JsfPortletDDProperties.INIT_HELP)) {
                            returnPage = initParams[ii].getValue();
                        }
                    }
                }
            }
            
        }
        return returnPage;
    }
}
