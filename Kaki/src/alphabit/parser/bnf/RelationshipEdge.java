package alphabit.parser.bnf;

import org.jgrapht.graph.DefaultEdge;

public class RelationshipEdge extends DefaultEdge {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExpressionNode v1;
    private ExpressionNode v2;
    private String label;

    public RelationshipEdge(ExpressionNode v1, ExpressionNode v2, String label) {
        this.v1 = v1;
        this.v2 = v2;
        this.label = label;
    }

    public ExpressionNode getV1() {
        return v1;
    }

    public ExpressionNode getV2() {
        return v2;
    }

    public String toString() {
        return label;
    }
}