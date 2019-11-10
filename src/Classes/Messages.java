package Classes;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class Messages {
	Shell shell;
	
	public Messages(Shell _shell) {
		shell = _shell;
	}

	public void errorMessage(String title, String message) {
		MessageDialog.openError(shell, title, message);
	}

	public void confirmMessage(String title, String message) {
		MessageDialog.openConfirm(shell, title, message);
	}

	public void informationMessage(String title, String message) {
		MessageDialog.openInformation(shell, title, message);
	}

	public void questionMessage(String title, String message) {
		MessageDialog.openQuestion(shell, title, message);
	}

	public void warningMessage(String title, String message) {
		MessageDialog.openWarning(shell, title, message);
	}
}
