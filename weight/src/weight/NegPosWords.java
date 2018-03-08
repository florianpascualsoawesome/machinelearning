package weight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class NegPosWords {
	
	//String[] positivewords;
	//String[] negativewords;
	private transient HashMap<Object,Object> positivewords = new HashMap<Object,Object>();
	private transient HashMap<Object,Object> negativewords = new HashMap<Object,Object>();
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	
	public void init() throws IOException {
		BufferedReader negativefile=null;
		BufferedReader positivefile=null;
		String positivewordsfile="positive-words.txt";
		String negativewordsfile="negative-words.txt";
		positivefile = new BufferedReader(new FileReader(positivewordsfile));
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
		}
	}
	
	public static void main(String[] args) throws IOException {

	      // Nous déclarons nos objets en dehors du bloc try/catch

		long start_t = System.currentTimeMillis();
		/* Le chemin vers le fichier à lire */
		String pathFichier="dataset-2.txt";
		NegPosWords test = new NegPosWords();

		test.init();
		
		BufferedReader fluxEntree=null;
		try {
			/* Création du flux vers le fichier texte */
			fluxEntree = new BufferedReader(new FileReader(pathFichier));

			/* Lecture d'une ligne */
			String ligneLue = fluxEntree.readLine();
			int cpt = 1;
			while(ligneLue!=null){
				String[] mots = ligneLue.split(" ");
				int poids = 0;
				for(String mot : mots) {
					if(test.isPositive(mot)) { poids++; }
					if(test.isNegative(mot)) { poids--; }
				}
				String ispos = ""; if(poids > 0) ispos = "Positif"; if(poids < 0) ispos = "Negatif"; if(poids == 0) ispos = "Neutre";
				System.out.println(cpt+" : "+ispos+" ("+poids+")");
				ligneLue = fluxEntree.readLine();
				cpt++;
			}
		}
		catch(IOException exc){
			exc.printStackTrace();
		}
		finally{
			try{
				if(fluxEntree!=null){
					/* Fermeture du flux vers le fichier */
					fluxEntree.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	     // String[] mots = ligne.split(" ");
	      System.out.println("Temps d'execution final : "+(System.currentTimeMillis()-start_t));
	}
	
	public boolean isPositive(String mot) {
		boolean boo = false;
		if(positivewords.containsValue(mot)) {
		boo = true; System.out.println(" - "+ mot); 
		}
		return boo;
	}
	
	public boolean isNegative(String mot) {
		boolean boo = false;
		if(negativewords.containsValue(mot)) {
			boo = true; System.err.println(" - "+mot); 
			}
		return boo;
	}
}
