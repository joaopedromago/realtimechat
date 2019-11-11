package Classes;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Chat {
	Messages messagesLib;
	Shell shell;
	Text messages;
	Text message;
	Text recipient;
	Text yourName;

	public Chat(Shell _shell, Text _message, Text _recipient, Text _messages, Text _yourName) {
		shell = _shell;
		messagesLib = new Messages(shell);
		message = _message;
		recipient = _recipient;
		messages = _messages;
		yourName = _yourName;
	}

	public void sendMessage() {
		String recipientName = recipient.getText();
		String yourNameText = yourName.getText();
		String messageText = message.getText();
		String additional = "";

		if (recipientName.length() > 0) {
			additional += " (enviado para " + recipientName + ")";
		} else {
			additional += " (enviado para todos)";
		}

		if (yourNameText.length() == 0) {
			messagesLib.error("Error!", "Você deve informar um nome de exibição antes de enviar uma mensagem!");
			return;
		}
		if (messageText.length() > 0) {
			messages.append(yourName.getText() + additional + ":\n	" + message.getText() + "\n");
			message.setText("");
		}
		message.forceFocus();
	}
}
