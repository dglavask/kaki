package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public class EosNode implements GrammerNode{
	private String value;

	public EosNode(String value) {
	    this.value = value.replace("\"", "");
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return GrammerNode.EOS;
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
