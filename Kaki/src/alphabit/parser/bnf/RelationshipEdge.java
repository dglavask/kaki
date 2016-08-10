package alphabit.parser.bnf;

import org.jgrapht.graph.DefaultEdge;

public class RelationshipEdge extends DefaultEdge {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GrammerNode v1;
    private GrammerNode v2;
    private String label;

    public RelationshipEdge(GrammerNode v1, GrammerNode v2, String label) {
        this.v1 = v1;
        this.v2 = v2;
        this.label = label;
    }

    public GrammerNode getV1() {
        return v1;
    }

    public GrammerNode getV2() {
        return v2;
    }

    public String toString() {
        return label;
    }
}