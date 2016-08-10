package alphabit.parser.bnf;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import alphabit.parser.bnf.Tokenizer.Token;

public class FiniteStateAutomaton {

	public interface StateGraphVisitor {
		public void visit(State node);

		public void visit(Transition node);
	}

	public class Visitor implements StateGraphVisitor {

		@Override
		public void visit(State node) {
			System.out.println("FSA: I am in state: " + node.getName());			
			
		}

		@Override
		public void visit(Transition node) {
			// TODO Auto-generated method stub
			System.out.println("FSA: Going over transition: " + node.getName());
			

		}

	}

	private DirectedGraph<State, Transition> stateGraph;
	private Visitor visitor;
	private GraphIterator<State, Transition> iterator;
	private static State currentState;

	public FiniteStateAutomaton(DirectedMultigraph<State, Transition> directedGraph) {
		stateGraph = directedGraph;
		iterator = new DepthFirstIterator<State, Transition>(stateGraph);
		visitor = new Visitor();
		currentState = iterator.next();
		currentState.accept(visitor);
	}

	public void step(Token current_token) throws ParseException {
		System.out.println("FSA: Analizing " + current_token.sequence + " token... TOKRNID=" + current_token.token);
		changeState(FiniteStateAutomaton.currentState, current_token.token);
	}

	private void changeState(State cState, int token) throws ParseException {
		Set<Transition> possibleTransitions = stateGraph.edgesOf(cState);
		State nextState = null;

		for (Iterator<Transition> it = possibleTransitions.iterator(); it.hasNext();) {
			Transition t = it.next();
			if (t.getType() == token) {
				nextState = stateGraph.getEdgeTarget(t);
				t.accept(visitor);
			}
		}

		if (nextState == null) {
			throw new ParseException("Unexpected symbol %s found", token);
		} else {
			FiniteStateAutomaton.currentState = nextState;
			FiniteStateAutomaton.currentState.accept(visitor);
		}
	}

	public State getCurrentState() {
		return currentState;
	}

	boolean inFinishState() {
		if (currentState.getName() == "finish") {
			return true;
		}
		return false;

	}

}
