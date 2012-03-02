/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
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
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
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
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */
package org.netbeans.modules.css.lib.api.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author mfukala@netbeans.org
 */
public interface Node {

    public String name();

    public Collection<Node> children();

    public Node parent();
    
    public void accept(NodeVisitor visitor);

    public CharSequence image();
    
    
    
    
    static abstract class AbstractNode implements Node {
        
        private Node parent;

        void setParent(Node parent) {
            this.parent = parent;
        }
        
        @Override
        public Node parent() {
            return parent;
        }

        @Override
        public void accept(NodeVisitor visitor) {
            if(visitor.visit(this)) {
                for(Node child : children()) {
                    child.accept(visitor);
                }
                visitor.unvisit(this);
            }
        }

    }
    
    static class ResolvedTokenNode extends AbstractNode {
        
        private ResolvedToken resolvedToken = null;

        public ResolvedTokenNode() {
        }
        
        public void setResolvedToken(ResolvedToken resolvedToken) {
            this.resolvedToken = resolvedToken;
        }
        
        @Override
        public Collection<Node> children() {
            return Collections.emptyList();
        }
        
        public Token getToken() {
            return resolvedToken.token();
        }

        @Override
        public CharSequence image() {
            return resolvedToken.token().image();
        }

        @Override
        public String toString() {
            return resolvedToken.token().toString();
        }
       
        @Override
        public String name() {
            return resolvedToken.getGrammarElement().value();
        }
        
    }
    
    static class GrammarElementNode extends AbstractNode {
        
        private GrammarElement element;

        private Collection<Node> children = new ArrayList<Node>();

        public GrammarElementNode(GrammarElement group) {
            this.element = group;
        }

        public <T extends AbstractNode> T addChild(T node) {
            children.add(node);
            node.setParent(this);
            
            return node;
        }
        
        public <T extends AbstractNode> boolean removeChild(T node) {
            node.setParent(null);
            return children.remove(node);
        }
        
        @Override
        public String name() {
            return element.getName();
        }
        
        @Override
        public Collection<Node> children() {
            return children;
        }

        @Override
        public String toString() {
            return element.toString();
        }

        @Override
        public CharSequence image() {
            StringBuilder sb = new StringBuilder();
            for(Node child : children()) {
                sb.append(child.image());
            }
            return sb.toString();
        }

        
    }

   
}