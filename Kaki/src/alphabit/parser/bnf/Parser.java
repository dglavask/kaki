package alphabit.parser.bnf;

import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

import java.text.ParseException;
import java.util.LinkedList;

import alphabit.parser.bnf.Tokenizer.Token;
import alphabit.parser.bnf.grammer.GrammerNode;
import alphabit.parser.bnf.grammer.RelationshipEdge;

public class Parser {
	LinkedList<Token> tokens;
	Token lookahead;
	FiniteStateAutomaton fsa;
	//UndirectedGraph<GrammerNode, RelationshipEdge> graph;

	//SyntaxTreeBuilder treeBuilder;

	/**
	 * Takes a list of tokens as parameters
	 * 
	 * @param tokens
	 * @throws ParseException
	 */
	public UndirectedGraph<GrammerNode, RelationshipEdge> parse(List<Token> tokens) throws ParseException {

		DirectedMultigraph<State, Transition> stateGraph = new DirectedMultigraph<State, Transition>(Transition.class);
		//graph = new SimpleGraph<GrammerNode, RelationshipEdge>(new ClassBasedEdgeFactory<GrammerNode, RelationshipEdge>(RelationshipEdge.class));
		State start = new State("start");// must have
		State expectingProduction = new State("expectingProduction");
		State expectingTerminalOrNonTerminal = new State("expectingTerminalOrNonTerminal");
		State expectingAndOrEos = new State("expectingAndOrEos");
		State expectingEof = new State("expectingEof");
		State finish = new State("finish");// must have
		Transition leftSideTerminal = new Transition("recievedLeftSideTerminalOnStart", Tokenizer.Token.NONTERMINAL);
		Transition arrow = new Transition("recievedProduction", Tokenizer.Token.PRODUCTION);
		Transition terminal = new Transition("recievedTerminal", Tokenizer.Token.TERMINAL);
		Transition nonTerminal = new Transition("recievedNonTerminal", Tokenizer.Token.NONTERMINAL);
		Transition and = new Transition("recievedAnd", Tokenizer.Token.AND);
		Transition or = new Transition("recievedOr", Tokenizer.Token.OR);
		Transition eos = new Transition("recievedEOS", Tokenizer.Token.ENDOFSTATEMENT);
		Transition lst = new Transition("recievedLeftSideTerminal", Tokenizer.Token.NONTERMINAL);
		Transition eof = new Transition("recievedEOF", Tokenizer.Token.EPSILON);
		stateGraph.addVertex(start);
		stateGraph.addVertex(expectingProduction);
		stateGraph.addVertex(expectingTerminalOrNonTerminal);
		stateGraph.addVertex(expectingAndOrEos);
		stateGraph.addVertex(expectingEof);
		stateGraph.addVertex(finish);
		stateGraph.addEdge(start, expectingProduction, leftSideTerminal);
		stateGraph.addEdge(expectingProduction, expectingTerminalOrNonTerminal, arrow);

		stateGraph.addEdge(expectingTerminalOrNonTerminal, expectingAndOrEos, terminal);
		stateGraph.addEdge(expectingTerminalOrNonTerminal, expectingAndOrEos, nonTerminal);

		stateGraph.addEdge(expectingAndOrEos, expectingTerminalOrNonTerminal, and);
		stateGraph.addEdge(expectingAndOrEos, expectingTerminalOrNonTerminal, or);
		stateGraph.addEdge(expectingAndOrEos, expectingEof, eos);
		stateGraph.addEdge(expectingEof, finish, eof);
		stateGraph.addEdge(expectingEof, expectingProduction, lst);
		fsa = new FiniteStateAutomaton(stateGraph);

		this.tokens = new LinkedList<Token>(tokens);
		lookahead = this.tokens.getFirst();

		//treeBuilder = new SyntaxTreeBuilder();
		// start();

		do {
			fsa.step(lookahead);
			if (lookahead.token == Token.EPSILON) {
				System.out.println("Parsing finished successfully!");
				break;
			}
			nextToken();
		} while (!fsa.inFinishState());

		return fsa.getSyntaxTree();
	}

	private void nextToken() {
		tokens.pop();
		// at the end of input we return an epsilon token
		if (tokens.isEmpty())
			lookahead = new Token(Token.EPSILON, "");
		else
			lookahead = tokens.getFirst();
	}	
}