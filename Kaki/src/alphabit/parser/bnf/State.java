package alphabit.parser.bnf;

import alphabit.parser.bnf.FiniteStateAutomaton.StateGraphVisitor;

public class State {

	private String name;
	
	public State(String name){
		this.name = name; 
	}
	
	public String getName(){
		return name;
	}
	
	public void accept(StateGraphVisitor visitor){
		visitor.visit(this);
	}
}
