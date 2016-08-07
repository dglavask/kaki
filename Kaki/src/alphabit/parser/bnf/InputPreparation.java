package alphabit.parser.bnf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputPreparation {

	public InputPreparation(){
		
	}
	
	private static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        
	        
	        while (line != null) {
	        	if (!line.contains("//")) {
	        		sb.append(line);
	      	    }	
	            	            
	            line = br.readLine();
	        }
	        
	        return sb.toString().replaceAll("\\s+","");
	    } finally {
	        br.close();
	    }
	}
	
	public String getInput(String file){
		try {
			return readFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}

}
