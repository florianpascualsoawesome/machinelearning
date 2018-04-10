import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class ByeStepWords {

	private String lemmaFile = "dataset_lemma.csv";
	private String posFile = "dataset_pos.csv";
	private String texte;
	private String lemme;
	private StringBuilder textefinal;


	public ByeStepWords(String avisfile, String lemmaFile){
		this.lemmaFile =avisfile;
		this.posFile =lemmaFile;
		textefinal=new StringBuilder();

	}

	/*public static void main(String[] args) throws IOException {
		
		ByeStepWords d = new ByeStepWords();
		d.destroy(posFile, lemmaFile);
		System.out.println(d.textefinal);
		
	}*/
	

	
	public void destroy()  {
		try{

			BufferedReader readfile=null;
			BufferedReader readlemma=null;
			File ff=new File("resultat.txt"); // définir l'arborescence
			ff.createNewFile();
			FileWriter ffw=new FileWriter(ff);
			readfile = new BufferedReader(new FileReader(lemmaFile));
			readlemma = new BufferedReader(new FileReader(posFile));
			String ligneLue = readfile.readLine();
			String ligneLemmaLue = readlemma.readLine();
			int cpt = 0;

			String[] forbidden={
					"DT","VV","VB","VVD","VVG","VVN","VVP",
					"VVZ","WRB","CC","RB","NP","IN","PP",
					"WP","TO","CC","NN","NNS"};
			HashSet<String> forbidden_set=new HashSet<>();
			forbidden_set.addAll(Arrays.asList(forbidden));

			while(ligneLue!=null && cpt < 1000){
				if(!ligneLue.contains(" ")) {} else {
					cpt++;
					System.out.println(cpt);
					ligneLue = readfile.readLine();
					ligneLemmaLue = readlemma.readLine();
					String[] ligne = ligneLue.split(" ");
					String[] lemma = ligneLemmaLue.split(" ");
					for(int i = 0; i < ligne.length; i++) {
						if(! forbidden_set.contains(lemma[i])){
							textefinal.append(ligne[i]);
							textefinal.append(" ");
						}
						//System.out.print(ligne[i].toString() + "\n" + posFile[i].toString() + "\n\n");
					}
					textefinal.append("\n");

				}}
			ffw.write(textefinal.toString());
			ffw.close();
			readfile.close();
			readlemma.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}

		
	}
		
}
