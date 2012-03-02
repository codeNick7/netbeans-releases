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
package org.netbeans.modules.parsing.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.netbeans.junit.NbTestCase;
import org.netbeans.modules.parsing.lucene.support.Convertor;
import org.netbeans.modules.parsing.lucene.support.DocumentIndex;
import org.netbeans.modules.parsing.lucene.support.Index;
import org.netbeans.modules.parsing.lucene.support.IndexDocument;
import org.netbeans.modules.parsing.lucene.support.IndexManager;
import org.netbeans.modules.parsing.lucene.support.Queries;

/**
 * Checks reader / writer behaviour with Lucene transationsal commits
 * 
 * @author sdedic
 */
public class IndexTransactionTest extends NbTestCase {

    public IndexTransactionTest(String name) {
        super(name);
    }
    
    private LuceneIndex index;
    private File cache;
    
    private void setupLuceneIndex() throws Exception {
        clearWorkDir();
        final File wd = getWorkDir();
        cache = new File(wd,"cache");
        cache.mkdirs();
        index = LuceneIndex.create(cache, new KeywordAnalyzer());
        
    }

    /**
     * Checks that Index.query is not affected by Index.flush, before Index.store
     * 
     * @throws Exception 
     */
    
    public void testIndexQueryAndFlush() throws Exception {
        setupLuceneIndex();

        //Empty index => invalid
        assertEquals(Index.Status.EMPTY, index.getStatus(true));
        final List<String> refs = new ArrayList<String>();
        refs.add("A");
        final Set<String> toDel = new HashSet<String>();
        
        index.txStore(
                refs,
                toDel,
                new StrToDocConvertor("resources"),
                new StrToQueryCovertor("resource"));

        //Existing index => valid
        assertEquals(Index.Status.WRITING, index.getStatus(true));
        assertTrue(cache.listFiles().length>0);
        
        // Open a reader, and check that the reader does NOT see the change, although flushed
        Collection<String> result = new LinkedList<String>();
        AtomicBoolean cancel = new AtomicBoolean(false);
        index.query(
                result, 
                new DocToStrConvertor("resources"),
                Queries.createFieldSelector("resources"), 
                cancel, 
                Queries.createQuery("resources", "resources", "A", Queries.QueryKind.EXACT)
        );
        assertEquals("Reader must not see uncommitted data", 0, result.size());
        
        // flush the writer
        refs.clear(); refs.add("AB");
        index.store(
                refs, 
                toDel,
                new StrToDocConvertor("resources"), 
                new StrToQueryCovertor("resources"), 
                true
        );
        
        assertEquals(Index.Status.VALID, index.getStatus(true));
        
        index.query(
                result, 
                new DocToStrConvertor("resources"),
                Queries.createFieldSelector("resources"), 
                cancel, 
                Queries.createQuery("resources", "resources", "A", Queries.QueryKind.EXACT)
        );
        assertEquals("Reader sees data after store", 1, result.size());

        result.clear();
        index.query(
                result, 
                new DocToStrConvertor("resources"),
                Queries.createFieldSelector("resources"), 
                cancel, 
                Queries.createQuery("resources", "resources", "A", Queries.QueryKind.PREFIX)
        );
        assertEquals("Reader sees data after store", 2, result.size());
    }
    
    /**
     * Checks that DocumentIndex.addDocument does not affect DocumentIndex.query
     * or DocumentIndex.findByPrimaryKey until DocumentIndex.store is called
     * 
     * @throws Exception 
     */
    public void testAddDocumentAndQuery() throws Exception {
        setupLuceneIndex();
        
        DocumentIndex docIndex = IndexManager.createDocumentIndex(index);
        IndexDocument doc = IndexManager.createDocument("manicka");
        doc.addPair("name", "manicka", true, false);
        doc.addPair("age", "10", true, true);
        
        Collection<? extends IndexDocument> results = 
                docIndex.query("name", "manicka", Queries.QueryKind.EXACT, "age");
        
        assertTrue("Index must be initially empty", results.isEmpty());
        
        // assume the cache is not flushed
        docIndex.addDocument(doc);
        
        assertTrue("addDocument visible without flush", results.isEmpty());
        
        IndexDocument doc2 = IndexManager.createDocument("hurvinek");
        doc2.addPair("name", "hurvinek", true, true);
        doc2.addPair("age", "11", true, true);
        
        // force flush to disk
        ((DocumentIndexImpl)docIndex).testClarDataRef();
        docIndex.addDocument(doc2);
        
        results = 
                docIndex.query("age", "1", Queries.QueryKind.PREFIX, "name", "age");
        
        assertTrue("addDocument visible without flush", results.isEmpty());
        
        // now store
        docIndex.store(false);
        
        results = 
                docIndex.query("age", "1", Queries.QueryKind.PREFIX, "name", "age");
        
        assertEquals(2, results.size());
    }
    
    public void testRemoveDocumentAndQuery() throws Exception {
        setupLuceneIndex();
        
        DocumentIndex docIndex = IndexManager.createDocumentIndex(index);
        IndexDocument doc = IndexManager.createDocument("manicka");
        doc.addPair("name", "manicka", true, false);
        doc.addPair("age", "10", true, true);
        
        Collection<? extends IndexDocument> results;
        
        // assume the cache is not flushed
        docIndex.addDocument(doc);

        IndexDocument doc2 = IndexManager.createDocument("hurvinek");
        doc2.addPair("name", "hurvinek", true, true);
        doc2.addPair("age", "11", true, true);
        docIndex.addDocument(doc2);
        // now store
        
        docIndex.store(false);
        
        results = 
                docIndex.query("age", "1", Queries.QueryKind.PREFIX, "name", "age");
        assertEquals(2, results.size());
        
        // now remove one of the documents
        docIndex.removeDocument(doc.getPrimaryKey());
        
        results = 
                docIndex.query("age", "1", Queries.QueryKind.PREFIX, "name", "age");
        assertEquals(2, results.size());
        
        // force flush
        ((DocumentIndexImpl)docIndex).testClarDataRef();
        docIndex.removeDocument(doc2.getPrimaryKey());
        
        results = 
                docIndex.query("name", "hurvinek", Queries.QueryKind.EXACT, "name", "age");
        assertEquals(1, results.size());
        
        // store
        docIndex.store(false);
        
        results = 
                docIndex.query("name", "manicka", Queries.QueryKind.EXACT, "name", "age");
        assertEquals(0, results.size());
        results = 
                docIndex.query("name", "hurvinek", Queries.QueryKind.EXACT, "name", "age");
        assertEquals(0, results.size());
    }
    
    private static class DocToStrConvertor implements Convertor<Document, String> {
        private String name;

        public DocToStrConvertor(String name) {
            this.name = name;
        }

        @Override
        public String convert(Document p) {
            return p.get(name);
        }
    }

    private static class StrToDocConvertor implements Convertor<String, Document>{
        
        private final String name;
        
        public StrToDocConvertor(final String name) {
            this.name = name;
        }
        
        @Override
        public Document convert(final String p) {
            final Document doc = new Document();
            doc.add(new Field(name, p, Field.Store.YES, Field.Index.ANALYZED));
            return doc;
        }        
    }
    
    private static class StrToQueryCovertor implements Convertor<String, Query> {
        
        private final String name;
        
        public StrToQueryCovertor(final String name) {
            this.name = name;
        }
        
        @Override
        public Query convert(String p) {
            return new TermQuery(new Term(name, p));
        }        
    }
}
