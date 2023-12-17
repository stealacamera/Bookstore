package controllers;

import bll.IServices.IEmployeeService;
import exceptions.EmptyInputException;
import startup.Session;
import utils.Utils;
import views.IView;
import views.LoginView;

public class LoginController {
	private IEmployeeService employeeService;
	
	public LoginController(IEmployeeService employeeService) {
		this.employeeService = employeeService;		
	}
	
	public IView getIndexView() {
		LoginView view = new LoginView();
		
		view.setLoginAction(e -> {
			String username = view.getUsername(), password = view.getPassword();
			
			try {
				if(!employeeService.canLogin(username, password))
					view.displayError("Incorrect username and/or password. Please try again.");
				else {
					Session.setCurrentUser(employeeService.getByUsername(username));
					Utils.getCurrentStage(e).close();
				}
			} catch(EmptyInputException ex) {
				view.displayError(ex.getLocalizedMessage());					
			}
		});
		
		return view;
	}
}