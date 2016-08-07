package alphabit.parser.bnf;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class Compiler {
	Visitor visitor = new Visitor();
	GraphIterator<ExpressionNode, RelationshipEdge> iterator;
    
	public Compiler(UndirectedGraph<ExpressionNode,RelationshipEdge> graph){
		this.iterator = new DepthFirstIterator<ExpressionNode, RelationshipEdge>(graph);
	}

	public void compile(){
		while (iterator.hasNext()) {
	        iterator.next().accept(visitor);
	    }
	}
}
