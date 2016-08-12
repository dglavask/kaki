package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public class AssigmentNode implements GrammerNode{
	private String identifier;	

	public AssigmentNode(String name) {
	    this.identifier = name;	    
	  }

	public int getType() {
		//only for the case of case non sensitive grammer
		return GrammerNode.GENERIC;
	}	

	public String getValue() {
		// removing the <> from the non terminal
		return null;
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
