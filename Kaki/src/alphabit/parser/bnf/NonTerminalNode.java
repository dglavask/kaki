package alphabit.parser.bnf;

public class NonTerminalNode implements GrammerNode{
	private String identifier;	

	public NonTerminalNode(String name) {
	    this.identifier = name;	    
	  }

	public int getType() {
		return GrammerNode.NONTERMINAL_NODE;
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
