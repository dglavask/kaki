package alphabit.parser.bnf;

public class LeftSideNode implements GrammerNode{
	private String identifier;	

	public LeftSideNode(String name) {
	    this.identifier = name;	    
	  }

	public int getType() {
		//only for the case of case non sensitive grammer
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
