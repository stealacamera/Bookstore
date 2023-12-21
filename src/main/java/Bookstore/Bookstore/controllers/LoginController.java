package Bookstore.Bookstore.controllers;

import Bookstore.Bookstore.startup.Session;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.views.LoginView;

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