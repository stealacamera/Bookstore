package Bookstore.Bookstore.views;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

abstract public class IView extends Pane {	
	public void displayError(String message) {
		Alert error = new Alert(AlertType.ERROR);		
		Label messageL = new Label(message);
		messageL.setId("alert_error_message");

		Button okButton = (Button) error.getDialogPane().lookupButton(ButtonType.OK);
		okButton.setId("error-ok-btn");
		
		messageL.setWrapText(true);
		error.getDialogPane().setContent(messageL);
		error.show();
	}
}
