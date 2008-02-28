/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 4.1 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=true,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package org.netbeans.modules.css.parser;

public class SimpleNode implements Node {

    protected Node parent;
    protected Node[] children;
    protected int id;
    protected Object value;
    protected CSSParser parser;
    protected Token firstToken;
    protected Token lastToken;
    private String image;

    public SimpleNode(int i) {
        id = i;
    }

    public SimpleNode(CSSParser p, int i) {
        this(i);
        parser = p;
    }

    public int kind() {
        return id;
    }

    public int startOffset() {
        return jjtGetFirstToken().offset;
    }

    public int endOffset() {
        //why this happens???
        if (jjtGetLastToken().image == null) {
            System.err.println("ERROR: lastToken image is null! : " + jjtGetLastToken());
            return jjtGetLastToken().offset;
        } else {
            return jjtGetLastToken().offset + jjtGetLastToken().image.length();
        }
    }

    public String image() {
        synchronized (this) {
            if (image == null) {
                StringBuffer sb = new StringBuffer();
                if (jjtGetFirstToken() == jjtGetLastToken()) {
                    image = jjtGetFirstToken().image;
                } else {
                    Token t = jjtGetFirstToken();
                    Token last = jjtGetLastToken();
                    while (t != null && t.offset <= last.offset) { //also include the last token
                        sb.append(t.image);
                        t = t.next;
                    }
                    image = sb.toString();
                }
            }

            return image;
        }
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(Node n) {
        parent = n;
    }

    public Node jjtGetParent() {
        return parent;
    }

    public void jjtAddChild(Node n, int i) {
        if (children == null) {
            children = new Node[i + 1];
        } else if (i >= children.length) {
            Node c[] = new Node[i + 1];
            System.arraycopy(children, 0, c, 0, children.length);
            children = c;
        }
        children[i] = n;
    }

    public Node jjtGetChild(int i) {
        return children[i];
    }

    public int jjtGetNumChildren() {
        return (children == null) ? 0 : children.length;
    }

    public void jjtSetValue(Object value) {
        this.value = value;
    }

    public Object jjtGetValue() {
        return value;
    }

    public Token jjtGetFirstToken() {
        return firstToken;
    }

    public void jjtSetFirstToken(Token token) {
        this.firstToken = token;
    }

    public Token jjtGetLastToken() {
        return lastToken;
    }

    public void jjtSetLastToken(Token token) {
        this.lastToken = token;
    }

    /* You can override these two methods in subclasses of SimpleNode to
    customize the way the node appears when the tree is dumped.  If
    your output uses more than one line you should override
    toString(String), otherwise overriding toString() is probably all
    you need to do. */
    public String toString() {
        return CSSParserTreeConstants.jjtNodeName[id] 
                + " [" 
                + startOffset()
                + " - " 
                + endOffset() 
                + "]";
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    /* Override this method if you want to customize how the node dumps
    out its children. */
    public void dump(String prefix) {
        System.out.println(toString(prefix));
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode) children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    public void visitChildren(NodeVisitor visitor) {
        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode) children[i];
                if (n != null) {
                    visitor.visit(n);
                    n.visitChildren(visitor);
                }
            }
        }
    }
}

/* JavaCC - OriginalChecksum=0f675acc04cfd880a8baf2208510fa8c (do not edit this line) */
