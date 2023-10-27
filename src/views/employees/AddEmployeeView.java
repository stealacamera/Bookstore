package views.employees;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.helpers.CustomDate;
import ui.BaseView;
import ui.Form;

public class AddEmployeeView extends BaseView implements Form {
	private VBox pane = new VBox();
	
	private DatePicker birthdateDp = new DatePicker();
	private PasswordField passwordTf = new PasswordField();
	private ComboBox<Integer> accessLvlMenu;
	private Button addBtn = new Button("Add");
	
	private TextField usernameTf = new TextField(), fullNameTf = new TextField(), 
			emailTf = new TextField(), phoneNumTf = new TextField(), salaryTf = new TextField();
	
	public AddEmployeeView() {
		accessLvlMenu = new ComboBox<>();
		accessLvlMenu.getItems().addAll(1, 2);
		accessLvlMenu.setValue(1);
		
		createLayout();
	}
	
	private void createLayout() {
		GridPane formPane = new GridPane();
		
		pane = createPane("New employee form", formPane, 
				new Control[] {usernameTf, fullNameTf, emailTf, passwordTf, phoneNumTf, birthdateDp, salaryTf}, 
				new String[] {"Username:", "Full name:", "Email:", "Password:", "Phone number:", "Birthdate:", "Salary:"});
		
		formPane.add(new Label("Access level:"), 0, formPane.getRowCount());
		formPane.add(accessLvlMenu, 1, formPane.getRowCount() - 1);
		pane.getChildren().add(addBtn);
	}
	
	public void setAddAction(EventHandler<ActionEvent> action) {
		addBtn.setOnAction(action);
	}
	
	public String getUsername() {
		return usernameTf.getText();
	}
	
	public String getFullName() {
		return fullNameTf.getText();
	}
	
	public String getEmail() {
		return emailTf.getText();
	}
	
	public String getPassword() {
		return passwordTf.getText();
	}
	
	public String getPhoneNum() {
		return phoneNumTf.getText();
	}
	
	public String getBirthdate() {
		return birthdateDp.getValue() == null ? "" : CustomDate.format(birthdateDp.getValue());
	}
	
	public double getSalary() {
		return salaryTf.getText().isBlank() ? 0 : Double.parseDouble(salaryTf.getText());
	}
	
	public Integer getAccessLvl() {
		return accessLvlMenu.getSelectionModel().getSelectedItem();
	}
}
