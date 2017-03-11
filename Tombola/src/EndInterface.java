import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class EndInterface {
	Giocatore g;
	Giocatore w;
	protected Shell shell;
	
	public EndInterface(Giocatore g, Giocatore w){
		this.g= g;
		this.w = w;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			EndInterface window = new EndInterface(null, null);
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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");

	}

}
