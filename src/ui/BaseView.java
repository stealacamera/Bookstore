package ui;

import java.util.function.UnaryOperator;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.util.converter.IntegerStringConverter;

abstract public class BaseView extends Pane {
	public TextFormatter<Integer> getPositiveNumberFormatter() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			String newText = change.getControlNewText();
			
			if(newText.matches("([1-9][0-9]*)?"))
				return change;
			
			return null;
		};
		
		return new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter);
	}
	
	public void displayError(String message) {
		Alert error = new Alert(AlertType.ERROR);
		Label messageL = new Label(message);
		
		messageL.setWrapText(true);
		error.getDialogPane().setContent(messageL);
		error.show();
	}
}
