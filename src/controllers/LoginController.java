package controllers;

import exceptions.EmptyInputException;
import exceptions.WrongFormatException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;
import models.Employee;
import views.LoginView;

public class LoginController {
	private LoginView view;
	private int loggedInUserIndex;
	
	public LoginController(LoginView view) {
		this.view = view;
		this.view.setLoginAction(new LoginBtnListener());
	}
	
	public int getLoggedInUserIndex() {
		return loggedInUserIndex;
	}
	
	private boolean canLogin(String username, String password) throws EmptyInputException, WrongFormatException {
		int index = Employee.findIndex(username);
		
		if(index != -1)
			return Employee.get(index).isCorrectPassword(password);
		
		return false;
	}
	
	class LoginBtnListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			String username = view.getUsername(), password = view.getPassword();
						
			try {
				if(!canLogin(username, password))
					view.displayError("Incorrect username and/or password. Please try again.");
				else {
					loggedInUserIndex = Employee.findIndex(username);
					
					Node node = (Node) e.getSource();
					Stage currentStage = (Stage) node.getScene().getWindow();
					currentStage.close();
				}
			} catch(EmptyInputException | WrongFormatException ex) {
				view.displayError(ex.getLocalizedMessage());					
			}
		}
	}
}