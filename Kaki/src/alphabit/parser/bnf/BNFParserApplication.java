package alphabit.parser.bnf;

import java.text.ParseException;
import java.util.Map;
import javax.swing.JFrame;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.hierarchical.JGraphHierarchicalLayout;

public class BNFParserApplication {

	public static void showGraph(UndirectedGraph<String,DefaultEdge> graph ){
		JFrame frame = new JFrame();
		frame.setSize(400, 400);
		JGraph jgraph = new JGraph(new JGraphModelAdapter<String, DefaultEdge>(graph));
		final  JGraphHierarchicalLayout hir = new JGraphHierarchicalLayout();
		final JGraphFacade graphFacade = new JGraphFacade(jgraph);      
		hir.run(graphFacade);
		final Map<?, ?> nestedMap = graphFacade.createNestedMap(true, true);
		jgraph.getGraphLayoutCache().edit(nestedMap);
		
		
		frame.getContentPane().add(jgraph);
		frame.setVisible(true);
	}
	
	public static String getTokenName(int sequence) {
		switch (sequence) {
		case 1:
			return "NONTERMINAL";
		case 2:
			return "TERMINAL";
		case 3:
			return "PRODUCTION";
		case 4:
			return "OR";
		case 5:
			return "AND";
		case 6:
			return "ENDOFSTATEMENT";

		}
		return "undefined";
	}

	public static void main(String[] args) {
		System.out.println("BNF Parser Alpha");

		Parser parser = new Parser();
		InputPreparation prep = new InputPreparation();
		String inputFile = "res/example.bnf";
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.add("\\<[a-zA-Z][a-zA-Z0-9]*\\>", 1); // non-terminal
		tokenizer.add("\\\"[^\\\"]*\\\"", 2); // terminal
		tokenizer.add("\\=\\>", 3); // production
		tokenizer.add("\\|", 4); // or
		tokenizer.add("\\&", 5); // and
		tokenizer.add("\\;", 6); // end of statement

		System.out.println("Parsing file: " + inputFile + " ...");
		UndirectedGraph<ExpressionNode, RelationshipEdge> graph = null;
		
		try {
			System.out.println(prep.getInput(inputFile));
			tokenizer.tokenize(prep.getInput(inputFile));

			for (Tokenizer.Token tok : tokenizer.getTokens()) {
				System.out.println("" + getTokenName(tok.token) + " " + tok.sequence);
			}
			System.out.println("Parser starting ...");

			//long startTime = System.nanoTime();
			graph = parser.parse(tokenizer.getTokens());
			System.out.println("Parsing finished successfully!");
			//long endTime = System.nanoTime();
			//System.out.println("Parsing time = " + (endTime - startTime) / 1000000 + "msec");
			System.out.println("Compiling starting ...");
			Compiler compiler = new Compiler(graph);
			compiler.compile();
			System.out.println("Finish compiling ...");

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//showGraph(sgraph);
		

	}

}
