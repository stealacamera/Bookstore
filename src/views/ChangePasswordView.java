package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ui.BaseView;

public class ChangePasswordView extends BaseView {
	private VBox pane = new VBox();
	private PasswordField currentPasswordTf = new PasswordField(), newPasswordTf = new PasswordField();
	private Button submitBt = new Button("Submit");
	
	public ChangePasswordView() {
		createLayout();
	}
	
	private void createLayout() {
		Label currentPasswordL = new Label("Current password:"), newPasswordL = new Label("New password:");
		currentPasswordL.setTextFill(Color.GRAY);
		newPasswordL.setTextFill(Color.GRAY);
		
		VBox currentPasswordPane = new VBox(currentPasswordL, currentPasswordTf), 
				newPasswordPane = new VBox(newPasswordL, newPasswordTf),
				fieldsPane = new VBox(currentPasswordPane, newPasswordPane);
		
		currentPasswordPane.setSpacing(5);
		newPasswordPane.setSpacing(5);
		fieldsPane.setSpacing(10);
		
		submitBt.setMaxWidth(Double.MAX_VALUE);
		submitBt.setPrefHeight(30);
		
		pane.getChildren().addAll(fieldsPane, submitBt);
		pane.setPadding(new Insets(30));
		pane.setSpacing(20);
		pane.setAlignment(Pos.CENTER);
	}
	
	public String getCurrentPassword() {
		return currentPasswordTf.getText();
	}
	
	public String getNewPassword() {
		return newPasswordTf.getText();
	}
	
	public void setSubmitAction(EventHandler<ActionEvent> action) {
		submitBt.setOnAction(action);
	}
}
