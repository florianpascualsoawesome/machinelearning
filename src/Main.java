
public class Main {

	
	public  static void write_all(){

	    long t_start=System.currentTimeMillis();
		WordProcessing wordProcessing=new WordProcessing(
				"datas/dataset.csv",
				"datas/labels.csv",
				true,
				"datas/dataset_lemma.csv",
				"datas/dataset_pos.csv",
				"datas/dataset_weight",
				"datas/positive-words.txt",
				"datas/negative-words.txt");
		System.out.println("Data load time : "+(System.currentTimeMillis()-t_start) + " ms");

		t_start=System.currentTimeMillis();
		wordProcessing.processAnnotation();
		System.out.println("Data Tagging time : "+(System.currentTimeMillis()-t_start) + " ms");

        t_start=System.currentTimeMillis();
		wordProcessing.destroy_stop_words();
        System.out.println("Stop word removal time : "+(System.currentTimeMillis()-t_start) + " ms");

		t_start=System.currentTimeMillis();
		wordProcessing.processLemmatization();
		System.out.println("Data Lemmatization time : "+(System.currentTimeMillis()-t_start) + " ms");

		t_start=System.currentTimeMillis();
		wordProcessing.buildWeight();
		System.out.println("Data Weight time : "+(System.currentTimeMillis()-t_start) + " ms");
	}

	public static void convert_to_arff(){
        long t_start=System.currentTimeMillis();
        WordProcessing wordProcessing=new WordProcessing(
                "datas/dataset.csv",
                "datas/labels.csv",
                false,
                "datas/dataset_lemma.csv",
                "datas/dataset_pos.csv",
                "datas/dataset_weight",
                "datas/positive-words.txt",
                "datas/negative-words.txt");
        wordProcessing.write("datas/WEKA/datasetArff2.arff");
        System.out.println("ARFF write time : "+(System.currentTimeMillis()-t_start) + " ms");
    }

	public static void main(String[] args) {
        convert_to_arff();
	}

}
