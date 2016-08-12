package alphabit.parser.bnf.grammer;

import alphabit.parser.bnf.GrammerNodeVisitor;

public interface GrammerNode{
	public static final int EPSILON_NODE = 0;
	public static final int NONTERMINAL_NODE = 1;
	public static final int TERMINAL_NODE = 2;
	public static final int PRODUCTION_NODE = 3;
	public static final int OR_NODE = 4;
	public static final int AND_NODE = 5;
	public static final int EOS = 6;
	public static final int GENERIC = 100;
	
	public int getType();	
	public String getValue();
	public void setType(int type);	
	public void setValue(String value);
	public void accept(GrammerNodeVisitor visitor);	
}
