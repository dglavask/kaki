package alphabit.parser.bnf;

public interface ExpressionNodeVisitor {
	public void visit(TerminalNode node);
	public void visit(NonTerminalNode node);	  
	public void visit(ProductionNode node);
	public void visit(AndExpressionNode node);
	public void visit(OrNode node);
	public void visit(EosNode node);
	public void visit(LeftSideNode node);
}
