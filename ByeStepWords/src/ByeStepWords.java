import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ByeStepWords {

	public static final String avisfile = "dataset_lemma.csv";	
	public static final String lemma = "dataset_pos.csv";
	String texte;
	String lemme;
	StringBuilder textefinal = new StringBuilder("");
	
	public static void main(String[] args) throws IOException {
		
		ByeStepWords d = new ByeStepWords();
		d.destroy(lemma, avisfile);
		System.out.println(d.textefinal);
		
	}
	

	
	public void destroy(String lemmas, String avisfile) throws IOException {
		
		BufferedReader readfile=null;
		BufferedReader readlemma=null;
		File ff=new File("resultat.txt"); // définir l'arborescence
		ff.createNewFile();
		FileWriter ffw=new FileWriter(ff);
		readfile = new BufferedReader(new FileReader(avisfile));
		readlemma = new BufferedReader(new FileReader(lemmas));
		String ligneLue = readfile.readLine();
		String ligneLemmaLue = readlemma.readLine();
		int cpt = 0;
		while(ligneLue!=null && cpt < 1000){
			if(!ligneLue.contains(" ")) {} else {
			cpt++;
			System.out.println(cpt);
			ligneLue = readfile.readLine();
			ligneLemmaLue = readlemma.readLine();
			String[] ligne = ligneLue.split(" ");
			String[] lemma = ligneLemmaLue.split(" ");
			for(int i = 0; i < ligne.length; i++) {
				if(!lemma[i].contains("DT") && !lemma[i].contains("VV") && !lemma[i].contains("VB") && !lemma[i].contains("VVD") &&
				   !lemma[i].contains("VVG") && !lemma[i].contains("VVN") && !lemma[i].contains("VVP") &&
				   !lemma[i].contains("VVZ") && !lemma[i].contains("WRB") && !lemma[i].contains("CC") &&
				   !lemma[i].contains("RB") && !lemma[i].contains("NP") && !lemma[i].contains("IN") &&
				   !lemma[i].contains("PP") && !lemma[i].contains("WP") && !lemma[i].contains("TO") && !lemma[i].contains("CC")
				   && !lemma[i].contains("NN") && !lemma[i].contains("NNS")) {
				textefinal.append(ligne[i] + " ");} 
				//System.out.print(ligne[i].toString() + "\n" + lemma[i].toString() + "\n\n");


				
			}
			
			textefinal.append("\n");

		}}
		String textefinale = textefinal.toString();
		ffw.write(textefinale);
		ffw.close();
		readfile.close();
		readlemma.close();
		
	}
		
}
