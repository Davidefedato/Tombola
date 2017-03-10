import java.util.ArrayList;

public class Giocatore {
	private String nome;
	private int punti;
	int id;
	private ArrayList<String> premi = new ArrayList<String>();
	public Giocatore(String nome, int punti, int id) {
		super();
		this.nome = nome;
		this.punti = punti;
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getPunti() {
		return punti;
	}
	public void setPunti(int punti) {
		this.punti = punti;
	}
	public ArrayList<String> getPremi() {
		return premi;
	}
	public void addPremio(String s) {
		premi.add(s);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
