import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadClient extends Thread {
	
	private Socket s;
	private Client c;
	ArrayList<Integer> elencoNumeri = new ArrayList<Integer>();
	
	//deve essere inizializzato con il socket e con il riferimento della parte grafica
	public ThreadClient(Socket s, Client c){
		this.s = s;
		this.c = c;
	}
	
	public void run(){
		super.run();
		//all'infinito resta in ascolto di nuovi messaggi nel socket
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			elencoNumeri.add(Integer.parseInt(in.readLine()));
			System.out.println("Numeri" + elencoNumeri);
			
			while(!in.readLine().equals("fine")){
				elencoNumeri.add(Integer.parseInt(in.readLine()));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
}