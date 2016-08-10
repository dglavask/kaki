package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public class TerminalNode implements GrammerNode{
	private String value;

	public TerminalNode(String value) {
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
}
