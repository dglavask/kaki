package alphabit.parser.bnf;

public class Visitor implements ExpressionNodeVisitor{

	SourceFile source;
	
	public Visitor(){
		source = new SourceFile("generated/genericParser.java");
	}
	
	public void visit(NonTerminalExpressionNode node){
		System.out.println("I found a non terminal " + node.getValue());
	}

	@Override
	public void visit(TerminalExpressionNode node) {
		System.out.println("I found a terminal " + node.getValue());
		
	}

	@Override
	public void visit(ProductionExpressionNode node) {
		System.out.println("I found a production " + node.getValue());
	}

	@Override
	public void visit(AndExpressionNode node) {
		System.out.println("I found an and " + node.getValue());
	}

	@Override
	public void visit(OrExpressionNode node) {
		System.out.println("I found an or " + node.getValue());
	}

	@Override
	public void visit(EosExpressionNode node) {
		System.out.println("I found an end of command " + node.getValue());
	}

	@Override
	public void visit(LeftNonTerminalExpressionNode node) {
		System.out.println("I found left production side " + node.getValue());
		source.write("private void " + node.getValue() + "(){\n}");
		
	}
}
