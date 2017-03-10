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
	private Text txtNome;
	private Text txtIP;
	Button btnAmbo;
	Button btnTerna;
	Button btnQuaterna;
	Button btnCinquina;
	Button btnTombola;
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
	
	public boolean checkNumero(String s){
		int n = 0;
		int riga[] = new int[3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				if (!cartella[i][j].getText().equals("") && elencoV.contains(Integer.parseInt(cartella[i][j].getText()))) {
					System.out.println("numero vincente trovato: " + i);
					//n++;
					riga[i]++;
				}
			}
		}
		if(s.equals("AMBO")){
			for(int i=0; i<3; i++){
				if(riga[i] == 2){
					btnAmbo.setEnabled(false);
					return true;
				}
			}
			return false;
		}else if(s.equals("TERNA")){
			for(int i=0; i<3; i++){
				if(riga[i] == 3){
					btnTerna.setEnabled(false);
					return true;
				}
			}
			return false;
		}else if(s.equals("QUATERNA")){
			for(int i=0; i<3; i++){
				if(riga[i] == 4){
					btnQuaterna.setEnabled(false);
					return true;
				}
			}
			return false;
		}else if(s.equals("CINQUINA")){
			for(int i=0; i<3; i++){
				if(riga[i] == 5){
					btnCinquina.setEnabled(false);
					return true;
				}
			}
			return false;
		}else if(s.equals("TOMBOLA")){
			for(int i=0; i<3; i++){
				if(riga[i] == 15){
					btnTombola.setEnabled(false);
					return true;
				}
			}
			return false;
		}
		
		return false;
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
					out = new PrintWriter(s.getOutputStream(), true);
					out.println(txtNome.getText());
					//crea un thread di ascolto dei messaggi a cui passerà il socket e la parte grafica
					tc.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRichiedi.setBounds(336, 98, 75, 25);
		btnRichiedi.setText("Connetti");
		
		
		btnAmbo = new Button(shell, SWT.NONE);
		btnAmbo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkNumero("AMBO")){
					out.println("AMBO");
					punteggio += 10;
					System.out.println("CLIENT >> Hai fatto AMBO");
				}else{
					System.out.println("CLIENT >> Dove lo vedi ambo??? COGLIONE!!!");
					
				}
				
			}
		});
		btnAmbo.setBounds(10, 227, 75, 25);
		btnAmbo.setText("Ambo");
		
		btnTerna = new Button(shell, SWT.NONE);
		btnTerna.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkNumero("TERNA")){
					out.println("TERNA");
					punteggio += 10;
					System.out.println("CLIENT >> Hai fatto TERNA");
				}else{
					System.out.println("CLIENT >> Dove lo vedi TERNA??? COGLIONE!!!");
					
				}
				
			}
		});
		btnTerna.setBounds(91, 227, 75, 25);
		btnTerna.setText("Terna");
		
		btnQuaterna = new Button(shell, SWT.NONE);
		btnQuaterna.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkNumero("QUATERNA")){
					out.println("QUATERNA");
					punteggio += 10;
					System.out.println("CLIENT >> Hai fatto QUATERNA");
				}else{
					System.out.println("CLIENT >> Dove lo vedi QUATERNA??? COGLIONE!!!");
					
				}
				
			}
		});
		btnQuaterna.setBounds(172, 227, 75, 25);
		btnQuaterna.setText("Quaterna");
		
		btnCinquina = new Button(shell, SWT.NONE);
		btnCinquina.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkNumero("CINQUINA")){
					out.println("CINQUINA");
					punteggio += 10;
					System.out.println("CLIENT >> Hai fatto CINQUINA");
				}else{
					System.out.println("CLIENT >> Dove lo vedi CINQUINA??? COGLIONE!!!");
					
				}
				
			}
		});
		btnCinquina.setBounds(253, 227, 75, 25);
		btnCinquina.setText("Cinquina");
		
		btnTombola = new Button(shell, SWT.NONE);
		btnTombola.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(checkNumero("TOMBOLA")){
					out.println("TOMBOLA");
					punteggio += 10;
					System.out.println("CLIENT >> Hai fatto TOMBOLA");
				}else{
					System.out.println("CLIENT >> Niente TOMBOLA...");
					
				}
				
			}
		});
		btnTombola.setBounds(336, 227, 75, 25);
		btnTombola.setText("Tombola");
		
		Label lblNome = new Label(shell, SWT.NONE);
		lblNome.setBounds(273, 10, 55, 15);
		lblNome.setText("Nome");
		
		txtNome = new Text(shell, SWT.BORDER);
		txtNome.setBounds(335, 7, 76, 21);
		
		Label lblIpServer = new Label(shell, SWT.NONE);
		lblIpServer.setBounds(273, 54, 55, 15);
		lblIpServer.setText("IP Server");
		
		txtIP = new Text(shell, SWT.BORDER);
		txtIP.setBounds(335, 54, 76, 21);
		
		cartella = new Label[3][10];
		int x = 10, y = 10, c =1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				cartella[i][j] = new Label(shell, SWT.CENTER);
				cartella[i][j].setBounds(x, y, 20, 20);
				cartella[i][j].setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
				x += 22;
				if (x >= 220) {
					y += 40;
					x = 10;
				}
			}
		}

	}
}
