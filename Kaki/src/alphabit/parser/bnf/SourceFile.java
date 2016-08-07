package alphabit.parser.bnf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class SourceFile {
	private File source;
	private PrintWriter writer;

	public SourceFile(String path) {
		this.source = new File(path);
		

		try {
			source.createNewFile();
			this.writer = new PrintWriter(source, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void write(String text){
		writer.print(text);
		writer.close();
	}
	
	

}
