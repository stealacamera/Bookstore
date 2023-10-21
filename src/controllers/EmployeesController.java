package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Employee;
import models.helpers.ListIO;
import views.employees.AddEmployeeView;
import views.employees.ModifyEmployeeView;

public class EmployeesController {
	
	public ObservableList<Employee> getEmployees() {
		return Employee.getAll();
	}
	
	private void add() {
		AddEmployeeView addView = new AddEmployeeView();
		Stage newStage = new Stage();
		
		setSubmitEmployeeListener(addView);
		newStage.setTitle("Add new employee");
		newStage.setScene(new Scene(addView.getView()));
		newStage.initModality(Modality.APPLICATION_MODAL);
		
		newStage.showAndWait();
	}
	
	private void modify(int employeeIndex) {
		ModifyEmployeeView modifyView = new ModifyEmployeeView();
		Stage newStage = new Stage();
		
		Employee employee = Employee.get(employeeIndex);
		
		modifyView.setExistingUsername(employee.getUsername());
		modifyView.setExistingFullName(employee.getFullName());
		modifyView.setExistingEmail(employee.getEmail());
		modifyView.setExistingPhoneNum(employee.getPhoneNum());
		modifyView.setExistingBirthdate(employee.getBirthdate());
		modifyView.setExistingSalary(employee.getSalary());
		modifyView.setExistingPermissions(employee.getPermissions());
			
		modifyView.getSubmitBt().setOnAction(e -> {
			try {
				employee.setUsername(modifyView.getUsername());
				employee.setFullName(modifyView.getFullName());
				employee.setEmail(modifyView.getEmail());
				employee.setPhoneNum(modifyView.getPhoneNum());
				employee.setBirthdate(modifyView.getBirthdate());
				employee.setSalary(modifyView.getSalary());
				employee.setPermissions(modifyView.getPermissions());
				
				Employee.reposition(employeeIndex, employee);
				ListIO.writeToFile(Employee.FILE_NAME, new ArrayList<>(Employee.getAll()));
				newStage.close();
			} catch(NumberFormatException ex) {
					modifyView.displayError("Salary is required to be a decimal number.");
			} catch(EmptyInputException | NonPositiveInputException | WrongFormatException ex) {
				modifyView.displayError(ex.getLocalizedMessage());
			}
		});
		
		newStage.setTitle("Modify employee information");
		newStage.setScene(new Scene(modifyView.getView()));
		newStage.initModality(Modality.APPLICATION_MODAL);
		
		newStage.showAndWait();
	}
	
	private void delete(int index) {
		Employee.remove(index);
		ListIO.writeToFile(Employee.FILE_NAME, new ArrayList<>(Employee.getAll()));
	}
	
	public void setAddListener(ListView<Employee> employeesLv, Button bt) {
		bt.setOnAction(e -> {
			add();
			employeesLv.refresh();
		});
	}
	
	public void setModifyListener(ListView<Employee> employeesLv, Button bt) {
		bt.setOnAction(e -> {
			try {
				modify(employeesLv.getSelectionModel().getSelectedIndex());
				employeesLv.refresh();
			} catch(IndexOutOfBoundsException ex) {
				//do nothing
			}
		});
	}
	
	public void setDeleteListener(ListView<Employee> employeesLv, Button bt) {
		bt.setOnAction(e -> {
			try {
				delete(employeesLv.getSelectionModel().getSelectedIndex());
				employeesLv.refresh();
			} catch(IndexOutOfBoundsException ex) {
				//do nothing
			}
		});
	}
	
	private void setSubmitEmployeeListener(AddEmployeeView view) {
		view.getAddBt().setOnAction(e -> {
			try {	
				Employee.add(new Employee(createNewEmployeeFile(view)));
				ListIO.writeToFile(Employee.FILE_NAME, new ArrayList<>(Employee.getAll()));
				
				Node node = (Node) e.getSource();
				Stage currentStage = (Stage) node.getScene().getWindow();
				currentStage.close();
			} catch(FileNotFoundException | ExistingObjectException | WrongFormatException | EmptyInputException | NonPositiveInputException ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
	}
	
	private File createNewEmployeeFile(AddEmployeeView view) {
		File addEmployeeFile = new File("New Employee.txt");
		
		try(PrintWriter write = new PrintWriter(addEmployeeFile)) {
			write.println(view.getUsername() + "\n" + view.getFullName() + "\n" + view.getEmail() + "\n" + view.getPassword());
			write.println(view.getBirthdate() + "\n" + view.getPhoneNum() + "\n" + view.getSalary() + "\n" + view.getAccessLvl());
		} catch(FileNotFoundException ex) {
			view.displayError(ex.getLocalizedMessage());
		}
		
		return addEmployeeFile;
	}
}
