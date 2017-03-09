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
	ArrayList<Integer> numeri = new ArrayList<Integer>();
	public ServerThread(Server s, BufferedReader in, Socket sk) {
		super();
		this.s = s;
		this.in = in;
		this.sk = sk;
	}
	
	public void run() {
		while(true){
			try {
				numeri.add(Integer.parseInt(in.readLine()));
				System.out.println("SERVER >> Ricecuto: " + numeri.get(numeri.size()-1) + " da " + sk.getLocalAddress());
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
