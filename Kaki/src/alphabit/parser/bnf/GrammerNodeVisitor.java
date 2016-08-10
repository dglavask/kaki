package alphabit.parser.bnf;

import alphabit.parser.bnf.grammer.AndNode;
import alphabit.parser.bnf.grammer.AssigmentNode;
import alphabit.parser.bnf.grammer.EofNode;
import alphabit.parser.bnf.grammer.EosNode;
import alphabit.parser.bnf.grammer.GrammerRootNode;
import alphabit.parser.bnf.grammer.NonTerminalNode;
import alphabit.parser.bnf.grammer.ProductionNode;
import alphabit.parser.bnf.grammer.RightSideNode;
import alphabit.parser.bnf.grammer.RuleNode;
import alphabit.parser.bnf.grammer.RuleSetNode;
import alphabit.parser.bnf.grammer.TerminalNode;

public interface GrammerNodeVisitor {
	public void visit(TerminalNode node);
	public void visit(NonTerminalNode node);	  
	public void visit(ProductionNode node);
	public void visit(AndNode node);
	public void visit(OrNode node);
	public void visit(EosNode node);
	public void visit(LeftSideNode node);
	public void visit(RightSideNode rightSideNode);
	public void visit(AssigmentNode assigmentNode);
	public void visit(RuleNode ruleNode);
	public void visit(RuleSetNode ruleSetNode);
	public void visit(EofNode eofNode);
	public void visit(GrammerRootNode grammerRootNode);
}
