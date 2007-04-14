/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.hudson.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.modules.hudson.api.HudsonJob;
import org.netbeans.modules.hudson.api.HudsonJob.Color;
import org.netbeans.modules.hudson.api.HudsonVersion;
import org.netbeans.modules.hudson.impl.HudsonJobImpl;
import org.netbeans.modules.hudson.util.Utilities;
import org.netbeans.modules.hudson.util.Utilities;
import org.openide.ErrorManager;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Hudson Server Connector
 *
 * @author Michal Mocnak
 */
public class HudsonConnector {
    
    private static final String XML_API_URL ="/api/xml";
    private static final String JOB_ELEMENT_NAME = "job";
    private static final String NAME_ELEMENT_NAME = "name";
    private static final String URL_ELEMENT_NAME = "url";
    private static final String COLOR_ELEMENT_NAME = "color";
    private static final String BUILD_URL = "build";
    
    private DocumentBuilder builder;
    private HudsonInstanceImpl instance;
    private HudsonVersion version;
    
    private boolean isConnected = false;
    
    /**
     * Creates a new instance of HudsonXMLFacade
     *
     * @param instanceURL
     */
    public HudsonConnector(HudsonInstanceImpl instance) {
        this.instance = instance;
        
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }  catch (ParserConfigurationException ex) {
            ErrorManager.getDefault().log(ErrorManager.ERROR,NbBundle.getMessage(HudsonConnector.class, "MSG_ParserError", ex.getLocalizedMessage()));
        }
    }
    
    public synchronized boolean isConnected() {
        return isConnected;
    }
    
    public synchronized HudsonVersion getHudsonVersion() {
        if (null == version)
            version = retrievedHudsonVersion();
        
        return version;
    }
    
    private synchronized HudsonVersion retrievedHudsonVersion() {
        HudsonVersion v = null;
        
        try {
            URL u = new java.net.URL(instance.getUrl());
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            
            // Get string version
            String sVersion = conn.getHeaderField("X-Hudson");
            
            // Create a HudsonVersion object
            v = new HudsonVersionImpl(sVersion);
        } catch (MalformedURLException e) {
            // Nothing
        } catch (IOException e) {
            // Nothing
        }
        
        return v;
    }
    
    /**
     *
     * @return
     */
    public synchronized List<HudsonJob> getAllJobs() {
        ArrayList<HudsonJob> jobsArray = new ArrayList<HudsonJob>();
        Document doc = getDocument();
        
        if (null == doc)
            return jobsArray;
        
        NodeList jobs = doc.getElementsByTagName(JOB_ELEMENT_NAME);
        
        for (int i = 0; i < jobs.getLength(); i++) {
            Node job = jobs.item(i);
            String name = null;
            String url = null;
            Color color = null;
            
            for (int j = 0; j < job.getChildNodes().getLength(); j++) {
                Node jobNode = job.getChildNodes().item(j);
                
                if (jobNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    if (jobNode.getNodeName().equals(NAME_ELEMENT_NAME)) {
                        name = jobNode.getFirstChild().getTextContent();
                    } else if (jobNode.getNodeName().equals(URL_ELEMENT_NAME)) {
                        url = jobNode.getFirstChild().getTextContent();
                    } else if (jobNode.getNodeName().equals(COLOR_ELEMENT_NAME)) {
                        color = Color.valueOf(jobNode.getFirstChild().getTextContent());
                    }
                }
            }
            
            if (null != name && null != url && null != color)
                jobsArray.add(new HudsonJobImpl(name, url, color, instance));
        }
        
        return jobsArray;
    }
    
    public synchronized boolean startJob(HudsonJob job) {
        final ProgressHandle handle = ProgressHandleFactory.createHandle(
                NbBundle.getMessage(HudsonInstanceImpl.class, "MSG_Starting", job.getName()));
        
        // Start progress
        handle.start();
        
        try {
            final URL url = new URL(job.getUrl() + "/" + BUILD_URL);
            
            RequestProcessor.getDefault().post(new Runnable() {
                public void run() {
                    try {
                        // Start job
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        
                        if(conn.getResponseCode() != 200)
                            ErrorManager.getDefault().log("Can't start build HTTP error: " + conn.getResponseMessage());
                    } catch (IOException e) {
                        ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
                    } finally {
                        // Stop progress
                        handle.finish();
                    }
                }
            });
            
            return true;
        } catch (MalformedURLException e) {
            ErrorManager.getDefault().notify(ErrorManager.INFORMATIONAL, e);
        }
        
        return false;
    }
    
    private synchronized Document getDocument() {
        Document doc = null;
        
        try {
            URL u = new java.net.URL(instance.getUrl() + XML_API_URL);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            
            // Connected failed
            if(conn.getResponseCode() != 200) {
                isConnected = false;
                return null;
            }
            
            // Connected successfully
            if (!isConnected) {
                isConnected = true;
                version = retrievedHudsonVersion();
            }
            
            // Get input stream
            InputStream stream = conn.getInputStream();
            
            // Parse document
            doc = builder.parse(stream);
            
            // Check for right version
            if (!Utilities.isSupportedVersion(getHudsonVersion())) {
                HudsonVersion v = retrievedHudsonVersion();
                
                if (!Utilities.isSupportedVersion(v))
                    return null;
                
                version = v;
            }
            
            if(conn != null)
                conn.disconnect();
        } catch (MalformedURLException ex) {
            ErrorManager.getDefault().log(ErrorManager.ERROR,NbBundle.getMessage(HudsonConnector.class, "MSG_MalformedURL", instance.getUrl() + XML_API_URL));
        } catch (IOException ex) {
            ErrorManager.getDefault().log(ErrorManager.ERROR,NbBundle.getMessage(HudsonConnector.class, "MSG_IOError", instance.getUrl() + XML_API_URL));
        } catch (SAXException ex) {
            ErrorManager.getDefault().log(ErrorManager.ERROR,NbBundle.getMessage(HudsonConnector.class, "MSG_ParserError", ex.getLocalizedMessage()));
        } catch (NullPointerException ex) {
            ErrorManager.getDefault().log(ErrorManager.ERROR,NbBundle.getMessage(HudsonConnector.class, "MSG_ParserError", ex.getLocalizedMessage()));
        }
        
        return doc;
    }
}