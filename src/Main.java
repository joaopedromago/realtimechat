import java.util.UUID;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import Classes.Chat;
import Classes.Messages;
import Server.JMSCliente;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;

public class Main extends ApplicationWindow {
	private Text message;
	private Text recipient;
	private Text messages;
	private Chat chat;
	private Shell shell;
	private Text yourName;
	private Messages messagesLib;
	JMSCliente cliente;

	/**
	 * Create the application window.
	 */
	public Main() {
		super(null);

		setShellStyle(SWT.MIN | SWT.MAX);
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();

		// inicializando o servidor lado cliente
		cliente = new JMSCliente();
		cliente.setMyCode(yourName.getText());
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		UUID generatedName = UUID.randomUUID();

		Display display = Display.getDefault();
		Color backgroundColor = new Color(display, new RGB(255, 255, 255));
		Color messagesColor = new Color(display, new RGB(240, 240, 240));

		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(backgroundColor);

		Label title = new Label(container, SWT.CENTER);
		title.setBackground(backgroundColor);
		title.setBounds(10, 0, 770, 15);
		title.setText("Chat de mensagens, envie uma mensagem utilizando o servidor e divirta-se!");

		Label yourNameInfo = new Label(container, SWT.CENTER);
		yourNameInfo.setBackground(backgroundColor);
		yourNameInfo.setBounds(10, 30, 100, 15);
		yourNameInfo.setText("Nome exibição: ");

		yourName = new Text(container, SWT.BORDER);
		yourName.setText(generatedName.toString());
		yourName.setMessage("Informe seu nome");
		yourName.setBounds(120, 30, 190, 21);

		messages = new Text(container, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		messages.setBackground(messagesColor);
		messages.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		messages.setBounds(10, 60, 770, 430);

		recipient = new Text(container, SWT.BORDER);
		recipient.setMessage("Destinatário");
		recipient.setBounds(10, 500, 190, 21);
		recipient.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					message.forceFocus();
				}
			}
		});

		message = new Text(container, SWT.BORDER);
		message.setMessage("Digite sua mensagem aqui");
		message.setBounds(210, 500, 450, 21);
		message.forceFocus();
		message.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					chat.sendMessage();
				}
			}
		});

		Button sendMessage = new Button(container, SWT.NONE);
		sendMessage.setBounds(670, 500, 110, 22);
		sendMessage.setText("Enviar");
		sendMessage.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				chat.sendMessage();
			}
		});

		// creating classes instances
		chat = new Chat(shell, message, recipient, messages, yourName);
		messagesLib = new Messages(shell);

		messagesLib.information("Seu identificador único para receber mensagens privadas",
				generatedName.toString() + ", você pode altera-lo quando quiser no campo superior.");

		return container;
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
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
	 * 
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
