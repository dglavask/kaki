package alphabit.parser.bnf;

public interface ExpressionNode{
	public static final int NONTERMINAL_NODE = 1;
	public static final int TERMINAL_NODE = 2;
	public static final int PRODUCTION_NODE = 3;
	public static final int OR_NODE = 4;
	public static final int AND_NODE = 5;
	public static final int EOS = 6;
	
	public int getType();	
	public String getValue();
	public void accept(ExpressionNodeVisitor visitor);	
}
