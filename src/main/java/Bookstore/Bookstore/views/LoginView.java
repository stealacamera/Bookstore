package Bookstore.Bookstore.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

public class LoginView extends IView {
	private VBox pane = new VBox();
	private TextField usernameTf = new TextField();
	private PasswordField passwordTf = new PasswordField();
	private Button loginBt = new Button("Log in");
	
	public LoginView() {
		usernameTf.setId("username");
		passwordTf.setId("password");
		loginBt.setId("loginBtn");
		setId("login-view");
		
		createLayout();
		super.getChildren().add(pane);
	}
		
	private void createLayout() {		
		Text sceneTitle = new Text("Welcome back");		
		Label usernameL = new Label("Username:"), passwordL = new Label("Password:");
		
		VBox usernamePane = new VBox(usernameL, usernameTf), 
				passwordPane = new VBox(passwordL, passwordTf),
				fieldsPane = new VBox(usernamePane, passwordPane),
				nodesPane = new VBox(fieldsPane, loginBt);
		
		sceneTitle.setFont(Font.font(30));
		sceneTitle.setStyle("-fx-font-weight: bold");
		usernameL.setTextFill(Color.GRAY);
		passwordL.setTextFill(Color.GRAY);
				
		usernamePane.setSpacing(5);
		passwordPane.setSpacing(5);
		fieldsPane.setSpacing(10);
		nodesPane.setSpacing(20);
		
		loginBt.setMaxWidth(Double.MAX_VALUE);
		loginBt.setPrefHeight(30);
		
		pane.getChildren().addAll(sceneTitle, nodesPane);
		pane.setPadding(new Insets(50));
		pane.setSpacing(30);
		pane.setAlignment(Pos.CENTER);
	}
	
	public String getUsername() {
		return usernameTf.getText();
	}
	
	public String getPassword() {
		return passwordTf.getText();
	}
		
	public void setLoginAction(EventHandler<ActionEvent> action) {
		loginBt.setOnAction(action);
	}
}