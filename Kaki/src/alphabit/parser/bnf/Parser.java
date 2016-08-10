package alphabit.parser.bnf;

import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.SimpleGraph;

import java.text.ParseException;
import java.util.LinkedList;

import alphabit.parser.bnf.Tokenizer.Token;
import alphabit.parser.bnf.grammer.GrammerNode;
import alphabit.parser.bnf.grammer.RelationshipEdge;

public class Parser {
	LinkedList<Token> tokens;
	Token lookahead;
	FiniteStateAutomaton fsa;
	UndirectedGraph<GrammerNode, RelationshipEdge> graph;

	//SyntaxTreeBuilder treeBuilder;

	/**
	 * Takes a list of tokens as parameters
	 * 
	 * @param tokens
	 * @throws ParseException
	 */
	public SimpleGraph<GrammerNode, DefaultEdge> parse(List<Token> tokens) throws ParseException {

		DirectedMultigraph<State, Transition> stateGraph = new DirectedMultigraph<State, Transition>(Transition.class);
		graph = new SimpleGraph<GrammerNode, RelationshipEdge>(new ClassBasedEdgeFactory<GrammerNode, RelationshipEdge>(RelationshipEdge.class));
		State start = new State("start");// must have
		State expectingProduction = new State("expectingProduction");
		State expectingTerminalOrNonTerminal = new State("expectingTerminalOrNonTerminal");
		State expectingAndOrEos = new State("expectingAndOrEos");
		State expectingEof = new State("expectingEof");
		State finish = new State("finish");// must have
		Transition leftSideTerminal = new Transition("<leftSideTerminalOnStart>", Tokenizer.Token.NONTERMINAL);
		Transition arrow = new Transition("=>", Tokenizer.Token.PRODUCTION);
		Transition terminal = new Transition("\"terminal\"", Tokenizer.Token.TERMINAL);
		Transition nonTerminal = new Transition("<nonTerminal>", Tokenizer.Token.NONTERMINAL);
		Transition and = new Transition("&", Tokenizer.Token.AND);
		Transition or = new Transition("|", Tokenizer.Token.OR);
		Transition eos = new Transition(";", Tokenizer.Token.ENDOFSTATEMENT);
		Transition lst = new Transition("<leftSideTerminal>", Tokenizer.Token.NONTERMINAL);
		Transition eof = new Transition("EOF", Tokenizer.Token.EPSILON);
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

		return null;
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
/*
package alphabit.parser.bnf;

import java.util.List;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleGraph;
import java.text.ParseException;
import java.util.LinkedList;

import alphabit.parser.bnf.Tokenizer.Token;

public class Parser {
	
	LinkedList<Token> tokens;
	Token lookahead;
	UndirectedGraph<ExpressionNode, RelationshipEdge> graph;
	
	public UndirectedGraph<ExpressionNode, RelationshipEdge> parse(List<Token> tokens) throws ParseException {
		this.tokens = new LinkedList<Token>(tokens);
		lookahead = this.tokens.getFirst();
		graph = new SimpleGraph<ExpressionNode, RelationshipEdge>(new ClassBasedEdgeFactory<ExpressionNode, RelationshipEdge>(RelationshipEdge.class));
		start();

		if (lookahead.token != Token.EPSILON)
			throw new ParseException("Unexpected symbol %s found", lookahead.token);


		return graph;
	}

	private void nextToken() {
		tokens.pop();
		// at the end of input we return an epsilon token
		if (tokens.isEmpty())
			lookahead = new Token(Token.EPSILON, "");
		else
			lookahead = tokens.getFirst();
	}
	
	private ExpressionNode start() throws ParseException {
		if (lookahead.token == Token.NONTERMINAL) {
			System.out.print("NONTERMINAL ");
			ExpressionNode startNode = (ExpressionNode) new LeftNonTerminalExpressionNode(lookahead.sequence);
			nextToken();
			ExpressionNode productionNode = expectingProductionSymbol();
			graph.addVertex(startNode);		
			graph.addEdge(productionNode, startNode, new RelationshipEdge(productionNode,startNode,""));
			return productionNode;
		} else {
			throw new ParseException("Unexpected symbol %s found", lookahead.token);
		}
	}

	private ExpressionNode expectingProductionSymbol() throws ParseException {
		if (lookahead.token == Token.PRODUCTION) {
			System.out.print("PRODUCTION ");
			ExpressionNode productionNode = (ExpressionNode) new ProductionExpressionNode(lookahead.sequence);
			graph.addVertex(productionNode);
			nextToken();
			ExpressionNode nTNode = expectingTermOrNTerm();
			graph.addEdge(productionNode, nTNode,new RelationshipEdge(productionNode,nTNode,""));
			return productionNode;
		} else {
			throw new ParseException("Unexpected symbol %s found", lookahead.token);
		}
	}

	private ExpressionNode expectingTermOrNTerm() throws ParseException {
		if (lookahead.token == Token.NONTERMINAL) {
			System.out.print("NONTERMINAL ");
			ExpressionNode nTerm = (ExpressionNode) new NonTerminalExpressionNode(lookahead.sequence);
			graph.addVertex(nTerm);
			nextToken();
			ExpressionNode specSym=expectingSpecialSymbols();
			graph.addEdge(specSym, nTerm,new RelationshipEdge(specSym,nTerm,""));
			return specSym;
		} else if (lookahead.token == Token.TERMINAL) {
			System.out.print("TERMINAL ");
			ExpressionNode Term = (ExpressionNode) new TerminalExpressionNode(lookahead.sequence);
			graph.addVertex(Term);
			nextToken();
			ExpressionNode specSym=expectingSpecialSymbols();
			graph.addEdge(specSym, Term,new RelationshipEdge(specSym,Term,""));
			return specSym;
		} else {
			throw new ParseException("Unexpected symbol %s found", lookahead.token);
		}

	}

	private ExpressionNode expectingSpecialSymbols() throws ParseException {
		if (lookahead.token == Token.AND) {
			System.out.print("AND ");
			ExpressionNode And = (ExpressionNode) new AndExpressionNode(lookahead.sequence);
			graph.addVertex(And);
			nextToken();
			ExpressionNode tOrnT=expectingTermOrNTerm();
			graph.addEdge(And, tOrnT,new RelationshipEdge(And,tOrnT,""));
			return And;
		} else if (lookahead.token == Token.OR) {
			System.out.print("OR ");
			ExpressionNode Or = (ExpressionNode) new OrExpressionNode(lookahead.sequence);
			graph.addVertex(Or);
			nextToken();
			ExpressionNode tOrnT=expectingTermOrNTerm();
			graph.addEdge(Or, tOrnT,new RelationshipEdge(Or,tOrnT,""));
			return Or;
		} 
		else if (lookahead.token == Token.ENDOFSTATEMENT) {
			return expectingNTermOrFinish();
		} 
		else {
			throw new ParseException("Unexpected symbol %s found", lookahead.token);
		}
	}

	private ExpressionNode expectingNTermOrFinish() throws ParseException { 
		if (lookahead.token == Token.NONTERMINAL) {
			return start();
		}
		else if (lookahead.token == Token.ENDOFSTATEMENT) {
			System.out.print("EOS\n");
			ExpressionNode Eos = (ExpressionNode) new EosExpressionNode(lookahead.sequence);
			graph.addVertex(Eos);
			nextToken();
			ExpressionNode tOrnT=expectingNTermOrFinish();
			graph.addEdge(Eos, tOrnT, new RelationshipEdge(Eos,tOrnT,""));
			return Eos;
		} 
		else if (lookahead.token == Token.EPSILON) {
			ExpressionNode Eos = (ExpressionNode) new EosExpressionNode(lookahead.sequence);
			graph.addVertex(Eos);
			return Eos;
		} 
		else {
			throw new ParseException("Unexpected symbol %s found", lookahead.token);
		}
	}

}
*/
