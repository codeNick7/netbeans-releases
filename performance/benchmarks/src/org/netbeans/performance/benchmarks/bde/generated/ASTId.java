/* Generated By:JJTree: Do not edit this line. ASTId.java */

package org.netbeans.performance.benchmarks.bde.generated;

public class ASTId extends SimpleNode {
  public ASTId(int id) {
    super(id);
  }

  public ASTId(BDEParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(BDEParserVisitor visitor, Object data) throws Exception {
    return visitor.visit(this, data);
  }
}
