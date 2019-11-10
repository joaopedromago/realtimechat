package Classes;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class Chat {
	private Messages messages;
	Shell shell;
	
	public Chat(Shell _shell) {
		shell = _shell;
		messages = new Messages(shell);
	}
	
	public void sendMessage(Event e) {
		messages.confirmMessage("Suesso", "Mensagem enviada com sucesso!");
	}
}
