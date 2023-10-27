package views.employees;

import controllers.EmployeesController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import models.Employee;
import ui.BaseView;

public class ManageEmployeesView extends BaseView {
	private EmployeesController controller;
	private VBox pane = new VBox();
	private Button addBt = new Button("Add new employee"), modifyBt = new Button("Modify"), 
			deleteBt = new Button("Delete");
	private ListView<Employee> employeesLv = new ListView<>();
	
	public ManageEmployeesView(EmployeesController controller) {
		this.controller = controller;
		setList();
		createLayout();
	}
	
	private void createLayout() {
		addBt.setPrefHeight(40);
		modifyBt.setPrefHeight(40);
		deleteBt.setPrefHeight(40);
		employeesLv.setPrefWidth(300);
		
		controller.setAddListener(employeesLv, addBt);
		controller.setModifyListener(employeesLv, modifyBt);
		controller.setDeleteListener(employeesLv, deleteBt);
		
		HBox btPane = new HBox(addBt, modifyBt, deleteBt);
		btPane.setSpacing(10);
		btPane.setPrefWidth(200);
		
		pane.getChildren().addAll(btPane, employeesLv);
		pane.setSpacing(20);
		pane.setPadding(new Insets(30));		
	}
	
	private void setList() {
		employeesLv.setItems(controller.getEmployees());
		employeesLv.setFocusTraversable(false);
		
		employeesLv.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
			@Override
			public ListCell<Employee> call(ListView<Employee> arg0) {
				return new ListCell<Employee>() {
					@Override
					protected void updateItem(Employee employee, boolean empty) {
						super.updateItem(employee, empty);
						
						if(empty || employee == null) {
							setText(null);
							setGraphic(null);
						} else {
							if(employee.getAccessLvl() == 3) {
								setDisable(true);
								setTextFill(Color.GRAY);
							}
							
							setText(employee.toString());
						}
					}
				};
			}
		});
	}
}
