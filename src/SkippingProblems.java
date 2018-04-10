import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SkippingProblems {
	

	
	public void solver(String file) throws IOException {
		BufferedReader bufferfile=null;

		bufferfile = new BufferedReader(new FileReader(file));
		String ligneLue = "";
		String total = "";
		while(ligneLue!=null){
			//System.out.println(ligneLue);
			ligneLue = bufferfile.readLine();
			ligneLue = ligneLue.replaceAll("--", ",");
			total += ligneLue + "\n";
		}
		bufferfile.close();
	}
	
	public static void main(String[] args) throws IOException {


		long start_t = System.currentTimeMillis();
		SkippingProblems test = new SkippingProblems();
		String file="dataset.csv";
		test.solver(file);
	      System.out.println("Temps d'execution final : "+(System.currentTimeMillis()-start_t));
	}
}
