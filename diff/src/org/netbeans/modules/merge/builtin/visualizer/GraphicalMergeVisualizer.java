/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2002 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.merge.builtin.visualizer;

import java.awt.Color;
import java.awt.Component;
//import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.Serializable;

import org.openide.util.NbBundle;
import org.openide.util.WeakListener;

import org.netbeans.api.diff.Difference;
import org.netbeans.api.diff.StreamSource;
import org.netbeans.spi.diff.MergeVisualizer;

/**
 * The default graphical visualizer of merge conflicts.
 *
 * @author  Martin Entlicher
 */
public class GraphicalMergeVisualizer extends MergeVisualizer implements Serializable {
    
    private Color colorUnresolvedConflict = new java.awt.Color(255, 160, 180);
    private Color colorResolvedConflict = new java.awt.Color(180, 255, 180);
    private Color colorOtherConflict = new java.awt.Color(160, 200, 255);
    
    /** The currently opened merge dialog. */
    private MergeDialogComponent merge;
    
    static final long serialVersionUID =-2175410667258166512L;
    /** Creates a new instance of GraphicalMergeVisualizer */
    public GraphicalMergeVisualizer() {
        merge = null;
        //System.out.println("Created a new GraphicalMergeVisualizer() = "+this);
    }
    
    /**
     * Get the display name of this merge visualizer.
     */
    public String getDisplayName() {
        return NbBundle.getMessage(GraphicalMergeVisualizer.class, "GraphicalMergeVisualizer.displayName");
    }
    
    /**
     * Get a short description of this merge visualizer.
     */
    public String getShortDescription() {
        return NbBundle.getMessage(GraphicalMergeVisualizer.class, "GraphicalMergeVisualizer.shortDescription");
    }
    
    /**
     * Show the visual representation of the merging process of two sources.
     * The result of the merging process can be saved into a Writer even
     * before all conflicts are actually resolved.
     *
     * @param diffs The list of conflicts.
     * @param source1 the source of the first file
     * @param source2 the source of the second file
     * @param result the information about the result source
     * @return The Component representing the diff visual representation
     *        or null, when the representation is outside the IDE.
     * @throws IOException when the reading from input streams fails.
     */
    public Component createView(Difference[] diffs, StreamSource source1,
                                StreamSource source2, StreamSource result) throws IOException {
        synchronized (this) {
            //System.out.println("createView(): merge = "+merge);
            if (merge == null) {
                merge = new MergeDialogComponent();
                merge.open();
            }
        }
        MergePanel panel = new MergePanel();
        MergeControl control = new MergeControl(panel);
        control.initialize(diffs, source1, source2, result,
                           colorUnresolvedConflict, colorResolvedConflict,
                           colorOtherConflict);
        merge.addVetoableChangeListener(WeakListener.vetoableChange(control, merge));
        merge.addMergePanel(panel);
        return merge;
    }
    
}
