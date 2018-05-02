import java.util.ArrayList;
import java.util.Arrays;

public class Main {


	public static void display_help(){
		System.out.println("Usage : datasetFile classFile arffFile is_training options");
		System.out.println("is_training : write training or test arff file");
		System.out.println("Available mode : "
				+"\n build : build the arff file"
				+"\n extract : extract prediction from weka prediction file  and write into csv file"
		);
		System.out.println("Available options : "
				+"\n lemmatize : lemmatize the dataset "
				+"\n stop_word : remove stop words ( need lemmatize option ) "
				+"\n weight : build the weight of lines"
		);
	}

	public static void main(String[] args) {

		String mode=args[0];
		if(mode.equals("build")){
			if(args.length<5){
				display_help();
				return;
			}
			String datasetFile=args[1];
			String classFile=args[2];
			String defaultArffFile=args[3];
			boolean is_training=Boolean.parseBoolean(args[4]);

			String[] options=Arrays.copyOfRange(args,5,args.length);
			System.out.println("Running preprocessing : "+datasetFile + ","+classFile+" -> "+defaultArffFile+(is_training? " training" : " test"));
			for(String option : options){
				System.out.print(option+",");
			}
			System.out.println("\n");
			PreprocessingTool.runAllPreprocess(options,datasetFile,classFile,defaultArffFile,is_training);
		}
		else if(mode.equals("extract")){
			if(args.length != 3){
				display_help();
				return;
			}
			String predictionFile=args[1];
			String outFile=args[2];
			System.out.println("Extracting "+predictionFile + " into "+outFile);
			PreprocessingTool.extractResults(predictionFile,outFile);
		}
		else{
			display_help();
			return;
		}


		/*String[] options_1={};
		PreprocessingTool.runAllPreprocess(options_1,datasetFile,classFile,defaultArffFile+".arff",true);*/

		/*String[] options_2={"lemmatize"};
		PreprocessingTool.runAllPreprocess(options_2,datasetFile,classFile,defaultArffFile+"lem.arff",true);*/

		/*String[] options_3={"lemmatize","stop_word"};
		PreprocessingTool.runAllPreprocess(options_3,datasetFile,classFile,defaultArffFile+"lem_sw.arff",true);*/

		/*String[] options_4={"weight"};
		PreprocessingTool.runAllPreprocess(options_4,datasetFile,classFile,defaultArffFile+"weight.arff",true);*/

		/*String[] options_5={"lemmatize","weight"};
		PreprocessingTool.runAllPreprocess(options_5,datasetFile,classFile,defaultArffFile+"lem_weight.arff",true);*/

	/*	String[] options_6={"lemmatize","weight","stop_word"};
		PreprocessingTool.runAllPreprocess(options_6,datasetFile,classFile,defaultArffFile+"lem_sw_weight.arff",true);*/
	}

}
