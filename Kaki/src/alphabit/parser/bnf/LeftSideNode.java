package alphabit.parser.bnf;

import alphabit.parser.bnf.grammer.GrammerNode;

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

	public void accept(GrammerNodeVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}
}
