package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public class ProductionNode implements GrammerNode{
	private String value;

	public ProductionNode(String value) {
	    this.value = value;
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return GrammerNode.PRODUCTION_NODE;
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
		this.value = value;
		// TODO Auto-generated method stub
		
	}
}
