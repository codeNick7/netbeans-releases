/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TerminalPanel.java
 *
 * Created on Feb 16, 2010, 5:44:59 PM
 */

package org.netbeans.terminal.example.comprehensive;

/**
 *
 * @author ivan
 */
public final class TerminalPanel extends javax.swing.JPanel {

    public static enum Provider {
	DEFAULT,
	TERM,
    }

    public static enum DispatchThread {
	EDT,
	RP,
    }

    public static enum Execution {
	RICH,
	NATIVE,
    }

    public static enum IOShuttling {
	INTERNAL,
	EXTERNAL,
    }

    /** Creates new form TerminalPanel */
    public TerminalPanel() {
        initComponents();
    }

    public String getCommand() {
	return commandTextField.getText();
    }

    public Provider getContainerProvider() {
	if (containerProviderRadioButton_DEFAULT.isSelected())
	    return Provider.DEFAULT;
	else
	    return Provider.TERM;
    }

    public Provider getIOProvider() {
	if (ioProviderRadioButton_DEFAULT.isSelected())
	    return Provider.DEFAULT;
	else
	    return Provider.TERM;
    }

    public DispatchThread getThread() {
	if (threadRadioButton_EDT.isSelected())
	    return DispatchThread.EDT;
	else
	    return DispatchThread.RP;
    }

    public Execution getExecution() {
	if (executionRadioButton_RICH.isSelected())
	    return Execution.RICH;
	else
	    return Execution.NATIVE;
    }

    public boolean isRestartable() {
	return restartableCheckBox.isSelected();
    }

    public IOShuttling getIOShuttling() {
	if (ioShutlingRadioButton_INTERNAL.isSelected())
	    return IOShuttling.INTERNAL;
	else
	    return IOShuttling.EXTERNAL;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                tcButtonGroup = new javax.swing.ButtonGroup();
                providerButtonGroup = new javax.swing.ButtonGroup();
                containerProviderButtonGroup = new javax.swing.ButtonGroup();
                ioProviderButtonGroup = new javax.swing.ButtonGroup();
                threadButtonGroup = new javax.swing.ButtonGroup();
                executionButtonGroup = new javax.swing.ButtonGroup();
                ioShuttlingButtonGroup = new javax.swing.ButtonGroup();
                commandLabel = new javax.swing.JLabel();
                commandTextField = new javax.swing.JTextField();
                containerProviderLabel = new javax.swing.JLabel();
                containerProviderRadioButton_DEFAULT = new javax.swing.JRadioButton();
                containerProviderRadioButton_TERM = new javax.swing.JRadioButton();
                jLabel1 = new javax.swing.JLabel();
                ioProviderRadioButton_DEFAULT = new javax.swing.JRadioButton();
                ioProviderRadioButton_TERM = new javax.swing.JRadioButton();
                threadLabel = new javax.swing.JLabel();
                threadRadioButton_EDT = new javax.swing.JRadioButton();
                rpRadioButton = new javax.swing.JRadioButton();
                restartableLabel = new javax.swing.JLabel();
                restartableCheckBox = new javax.swing.JCheckBox();
                excutionLabel = new javax.swing.JLabel();
                executionRadioButton_RICH = new javax.swing.JRadioButton();
                excutionRadioButton_NATIVE = new javax.swing.JRadioButton();
                ioShuttlingLabel = new javax.swing.JLabel();
                ioShutlingRadioButton_INTERNAL = new javax.swing.JRadioButton();
                ioShuttlingRadioButton_EXTERNAL = new javax.swing.JRadioButton();

                commandLabel.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.commandLabel.text")); // NOI18N

                commandTextField.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.commandTextField.text")); // NOI18N

                containerProviderLabel.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.containerProviderLabel.text")); // NOI18N

                containerProviderButtonGroup.add(containerProviderRadioButton_DEFAULT);
                containerProviderRadioButton_DEFAULT.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.containerProviderRadioButton_DEFAULT.text")); // NOI18N

                containerProviderButtonGroup.add(containerProviderRadioButton_TERM);
                containerProviderRadioButton_TERM.setSelected(true);
                containerProviderRadioButton_TERM.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.containerProviderRadioButton_TERM.text")); // NOI18N

                jLabel1.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.jLabel1.text")); // NOI18N

                ioProviderButtonGroup.add(ioProviderRadioButton_DEFAULT);
                ioProviderRadioButton_DEFAULT.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.ioProviderRadioButton_DEFAULT.text")); // NOI18N

                ioProviderButtonGroup.add(ioProviderRadioButton_TERM);
                ioProviderRadioButton_TERM.setSelected(true);
                ioProviderRadioButton_TERM.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.ioProviderRadioButton_TERM.text")); // NOI18N

                threadLabel.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.threadLabel.text")); // NOI18N

                threadButtonGroup.add(threadRadioButton_EDT);
                threadRadioButton_EDT.setSelected(true);
                threadRadioButton_EDT.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.threadRadioButton_EDT.text")); // NOI18N

                threadButtonGroup.add(rpRadioButton);
                rpRadioButton.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.rpRadioButton.text")); // NOI18N

                restartableLabel.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.restartableLabel.text")); // NOI18N
                restartableLabel.setToolTipText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.restartableLabel.toolTipText")); // NOI18N

                restartableCheckBox.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.restartableCheckBox.text")); // NOI18N

                excutionLabel.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.excutionLabel.text")); // NOI18N

                executionButtonGroup.add(executionRadioButton_RICH);
                executionRadioButton_RICH.setSelected(true);
                executionRadioButton_RICH.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.executionRadioButton_RICH.text")); // NOI18N

                executionButtonGroup.add(excutionRadioButton_NATIVE);
                excutionRadioButton_NATIVE.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.excutionRadioButton_NATIVE.text")); // NOI18N

                ioShuttlingLabel.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.ioShuttlingLabel.text")); // NOI18N

                ioShuttlingButtonGroup.add(ioShutlingRadioButton_INTERNAL);
                ioShutlingRadioButton_INTERNAL.setSelected(true);
                ioShutlingRadioButton_INTERNAL.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.ioShutlingRadioButton_INTERNAL.text")); // NOI18N

                ioShuttlingButtonGroup.add(ioShuttlingRadioButton_EXTERNAL);
                ioShuttlingRadioButton_EXTERNAL.setText(org.openide.util.NbBundle.getMessage(TerminalPanel.class, "TerminalPanel.ioShuttlingRadioButton_EXTERNAL.text")); // NOI18N

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(containerProviderLabel)
                                                                        .addComponent(commandLabel))
                                                                .addComponent(jLabel1)
                                                                .addComponent(ioShuttlingLabel))
                                                        .addComponent(excutionLabel))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(ioShutlingRadioButton_INTERNAL)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(ioShuttlingRadioButton_EXTERNAL))
                                                        .addComponent(commandTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(containerProviderRadioButton_DEFAULT)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(containerProviderRadioButton_TERM))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(executionRadioButton_RICH)
                                                                        .addComponent(ioProviderRadioButton_DEFAULT))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(excutionRadioButton_NATIVE)
                                                                        .addComponent(ioProviderRadioButton_TERM)
                                                                        .addComponent(rpRadioButton)))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(57, 57, 57)
                                                .addComponent(restartableLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(restartableCheckBox))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(84, 84, 84)
                                                .addComponent(threadLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(threadRadioButton_EDT)))
                                .addContainerGap(208, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(commandTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(commandLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(containerProviderLabel)
                                        .addComponent(containerProviderRadioButton_DEFAULT)
                                        .addComponent(containerProviderRadioButton_TERM))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(ioProviderRadioButton_DEFAULT)
                                        .addComponent(ioProviderRadioButton_TERM))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ioShuttlingLabel)
                                        .addComponent(ioShutlingRadioButton_INTERNAL)
                                        .addComponent(ioShuttlingRadioButton_EXTERNAL))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(excutionLabel)
                                        .addComponent(executionRadioButton_RICH)
                                        .addComponent(excutionRadioButton_NATIVE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(threadLabel)
                                        .addComponent(threadRadioButton_EDT)
                                        .addComponent(rpRadioButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(restartableLabel)
                                        .addComponent(restartableCheckBox))
                                .addContainerGap())
                );
        }// </editor-fold>//GEN-END:initComponents


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel commandLabel;
        private javax.swing.JTextField commandTextField;
        private javax.swing.ButtonGroup containerProviderButtonGroup;
        private javax.swing.JLabel containerProviderLabel;
        private javax.swing.JRadioButton containerProviderRadioButton_DEFAULT;
        private javax.swing.JRadioButton containerProviderRadioButton_TERM;
        private javax.swing.JLabel excutionLabel;
        private javax.swing.JRadioButton excutionRadioButton_NATIVE;
        private javax.swing.ButtonGroup executionButtonGroup;
        private javax.swing.JRadioButton executionRadioButton_RICH;
        private javax.swing.ButtonGroup ioProviderButtonGroup;
        private javax.swing.JRadioButton ioProviderRadioButton_DEFAULT;
        private javax.swing.JRadioButton ioProviderRadioButton_TERM;
        private javax.swing.JRadioButton ioShutlingRadioButton_INTERNAL;
        private javax.swing.ButtonGroup ioShuttlingButtonGroup;
        private javax.swing.JLabel ioShuttlingLabel;
        private javax.swing.JRadioButton ioShuttlingRadioButton_EXTERNAL;
        private javax.swing.JLabel jLabel1;
        private javax.swing.ButtonGroup providerButtonGroup;
        private javax.swing.JCheckBox restartableCheckBox;
        private javax.swing.JLabel restartableLabel;
        private javax.swing.JRadioButton rpRadioButton;
        private javax.swing.ButtonGroup tcButtonGroup;
        private javax.swing.ButtonGroup threadButtonGroup;
        private javax.swing.JLabel threadLabel;
        private javax.swing.JRadioButton threadRadioButton_EDT;
        // End of variables declaration//GEN-END:variables

}
