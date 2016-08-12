package alphabit.parser.bnf;

import alphabit.parser.bnf.grammer.GrammerNode;

public class OrNode implements GrammerNode{
	private String value;

	public OrNode(String value) {
	    this.value = value.replace("\"", "");
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return GrammerNode.TERMINAL_NODE;
	}
	
	public void accept(GrammerNodeVisitor visitor) {
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
