package alphabit.parser.bnf;

public class ProductionExpressionNode implements ExpressionNode{
	private String value;

	public ProductionExpressionNode(String value) {
	    this.value = value;
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return ExpressionNode.PRODUCTION_NODE;
	}
	
	public void accept(ExpressionNodeVisitor visitor) {
	    visitor.visit(this);
	  }
}
