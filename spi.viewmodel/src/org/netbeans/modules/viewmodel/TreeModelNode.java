/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2000 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.viewmodel;

import java.awt.event.ActionEvent;
import java.beans.PropertyEditor;
import java.lang.IllegalAccessException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.netbeans.spi.viewmodel.ColumnModel;
import org.netbeans.spi.viewmodel.ComputingException;
import org.netbeans.spi.viewmodel.NoInformationException;
import org.netbeans.spi.viewmodel.TreeModel;
import org.netbeans.spi.viewmodel.TableModel;
import org.netbeans.spi.viewmodel.UnknownTypeException;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.HelpCtx;
import org.openide.util.lookup.Lookups;


/**
 *
 * @author   Jan Jancura
 */
public class TreeModelNode extends AbstractNode {

    
    // variables ...............................................................

    private CompoundModel       model;
    private TreeModelRoot       treeModelRoot;
    private Object              object;

    
    // init ....................................................................

    /**
    * Creates root of call stack for given producer.
    */
    public TreeModelNode ( 
        CompoundModel model, 
        TreeModelRoot treeModelRoot,
        Object object
    ) {
        super (
            createChildren (model, treeModelRoot, object),
            Lookups.singleton (object)
        );
        this.model = model;
        this.treeModelRoot = treeModelRoot;
        this.object = object;
        treeModelRoot.registerNode (object, this); 
        refresh ();
        initProperties ();
    }
    
    private void initProperties () {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set ps = Sheet.createPropertiesSet ();
        ColumnModel[] columns = model.getColumns ();
        int i, k = columns.length;
        for (i = 0; i < k; i++)
            try {
                boolean isRO = model.isReadOnly (object, columns [i].getID ());
                ps.put (new MyProperty (
                    columns [i],
                    isRO
                ));
            } catch (UnknownTypeException e) {
            }
        sheet.put (ps);
        setSheet (sheet);
    }
    
    private static Children createChildren (
        CompoundModel model, 
        TreeModelRoot treeModelRoot,
        Object object
    ) {
        if (object == null) 
            throw new NullPointerException ();
        try {
            return model.isLeaf (object) ? 
                Children.LEAF : 
                new TreeModelChildren (model, treeModelRoot, object);
        } catch (UnknownTypeException e) {
            e.printStackTrace ();
            return Children.LEAF;
        }
    }
    
    public String getDisplayName () {
        try {
            return model.getDisplayName (object);
        } catch (UnknownTypeException e) {
            e.printStackTrace ();
            return object.toString ();
        } catch (ComputingException e) {
            return "Computing";
        }
    }
    
    public String getShortDescription () {
        try {
            return model.getShortDescription (object);
        } catch (UnknownTypeException e) {
            e.printStackTrace ();
            return null;
        } catch (ComputingException e) {
            return "Computing";
        }
    }

    public HelpCtx getHelpCtx() {
        return new HelpCtx (getClass ());
    }
    
    void refresh () {
//        try {
//            setDisplayName (model.getDisplayName (object));
//        } catch (UnknownTypeException e) {
//            setDisplayName (object.toString ());
//            e.printStackTrace ();
//        } catch (ComputingException e) {
//            setDisplayName ("Computing");
//        }
        try {
            setIconBase (model.getIconBase (object));
        } catch (UnknownTypeException e) {
            e.printStackTrace ();
        } catch (ComputingException e) {
        }
        Children ch = getChildren ();
        if (ch instanceof TreeModelChildren)
            ((TreeModelChildren) ch).refreshChildren ();
    }
    
    public Action[] getActions (boolean context) {
        if (context) 
            return treeModelRoot.getRootNode ().getActions (false);
        try {
            return model.getActions (object);
        } catch (UnknownTypeException e) {
            e.printStackTrace();
            return new Action [0];
        }
    }
    
    public Action getPreferredAction () {
        return new AbstractAction () {
            public void actionPerformed (ActionEvent e) {
                try {
                    model.performDefaultAction (object);
                } catch (UnknownTypeException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
    
    void setObject (Object o) {
        object = o;
        Children ch = getChildren ();
        if (ch instanceof TreeModelChildren)
            ((TreeModelChildren) ch).object = o;
        refresh ();
    }
    
    
    // innerclasses ............................................................
    
    /** Special locals subnodes (children) */
    private static final class TreeModelChildren extends Children.Keys {
            
        private boolean             initialezed = false;
        private CompoundModel       model;
        private TreeModelRoot       treeModelRoot;
        private Object              object;
        private HashMap             objectToNode = new HashMap ();
        
        
        TreeModelChildren (
            CompoundModel model,
            TreeModelRoot treeModelRoot,
            Object object
        ) {
            this.model = model;
            this.treeModelRoot = treeModelRoot;
            this.object = object;
        }
        
        protected void addNotify () {
            initialezed = true;
            refreshChildren ();
        }
        
        protected void removeNotify () {
            initialezed = false;
            setKeys (Collections.EMPTY_SET);
        }
        
        void refreshChildren () {
            if (!initialezed) return;
            try {
                Object[] ch = model.getChildren (object, 0, 0);
                int i, k = ch.length; 
                HashMap newObjectToNode = new HashMap ();
                for (i = 0; i < k; i++) {
                    if (ch [i] == null) {
                        System.out.println("model: " + model);
                        System.out.println("parent: " + object);
                        throw new NullPointerException ();
                    }
                    TreeModelNode tmn = (TreeModelNode) objectToNode.get 
                        (ch [i]);
                    if (tmn != null) {
                        tmn.setObject (ch [i]);
                        newObjectToNode.put (ch [i], tmn);
                    }
                }
                objectToNode = newObjectToNode;
                setKeys (ch);
            } catch (UnknownTypeException e) {
                setKeys (new Object [0]);
                e.printStackTrace ();
            } catch (NoInformationException e) {
                setKeys (new Object [0]);
            } catch (ComputingException e) {
                setKeys (new Object [0]);
            }
        }
        
//        protected void destroyNodes (Node[] nodes) {
//            int i, k = nodes.length;
//            for (i = 0; i < k; i++) {
//                TreeModelNode tmn = (TreeModelNode) nodes [i];
//                String name = null;
//                try {
//                    name = model.getDisplayName (tmn.object);
//                } catch (ComputingException e) {
//                } catch (UnknownTypeException e) {
//                }
//                if (name != null)
//                    nameToChild.remove (name);
//            }
//        }
        
        protected Node[] createNodes (Object object) {
            TreeModelNode tmn = new TreeModelNode (
                model, 
                treeModelRoot, 
                object
            );
            objectToNode.put (object, tmn);
            return new Node[] {tmn};
        }
    } // ItemChildren
    
    private class MyProperty extends PropertySupport {
        
        private String id;
        private ColumnModel columnModel;
        
        
        MyProperty (
            ColumnModel columnModel,
            boolean isRO
        ) {
            super (
                columnModel.getID (),
                columnModel.getType (),
                columnModel.getDisplayName (),
                columnModel.getShortDescription (), 
                true,
                !isRO
            );
            this.columnModel = columnModel;
            id = columnModel.getID ();
        }
        
        
        public Object getValue () {
            try {
                return model.getValueAt (object, id);
            } catch (ComputingException e) {
            } catch (UnknownTypeException e) {
                e.printStackTrace ();
            }
            return null;
        }
        
        public void setValue (Object v) throws IllegalAccessException, 
        IllegalArgumentException, java.lang.reflect.InvocationTargetException {
            try {
                model.setValueAt (object, id, v);
            } catch (UnknownTypeException e) {
                e.printStackTrace ();
            }
        }
        
        public PropertyEditor getPropertyEditor () {
            return columnModel.getPropertyEditor ();
        }
    }
}

