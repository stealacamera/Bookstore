package utils;

import java.util.function.UnaryOperator;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Forms {
	public static TextFormatter<Integer> getPositiveNumberFormatter() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			String newText = change.getControlNewText();
			return newText.matches("([1-9][0-9]*)?") ? change : null;
		};
		
		return new TextFormatter<Integer>(new IntegerStringConverter(), 0, filter);
	}
	
	public static TextFormatter<Double> getDecimalNumberFormatter() {
		UnaryOperator<TextFormatter.Change> filter = change -> {
			String newText = change.getControlNewText();
			return newText.matches("\\d*(\\.\\d*)?") ? change : null;
		};
		
		return new TextFormatter<Double>(new DoubleStringConverter(), 0.0, filter);
	}
	
	public static VBox createPane(String title, GridPane formPane, Control[] fields, String[] fieldLabels) {		
		for(int i = 0; i < fields.length; i++) {
			formPane.add(new Label(fieldLabels[i]), 0, i);
			formPane.add(fields[i], 1, i);
		}
				
		return formatPane(title, formPane);
	}
	
	private static VBox formatPane(String sceneTitle, GridPane formPane) {
		Text titleTxt = new Text(sceneTitle);
		titleTxt.setFont(Font.font(20));
		
		formPane.setHgap(10);
		formPane.setVgap(5);
		
		VBox pane = new VBox();
		pane.getChildren().addAll(titleTxt, formPane);
		pane.setPadding(new Insets(30, 60, 30, 60));
		pane.setSpacing(20);
		
		return pane; 
	}
}
