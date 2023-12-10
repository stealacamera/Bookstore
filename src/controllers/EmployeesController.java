package controllers;

import bll.IServices.IEmployeeService;
import bll.dto.EmployeeDTO;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import utils.Utils;
import views.IView;
import views.employees.ManageEmployeesView;
import views.employees.UpsertView;

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
