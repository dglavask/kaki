package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public class AndNode implements GrammerNode{
	private String value;

	public AndNode(String value) {
	    this.value = value.replace("\"", "");
	  }

	public String getValue() {
		return value;
	}

	public int getType() {
		return GrammerNode.AND_NODE;
	}
	
	public void accept(GrammerNodeVisitor visitor) {
	    visitor.visit(this);
	  }
}
