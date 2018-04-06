import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;


public class Converter {
	
	private NegPosWords dico;
	private PrintWriter writer; 
	
	//public displayFile

	Converter(String csvFile,String out) throws IOException {
		dico = new NegPosWords();
		dico.init();
		writer = new PrintWriter(out, "UTF-8");
		run(csvFile,out);
	}
	
	public void run(String csvFile, String out)throws IOException {
		initFile(out);
		Processing(csvFile, out);
	}
	
	public void initFile(String outFile) throws IOException {
		//try{
			
		//BufferedWriter bw = new BufferedWriter(new FileWriter(out)); 
		
		
		writer.append("@RELATION " + "Opinion\n" );
		writer.append("@ATTRIBUTE " + "Context " + "string\n" );
		writer.append("@ATTRIBUTE " + "Label " + "NUMERIC\n" );
		writer.append("@DATA\n");
		writer.println();
		/*bw.write("@RELATION " + "Opinion\n" );
		bw.write("@ATTRIBUTE " + "Context " + "STRING\n" );
		bw.write("@ATTRIBUTE " + "Label " + "NUMERIC\n" );
		bw.write("@ATTRIBUTE " + "Label " + "NUMERIC\n" );
		bw.write("@DATA\n");*/
		//bw.close();
		//writer.close();
		
		//} catch (IOException e) {
		//	System.out.println(e.getMessage());
		//}
	}
	
	
	public void Processing(String csvFile,String out) throws IOException {
		
		try {
		
			BufferedReader br = new BufferedReader(new FileReader(csvFile));
			    
		    String line = br.readLine();
		    int i = 0;
		    while (line != null) {
		    	i++;
		    	
		    	line = line.replaceAll("'", " ");
		    	line = line.replaceAll("\"", "'");
		    	System.out.println(i);
		    	int weight = describeSentence(line);
		    	writer.println("\"" + line + "\"" + ", " + weight);
		        line = br.readLine();
		    }	
		    
		    br.close();
		    writer.close();
		} catch (IOException e){
			System.out.println(e.getMessage());

		}
	}
	
	public int describeSentence(String s){
		int pos = 0;
		int neg = 0;
		
		String[] sentenceByWord = s.split(" ");
		for(String mot : sentenceByWord ){
			if(dico.isPositive(mot)){
				pos++;
			}
			else if (dico.isNegative(mot)){
				neg--;
				}
			
		}
		//System.out.println("describe" + s + a );
		
		return neg + pos;
	}
	
	/*public void TreatementSentence(String s,int weight,String out)throws IOException {
		
		try{
			
		writer.close();
		//bw.write("'" + s + "'" + "," + weight + "\n");
		
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}*/
	
	public static void main (String[] args ){
		try {
			
			String inFile = "/home/linwillem/workspace/Covert/src/dataset.csv";
			String outFile = "/home/linwillem/workspace/Covert/src/datasetArff2.arff";
			Converter c = new Converter(inFile ,outFile);
			
			BufferedReader br = new BufferedReader(new FileReader(outFile));	    
		    String line = br.readLine();
		    while (line != null) {
		   
		    	//System.out.println(line);		  
		        line = br.readLine();
		    }	    
		    br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
		
}
	
	
	


