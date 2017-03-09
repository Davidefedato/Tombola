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
	int v;
	public ArrayList<Integer> elencoNumeri = new ArrayList<Integer>();


	// deve essere inizializzato con il socket e con il riferimento della parte
	// grafica
	public ThreadClient(Socket s, Client c) {
		this.s = s;
		this.c = c;
	}

	public ArrayList <Integer> passaNumeri(){
		return elencoNumeri;
	}
	
	public int passaNumeriV(){
		return v;
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
				System.out.println("CLIENT >> " + elencoNumeri.get(i));
			}
			c.popolaLista();
			while(true){
				v = Integer.parseInt(in.readLine());
				c.listaVincenti();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}