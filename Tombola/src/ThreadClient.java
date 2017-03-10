import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadClient extends Thread {
	BufferedReader in;
	PrintWriter out;
	private Socket s;
	private Client c;
	public ArrayList<Integer> elencoNumeri;
	public ArrayList<Integer> numeriVincenti;

	// deve essere inizializzato con il socket e con il riferimento della parte
	// grafica
	public ThreadClient(Socket s, Client c, ArrayList<Integer> elencoNumeri, ArrayList<Integer> numeriVincenti) {
		this.s = s;
		this.c = c;
		this.elencoNumeri = elencoNumeri;
		this.numeriVincenti = numeriVincenti;
	}

	public ArrayList <Integer> passaNumeri(){
		return elencoNumeri;
	}
	
	public ArrayList<Integer> passaNumeriV(){
		return numeriVincenti;
	}
	
	public void run() {
		super.run();
		// all'infinito resta in ascolto di nuovi messaggi nel socket
		try {
			System.out.println(s);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			for (int i = 0; i < 15; i++) {
				elencoNumeri.add(Integer.parseInt(in.readLine()));
				//System.out.println("CLIENT >> " + elencoNumeri.get(i));
			}
			c.popolaLista();
			System.out.println("CLIENT >> In attesa dei numeri");
			while(true){
				String s1 = in.readLine();
				if(s1.charAt(0) <=57 && s1.charAt(0) >=48){
					int v = Integer.parseInt(s1);
					numeriVincenti.add(v);
					c.getNumero(v);
					System.out.println("CLIENT >> Ricevuto: " + v);
				}else{
					System.out.println("CLIENT >> Ricevuto: " + s1);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}