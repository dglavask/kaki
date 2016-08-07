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
