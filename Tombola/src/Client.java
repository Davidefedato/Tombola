import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.SearchIterator.ElementComparisonType;

public class Client {

	protected Shell shell;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	private Text lista;
	ArrayList <Integer> elenco = new ArrayList<Integer>();
	ThreadClient tc;
	
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

	
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Label lblNumeri = new Label(shell, SWT.NONE);
		lblNumeri.setBounds(10, 35, 256, 15);
		
		lista = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		lista.setEditable(false);
		lista.setBounds(10, 10, 289, 194);
		
		Button btnRichiedi = new Button(shell, SWT.NONE);
		btnRichiedi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//si connette al server e crea il socket
				try {
					s = new Socket("localhost", 9999);
					tc = new ThreadClient(s, Client.this);
					//crea un thread di ascolto dei messaggi a cui passer� il socket e la parte grafica
					tc.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRichiedi.setBounds(336, 30, 75, 25);
		btnRichiedi.setText("Richiedi");
		

		
		
		Button btnPopola = new Button(shell, SWT.NONE);
		btnPopola.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				elenco = tc.passaNumeri();
				lista.setText(""+elenco);
			}
		});
		btnPopola.setBounds(336, 72, 75, 25);
		btnPopola.setText("Popola");
		
		Button btnAmbo = new Button(shell, SWT.NONE);
		btnAmbo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				out.println("Ambo");
				for (int i=0; i<elenco.size(); i++){
					out.println(elenco.get(i));
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

	}
}
