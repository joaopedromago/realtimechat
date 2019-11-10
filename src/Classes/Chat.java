package Classes;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Chat {
	Messages messagesLib;
	Shell shell;
	Text messages;
	Text message;
	Text server;
	Text yourName;

	public Chat(Shell _shell, Text _message, Text _server, Text _messages, Text _yourName) {
		shell = _shell;
		messagesLib = new Messages(shell);
		message = _message;
		server = _server;
		messages = _messages;
		yourName = _yourName;
	}

	public void sendMessage() {
		if (yourName.getText().length() == 0) {
			messagesLib.error("Error!", "Você deve informar um nome de exibição antes de enviar uma mensagem!");
			return;
		}
		if (message.getText().length() > 0) {
			messages.append(yourName.getText() + ":\n	" + message.getText() + "\n");
			message.setText("");
		}
		message.forceFocus();
	}

	public void setServer() {
		messagesLib.confirm("Sucesso!", "Servidor setado com sucesso!");
		message.forceFocus();
	}
}
