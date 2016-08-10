package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public class EofNode implements GrammerNode{
	private String identifier;	

	public EofNode(String name) {
	    this.identifier = name;	    
	  }

	public int getType() {
		//only for the case of case non sensitive grammer
		return GrammerNode.EPSILON_NODE;
	}	

	public String getValue() {
		// removing the <> from the non terminal
		return null;
	}

	public void accept(GrammerNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}
