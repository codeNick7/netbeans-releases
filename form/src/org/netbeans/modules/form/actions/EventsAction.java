/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is Forte for Java, Community Edition. The Initial
 * Developer of the Original Code is Sun Microsystems, Inc. Portions
 * Copyright 1997-2000 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.netbeans.developer.modules.loaders.form.actions;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import javax.swing.plaf.*;

import com.netbeans.ide.util.HelpCtx;
import com.netbeans.ide.util.actions.*;
//import com.netbeans.ide.nodes.Cookies;
import com.netbeans.ide.nodes.Node;
//import com.netbeans.developer.modules.loaders.form.formeditor.FormEditor;
//import com.netbeans.developer.modules.loaders.form.formeditor.RADNode;
//import com.netbeans.developer.modules.loaders.form.formeditor.EventsList;
import com.netbeans.developer.modules.loaders.form.FormNodeCookie;

/** Events action - subclass of NodeAction - enabled on RADNodes.
*
* @author   Ian Formanek
*/
public class EventsAction extends CookieAction {
  /** generated Serialized Version UID */
  static final long serialVersionUID = 4498451658201207121L;

  /** @return the mode of action. Possible values are disjunctions of MODE_XXX
  * constants. */
  protected int mode() {
    return MODE_EXACTLY_ONE;
  }
  
  /** Creates new set of classes that are tested by the cookie.
  *
  * @return list of classes the that the cookie tests
  */
  protected Class[] cookieClasses () {
    return new Class[] { FormNodeCookie.class };
  }
  /** Human presentable name of the action. This should be
  * presented as an item in a menu.
  * @return the name of the action
  */
  public String getName() {
    return com.netbeans.ide.util.NbBundle.getBundle (EventsAction.class).getString ("ACT_Events");
  }

  /** Help context where to find more about the action.
  * @return the help context for this action
  */
  public HelpCtx getHelpCtx() {
    return new HelpCtx(EventsAction.class);
  }

  /** Icon resource.
  * @return name of resource for icon
  */
  protected String iconResource () {
    return "/com/netbeans/developer/modules/loaders/form/resources/events.gif";
  }

  /**
  * Standard perform action extended by actually activated nodes.
  *
  * @param activatedNodes gives array of actually activated nodes.
  */
  protected void performAction (Node[] activatedNodes) {
  }

  /** Returns a JMenuItem that presents the Action, that implements this
  * interface, in a MenuBar.
  * @return the JMenuItem representation for the Action
  * /
  public JMenuItem getMenuPresenter() {
    return getPopupPresenter ();
  }

  /** Returns a JMenuItem that presents the Action, that implements this
  * interface, in a Popup Menu.
  * @return the JMenuItem representation for the Action
  * /
  public JMenuItem getPopupPresenter() {
    JMenu popupMenu = new JMenu (FormEditor.getFormBundle ().getString ("ACT_Events"));
    popupMenu.setEnabled (isEnabled ());
    popupMenu.addMenuListener(new MenuListener() {
        Hashtable mapping = new Hashtable ();
        public void menuSelected(MenuEvent e) {
          JMenu menu = (JMenu)e.getSource ();
          menu.removeAll ();
          Node[] nodes = getActivatedNodes ();
          if (nodes.length == 0) return;
          Node n = nodes[0]; // we suppose that one node is activated

          if (! (Cookies.isInstanceOf (nodes[0].getCookie (), FormNodeCookie.class))) 
            return;
            
          RADNode node = ((FormNodeCookie) Cookies.getInstanceOf (n.getCookie(), FormNodeCookie.class)).getFormNode ();
          EventsList em = node.getEventsList ();
          EventsList.EventSet[] setHandlers = em.getEventSets ();
         
          for (int i = 0; i < setHandlers.length; i++) {
            String name = setHandlers[i].getName ();            
            JMenu m = new JMenu (name.substring (0, 1).toUpperCase () + name.substring (1));
            menu.add (m);
            EventsList.Event[] events = setHandlers[i].getEvents();
            for (int j = 0; j < events.length; j++) {
              StringBuffer menuText = new StringBuffer(events[j].getName ());
              if (events[j].getHandler () != null) {
                menuText.append (" [");
                menuText.append (events[j].getHandler ().getName ());
                menuText.append ("]");
              }
              JMenuItem jmi = new JMenuItem (menuText.toString ());
              m.add (jmi);
              mapping.put (jmi, events[j]);
              jmi.addActionListener (new ActionListener () {
                  public void actionPerformed (ActionEvent evt) {
                    EventsList.Event event = (EventsList.Event)mapping.get (evt.getSource ());
                    if (event != null) {
                      if (event.getHandler () == null)
                        event.createDefaultEventHandler ();
                      event.gotoEventHandler ();
                    }
                  }
                }
              );
            }
          }
        }
        public void menuDeselected(MenuEvent e) {
        }
        public void menuCanceled(MenuEvent e) {
        }
      }
    );
    return popupMenu;
  }
*/
}
/*
 * Log
 *  1    Gandalf   1.0         3/26/99  Ian Formanek    
 * $
 */
