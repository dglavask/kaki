package alphabit.parser.bnf;

public class NonTerminalExpressionNode implements ExpressionNode{
	private String identifier;	

	public NonTerminalExpressionNode(String name) {
	    this.identifier = name;	    
	  }

	public int getType() {
		return ExpressionNode.NONTERMINAL_NODE;
	}	

	public String getValue() {
		// removing the <> from the non terminal
		return identifier.substring(1, identifier.length() - 1);
	}

	public void accept(ExpressionNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}
