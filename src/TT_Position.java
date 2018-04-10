
public class TT_Position {
	private String pos;
	private String lemma;
	public String getPos() {
		return pos;
	}
	public String getLemma() {
		return lemma;
	}
	public TT_Position(String pos, String lemma) {
		super();
		this.pos = pos;
		this.lemma = lemma;
	}
	
	public String toString(){
		return "("+pos+","+lemma+")";
	}
}
