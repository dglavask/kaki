package alphabit.parser.bnf;

import org.jgraph.graph.DefaultEdge;

import alphabit.parser.bnf.FiniteStateAutomaton.StateGraphVisitor;

public class Transition extends DefaultEdge{

	private String name;
	private int type; 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Transition(String name, int type){
		this.name = name;
		this.type =type;
	}
	
	public String getName(){
		return name;
	}
	
	
	
	public void accept(StateGraphVisitor visitor){
		visitor.visit(this);
	}

	public int getType() {
		return type;
	}
	
}