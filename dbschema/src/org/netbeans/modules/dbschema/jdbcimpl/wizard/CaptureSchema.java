/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.dbschema.jdbcimpl.wizard;

import java.beans.*;
import java.text.MessageFormat;
import java.util.*;

import org.openide.ErrorManager;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.*;
import org.openide.loaders.DataFolder;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

import org.netbeans.modules.db.explorer.infos.ConnectionNodeInfo;

import org.netbeans.modules.dbschema.*;
import org.netbeans.modules.dbschema.jdbcimpl.*;

public class CaptureSchema {

    ResourceBundle bundle = NbBundle.getBundle("org.netbeans.modules.dbschema.jdbcimpl.resources.Bundle"); //NOI18N
    ResourceBundle bundleDB = NbBundle.getBundle("org.netbeans.modules.db.resources.Bundle"); //NOI18N

    private final String defaultName = bundle.getString("DefaultSchemaName"); //NOI18N
    private DBSchemaWizardData data;
    
    /** Creates new CaptureSchema */
    public CaptureSchema(DBSchemaWizardData data) {
        this.data = data;
    }

    protected void start() {
        //getting values from package panel
        String target = data.getName();
        DataFolder folder = data.getDestinationPackage();
        
        //getting values from table selection panel
        final LinkedList tables = data.getTables();
        final LinkedList views = data.getViews();
        final boolean allTables = data.isAllTables();
        
        ConnectionProvider cp = data.getConnectionProvider();
        
        try {
            final ConnectionProvider c = cp;
            
            final FileObject fo = folder.getPrimaryFile();
            if (target == null || target.equals("")) //NOI18N
                target = FileUtil.findFreeFileName(fo, defaultName, "dbschema"); //NOI18N
            
            final boolean conned = data.isConnected();
            final boolean ec = data.isExistingConn();
            final ConnectionNodeInfo cninfo = data.getConnectionNodeInfo();
            final String target1 = target;
            
            //delete values from panels
            data = null;
            
            RequestProcessor.getDefault().post(new Runnable() {
                public void run () {
                    try {
                        StatusDisplayer.getDefault().setStatusText(bundle.getString("CreatingDatabaseSchema")); //NOI18N
                        
                        final ProgressFrame pf = new ProgressFrame();
                        final SchemaElementImpl sei = new SchemaElementImpl(c);
                        
                        PropertyChangeListener listener = new PropertyChangeListener() {
                            public void propertyChange(PropertyChangeEvent event) {
                                String message;
                                
                                if (event.getPropertyName().equals("totalCount")) { //NOI18N
                                    pf.setMaximum(((Integer)event.getNewValue()).intValue());
                                    return;
                                }

                                if (event.getPropertyName().equals("progress")) { //NOI18N
                                    pf.setValue(((Integer)event.getNewValue()).intValue());
                                    return;
                                }
                                
                                if (event.getPropertyName().equals("tableName")) { //NOI18N
                                    message = MessageFormat.format(bundle.getString("CapturingTable"), new String[] {((String) event.getNewValue()).toUpperCase()}); //NOI18N
                                    pf.setMessage(message);
                                    return;
                                }
                                
                                if (event.getPropertyName().equals("FKt")) { //NOI18N
                                    message = MessageFormat.format(bundle.getString("CaptureFK"), new String[] {((String) event.getNewValue()).toUpperCase(), bundle.getString("CaptureFKtable")}); //NOI18N
                                    pf.setMessage(message);
                                    return;
                                }
                                
                                if (event.getPropertyName().equals("FKv")) { //NOI18N
                                    message = MessageFormat.format(bundle.getString("CaptureFK"), new String[] {((String) event.getNewValue()).toUpperCase(), bundle.getString("CaptureFKview")}); //NOI18N
                                    pf.setMessage(message);
                                    return;
                                }
                                
                                if (event.getPropertyName().equals("viewName")) { //NOI18N
                                    message = MessageFormat.format(bundle.getString("CapturingView"), new String[] {((String) event.getNewValue()).toUpperCase()}); //NOI18N
                                    pf.setMessage(message);
                                    return;
                                }
                                
                                if (event.getPropertyName().equals("cancel")) { //NOI18N
                                    sei.setStop(true);
                                    StatusDisplayer.getDefault().setStatusText(""); //NOI18N
                                    return;
                                }
                            }
                        };
                        
                        pf.propertySupport.addPropertyChangeListener(listener);
                        pf.show();
                        
                        sei.propertySupport.addPropertyChangeListener(listener);
                        final SchemaElement se = new SchemaElement(sei);
                        se.setName(DBIdentifier.create(target1));
                        if (allTables)
                            sei.initTables(c, tables, views, true);
                        else
                            sei.initTables(c, tables, views, false);

                        if (! sei.isStop()) {
                            fo.getFileSystem().runAtomicAction(new FileSystem.AtomicAction() {
                                public void run() throws java.io.IOException {
                                    FileObject fo1 = fo.createData(target1, "dbschema"); //NOI18N
                                    FileLock fl = fo1.lock();
                                    java.io.OutputStream out = fo1.getOutputStream(fl);
                                    if (out == null)
                                        throw new java.io.IOException("Unable to open output stream");

                                    pf.setMessage(bundle.getString("SavingDatabaseSchema")); //NOI18N
                                    StatusDisplayer.getDefault().setStatusText(bundle.getString("SavingDatabaseSchema")); //NOI18N

                                    se.save(out);
                                    fl.releaseLock();
                                }
                            });
                            
                            pf.setMessage(bundle.getString("SchemaSaved")); //NOI18N
                            StatusDisplayer.getDefault().setStatusText(bundle.getString("SchemaSaved")); //NOI18N
                            
                            pf.setVisible(false);
                            pf.dispose();                        
                        }
                        
                        if (conned)
                            if (ec)
                                cninfo.disconnect();
                            else
                                c.closeConnection();
                    } catch (Exception exc) {
                        ErrorManager.getDefault().notify(exc);
                    }
                }
            }, 0);
        } catch (Exception exc) {
            String message = MessageFormat.format(bundle.getString("UnableToCreateSchema"), new String[] {exc.getMessage()}); //NOI18N
            StatusDisplayer.getDefault().setStatusText(message);
            
            try {
                if (cp != null)
                    if (data.isConnected())
                        if (data.isExistingConn())
                            data.getConnectionNodeInfo().disconnect();
                        else
                            cp.closeConnection();
            } catch (Exception exc1) {
                //unable to disconnect
            }
        }
    }
}
