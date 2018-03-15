import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;


public class WordProcessing {
	

	private String inputFile;
	
	/**
	 * 
	 */
	private ArrayList<ArrayList<String>> lines;
	
	/**
	 * 
	 */
	private ArrayList<ArrayList<TT_Position>> lines_pos;
	
	/**
	 * 
	 */
	private static TreeTaggerWrapper<String> tt;
	
	Set<String> positivewords;
	Set<String> negativewords;
	private int text_size;
	
	
	public WordProcessing(String filePath) {
		super();
		this.inputFile = filePath;
		loadTT();
		lines=new ArrayList<>();
		lines_pos=new ArrayList<>();
		loadFile();
	}


	/**
	 * 
	 */
	private void loadTT(){
		 tt = new TreeTaggerWrapper<String>();
		 System.setProperty("treetagger.home","lib/TreeTagger");
		 tt = new TreeTaggerWrapper<String>();
		 try {
		      tt.setModel("english-utf8.par");
		 } catch (IOException  e) {
				e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void loadFile(){
		try {
			FileUtils.readLines(new File(inputFile), Charsets.UTF_8).forEach(line -> {
				lines.add( new ArrayList<>(Arrays.asList(line.split(" "))));
				text_size+=line.length();
			});
			System.out.println("Data loaded with succes : "+lines.size() +" lines, size :"+text_size);
			//lines.forEach(line -> System.out.println(line));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    
	/**
	 * 
	 * @param outputFile
	 */
	public void processAnnotation(String outputFile){
			
		 for(int i=0;i<lines.size();i++){
			 process(lines.get(i));
		 }
		 
		
		 try {
			 FileWriter fileWriter = new FileWriter(outputFile);
			 for(int i=0;i<lines_pos.size();i++){
				 for(int j=0;j<lines_pos.get(i).size();j++){
					 fileWriter.write(lines_pos.get(i).get(j).getPos()+" ");
				 }
				 fileWriter.write("\n");
			 }
			// fileWriter.flush();
			 fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param outputFile
	 */
	public void processLemmatization(String outputFile){
		 try {
			 FileWriter fileWriter = new FileWriter(outputFile);
			 for(int i=0;i<lines_pos.size();i++){
				 for(int j=0;j<lines_pos.get(i).size();j++){
					 fileWriter.write(lines_pos.get(i).get(j).getLemma()+" ");
				 }
				 fileWriter.write("\n");
			 }
			// fileWriter.flush();
			 fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param line
	 */
	private void process(ArrayList<String> line){
		ArrayList<TT_Position> positions=new ArrayList<>();
		tt.setHandler((token, pos, lemma) -> {
			positions.add(new TT_Position(pos, lemma));	 
	    });
		try {
			 tt.process(line);
			 lines_pos.add(positions);
		}
		 catch (IOException | TreeTaggerException e) {
			e.printStackTrace();
		}
	}
	
	public void buildWeight(String positivewordsfile,String negativewordsfile){
		
		/*="positive-words.txt";
		String ="negative-words.txt";*/
		
		try {
			positivewords = FileUtils.readLines(new File(positivewordsfile),Charsets.UTF_8)
				.stream().collect(Collectors.toSet());
			negativewords = FileUtils.readLines(new File(negativewordsfile),Charsets.UTF_8)
					.stream().collect(Collectors.toSet());
			
			int cpt=0;
			lines.forEach(line -> {
				int poids = 0;
				for(String word : line){
					if(positivewords.contains(word)) { poids++; }
					else if(negativewords.contains(word)) { poids--; }
				}
				String ispos = ""; 
				if(poids > 0) ispos = "Positif"; 
				if(poids < 0) ispos = "Negatif"; 
				if(poids == 0) ispos = "Neutre";
				//System.out.println(cpt+" : "+ispos+" ("+poids+")");
				//cpt++;
			});
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*positivefile = new BufferedReader(new FileReader(positivewordsfile));
		negativefile = new BufferedReader(new FileReader(negativewordsfile));
		String ligneLue = positivefile.readLine();
		int tab=0;
		while(ligneLue!=null){
			//System.out.println(ligneLue);
			ligneLue = positivefile.readLine();
			positivewords.put(tab,ligneLue);
			tab++;
		}
		ligneLue = negativefile.readLine();
		tab=0;
		while(ligneLue!=null){
			//System.out.println(ligneLue);
			ligneLue = negativefile.readLine();
			negativewords.put(tab,ligneLue);
			tab++;
		}*/
	}
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<lines_pos.size();i++){
			for(int j=0;j<lines.get(i).size();j++){
				sb.append(lines.get(i).get(j) + " "+lines_pos.get(i).get(j).toString()+"\n");
			}
		}
		return sb.toString();
	}
	
	
}

