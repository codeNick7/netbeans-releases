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
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */
package org.netbeans.modules.xml.schema.completion;

import java.util.List;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.xml.schema.completion.util.CompletionContextImpl;
import org.netbeans.modules.xml.schema.completion.util.CompletionUtil;
import org.netbeans.modules.xml.text.syntax.XMLSyntaxSupport;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Samaresh (Samaresh.Panda@Sun.Com)
 */
public class CompletionQuery extends AsyncCompletionQuery {
        
    /**
     * Creates a new instance of CompletionQuery
     */
    public CompletionQuery(FileObject primaryFile) {
        this.primaryFile = primaryFile;
    }    
    
    /**
     *
     */
    protected void prepareQuery(JTextComponent component) {
        this.component = component;
    }
    
    /**
     *
     */
    protected void query(CompletionResultSet resultSet,
            Document doc, int caretOffset) {                
        List<CompletionResultItem> items = getCompletionItems(doc, caretOffset);
        if(items != null) resultSet.addAllItems(items);
        resultSet.finish();
    }
    
    
    /**
     * This method is needed for unit testing purposes.
     */
    List<CompletionResultItem> getCompletionItems(Document doc, int caretOffset) {
        List<CompletionResultItem> completionItems = null;
        
        //Step 1: create a context
        XMLSyntaxSupport support = (XMLSyntaxSupport) ((BaseDocument)doc).getSyntaxSupport();
        context = new CompletionContextImpl(primaryFile, support, caretOffset);
        
        //Step 2: Accumulate all models and initialize the context
        if(!context.initModels() || !context.initContext()) {
            return null;
        }
                
        //Step 3: Query
        switch (context.getCompletionType()) {
            case COMPLETION_TYPE_ELEMENT:
                completionItems = CompletionUtil.getElements(context);
                break;
                
            case COMPLETION_TYPE_ATTRIBUTE:
                completionItems = CompletionUtil.getAttributes(context);
                break;
            
            case COMPLETION_TYPE_VALUE:
                completionItems = CompletionUtil.getElementValues(context);
                break;            
            
            case COMPLETION_TYPE_ENTITY:
                break;
            
            case COMPLETION_TYPE_NOTATION:
                break;
                
            default:
                break;
        }
        
        return completionItems;
    }
            
    private JTextComponent component;
    private FileObject primaryFile;
    private CompletionContextImpl context;
}
