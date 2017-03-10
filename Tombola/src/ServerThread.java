import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{
	Server s;
	BufferedReader in;
	PrintWriter out;
	Socket sk;
	Giocatore g;
	ArrayList<Integer> numeri = new ArrayList<Integer>();
	public ServerThread(Server s, BufferedReader in, Socket sk, Giocatore g) {
		super();
		this.s = s;
		this.in = in;
		this.sk = sk;
		this.g = g;
	}
	
	public void run() {
		while(true){
			try {
				String s1 = in.readLine();
				if(s1.charAt(0) <=57 && s1.charAt(0) >=48){
					int v = Integer.parseInt(s1);
					numeri.add(v);
					System.out.println("SERVER >> Ricecuto: " + numeri.get(numeri.size()-1) + " da " + sk.getLocalAddress());
				}else{
					switch(s1){
					case "AMBO":
						System.out.println("SERVER >> " + g.getNome() + " ha fatto " + s1);
						g.addPremio(s1);
						g.setPunti(g.getPunti() + 5);
						s.updatePlayer(g);
						break;
					case "TERNA":
						System.out.println("SERVER >> " + g.getNome() + " ha fatto " + s1);
						g.addPremio(s1);
						g.setPunti(g.getPunti() + 10);
						s.updatePlayer(g);
						break;
					case "QUATERNA":
						System.out.println("SERVER >> " + g.getNome() + " ha fatto " + s1);
						g.addPremio(s1);
						g.setPunti(g.getPunti() + 15);
						s.updatePlayer(g);
						break;
					case "CINQUINA":
						System.out.println("SERVER >> " + g.getNome() + " ha fatto " + s1);
						g.addPremio(s1);
						g.setPunti(g.getPunti() + 20);
						s.updatePlayer(g);
						break;
					case "TOMBOLA":
						System.out.println("SERVER >> " + g.getNome() + " ha fatto " + s1);
						g.addPremio(s1);
						g.setPunti(g.getPunti() + 50);
						s.updateTable();
						break;
					}
				}
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
