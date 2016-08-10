package alphabit.parser.bnf;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

public class Compiler {
	Visitor visitor = new Visitor();
	GraphIterator<GrammerNode, RelationshipEdge> iterator;
    
	public Compiler(UndirectedGraph<GrammerNode,RelationshipEdge> graph){
		this.iterator = new DepthFirstIterator<GrammerNode, RelationshipEdge>(graph);
	}

	public void compile(){
		while (iterator.hasNext()) {
	        iterator.next().accept(visitor);
	    }
	}
}
