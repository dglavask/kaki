package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

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

	public void accept(GrammerNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}
}
