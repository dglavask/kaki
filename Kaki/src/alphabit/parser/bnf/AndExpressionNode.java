package alphabit.parser.bnf;

public class AndExpressionNode implements GrammerNode{
	private String value;

	public AndExpressionNode(String value) {
	    this.value = value.replace("\"", "");
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return GrammerNode.TERMINAL_NODE;
	}
	
	public void accept(ExpressionNodeVisitor visitor) {
	    visitor.visit(this);
	  }
}
