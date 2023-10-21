package ui;

import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public interface Form {
	private VBox formatPane(String sceneTitle, GridPane formPane) {
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
	
	default VBox createPane(String title, GridPane formPane, Control controls[], String controlLabels[]) {
		for(int i = 0; i < controls.length; i++) {
			formPane.add(new Label(controlLabels[i]), 0, i);
			formPane.add(controls[i], 1, i);
		}
		
		return formatPane(title, formPane);
	}
}
