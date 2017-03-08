import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Client {

	protected Shell shell;
	private Socket s;
	private PrintWriter out;
	private BufferedReader in;
	
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
		
		Button btnRichiedi = new Button(shell, SWT.NONE);
		btnRichiedi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//si connette al server e crea il socket
				try {
					s = new Socket("localhost", 9999);
					//crea un thread di ascolto dei messaggi a cui passer� il socket e la parte grafica
					ThreadClient tc = new ThreadClient(s, Client.this);
					tc.start();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnRichiedi.setBounds(10, 81, 75, 25);
		btnRichiedi.setText("Richiedi");

	}
}
