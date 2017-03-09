import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ibm.icu.text.SearchIterator.ElementComparisonType;

public class Client {
	private int punteggio = 0;
	protected Shell shell;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	ArrayList <Integer> elenco = new ArrayList<Integer>();
	ArrayList <Integer> elencoV = new ArrayList<Integer>();
	ThreadClient tc;
	int v;
	private Label[][] cartella;
	
	public static void main(String[] args) {
		try {
			Client window = new Client();
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

	public void popolaLista(){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				elenco = tc.passaNumeri();
				Collections.sort(elenco);
				for(int i=0; i<elenco.size(); i++){
					if(cartella[0][elenco.get(i)/10].getText().equals("")){
						cartella[0][elenco.get(i)/10].setText(Integer.toString(elenco.get(i)));
					}else if(cartella[1][elenco.get(i)/10].getText().equals("")){
						cartella[1][elenco.get(i)/10].setText(Integer.toString(elenco.get(i)));
					}else if(cartella[2][elenco.get(i)/10].getText().equals("")){
						cartella[2][elenco.get(i)/10].setText(Integer.toString(elenco.get(i)));
					}
				}
			}
		});
	}
	
	public void listaVincenti(){
		elencoV = tc.passaNumeriV();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				
			}
		});
	}
	
	public boolean getNumero(int n){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<3; i++){
					for(int j=0; j<10; j++){
						if(!cartella[i][j].getText().equals("") && Integer.parseInt(cartella[i][j].getText()) == n){
							cartella[i][j].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
							i = 4;
							break;
						}
					}
				}
			}
		});
		return false;
	}
	
	public int checkNumero(){
		int n = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				if (!cartella[i][j].getText().equals("") && elencoV.contains(Integer.parseInt(cartella[i][j].getText()))) {
					n++;
					return n;
				}
			}
			n = 0;
		}
		return n;
	}
	
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Label lblNumeri = new Label(shell, SWT.NONE);
		lblNumeri.setBounds(10, 35, 256, 15);
		
		Button btnRichiedi = new Button(shell, SWT.NONE);
		btnRichiedi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//si connette al server e crea il socket
				try {
					s = new Socket("localhost", 9999);
					tc = new ThreadClient(s, Client.this, elenco, elencoV);
					//crea un thread di ascolto dei messaggi a cui passerà il socket e la parte grafica
					tc.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRichiedi.setBounds(336, 30, 75, 25);
		btnRichiedi.setText("Connetti");
		
		
		Button btnAmbo = new Button(shell, SWT.NONE);
		btnAmbo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkNumero() == 2){
					out.println("AMBO");
					punteggio += 10;
					System.out.println("CLIET >> Hai fatto AMBO");
				}else{
					System.out.println("CLIENT >> Dove lo vedi ambo??? COGLIONE!!!");
					
				}
				
			}
		});
		btnAmbo.setBounds(10, 227, 75, 25);
		btnAmbo.setText("Ambo");
		
		Button btnTerna = new Button(shell, SWT.NONE);
		btnTerna.setBounds(91, 227, 75, 25);
		btnTerna.setText("Terna");
		
		Button btnQuaterna = new Button(shell, SWT.NONE);
		btnQuaterna.setBounds(172, 227, 75, 25);
		btnQuaterna.setText("Quaterna");
		
		Button btnCinquina = new Button(shell, SWT.NONE);
		btnCinquina.setBounds(253, 227, 75, 25);
		btnCinquina.setText("Cinquina");
		
		Button btnTombola = new Button(shell, SWT.NONE);
		btnTombola.setBounds(336, 227, 75, 25);
		btnTombola.setText("Tombola");
		
		cartella = new Label[3][10];
		int x = 10, y = 10, c =1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				cartella[i][j] = new Label(shell, SWT.CENTER);
				cartella[i][j].setBounds(x, y, 20, 20);
				cartella[i][j].setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
				cartella[i][j].addMouseListener(new MouseListener() {
					@Override
					public void mouseDoubleClick(MouseEvent arg0) {
						cartella[i][j].setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					}

					@Override
					public void mouseDown(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseUp(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				});;
				x += 22;
				if (x >= 220) {
					y += 40;
					x = 10;
				}
			}
		}

	}
}
