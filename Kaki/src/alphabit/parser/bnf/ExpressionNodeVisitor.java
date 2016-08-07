package alphabit.parser.bnf;

public interface ExpressionNodeVisitor {
	public void visit(TerminalExpressionNode node);
	public void visit(NonTerminalExpressionNode node);	  
	public void visit(ProductionExpressionNode node);
	public void visit(AndExpressionNode node);
	public void visit(OrExpressionNode node);
	public void visit(EosExpressionNode node);
	public void visit(LeftNonTerminalExpressionNode node);
}
