import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.List;

import java.awt.Color;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Server {
	protected Shell shell;
	List list;
	private String[][] matrice;
	private Button btnPassa;
	BufferedReader in;
	PrintWriter out;
	Socket s;
	ServerSocket ss;
	ArrayList<Integer> nr;
	String messaggio;
	private ArrayList<ServerThread> st = new ArrayList<ServerThread>();
	private ArrayList<PrintWriter> pw = new ArrayList<PrintWriter>();
	Label[][] tabellone;
	private Text text;
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
	public void cancella(int n) {// passare numero
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
		for (i = 0; i < nr; i++) {
			for (j = 0; j < nc; j++) {
				riga = riga + " " + matrice[i][j];
			}
			riga = "";
			System.out.println();
		}
	}

	public boolean controllaNumero(ArrayList<Integer> en, int n) {
		int c = 0;
		for (int i = 0; i < en.size(); i++) {
			if ((n / 10) == (en.get(i) / 10)) {
				c++;
			}
			if (c >= 3) {
				return false;
			}
		}
		return true;
	}

	public void passaScheda() {
		ArrayList<Integer> elencoNumeri = new ArrayList<Integer>();
		int n;
		int riga;
		int colonna;
		for (int i = 0; i < 15; i++) {
			while (true) {
				n = 1 + ((int) Math.round(Math.random() * 89));
				if (!elencoNumeri.contains(n) && controllaNumero(elencoNumeri, n)) {
					elencoNumeri.add(n);
					for(int j=0; j<pw.size(); j++){
						pw.get(j).println(n);
					}
					System.out.println("SERVER >> " + n);
					break;
				} // if
			} // while
		} // for
	}
	
	public boolean getNumero(int n){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<9; i++){
					for(int j=0; j<10; j++){
						if(Integer.parseInt(tabellone[i][j].getText()) == n){
							tabellone[i][j].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
							i = 10;
							break;
						}
					}
				}
			}
		});
		return false;
	}

	public void numeriVincenti() {
		Thread t = null;
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				int n = 0;
				ArrayList<Integer> elencoNumeriV = new ArrayList<Integer>();
				for (int i = 0; i < 90; i++) {
					while (true) {
						n = 1 + ((int) Math.round(Math.random() * 89));
						if (!elencoNumeriV.contains(n)) {
							System.out.println("SERVER >> numero vincente: " + n);
							elencoNumeriV.add(n);
							getNumero(n);
							for(int j=0; j<pw.size(); j++){
								pw.get(j).println(n);
							}
							cancella(n); // passare numero
							aggiornaLista();
							break;
						} // if
					} // while
					try {
						t.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		});
		t.start();
	}

	public void connetti() {
		System.out.println("SERER >> In attesa di giocatori");
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

	public void controllaVincita() {
		nr = new ArrayList<Integer>();
		try {
			messaggio = in.readLine(); // messaggio di ambo/terna/...
			for (int i = 0; i < 15; i++) {
				nr.add(Integer.parseInt(in.readLine())); // prende i numeri dal
															// client
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// metodi
	protected void createContents() {
		// VARIABILI
		int nr = 9;
		int nc = 10;
		int contatore = 1;
		matrice = new String[nr][nc];
		String riga = "";
		//
		shell = new Shell();
		shell.setSize(450, 484);
		shell.setText("SWT Application");

		/*
		 * list = new List(shell, SWT.BORDER | SWT.V_SCROLL); list.setBounds(10,
		 * 10, 301, 242);
		 */

		btnPassa = new Button(shell, SWT.NONE);
		btnPassa.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				passaScheda();
				numeriVincenti();
			}
		});
		btnPassa.setBounds(10, 221, 75, 25);
		btnPassa.setText("Passa");

		tabellone = new Label[nr][nc];
		int x = 10, y = 10, c =1;
		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				tabellone[i][j] = new Label(shell, SWT.CENTER);
				tabellone[i][j].setBounds(x, y, 20, 20);
				tabellone[i][j].setText(Integer.toString(c++));
				tabellone[i][j].setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
				x += 22;
				if (x >= 220) {
					y += 22;
					x = 10;
				}
			}
		}

		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				matrice[i][j] = Integer.toString(contatore);
				System.out.print(matrice[i][j] + " ");
				riga = riga + " " + matrice[i][j];
				contatore++;
			}

			// list.add(riga);
			riga = "";
			System.out.println();
		}
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				connetti();
				
			}
		});
		t.start();
	}// createContents
}
