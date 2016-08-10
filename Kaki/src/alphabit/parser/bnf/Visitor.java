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

public class Visitor implements GrammerNodeVisitor{

	SourceFile source;
	
	public Visitor(){
		source = new SourceFile("generated/genericParser.java");
	}
	
	public void visit(NonTerminalNode node){
		System.out.println("I found a non terminal " + node.getValue());
	}

	@Override
	public void visit(TerminalNode node) {
		System.out.println("I found a terminal " + node.getValue());
		
	}

	@Override
	public void visit(ProductionNode node) {
		System.out.println("I found a production " + node.getValue());
	}

	@Override
	public void visit(AndNode node) {
		System.out.println("I found an and " + node.getValue());
	}

	@Override
	public void visit(OrNode node) {
		System.out.println("I found an or " + node.getValue());
	}

	@Override
	public void visit(EosNode node) {
		System.out.println("I found an end of command " + node.getValue());
	}

	@Override
	public void visit(LeftSideNode node) {
		System.out.println("I found left production side " + node.getValue());
		source.write("private void " + node.getValue() + "(){\n}");
		
	}

	@Override
	public void visit(RightSideNode rightSideNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AssigmentNode assigmentNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RuleNode ruleNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RuleSetNode ruleSetNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(EofNode eofNode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(GrammerRootNode grammerRootNode) {
		// TODO Auto-generated method stub
		
	}

}
