import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Server {

	protected Shell shell;
	List list;
	private String[][] matrice;
	private Button btnPassa;
	BufferedReader in;
	PrintWriter out;
	Socket s;
	ServerSocket ss;
	ArrayList <Integer> nr;
	String messaggio;
	private ArrayList<ServerThread> st = new ArrayList<ServerThread>();
	private ArrayList<PrintWriter> pw = new ArrayList<PrintWriter>();

	public static void main(String[] args) {
		try {
			Server window = new Server();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	// METODI
	public void cancella(int n) {//passare numero
		int i, j;
		int nr = 9;
		int nc = 10;

		for (i = 0; i < nr; i++) {
			for (j = 0; j < nc; j++) {
				if (matrice[i][j].equals(Integer.toString(n))) {
					matrice[i][j] = "X";
				}
			}

		}
	}

	public void aggiornaLista() {
		int i, j;
		int nr = 9;
		int nc = 10;
		String riga = "";
		list.removeAll();
		for (i = 0; i < nr; i++) {
			for (j = 0; j < nc; j++) {
				riga = riga + " " + matrice[i][j];
			}
			list.add(riga);
			riga = "";
			System.out.println();
		}
	}
	
	public boolean controllaNumero(ArrayList<Integer> en, int n){
		int c=0;
		for(int i=0; i<en.size(); i++){
			if((n/10)==(en.get(i)/10)){
				c++;
			}
			if(c>=3){
				return false;
			}
		}
		return true;
	}

	public void passaScheda(){
		ArrayList<Integer> elencoNumeri = new ArrayList<Integer>();
		int n;
		int riga;
		int colonna;
		for (int i = 0; i < 15; i++) {
			while (true) {
				n = 1 + ((int) Math.round(Math.random() * 89));
				if (!elencoNumeri.contains(n) && controllaNumero(elencoNumeri, n)) {
					elencoNumeri.add(n);
					out.println(n);
					System.out.println("SERVER >> " + n);
					break;
				}//if
			} //while
		}//for
	}
	
	public void numeriVincenti(){
		int n = 0;
		ArrayList<Integer> elencoNumeriV = new ArrayList<Integer>();
		for (int i = 0; i<90; i++){
			while (true) {
				n = 1 + ((int) Math.round(Math.random() * 89));
				if (!elencoNumeriV.contains(n)) {
					elencoNumeriV.add(n);
					out.println(n);
					cancella(n); //passare numero
					aggiornaLista();
					break;
				}//if
			} //while
		}
	
		
	}

	public void connetti(){
		try {
			ss = new ServerSocket(9999);
			s = ss.accept();
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
			st.add(new ServerThread(Server.this, in, s));
			pw.add(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void controllaVincita(){
		nr = new ArrayList <Integer>();
		try {
			messaggio = in.readLine(); //messaggio di ambo/terna/...
			for (int i=0; i<15; i++){
				nr.add(Integer.parseInt(in.readLine())); //prende i numeri dal client
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//metodi
	protected void createContents() {
		// VARIABILI
		int i, j;
		int nr = 9;
		int nc = 10;
		int contatore = 1;
		matrice = new String[nr][nc];
		String riga = "";
		//
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");

		list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(10, 10, 301, 242);

		
		btnPassa = new Button(shell, SWT.NONE);
		btnPassa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					passaScheda();
					numeriVincenti();
			}
		});
		btnPassa.setBounds(359, 84, 75, 25);
		btnPassa.setText("Passa");

		for (i = 0; i < nr; i++) {
			for (j = 0; j < nc; j++) {
				matrice[i][j] = Integer.toString(contatore);
				System.out.print(matrice[i][j] + " ");
				riga = riga + " " + matrice[i][j];
				contatore++;
			}

			list.add(riga);
			riga = "";
			System.out.println();
		}

		connetti();
	}//createContents
}
