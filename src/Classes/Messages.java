package Classes;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class Messages {
	Shell shell;

	public Messages(Shell _shell) {
		shell = _shell;
	}

	public void error(String title, String message) {
		MessageDialog.openError(shell, title, message);
	}

	public void confirm(String title, String message) {
		MessageDialog.openConfirm(shell, title, message);
	}

	public void information(String title, String message) {
		MessageDialog.openInformation(shell, title, message);
	}

	public void question(String title, String message) {
		MessageDialog.openQuestion(shell, title, message);
	}

	public void warning(String title, String message) {
		MessageDialog.openWarning(shell, title, message);
	}
}
