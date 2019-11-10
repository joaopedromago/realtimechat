import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import Classes.Chat;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

public class Main extends ApplicationWindow {
	private Text message;
	private Text server;
	private Text messages;
	private Chat chat;
	private Shell shell;

	/**
	 * Create the application window.
	 */
	public Main() {
		super(null);
		setShellStyle(SWT.MIN | SWT.MAX);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
		chat = new Chat(shell);
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));

		
		Label title = new Label(container, SWT.CENTER);
		title.setBounds(10, 10, 770, 15);
		title.setText("Chat de mensagens, informe um código para acessar um servidor e divirta-se!");
		
		server = new Text(container, SWT.BORDER);
		server.setMessage("Informe o código do servidor que deseja acessar");
		server.setBounds(325, 30, 335, 21);
		
		Button setServer = new Button(container, SWT.NONE);
		setServer.setBounds(670, 30, 110, 22);
		setServer.setText("Aplicar");
		
		messages = new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP );
		messages.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		messages.setBounds(10, 60, 770, 430);

		message = new Text(container, SWT.BORDER);
		message.setMessage("Digite sua mensagem aqui");
		message.setBounds(10, 500, 650, 21);
		
		Button sendMessage = new Button(container, SWT.NONE);
		sendMessage.setBounds(670, 500, 110, 22);
		sendMessage.setText("Enviar");
		sendMessage.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event e) {
				chat.sendMessage(e);
	        }
	      });
		
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Main window = new Main();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setMinimumSize(new Point(800, 600));
		super.configureShell(newShell);
		newShell.setText("Chat");
		shell = newShell;
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(800, 600);
	}
}
