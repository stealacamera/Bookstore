package views;

import controllers.HomepageController;
import controllers.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Employee;

public class Main extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Employee.setList();
		
		//Set up login page
		LoginView loginView = new LoginView();
		LoginController loginCtrl = new LoginController(loginView);
		Stage loginStage = new Stage();
		
		loginStage.setTitle("Log in");
		loginStage.setScene(new Scene(loginView.getView()));
		loginStage.showAndWait();
		
		//Set up home page
		int currentUserIndex = loginCtrl.getLoggedInUserIndex();
		HomepageController homepageCtrl = new HomepageController(Employee.get(currentUserIndex));
		HomepageView homepageView = new HomepageView(homepageCtrl);
		
		primaryStage.setTitle("Homepage");
		primaryStage.setScene(new Scene(homepageView.getView()));
		primaryStage.show();
	}
}
