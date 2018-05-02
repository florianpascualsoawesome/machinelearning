import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
/*import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;*/


public class WordProcessing {


	/*private String datasetFile;
    private String classFile;

	private String lemmaFile;
	private String posFile;
	private String weightFile;

	private String positivewordsfile;
	private  String negativewordsfile;


	private ArrayList<ArrayList<String>> lines;

	private ArrayList<Integer> lines_class;


	private ArrayList<ArrayList<TT_Position>> lines_pos;


	private static TreeTaggerWrapper<String> tt;

	private ArrayList<Integer> lines_weights;

	private Set<String> positivewords;
	private Set<String> negativewords;

	private int text_size;


	public WordProcessing(String datasetFile, String classFile, boolean readFromFile,String lemmaFile, String posFile, String weightFile,String positivewordsfile,String negativewordsfile) {
		super();
		lines=new ArrayList<>();
		lines_pos=new ArrayList<>();
		lines_weights=new ArrayList<Integer>();
		lines_class=new ArrayList<>();

        this.datasetFile=datasetFile;
        this.classFile=classFile;
		this.lemmaFile=lemmaFile;
		this.posFile=posFile;
		this.weightFile=weightFile;
		this.positivewordsfile=positivewordsfile;
		this.negativewordsfile=negativewordsfile;

		if(! readFromFile){
            loadTT();
            //clean();
            loadFile();
        }
        else{
		    loadFromFile();
        }

	}

	public void clean(){
		try{
			String text = FileUtils.readFileToString(new File(datasetFile),Charsets.UTF_8);
			String[] to_destroy_chars={"'","--","-"};
		    for(String str : to_destroy_chars){
		    	if(text.contains(str)){
					text=text.replaceAll(str,"");
				}
			}
			FileUtils.write(new File(datasetFile),text,Charsets.UTF_8);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private void loadFromFile(){
        try{
            BufferedReader lemmaFileReader=new BufferedReader(new FileReader(lemmaFile));
            BufferedReader weightFileReader=new BufferedReader(new FileReader(weightFile));
           // BufferedReader classFileReader=new BufferedReader(new FileReader(classFile));
            boolean read=true;
            while(read){
                String lemmaFileLine=lemmaFileReader.readLine();
                String weightFileLine=weightFileReader.readLine();
                //String classFileLine=classFileReader.readLine();
                read=(lemmaFileLine != null && weightFileLine != null
						//&& classFileLine != null
				);
                if(read){

                    lines_pos.add( new ArrayList<>());
                    String[] lemmas=lemmaFileLine.split(" ");
                    for(String lemma : lemmas){
                        lines_pos.get(lines_pos.size()-1).add(new TT_Position("",lemma));
                    }
                    text_size+=lemmaFileLine.length();

                    lines_weights.add(Integer.parseInt(weightFileLine));
                    //lines_class.add(Integer.parseInt(classFileLine));
                }
                //String weightFileReader=new
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


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

	public void processAnnotation(){

		for(int i=0;i<lines.size();i++){
			//process(lines.get(i));
		}

        try {
            FileWriter fileWriter = new FileWriter(posFile);
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


	public void processLemmatization(){
	    try{
            FileWriter fileWriter = new FileWriter(lemmaFile);
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

	public void destroy_stop_words(){
		String[] stop_words={
				"DT","VV","VB","VVD", "VVG","VVN","VVP", "VVZ","WRB",
				"CC", "RB","NP","IN", "PP","WP","TO","CC","NN","NNS"};
		HashSet<String> stop_words_set=new HashSet<>(Arrays.asList(stop_words));
		StringBuilder textefinal = new StringBuilder();
		for(int i=0;i<lines.size();i++){
			ArrayList<TT_Position> line_pos=lines_pos.get(i);
			for(int j=0;j<line_pos.size();j++){
				if(! stop_words_set.contains(line_pos.get(j).getPos())){
					textefinal.append(line_pos.get(j).getLemma());
				}
			}
		}
		try{
			FileUtils.write(new File(datasetFile),textefinal.toString(),Charsets.UTF_8);
		}
		catch (IOException e){
			e.printStackTrace();
		}

	}


	public void buildWeight(){

		="positive-words.txt";
		String ="negative-words.txt";

		try {
			positivewords = new HashSet<>(FileUtils.readLines(new File(positivewordsfile),Charsets.UTF_8));
			negativewords = new HashSet<>(FileUtils.readLines(new File(negativewordsfile),Charsets.UTF_8));

			int cpt=0;
			lines.forEach(line -> {
				int poids = 0;
				for(String word : line){
					if(positivewords.contains(word)) { poids++; }
					else if(negativewords.contains(word)) { poids--; }
				}
				lines_weights.add(poids);
			});

            FileWriter fileWriter=new FileWriter(weightFile);
            for(Integer weight: lines_weights){
                fileWriter.write(weight+"\n");
            }
            fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void buildWeight2(String weightFile){
		try{
			for (String line : FileUtils.readLines(new File(weightFile), Charsets.UTF_8)) {
				Pattern ptn = Pattern.compile("\\s+");
				Matcher match = ptn.matcher(line);
				String delimiter=" ";
				String after = match.replaceAll(delimiter);
				String[] parts=after.split(delimiter);
				/*for(String part : parts){
					System.out.print(part+",");
				}
				System.out.println();
				String pos=parts[0];
				double posScore= Double.parseDouble(parts[2]);
				double negScore= Double.parseDouble(parts[3]);
				String lemma=parts[4].split("#")[0];
				if(pos.equals("a")) {
					if(negativewords.contains(lemma) || positivewords.contains(lemma)){
						System.out.println(lemma + ":" + posScore + " , " + negScore);
					}
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	public void write(String arff_file){
	    try{
            FileWriter fileWriter=new FileWriter(arff_file);
            fileWriter.write("@RELATION " + "Opinion\n" );
            fileWriter.write("@ATTRIBUTE " + "Context string\n" );
            fileWriter.write("@ATTRIBUTE " + "Label NUMERIC\n" );
			fileWriter.write("@ATTRIBUTE " + "Class {1,-1,?}\n");

            fileWriter.write("@DATA\n");

            for(int i=0;i<lines_pos.size();i++){
                ArrayList<TT_Position> line_positions=lines_pos.get(i);
                fileWriter.write("'");
                for(TT_Position tt_position : line_positions){
                    fileWriter.write(tt_position.getLemma() + " ");
                }
                fileWriter.write("',"+lines_weights.get(i)+","
						+"?"
						//+ lines_class.get(i)
						+"\n");
            }

            fileWriter.close();
        }
        catch(IOException e){
	        e.printStackTrace();
        }


    }
	public String toString(){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<lines_pos.size();i++){
			for(int j=0;j<lines.get(i).size();j++){
				sb.append(lines.get(i).get(j));
				sb.append(" ");
				sb.append(lines_pos.get(i).get(j).toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}

	public static void extractResults(String prediction_file, int beginLine, int endLine, String outputFule){
		try{
			int lineIdx=1;
			ArrayList<Integer> results=new ArrayList<>();
			FileWriter fileWriter=new FileWriter(outputFule);
			for(String line : FileUtils.readLines(new File(prediction_file), Charsets.UTF_8)) {
				if(lineIdx>= beginLine && lineIdx <= endLine){
					String result_str= line.split(":")[2];
					String str = ( result_str.charAt(0) == '-') ? result_str.substring(0,2) : result_str.substring(0,1);
					results.add(Integer.parseInt(str));
				}
				lineIdx++;
			}
			System.out.println(results.size());
			for(Integer res : results){
				fileWriter.write(res+"\n");
			}
			fileWriter.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}*/



}
