/*
 * DebuggingView.java
 *
 * Created on 3. duben 2008, 15:17
 */

package org.netbeans.modules.debugger.jpda.ui.debugging;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.netbeans.modules.debugger.jpda.ui.views.ViewModelListener;

import org.openide.explorer.ExplorerManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author  Dan
 */
public class DebuggingView extends TopComponent implements org.openide.util.HelpCtx.Provider,
       ExplorerManager.Provider, PropertyChangeListener, TreeExpansionListener  {

    /** unique ID of <code>TopComponent</code> (singleton) */
    private static final String ID = "debugging"; //NOI18N
    
    private ExplorerManager manager = new ExplorerManager();
    private transient ViewModelListener viewModelListener;
    
    private DebugTreeView treeView;
    private JPanel treePanel;
    private TapPanel tapPanel;
    private InfoPanel infoPanel;
    
    /**
     * instance/singleton of this class
     *
     * @see  #getInstance
     */
    private static Reference<DebuggingView> instance = null;
    
    
    /** Creates new form DebuggingView */
    public DebuggingView() {
        setIcon (Utilities.loadImage ("org/netbeans/modules/debugger/jpda/resources/debugging.png")); // NOI18N
        // Remember the location of the component when closed.
        putClientProperty("KeepNonPersistentTCInModelWhenClosed", Boolean.TRUE); // NOI18N
        
        initComponents();
        
        treeView = new DebugTreeView();
        treeView.setRootVisible(false);
        //treeView.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        treePanel = new ZebraPanel(12,12);
        mainPanel.add(treePanel, BorderLayout.CENTER);
        
        treePanel.setLayout(new BorderLayout());
        treePanel.add(treeView, BorderLayout.CENTER);
        
        tapPanel = new TapPanel();
        tapPanel.setOrientation(TapPanel.DOWN);
        // tooltip
        KeyStroke toggleKey = KeyStroke.getKeyStroke(KeyEvent.VK_T,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        String keyText = Utilities.keyToString(toggleKey);
        tapPanel.setToolTipText(NbBundle.getMessage(DebuggingView.class, "LBL_TapPanel", keyText)); //NOI18N
        mainPanel.add(tapPanel, BorderLayout.SOUTH);
        
        infoPanel = new InfoPanel();
        tapPanel.add(infoPanel);
        
        manager.addPropertyChangeListener(this);
        treeView.addTreeExpansionListener(this);
        
        Collection<Node> col1 = new ArrayList<Node>();
        col1.add(new ElemNode("subnode 1", 4));
        col1.add(new ElemNode("subnode 2", 6));
        ElemNode rootNode = new ElemNode("root node", new ElemNodeChildren2(col1));

        sessionComboBox.removeAllItems();
        sessionComboBox.addItem(rootNode.getDisplayName());
        manager.setRootContext(rootNode);
    }
 
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainScrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        sessionComboBox = new javax.swing.JComboBox();

        setLayout(new java.awt.BorderLayout());

        mainScrollPane.setBorder(null);
        mainScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mainPanel.setLayout(new java.awt.BorderLayout());

        leftPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("Tree.background"));
        leftPanel.setPreferredSize(new java.awt.Dimension(24, 0));
        leftPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        mainPanel.add(leftPanel, java.awt.BorderLayout.WEST);

        rightPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("Tree.background"));
        rightPanel.setPreferredSize(new java.awt.Dimension(24, 0));
        rightPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        mainPanel.add(rightPanel, java.awt.BorderLayout.EAST);

        sessionComboBox.setMaximumRowCount(1);
        mainPanel.add(sessionComboBox, java.awt.BorderLayout.NORTH);

        mainScrollPane.setViewportView(mainPanel);

        add(mainScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JComboBox sessionComboBox;
    // End of variables declaration//GEN-END:variables

    public void setRootContext(Node root) {
        manager.setRootContext(root);
        sessionComboBox.removeAllItems();
        sessionComboBox.addItem(root.getDisplayName());
    }
    
    public ExplorerManager getExplorerManager() {
        return manager;
    }

    public static DebuggingView getInstance() {
        DebuggingView view;
        view = (DebuggingView) WindowManager.getDefault().findTopComponent(ID);
        if (view == null) {
            view = getDefault();
        }
        return view;
    }
    
    /**
     * Singleton accessor reserved for the window systemm only. The window
     * system calls this method to create an instance of this
     * <code>TopComponent</code> from a <code>.settings</code> file.
     * <p>
     * <em>This method should not be called anywhere except from the window
     * system's code. </em>
     *
     * @return  singleton - instance of this class
     */
    public static synchronized DebuggingView getDefault() {
        DebuggingView view;
        if (instance == null) {
            view = new DebuggingView();
            instance = new WeakReference<DebuggingView>(view);
        } else {
            view = instance.get();
            if (view == null) {
                view = new DebuggingView();
                instance = new WeakReference<DebuggingView>(view);
            }
        }
        return view;
    }
    
//    public void propertyChange(PropertyChangeEvent evt) {
//        //throw new UnsupportedOperationException("Not supported yet.");
//    }
    
    @Override
    protected String preferredID() {
        return this.getClass().getName();
    }

    @Override
    protected void componentShowing() {
        super.componentShowing ();
        if (viewModelListener != null) {
            viewModelListener.setUp();
            return;
        }
//        if (debuggingPanel == null) {
//            setLayout(new BorderLayout ());
//            debuggingPanel = new DebuggingPanel();
//            debuggingPanel.setName(NbBundle.getMessage (DebuggingView2.class, "CTL_Debugging_tooltip")); // NOI18N
//            add(debuggingPanel, BorderLayout.CENTER);
//        }
        if (viewModelListener != null)
            throw new InternalError ();
        viewModelListener = new ViewModelListener (
            "DebuggingView",
            this
        );
    }
    
    @Override
    protected void componentHidden() {
        super.componentHidden ();
        viewModelListener.destroy ();
    }
    
//    public org.openide.util.HelpCtx getHelpCtx() {
//        return new org.openide.util.HelpCtx("NetbeansDebuggerSourcesNode"); // NOI18N
//    }
    
    @Override
    public int getPersistenceType() {
        return PERSISTENCE_ALWAYS;
    }
        
    @Override
    public boolean requestFocusInWindow() {
//        return super.requestFocusInWindow();
//        if (debuggingPanel == null) return false;
        return treeView.requestFocusInWindow ();
    }
    
    @Override
    public String getName() {
        return NbBundle.getMessage (DebuggingView.class, "CTL_Debugging_view"); // NOI18N
    }
    
    @Override
    public String getToolTipText() {
        return NbBundle.getMessage (DebuggingView.class, "CTL_Debugging_tooltip"); // NOI18N
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (ExplorerManager.PROP_ROOT_CONTEXT.equals(propertyName) || 
                ExplorerManager.PROP_NODE_CHANGE.equals(propertyName)) {
            refreshView();
        }
    }

    public void treeExpanded(TreeExpansionEvent event) {
        refreshView();
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        refreshView();
    }
    
    // **************************************************************************
    // **************************************************************************
    
    private void refreshView() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                leftPanel.removeAll();
                rightPanel.removeAll();
                for (TreePath path : treeView.getVisiblePaths()) {
                    JTree tree = treeView.getTree();
                    Rectangle rect = tree.getRowBounds(tree.getRowForPath(path));
                    double height = rect.getHeight();
                    ImageIcon imageIcon = new ImageIcon(Utilities.loadImage(
                            "org/netbeans/modules/debugger/jpda/resources/debugging.png"));
                    JLabel label = new JLabel(imageIcon);
                    label.setBackground(Color.YELLOW);
                    label.setPreferredSize(new Dimension(imageIcon.getIconWidth(), (int)Math.round(height)));
                    leftPanel.add(label);
                    ClickableIcon icon = new ClickableIcon();
                    
                    // [TODO] put this inside ClicableIcon constructor
                    icon.setPreferredSize(new Dimension(imageIcon.getIconWidth(), (int)Math.round(height)));
                    rightPanel.add(icon);
                }
                leftPanel.revalidate();
                leftPanel.repaint();
                rightPanel.revalidate();
                rightPanel.repaint();
            }
        });
    }
    
    // **************************************************************************
    // ElemNode
    // **************************************************************************
    static class ElemNode extends AbstractNode {
        
        private String description;
        
        public ElemNode(String description, int childrenCount) {
            super(childrenCount == 0 ? Children.LEAF : new ElemNodeChildren(childrenCount));
            this.description = description;
            setDisplayName(description); 
        }
        
        public ElemNode(String description, Children children) {
            super(children);
            this.description = description;
            setDisplayName(description); 
        }
        
        @Override
        public Image getIcon(int type) {
             return Utilities.loadImage ("org/netbeans/modules/debugger/jpda/resources/classLoader.gif");
        }

        @Override
        public Image getOpenedIcon(int type) {
            return Utilities.loadImage ("org/netbeans/modules/debugger/jpda/resources/classLoaderOpen.gif");
        }

        @Override
        public String getDisplayName() {
            return description;
        }
        
    }

    static class ElemNodeChildren extends Children.Array {

        private int count;
        
        ElemNodeChildren(int count) {
            this.count = count;
        }
        
        @Override
        protected Collection<Node> initCollection() {
            Collection<Node> result = new ArrayList<Node>();
            for (int x = 1; x <= count; x++) {
                result.add(new ElemNode("node " + x, 0));
            }
            return result;
        }

    }

    static class ElemNodeChildren2 extends Children.Array {

        private Collection<Node> children;
        
        ElemNodeChildren2(Collection<Node> children) {
            this.children = children;
        }
        
        @Override
        protected Collection<Node> initCollection() {
            return children;
        }

    }

}