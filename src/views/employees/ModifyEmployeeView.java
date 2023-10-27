package views.employees;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.helpers.CustomDate;
import models.helpers.Role;
import ui.BaseView;
import ui.Form;

public class ModifyEmployeeView extends BaseView implements Form {
	private VBox pane = new VBox();
	private FlowPane permissionBoxes = new FlowPane();	
	private Button submitBt = new Button("Submit changes");
	
	private DatePicker birthdateDp = new DatePicker();
	private TextField usernameTf = new TextField(), fullNameTf = new TextField(), 
			emailTf = new TextField(), phoneNumTf = new TextField(), salaryTf = new TextField();
	
	public ModifyEmployeeView() {
		createLayout();
	}
	
	private void createLayout() {
		permissionBoxes.setHgap(10);
		permissionBoxes.setVgap(5);
		permissionBoxes.setPrefWidth(200);
		
		GridPane formPane = new GridPane();
		
		pane = createPane("Editing employee", formPane, new Control[] {usernameTf, fullNameTf, emailTf, phoneNumTf, birthdateDp, salaryTf}, 
				new String[] {"Username:", "Full name:", "Email:", "Phone number:", "Birthdate:", "Salary:"});
		
		formPane.add(new Label("Permissions:"), 0, formPane.getRowCount());
		formPane.add(permissionBoxes, 1, formPane.getRowCount() - 1);
		GridPane.setRowSpan(permissionBoxes, GridPane.REMAINING);
		
		pane.getChildren().add(submitBt);
	}
	
	public void setExistingUsername(String username) {
		usernameTf.setText(username);
	}
	
	public void setExistingFullName(String fullName) {
		fullNameTf.setText(fullName);
	}
	
	public void setExistingEmail(String email) {
		emailTf.setText(email);
	}
	
	public void setExistingPhoneNum(String phoneNum) {
		phoneNumTf.setText(phoneNum);
	}
	
	public void setExistingBirthdate(LocalDate birthdate) {
		birthdateDp.setValue(birthdate);
	}
	
	public void setExistingSalary(double salary) {
		salaryTf.setText(salary + "");
	}
	
	public void setExistingPermissions(HashMap<Role, Boolean> userPermissions) {		
		for(Map.Entry<Role, Boolean> permission: userPermissions.entrySet()) {
			CheckBox box = new CheckBox(permission.getKey().toString());
			box.setSelected(permission.getValue());
			
			permissionBoxes.getChildren().add(box);
		}
	}
	
	public void setSubmitAction(EventHandler<ActionEvent> action) {
		submitBt.setOnAction(action);
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
	
	public String getPhoneNum() {
		return phoneNumTf.getText();
	}
	
	public String getBirthdate() {
		return birthdateDp.getValue() == null ? "" : CustomDate.format(birthdateDp.getValue());
	}
	
	public double getSalary() {
		return salaryTf.getText().isBlank() ? 0 : Double.parseDouble(salaryTf.getText());
	}
	
	public boolean[] getPermissions() {
		boolean[] permissionBoxesValues = new boolean[permissionBoxes.getChildren().size()];
		
		for(int i = 0; i < permissionBoxesValues.length; i++)
			permissionBoxesValues[i] = ((CheckBox)(permissionBoxes.getChildren().get(i))).isSelected();
		
		return permissionBoxesValues;
	}
}
