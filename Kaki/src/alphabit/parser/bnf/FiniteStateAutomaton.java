package alphabit.parser.bnf;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import alphabit.parser.bnf.Tokenizer.Token;
import alphabit.parser.bnf.grammer.StatementNode;
import alphabit.parser.bnf.grammer.TerminalNode;
import alphabit.parser.bnf.grammer.AndNode;
import alphabit.parser.bnf.grammer.EofNode;
import alphabit.parser.bnf.grammer.EosNode;
import alphabit.parser.bnf.grammer.GrammerNode;
import alphabit.parser.bnf.grammer.GrammerRootNode;
import alphabit.parser.bnf.grammer.LeftSideNode;
import alphabit.parser.bnf.grammer.NonTerminalNode;
import alphabit.parser.bnf.grammer.ProductionNode;
import alphabit.parser.bnf.grammer.RelationshipEdge;
import alphabit.parser.bnf.grammer.RightSideNode;
import alphabit.parser.bnf.grammer.RuleNode;

public class FiniteStateAutomaton {

	private static UndirectedGraph<GrammerNode, RelationshipEdge> syntaxTree;
	private static int i;

	private static String getID() {
		return Integer.toString(i++);
	}

	public interface StateGraphVisitor {
		public void visit(State node);

		public void visit(Transition node);
	}

	public static class Visitor implements StateGraphVisitor {

		private static RuleNode currentRule = null;
		private static GrammerNode currentAndOr = null;
		private static GrammerRootNode rootNode = null;
		private static RightSideNode currentRightSide = null;
		private static LeftSideNode currentLeftSide = null;
		private static ProductionNode currentProductionNode = null;
		private static GrammerNode currentTerminalOrNonTerminal = null;

		@Override
		public void visit(State node) {
			System.out.println("FSA: I am in state: " + node.getName());

		}

		@Override
		public void visit(Transition node) {
			System.out.println("FSA: Going over transition: " + node.getName());

			switch (node.getName()) {
			case "recievedLeftSideTerminalOnStart":

				GrammerRootNode grn = new GrammerRootNode("File");
				rootNode = grn;
				syntaxTree.addVertex(grn);

				RuleNode rn = new RuleNode("Rule");
				syntaxTree.addVertex(rn);
				syntaxTree.addEdge(rootNode, rn, new RelationshipEdge(rootNode, rn, getID()));

				currentRule = rn;

				ProductionNode pn = new ProductionNode("Production");

				currentProductionNode = pn;

				syntaxTree.addVertex(pn);
				syntaxTree.addEdge(rn, pn, new RelationshipEdge(rn, pn, getID()));

				LeftSideNode lsn = new LeftSideNode(node.getName());
				syntaxTree.addVertex(lsn);

				currentLeftSide = lsn;

				syntaxTree.addEdge(pn, lsn, new RelationshipEdge(pn, lsn, getID()));

				RightSideNode rsn = new RightSideNode(node.getName());
				syntaxTree.addVertex(rsn);
				currentRightSide = rsn;
				syntaxTree.addEdge(pn, rsn, new RelationshipEdge(pn, rsn, getID()));

				NonTerminalNode ntn = new NonTerminalNode(node.getName());
				syntaxTree.addVertex(ntn);

				syntaxTree.addEdge(lsn, ntn, new RelationshipEdge(lsn, ntn, getID()));

				break;
			case "recievedProduction":
				currentProductionNode.setValue(node.getName());
				break;
			case "recievedNonTerminal":
				NonTerminalNode ntn1 = new NonTerminalNode(node.getName());
				currentTerminalOrNonTerminal = ntn1;
				syntaxTree.addVertex(ntn1);
				break;
			case "recievedTerminal":
				TerminalNode tn = new TerminalNode(node.getName());
				currentTerminalOrNonTerminal = tn;
				syntaxTree.addVertex(tn);
				break;
			case "recievedAnd":
				AndNode an = new AndNode(node.getName());
				syntaxTree.addVertex(an);
				if (currentAndOr == null) {
					currentAndOr = an;
					syntaxTree.addEdge(currentRightSide, an, new RelationshipEdge(currentRightSide, an, getID()));
					syntaxTree.addEdge(currentAndOr, currentTerminalOrNonTerminal,
							new RelationshipEdge(currentAndOr, currentTerminalOrNonTerminal, getID()));

				} else {
					syntaxTree.addEdge(currentAndOr, an, new RelationshipEdge(currentAndOr, an, getID()));
					currentAndOr = an;
					syntaxTree.addEdge(currentAndOr, currentTerminalOrNonTerminal,
							new RelationshipEdge(currentAndOr, currentTerminalOrNonTerminal, getID()));

				}
				currentTerminalOrNonTerminal = null;
				break;
			case "recievedOr":
				OrNode on = new OrNode(node.getName());
				syntaxTree.addVertex(on);
				if (currentAndOr == null) {
					currentAndOr = on;
					syntaxTree.addEdge(currentRightSide, on, new RelationshipEdge(currentRightSide, on, getID()));
					syntaxTree.addEdge(currentAndOr, currentTerminalOrNonTerminal,
							new RelationshipEdge(currentAndOr, currentTerminalOrNonTerminal, getID()));

				} else {
					syntaxTree.addEdge(currentAndOr, on, new RelationshipEdge(currentAndOr, on, getID()));
					currentAndOr = on;
					syntaxTree.addEdge(currentAndOr, currentTerminalOrNonTerminal,
							new RelationshipEdge(currentAndOr, currentTerminalOrNonTerminal, getID()));

				}
				currentTerminalOrNonTerminal = null;
				break;
			case "recievedEOS":
				EosNode eosn = new EosNode(node.getName());
				syntaxTree.addVertex(eosn);
				syntaxTree.addEdge(currentRule, eosn, new RelationshipEdge(currentRule, eosn, getID()));
				if (currentAndOr == null) {
					syntaxTree.addEdge(currentRightSide, currentTerminalOrNonTerminal,
							new RelationshipEdge(currentRightSide, currentTerminalOrNonTerminal, getID()));
				} else {
					syntaxTree.addEdge(currentAndOr, currentTerminalOrNonTerminal,
							new RelationshipEdge(currentAndOr, currentTerminalOrNonTerminal, getID()));
				}
				currentRule = null;
				currentAndOr = null;
				currentRightSide = null;
				currentLeftSide = null;
				currentProductionNode = null;
				currentTerminalOrNonTerminal = null;

				break;
			case "recievedLeftSideTerminal":
				RuleNode rn_next = new RuleNode("Rule");
				syntaxTree.addVertex(rn_next);
				syntaxTree.addEdge(rootNode, rn_next, new RelationshipEdge(rootNode, rn_next, getID()));
				currentRule = rn_next;
				ProductionNode pn_next = new ProductionNode("Production");
				currentProductionNode = pn_next;
				syntaxTree.addVertex(pn_next);
				syntaxTree.addEdge(rn_next, pn_next, new RelationshipEdge(rn_next, pn_next, getID()));
				LeftSideNode lsn_next = new LeftSideNode(node.getName());
				syntaxTree.addVertex(lsn_next);
				currentLeftSide = lsn_next;
				syntaxTree.addEdge(pn_next, lsn_next, new RelationshipEdge(pn_next, lsn_next, getID()));
				RightSideNode rsn_next = new RightSideNode(node.getName());
				syntaxTree.addVertex(rsn_next);
				currentRightSide = rsn_next;
				syntaxTree.addEdge(pn_next, rsn_next, new RelationshipEdge(pn_next, rsn_next, getID()));
				NonTerminalNode ntn_next = new NonTerminalNode(node.getName());
				syntaxTree.addVertex(ntn_next);
				syntaxTree.addEdge(lsn_next, ntn_next, new RelationshipEdge(lsn_next, ntn_next, getID()));
				break;
			case "recievedEOF":
				EofNode eofn = new EofNode(node.getName());
				syntaxTree.addVertex(eofn);
				syntaxTree.addEdge(rootNode, eofn, new RelationshipEdge(rootNode, eofn, getID()));
				break;
			}

		}

	}

	private DirectedGraph<State, Transition> stateGraph;
	private Visitor visitor;
	private GraphIterator<State, Transition> iterator;
	private static State currentState;

	public FiniteStateAutomaton(DirectedMultigraph<State, Transition> directedGraph) {
		i = 0;
		syntaxTree = new SimpleGraph<GrammerNode, RelationshipEdge>(
				new ClassBasedEdgeFactory<GrammerNode, RelationshipEdge>(RelationshipEdge.class));
		stateGraph = directedGraph;
		iterator = new DepthFirstIterator<State, Transition>(stateGraph);
		visitor = new Visitor();
		currentState = iterator.next();
		currentState.accept(visitor);
	}

	public void step(Token current_token) throws ParseException {
		System.out.println("FSA: Analizing " + current_token.sequence + " token... TOKEN_ID=" + current_token.token);
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

	public UndirectedGraph<GrammerNode, RelationshipEdge> getSyntaxTree() {
		return syntaxTree;
	}

}
