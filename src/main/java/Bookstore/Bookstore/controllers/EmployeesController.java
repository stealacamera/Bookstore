package Bookstore.Bookstore.controllers;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.views.employees.ManageEmployeesView;
import Bookstore.Bookstore.views.employees.UpsertView;

public class EmployeesController {
	private IEmployeeService employeeService;
	
	public EmployeesController(IEmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	public IView getIndexView() {
		ManageEmployeesView view = new ManageEmployeesView(employeeService.getAll());
		
		view.setInsertListener(e -> {
			Utils.createPopupStage("Add new employee", getInsertView()).showAndWait();
			view.refreshList(employeeService.getAll());
		});
		
		view.setModifyListener(e -> {
			try {
				EmployeeDTO model = view.getSelectedItem();
				
				if(model == null)
					throw new Exception("Please select an employee");
				
				Utils.createPopupStage("Modify employee information", getModifyView(model)).showAndWait();
				view.refreshList(employeeService.getAll());
			} catch(Exception ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		view.setDeleteListener(e -> {
			try {
				int index = view.getSelectedIndex();
				
				if(index == -1)
					throw new Exception("Please select an employee");
				
				employeeService.remove(index);
				view.refreshList(employeeService.getAll());
			} catch(Exception ex) {
				view.displayError(ex.getMessage());
			} 
		});
		
		return view;
	}
	
	public IView getInsertView() {
		UpsertView view = new UpsertView(new EmployeeDTO());
		
		view.setSubmitAction(e -> {
			try {
				employeeService.add(view.submitValues());
				Utils.getCurrentStage(e).close();
			} catch (ExistingObjectException | EmptyInputException | NonPositiveInputException | WrongFormatException
					| WrongLengthException | IncorrectPermissionsException exc) {
				view.displayError(exc.getMessage());
			}
		});
		
		return view;
	}
	
	public IView getModifyView(EmployeeDTO employee) {
		UpsertView view = new UpsertView(employee);
		
		view.setSubmitAction(e -> {
			try {
				employeeService.update(employee, view.submitValues());
				Utils.getCurrentStage(e).close();
			} catch (ExistingObjectException | EmptyInputException | NonPositiveInputException
					| WrongFormatException | WrongLengthException | IncorrectPermissionsException exc) {
				view.displayError(exc.getMessage());
			}
		});
		
		return view;
	}
}
