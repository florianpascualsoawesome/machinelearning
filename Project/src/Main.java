
public class Main {

	
	
	public static void main(String[] args) {
		long t_start=System.currentTimeMillis();
		WordProcessing wordProcessing=new WordProcessing("datas/dataset.csv");
		System.out.println("Data load time : "+(System.currentTimeMillis()-t_start) + " ms");
		t_start=System.currentTimeMillis();
		wordProcessing.processAnnotation("datas/dataset_pos.csv");
		System.out.println("Data Tagging time : "+(System.currentTimeMillis()-t_start) + " ms");
		
		t_start=System.currentTimeMillis();
		wordProcessing.processLemmatization("datas/dataset_lemma.csv");
		System.out.println("Data Lemmatization time : "+(System.currentTimeMillis()-t_start) + " ms");
		
		t_start=System.currentTimeMillis();
		wordProcessing.buildWeight("positive-words.txt","negative-words.txt");
		System.out.println("Data Weight time : "+(System.currentTimeMillis()-t_start) + " ms");

		//System.out.println(wordProcessing.toString());
	}

}
