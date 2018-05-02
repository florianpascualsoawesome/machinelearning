import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.annolab.tt4j.TreeTaggerException;
import org.annolab.tt4j.TreeTaggerWrapper;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

/**
 * Class which wrap all preprocessing tools
 */
public class PreprocessingTool {



    /**
     * The text as list of sentence ( sentence as list of string )
     */
    private ArrayList<ArrayList<String>> lines;

    /**
     * The class of each line
     */
    private ArrayList<Integer> lines_class;

    /**
     * The gram pos for each word of the text
     * Mapped with lines, pos( lines[i][j] ) = lines_pos[i][j]
     */
    private ArrayList<ArrayList<TT_Position>> lines_pos;

    /**
     * The tree tagger wrapper class
     */
    private static TreeTaggerWrapper<String> tt;

    /**
     * The weight of each line
     */
    private ArrayList<Float> lines_weights;

    private ArrayList<Integer> lines_weights2;

    /** Set of positive words from positivewords dico */
    private Set<String> positivewords;

    /** Set of negative words from negativewords dico */
    private Set<String> negativewords;

    /** The total text size in characters */
    private int text_size;


    public PreprocessingTool(){
        lines=new ArrayList<>();
        lines_pos=new ArrayList<>();
        lines_weights=new ArrayList<>();
        lines_weights2=new ArrayList<>();
        lines_class=new ArrayList<>();
        text_size=0;
        loadTT();
    }

    /**
     *
     * @param srcDataset
     * @param dstDataset
     */
    public static  void clean(String srcDataset, String dstDataset){
        try{
            StringBuilder sb=new StringBuilder();


            String[] to_destroy_chars={"'","--","-","@card@","@ord@","&","\\*","\""};
            HashSet<String> to_destroy_chars_set=new HashSet(Arrays.asList(to_destroy_chars));
            for(String line :  FileUtils.readLines(new File(srcDataset),Charsets.UTF_8)){
               String[] words=line.split(" ");
               for(String word : words){
                    if(! to_destroy_chars_set.contains(word)){
                        sb.append(word+" ");
                    }
               }
               sb.append("\n");
            }

            String new_str=sb.toString();
            for(String dc : to_destroy_chars){
               new_str=new_str.replaceAll(dc,"");
            }
            FileUtils.write(new File(dstDataset),new_str,Charsets.UTF_8);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Load the TreeTagger wrapper
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
     * Load dataset and class file
     * @param datasetFile : the original dataset file
     * @param classFile : the corresponding class for the dataset
     */
    public void loadFile(String datasetFile, String classFile){
        try {
            text_size=0;
            lines=new ArrayList<>();
            lines_class=new ArrayList<>();

            FileUtils.readLines(new File(datasetFile), Charsets.UTF_8).forEach(line -> {
                lines.add( new ArrayList<>(Arrays.asList(line.split(" "))));
                text_size+=line.length();
            });
            FileUtils.readLines(new File(classFile),Charsets.UTF_8).forEach(line -> {
                lines_class.add(Integer.parseInt(line));
            });
            System.out.println("Data loaded with succes : "+lines.size() +" lines, size :"+text_size);
            //lines.forEach(line -> System.out.println(line));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Add gramm pos of lines
     * @assert dataset already load
     */
    public void lemmatize(){
        for(int i=0;i<lines.size();i++){
            lemmatize(lines.get(i));
        }
    }


    private void lemmatize(ArrayList<String> line){
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

    /**
     * Build a new dataset : original_datasetFile with removal of words with gram pos belonging to
     * set of stop_words
     * @param newDatasetFile
     */
    public void destroy_stop_words(String newDatasetFile){
        String[] stop_words={ "CC","CD","DT","WRB","NP","NPS","IN", "PP","WP","TO","WP$","WDT","SYM"};
        HashSet<String> stop_words_set=new HashSet<>(Arrays.asList(stop_words));
        StringBuilder textefinal = new StringBuilder();

        for(int i=0;i<lines.size();i++){
            ArrayList<TT_Position> line_pos=lines_pos.get(i);
            for(int j=0;j<line_pos.size();j++){
                if(! stop_words_set.contains(line_pos.get(j).getPos())){
                    textefinal.append(line_pos.get(j).getLemma()+" ");
                }
            }
            textefinal.append("\n");
        }
        try{
            FileUtils.write(new File(newDatasetFile),textefinal.toString(),Charsets.UTF_8);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Build the weight for each line
     * @param positivewordsfile : dictionnary of  positive words
     * @param negativewordsfile : dictionnary of  negative words
     * @param lexiconFile : the sentiwordnet dictionnary
     */
    public void buildWeight(String positivewordsfile, String negativewordsfile, String lexiconFile ){
        try {
            Set<String> positivewords = new HashSet<>(FileUtils.readLines(new File(positivewordsfile),Charsets.UTF_8));
            Set<String>negativewords = new HashSet<>(FileUtils.readLines(new File(negativewordsfile),Charsets.UTF_8));
            Map<String,Float> posWordsWeight=new HashMap<>();
            Map<String,Float> negWordsWeight=new HashMap<>();
            lines_weights=new ArrayList<>();


            for (String line : FileUtils.readLines(new File(lexiconFile), Charsets.UTF_8)) {
                Pattern ptn = Pattern.compile("\\s+");
                Matcher match = ptn.matcher(line);
                String delimiter=" ";
                String after = match.replaceAll(delimiter);
                String[] parts=after.split(delimiter);

                if(parts.length >= 5){
                    String pos=parts[0];
                    float posScore= Float.parseFloat(parts[2]);
                    float negScore= Float.parseFloat(parts[3]);
                    String lemma=parts[4].split("#")[0];
                    if(pos.equals("a")) {
                        if(negativewords.contains(lemma)){
                            negWordsWeight.put(lemma,negScore);
                        }
                        else if(positivewords.contains(lemma)){
                            posWordsWeight.put(lemma,posScore);
                        }
                    }
                }
            }

            for(ArrayList<String> line : lines){
                float line_weight = 0;
                int line_weight2=0;
                for(String word : line){
                    float word_pos_value=posWordsWeight.containsKey(word) ? posWordsWeight.get(word) : 0f;
                    float word_neg_value=negWordsWeight.containsKey(word) ?negWordsWeight.get(word) : 0f;

                    if(word_neg_value !=0 && word_pos_value != 0){
                        line_weight += (word_pos_value >= word_neg_value) ? word_pos_value : -word_neg_value;
                    }
                    else if(word_neg_value != 0){
                        line_weight -= word_neg_value;
                    }
                    else if(word_pos_value != 0){
                        line_weight += word_pos_value;
                    }

                    if(positivewords.contains(word)){
                        line_weight2 += 1;
                    }
                    else if(negativewords.contains(word)){
                        line_weight2 -=1;
                    }
                }
                lines_weights.add(line_weight);
                lines_weights2.add(line_weight2);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Write new data_set with specified preprocessing into arff_file
     * @param arff_file : the arff_file
     * @param options : the options to use {lemmatize,weight,stop_word}
     * @param is_training : write arff training file or test file
     */
    public void write(String arff_file,HashSet<String> options, boolean is_training){
        try{
            FileWriter fileWriter=new FileWriter(arff_file);
            fileWriter.write("@RELATION " + "Opinion\n" );
            fileWriter.write("@ATTRIBUTE " + "Context string\n");
            if(options.contains("weight")){
                fileWriter.write("@ATTRIBUTE " + "Weight NUMERIC\n" );
            }

            fileWriter.write("@ATTRIBUTE " + "Class {1,-1}\n");
            fileWriter.write("@DATA\n");

            for(int i=0;i<lines.size();i++){
                if(options.contains("lemmatize")){
                    ArrayList<TT_Position> line_positions=lines_pos.get(i);
                    fileWriter.write("'");
                    for(TT_Position tt_position : line_positions){
                        fileWriter.write(tt_position.getLemma() + " ");
                    }
                }
                else{
                    ArrayList<String> line_words=lines.get(i);
                    fileWriter.write("'");
                    for(String word : line_words){
                        fileWriter.write(word+ " ");
                    }
                }
                fileWriter.write("'");
                if(options.contains("weight")){
                    fileWriter.write(","+lines_weights.get(i));
                }

                if(is_training){
                    fileWriter.write(","+lines_class.get(i));
                }
                else{
                    fileWriter.write(",?");
                }
                fileWriter.write("\n");

            }

            fileWriter.close();
            System.out.println(arff_file + " created with success ");
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }


    /**
     * run all specified preprocess into an arff_file
     * @param options : the options to use {lemmatize,weight}
     * @param datasetFile : the original dataset file
     * @param classFile : the corresponding class for the dataset
     * @param arff_file : the arff_file
     * @param is_training : write arff training file or test file
     */
    public static void runAllPreprocess(String[] options, String datasetFile, String classFile, String arffFile, boolean is_training_set){

        HashSet<String> optionsSet=new HashSet<>(Arrays.asList(options));
        String positivewordsfile=	"datas/input/positive-words.txt";
        String negativewordsfile="datas/input/negative-words.txt";
        String lexiconFile="datas/input/SentiWordNet_3.0.0_20130122.txt";

       PreprocessingTool pt=new PreprocessingTool();

       String dataset_to_use=datasetFile;

       if(optionsSet.contains("clean")){
           dataset_to_use=datasetFile+"2";
           PreprocessingTool.clean(datasetFile,dataset_to_use);

       }
       pt.loadFile(dataset_to_use,classFile);
       if(optionsSet.contains("lemmatize")){
           pt.lemmatize();
           //pt.display_cat();
           if(optionsSet.contains("stop_word")){
               String tmpDataset=dataset_to_use+"no_sw";
               pt.destroy_stop_words(tmpDataset);
               PreprocessingTool pt2=new PreprocessingTool();
               pt2.loadFile(tmpDataset,classFile);
               pt2.lemmatize();
               pt=pt2;
              /* System.out.println("new  cat");
               pt.display_cat();*/
           }
       }
        if(optionsSet.contains("weight")){
            pt.buildWeight(positivewordsfile,negativewordsfile,lexiconFile);
            //pt.measure_weight_precision();
        }
       pt.write(arffFile,optionsSet,is_training_set);

    }


    public void measure_weight_precision(){
        int nb_good_weight=0;
        int nb_good_weight2=0;
        int nb_null=0, nb_null2=0;
        for(int i=0;i<lines_class.size();i++){

            if(lines_weights.get(i) < 0 && lines_class.get(i) == -1){
                nb_good_weight++;
            }
            else if(lines_weights.get(i) > 0 && lines_class.get(i) == 1){
                nb_good_weight++;
            }
            else if(lines_weights.get(i) == 0){
                nb_null++;
            }

            if(lines_weights2.get(i) < 0 && lines_class.get(i) == -1){
                nb_good_weight2++;
            }
            else if(lines_weights2.get(i) > 0 && lines_class.get(i) == 1){
                nb_good_weight2++;
            }
            else if(lines_weights2.get(i) == 0){
                nb_null2++;
            }
        }
        System.out.println("Good weight : "+nb_good_weight+"/"+lines_class.size() +" nbnull="+nb_null);
        System.out.println("Good weight2 : "+nb_good_weight2+"/"+lines_class.size() + " nbnull="+nb_null2);
    }


    /**
     * Extract weka resutls to a simple csv file with all labels
     * @param prediction_file
     * @param outputFule
     */
    public static void extractResults(String prediction_file, String outputFule){
        try{
            int lineIdx=0;
            ArrayList<Integer> results=new ArrayList<>();
            FileWriter fileWriter=new FileWriter(outputFule);
            for(String line : FileUtils.readLines(new File(prediction_file), Charsets.UTF_8)) {
                if( ! line.isEmpty()){
                    if(lineIdx>0){ // first line contains useless char
                        String result_str= line.split(":")[2];
                        String str = ( result_str.charAt(0) == '-') ? result_str.substring(0,2) : result_str.substring(0,1);
                        results.add(Integer.parseInt(str));
                    }
                    lineIdx++;
                }

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
    }

}

