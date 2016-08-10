package alphabit.parser.bnf;

public class ProductionNode implements GrammerNode{
	private String value;

	public ProductionNode(String value) {
	    this.value = value;
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return GrammerNode.PRODUCTION_NODE;
	}
	
	public void accept(ExpressionNodeVisitor visitor) {
	    visitor.visit(this);
	  }
}
