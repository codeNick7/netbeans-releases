/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.api.visual.widget;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.Layout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.util.GeomUtil;
import org.openide.util.Lookup;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author David Kaspar
 */
// TODO - Should Widget be an abstract class?
public class Widget {

    private static final HashMap<String, WidgetAction.Chain> EMPTY_HASH_MAP = new HashMap<String, WidgetAction.Chain> (0);

    private Scene scene;
    private Widget parentWidget;

    private List<Widget> children;
    private List<Widget> childrenUm;

    private WidgetAction.Chain actionsChain;
    private HashMap<String, WidgetAction.Chain> toolsActions = EMPTY_HASH_MAP;

    private ArrayList<Widget.Dependency> dependencies;

    private boolean opaque;
    private Paint background;
    private Color foreground;
    private Font font;
    private Border border;
    private Layout layout;
    private Point preferredLocation;
    private Rectangle minimumBounds;
    private Rectangle maximumBounds;
    private Rectangle preferredBounds;
    private boolean checkClipping;

    private ObjectState state = ObjectState.createNormal ();

    private Cursor cursor;
    private String toolTipText;

    private Point location;
    private Rectangle bounds;
    private Rectangle calculatedPreferredBounds;

    private boolean requiresFullValidation;
    private boolean requiresPartValidation;

    private boolean requiresFullJustification;
    private boolean requiresPartJustification;

    // TODO - replace Scene parameter with an interface
    public Widget (Scene scene) {
        if (scene == null)
            scene = (Scene) this;
        this.scene = scene;
        children = new ArrayList<Widget> ();
        childrenUm = Collections.unmodifiableList (children);

        actionsChain = new WidgetAction.Chain ();

        opaque = false;
        font = null;
        background = Color.WHITE;
        foreground = Color.BLACK;
        border = BorderFactory.createEmptyBorder ();
        layout = LayoutFactory.createAbsoluteLayout ();
        preferredLocation = null;
        preferredBounds = null;
        checkClipping = false;

        location = new Point ();
        bounds = null;
        calculatedPreferredBounds = null;
        requiresFullValidation = true;
        requiresPartValidation = true;
    }

    public final Scene getScene () {
        return scene;
    }

    protected Graphics2D getGraphics () {
        return scene.getGraphics ();
    }

    public final Widget getParentWidget () {
        return parentWidget;
    }

    public final List<Widget> getChildren () {
        return childrenUm;
    }

    public final void addChild (Widget child) {
        assert child.parentWidget == null;
        Widget widget = this;
        while (widget != null) {
            assert widget != child;
            widget = widget.parentWidget;
        }
        children.add(child);
        child.parentWidget = this;
        child.revalidate();
        revalidate ();
    }

    public final void addChild (int index, Widget child) {
        assert child.parentWidget == null;
        children.add (index, child);
        child.parentWidget = this;
        child.revalidate ();
        revalidate ();
    }

    public final void removeChild (Widget child) {
        assert child.parentWidget == this;
        child.parentWidget = null;
        children.remove (child);
        child.revalidate ();
        revalidate ();
    }

    public final void removeFromParent () {
        if (parentWidget != null)
            parentWidget.removeChild (this);
    }

    public final void removeChildren () {
        while (! children.isEmpty ())
            removeChild (children.get (0));
    }

    public final void addChildren (List<? extends Widget> children) {
        for (Widget child : children)
            addChild (child);
    }

    public final void removeChildren (List<Widget> widgets) {
        for (Widget widget : widgets)
            removeChild (widget);
    }

    public final void bringToFront () {
        if (parentWidget == null)
            return;
        List<Widget> children = parentWidget.children;
        int i = children.indexOf (this);
        if (i < 0)
            return;
        children.remove (i);
        children.add (children.size (), this);
        revalidate ();
        parentWidget.revalidate ();
    }

    public final void bringToBack () {
        if (parentWidget == null)
            return;
        List<Widget> children = parentWidget.children;
        int i = children.indexOf (this);
        if (i <= 0)
            return;
        children.remove (i);
        children.add (0, this);
        revalidate ();
        parentWidget.revalidate ();
    }

    public final WidgetAction.Chain getActions () {
        return actionsChain;
    }

    public final WidgetAction.Chain getActions (String tool) {
        return toolsActions.get (tool);
    }

    public final WidgetAction.Chain createActions (String tool) {
        if (tool == null)
            return actionsChain;
        if (toolsActions == EMPTY_HASH_MAP) {
            toolsActions = new HashMap<String, WidgetAction.Chain> ();
            toolsActions.put (null, actionsChain);
        }
        WidgetAction.Chain chain = toolsActions.get (tool);
        if (chain == null) {
            chain = new WidgetAction.Chain ();
            toolsActions.put (tool, chain);
        }
        return chain;
    }

    public Lookup getLookup () {
        return Lookup.EMPTY;
    }

    public final void addDependency (Widget.Dependency dependency) {
        if (dependencies == null)
            dependencies = new ArrayList<Widget.Dependency> ();
        dependencies.add (dependency);
    }

    public final void removeDependency (Widget.Dependency dependency) {
        if (dependencies == null)
            return;
        dependencies.remove (dependency);
    }

    public final boolean isOpaque () {
        return opaque;
    }

    public final void setOpaque (boolean opaque) {
        this.opaque = opaque;
        repaint ();
    }

    public final Paint getBackground () {
        return background != null ? background : parentWidget.getBackground ();
    }

    public final void setBackground (Paint background) {
        this.background = background;
        repaint ();
    }

    public final Color getForeground () {
        return foreground != null ? foreground : parentWidget.getForeground ();
    }

    public final void setForeground (Color foreground) {
        this.foreground = foreground;
        repaint ();
    }

    public final Font getFont () {
        return font != null ? font : parentWidget.getFont ();
    }

    public final void setFont (Font font) {
        this.font = font;
        revalidate ();
    }

    public final Border getBorder () {
        return border;
    }

    public final void setBorder (Border border) {
        assert border != null;
        boolean repaintOnly = this.border.getInsets ().equals (border.getInsets ());
        this.border = border;
        revalidate (repaintOnly);
    }

    public final void setBorder (javax.swing.border.Border swingBorder) {
        assert swingBorder != null;
        setBorder (BorderFactory.createSwingBorder (scene, swingBorder));
    }

    public final Layout getLayout () {
        return layout;
    }

    public final void setLayout (Layout layout) {
        this.layout = layout;
        revalidate ();
    }

    public final Rectangle getMinimumBounds () {
        return minimumBounds != null ? new Rectangle (minimumBounds) : null;
    }

    public final void setMinimumBounds (Rectangle minimumBounds) {
        this.minimumBounds = minimumBounds;
    }

    public final Rectangle getMaximumBounds () {
        return maximumBounds != null ? new Rectangle (maximumBounds) : null;
    }

    public final void setMaximumBounds (Rectangle maximumBounds) {
        this.maximumBounds = maximumBounds;
    }

    public final Point getPreferredLocation () {
        return preferredLocation != null ? new Point (preferredLocation) : null;
    }

    public final void setPreferredLocation (Point preferredLocation) {
        if (GeomUtil.equals (this.preferredLocation, preferredLocation))
            return;
        this.preferredLocation = preferredLocation;
        revalidate ();
    }

    public final boolean isPreferredBoundsSet () {
        return preferredBounds != null;
    }

    public final Rectangle getPreferredBounds () {
        Rectangle rect;
        if (isPreferredBoundsSet ())
            rect = new Rectangle (preferredBounds);
        else {
            if (calculatedPreferredBounds == null)
                calculatedPreferredBounds = calculatePreferredBounds ();
            rect = new Rectangle (calculatedPreferredBounds);
        }
        if (minimumBounds != null)
            rect.add (minimumBounds);
        if (maximumBounds != null)
            rect.intersection (maximumBounds);
        return rect;
    }

    private Rectangle calculatePreferredBounds () {
        Insets insets = border.getInsets ();
        Rectangle clientArea = calculateClientArea ();
        for (Widget child : children) {
            Point location = child.getLocation ();
            Rectangle bounds = child.getBounds ();
            bounds.translate (location.x, location.y);
            clientArea.add (bounds);
        }
        clientArea.x -= insets.left;
        clientArea.y -= insets.top;
        clientArea.width += insets.left + insets.right;
        clientArea.height += insets.top + insets.bottom;
        return clientArea;
    }

    protected Rectangle calculateClientArea () {
        return new Rectangle ();
    }

    public final void setPreferredBounds (Rectangle preferredBounds) {
        if (GeomUtil.equals (this.preferredBounds, preferredBounds))
            return;
        this.preferredBounds = preferredBounds;
        revalidate ();
    }

    public final boolean isCheckClipping () {
        return checkClipping;
    }

    public final void setCheckClipping (boolean checkClipping) {
        this.checkClipping = checkClipping;
        repaint ();
    }

    public final Cursor getCursor () {
        return cursor;
    }

    public final void setCursor (Cursor cursor) {
        this.cursor = cursor;
    }

    public final String getToolTipText () {
        return toolTipText;
    }

    public final void setToolTipText (String toolTipText) {
        this.toolTipText = toolTipText;
    }

    public final ObjectState getState () {
        return state;
    }

    public final void setState (ObjectState state) {
        ObjectState previousState = this.state;
        this.state = state;
        notifyStateChanged (previousState, state);
    }

    protected void notifyStateChanged (ObjectState previousState, ObjectState state) {
    }

    public final Point convertLocalToScene (Point localLocation) {
        Point sceneLocation = new Point (localLocation);
        Widget widget = this;
        while (widget != null) {
            if (widget == scene)
                break;
            Point location = widget.getLocation ();
            sceneLocation.x += location.x;
            sceneLocation.y += location.y;
            widget = widget.getParentWidget ();
        }
        return sceneLocation;
    }

    public final Rectangle convertLocalToScene (Rectangle localRectangle) {
        Rectangle sceneRectangle = new Rectangle (localRectangle);
        Widget widget = this;
        while (widget != null) {
            if (widget == scene)
                break;
            Point location = widget.getLocation ();
            sceneRectangle.x += location.x;
            sceneRectangle.y += location.y;
            widget = widget.getParentWidget ();
        }
        return sceneRectangle;
    }

    public final Point convertSceneToLocal (Point sceneLocation) {
        Point localLocation = new Point (sceneLocation);
        Widget widget = this;
        while (widget != null) {
            if (widget == scene)
                break;
            Point location = widget.getLocation ();
            localLocation.x -= location.x;
            localLocation.y -= location.y;
            widget = widget.getParentWidget ();
        }
        return localLocation;
    }

    public final Point getLocation () {
        return location != null ? new Point (location) : null;
    }

    public final Rectangle getBounds () {
        return bounds != null ? new Rectangle (bounds) : null;
    }

    public final void resolveBounds (Point location, Rectangle bounds) {
        this.location = location != null ? location : new Point ();
        this.bounds = bounds != null ? new Rectangle (bounds) : new Rectangle (getPreferredBounds ());
    }

    public final Rectangle getClientArea () {
        Rectangle bounds = getBounds ();
        if (bounds == null)
            return null;
        Insets insets = getBorder ().getInsets ();
        return new Rectangle (bounds.x + insets.left, bounds.y + insets.top, bounds.width - insets.left - insets.right, bounds.height - insets.top - insets.bottom);
    }

    public boolean isHitAt (Point localLocation) {
        return getBounds ().contains (localLocation);
    }

    // NOTE - has to be called before a change is set into the widget when the change immediatelly affects calculation of the local/scene location/boundary (means any property used in convertLocalToScene) because repaint/revalidate needs to calculate old scene boundaries
    public final void repaint () {
        scene.revalidateWidget (this);
    }

    public boolean isValidated () {
        return ! requiresPartValidation;
    }

    // NOTE - has to be called before a change is set into the widget when the change affects the local/scene location/boundary because repaint/revalidate needs to calculate old scene boundaries
    public final void revalidate (boolean repaintOnly) {
        if (repaintOnly)
            repaint ();
        else
            revalidate ();
    }

    // NOTE - has to be called before a change is set into the widget when the change affects the local/scene location/boundary because repaint/revalidate needs to calculate old scene boundaries
    public final void revalidate () {
        requiresFullValidation = true;
        revalidateUptoRoot ();
    }

    protected boolean isRepaintRequiredForRevalidating () {
        return true;
    }

    private void revalidateUptoRoot () {
        if (requiresPartValidation)
            return;
        if (isRepaintRequiredForRevalidating ())
            repaint ();
        calculatedPreferredBounds = null;
        requiresPartValidation = true;
        if (parentWidget != null)
            parentWidget.revalidateUptoRoot ();
        if (dependencies != null)
            for (Dependency dependency : dependencies)
                dependency.revalidateDependency ();
    }

    void layout (boolean fullValidation) {
        boolean childFullValidation = fullValidation || requiresFullValidation;
        for (Widget widget : children)
            widget.layout (childFullValidation);

        if (fullValidation)
            if (dependencies != null)
                for (Dependency dependency : dependencies)
                    dependency.revalidateDependency ();

        if (childFullValidation  ||  requiresPartValidation) {
            layout.layout (this);
            if (layout.requiresJustification (this)) {
                rejustify ();
            }
        }

        requiresFullValidation = false;
        requiresPartValidation = false;
    }

    private void rejustify () {
        requiresFullJustification = true;
        rejustifyUptoRoot ();
    }

    private void rejustifyUptoRoot () {
        if (requiresPartJustification)
            return;
        requiresPartJustification = true;
        if (parentWidget != null)
            parentWidget.rejustifyUptoRoot ();
    }

    final void justify () {
        if (requiresFullJustification) {
            layout.justify (this);
            if (layout.requiresJustification (this))
                for (Widget child : children)
                    child.rejustify ();
        }

        for (Widget widget : children)
            if (widget.requiresPartJustification)
                widget.justify ();

        requiresFullJustification = false;
        requiresPartJustification = false;
    }

    public final void paint () {
        assert bounds != null : "Scene.validate was not called";
        Graphics2D gr = scene.getGraphics ();

        AffineTransform previousTransform = gr.getTransform();
        gr.translate (location.x, location.y);

        Shape tempClip = null;
        if (checkClipping) {
            tempClip = gr.getClip ();
            gr.clip (bounds);
        }

        if (! checkClipping  ||  bounds.intersects (gr.getClipBounds ())) {
            Border border = getBorder ();
            Insets insets = border.getInsets ();

            if (isOpaque ()) {
                gr.setPaint (getBackground ());
                if (border.isOpaque ()) {
                    gr.fillRect (bounds.x, bounds.y, bounds.width, bounds.height);
                } else {
                    gr.fillRect (bounds.x + insets.left, bounds.y + insets.top, bounds.width - insets.left - insets.right, bounds.height - insets.top - insets.bottom);
                }
            }

            border.paint (gr, new Rectangle (bounds));

            if (checkClipping)
                gr.clipRect (bounds.x + insets.left, bounds.y + insets.top, bounds.width - insets.left - insets.right, bounds.height - insets.top - insets.bottom);

            paintWidget ();
            paintChildren ();
        }

        if (checkClipping)
            gr.setClip (tempClip);

        gr.setTransform(previousTransform);
    }

    protected void paintWidget () {
    }

    protected void paintChildren () {
        if (checkClipping) {
            Rectangle clipBounds = scene.getGraphics ().getClipBounds ();
            for (Widget child : children) {
                Point location = child.getLocation ();
                Rectangle bounds = child.getBounds ();
                bounds.translate (location.x, location.y);
                if (bounds.intersects (clipBounds))
                    child.paint ();
            }
        } else
            for (Widget child : children)
                child.paint ();
    }

    public final boolean equals (Object obj) {
        return this == obj;
    }

    public interface Dependency {

        public void revalidateDependency ();

    }

}
