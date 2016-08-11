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
	
	private static String getID(){
		return Integer.toString(i++);
	}

	public interface StateGraphVisitor {
		public void visit(State node);

		public void visit(Transition node);
	}

	public static class Visitor implements StateGraphVisitor {

		private static RuleNode currentRule = null;
		private static GrammerNode homeNode = null;
		private static GrammerNode nextNodeToBuild = null;
		private static GrammerRootNode rootNode = null;
		private static RightSideNode currentRightSide = null;
		
		@Override
		public void visit(State node) {
			System.out.println("FSA: I am in state: " + node.getName());

		}

		@Override
		public void visit(Transition node) {
			// TODO Auto-generated method stub
			System.out.println("FSA: Going over transition: " + node.getName());			

			switch (node.getName()) {
			case "recievedLeftSideTerminalOnStart":
				GrammerRootNode grn = new GrammerRootNode("File");
				rootNode = grn;
				syntaxTree.addVertex(grn);
				System.out.println("FSA BUILD: Creating node GrammerRootNode");
				//syntaxTree.addVertex(sn);				
				//System.out.println("FSA BUILD: Creating node Statement");
				RuleNode rn = new RuleNode("Rule");
				syntaxTree.addVertex(rn);
				System.out.println("FSA BUILD: Creating node Rule");
				//syntaxTree.addEdge(grn, sn, new RelationshipEdge(grn,sn,getID()));
				syntaxTree.addEdge(rootNode, rn,new RelationshipEdge(rootNode,rn,getID()));
				currentRule = rn;
				nextNodeToBuild = new LeftSideNode(node.getName());
				break;
			case "recievedProduction":
				System.out.println("FSA BUILD: Build nextNode -> left non terminal");
				ProductionNode pn = new ProductionNode(node.getName());
				syntaxTree.addVertex(pn);
				syntaxTree.addEdge(currentRule,pn,new RelationshipEdge(currentRule,pn,getID()));
				System.out.println("FSA BUILD: Creating node Production");
				syntaxTree.addVertex(nextNodeToBuild);
				System.out.println("FSA BUILD: Creating node LeftSideNode");
				syntaxTree.addEdge(pn, nextNodeToBuild,new RelationshipEdge(pn,nextNodeToBuild,getID()));
				homeNode = pn;
				break;
			case "recievedTerminal":
				if (currentRightSide == null){
					RightSideNode rsn = new RightSideNode("RightSideNode");
					currentRightSide = rsn;
					
					syntaxTree.addVertex(rsn);
					System.out.println("FSA BUILD: Creating node RightSideNode");
					syntaxTree.addEdge(homeNode, currentRightSide,new RelationshipEdge(homeNode,currentRightSide,getID()));
					homeNode =rsn;
				}
				
				System.out.println("FSA BUILD: Connect  homeNode with RightSideNode");
				TerminalNode tn = new TerminalNode(node.getName());
				nextNodeToBuild = tn;
				//System.out.println("FSA BUILD: Creating node TerminalNode");

				break;
			case "recievedNonTerminal":
				if (currentRightSide == null){
					RightSideNode rsn = new RightSideNode("RightSideNode");
					currentRightSide = rsn;
					
					syntaxTree.addVertex(rsn);
					System.out.println("FSA BUILD: Creating node RightSideNode");
					syntaxTree.addEdge(homeNode, currentRightSide,new RelationshipEdge(homeNode,currentRightSide,getID()));
					homeNode =rsn;
				}
				
				System.out.println("FSA BUILD: Connect  homeNode with RightSideNode");
				NonTerminalNode ntn = new NonTerminalNode(node.getName());
				nextNodeToBuild = ntn;
				//System.out.println("FSA BUILD: Creating node NonTerminalNode");
				// set home to right side node
				break;
			case "recievedAnd":
				AndNode an = new AndNode(node.getName());
				syntaxTree.addVertex(an);
				System.out.println("FSA BUILD: Creating node AndNode");
				syntaxTree.addEdge(homeNode, an,new RelationshipEdge(homeNode,an,getID()));
				System.out.println("FSA BUILD: Connect  homeNode with AndNode");
				syntaxTree.addVertex(nextNodeToBuild);
				syntaxTree.addEdge(an, nextNodeToBuild,new RelationshipEdge(an,nextNodeToBuild,getID()));
				homeNode=an;
				break;
			case "recievedOr":
				OrNode on = new OrNode(node.getName());
				syntaxTree.addVertex(on);
				System.out.println("FSA BUILD: Creating node OrNode");
				syntaxTree.addEdge(homeNode, on,new RelationshipEdge(homeNode,currentRightSide,getID()));
				System.out.println("FSA BUILD: Connect  homeNode with OrNode");
				syntaxTree.addVertex(nextNodeToBuild);
				syntaxTree.addEdge(on, nextNodeToBuild,new RelationshipEdge(on,nextNodeToBuild,getID()));
				homeNode=on;
				break;
			case "recievedEOS":
				syntaxTree.addVertex(nextNodeToBuild); // builds the terminal
				EosNode en = new EosNode(node.getName());
				syntaxTree.addVertex(en);
				System.out.println("FSA BUILD: Creating node EosNode");
				syntaxTree.addEdge(currentRule, en,new RelationshipEdge(currentRule,en,getID()));
				System.out.println("FSA BUILD: Connect  currentRule with EosNode");
				currentRule = null;
				break;
			case "recievedLeftSideTerminal":
				// get parent from current rule
				/*StatementNode parent = null;				
				Iterator<RelationshipEdge> iterator = syntaxTree.edgesOf(currentRule).iterator();
			    while(iterator.hasNext()) {
			    	RelationshipEdge con = iterator.next();
			        if(con.toString()=="parent") {
			        	parent=(StatementNode) syntaxTree.getEdgeSource(con);
			        }
			    }								
				
				StatementNode snNew = new StatementNode("Statement");
				syntaxTree.addVertex(snNew);
				System.out.println("FSA BUILD: Creating node Statement");
				syntaxTree.addEdge(parent, snNew,new RelationshipEdge(parent,snNew,getID()));
				*/
				RuleNode rnNew = new RuleNode("Rule");
				syntaxTree.addVertex(rnNew);
				syntaxTree.addEdge(rootNode, rnNew,new RelationshipEdge(rootNode,rnNew,getID()));
				System.out.println("FSA BUILD: Creating node Rule");
				// set currentRule to newly created rule
				currentRule = rnNew;
				break;
			case "recievedEOF":
				
				syntaxTree.addVertex(nextNodeToBuild);
				
				EofNode eofn = new EofNode(node.getName());
				syntaxTree.addVertex(eofn);
				System.out.println("FSA BUILD: Creating node EOFNode");
				syntaxTree.addEdge(rootNode, eofn,new RelationshipEdge(homeNode,currentRightSide,getID()));
				System.out.println("FSA BUILD: Connect  rootNode with EOFNode");				
				syntaxTree.addEdge(homeNode, nextNodeToBuild,new RelationshipEdge(homeNode,nextNodeToBuild,getID()));				
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
