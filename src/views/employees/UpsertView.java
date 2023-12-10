package views.employees;

import java.util.Map;

import bll.dto.EmployeeDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.utilities.CustomDate;
import models.utilities.Role;
import utils.Forms;
import views.IView;

public class UpsertView extends IView {
	private EmployeeDTO model;
	private VBox pane = new VBox();
	
	private DatePicker birthdateDp = new DatePicker();
	private Button submitBtn = new Button("Submit");
	private TextField usernameTf = new TextField(), fullNameTf = new TextField(), 
			emailTf = new TextField(), phoneNumTf = new TextField(), salaryTf = new TextField();
	
	// Specific to insertion
	private PasswordField passwordTf;
	private ComboBox<Integer> accessLvlMenu;
	
	// Specific to update
	private FlowPane permissionBoxes;
	
	public UpsertView(EmployeeDTO model) {
		this.model = model;
		createLayout(model.getId() == 0);
		
		if(model.getId() != 0)
			prefillUpdateForm(model);
		
		getChildren().add(pane);
	}
	
	public void setSubmitAction(EventHandler<ActionEvent> action) {
		submitBtn.setOnAction(action);
	}
	
	private void createLayout(boolean toInsert) {
		GridPane formPane = new GridPane();
		salaryTf.setTextFormatter(Forms.getDecimalNumberFormatter());
		

		Control[] fields = {usernameTf, fullNameTf, emailTf, salaryTf, phoneNumTf, birthdateDp};
		String[] fieldLabels = {"Username:", "Full name:", "Email:", "Salary:", "Phone number:", "Birthdate:"};
				
		if(toInsert) {
			passwordTf = new PasswordField();
			
			Control[] fieldsUpdated = new Control[fields.length + 1];
			String[] fieldLabelsUpdated = new String[fieldLabels.length + 1];
			
			System.arraycopy(fields, 0, fieldsUpdated, 0, fields.length);
			fieldsUpdated[fieldsUpdated.length - 1] = passwordTf;
			
			System.arraycopy(fieldLabels, 0, fieldLabelsUpdated, 0, fieldLabels.length);
			fieldLabelsUpdated[fieldLabelsUpdated.length - 1] = "Password:";
						
			pane = Forms.createPane("New employee form", formPane, fieldsUpdated, fieldLabelsUpdated);
			
			// Setting access level menu
			accessLvlMenu = new ComboBox<>();
			accessLvlMenu.getItems().addAll(1, 2);
			accessLvlMenu.setValue(1);
						
			formPane.add(new Label("Access level:"), 0, formPane.getRowCount());
			formPane.add(accessLvlMenu, 1, formPane.getRowCount() - 1);
		} else {
			pane = Forms.createPane("Editing employee", formPane, fields, fieldLabels);
			permissionBoxes = new FlowPane();
			
			permissionBoxes.setHgap(10);
			permissionBoxes.setVgap(5);
			permissionBoxes.setPrefWidth(200);
			
			formPane.add(new Label("Permissions:"), 0, formPane.getRowCount());
			formPane.add(permissionBoxes, 1, formPane.getRowCount() - 1);
			GridPane.setRowSpan(permissionBoxes, GridPane.REMAINING);
		}
		
		pane.getChildren().add(submitBtn);
	}
	
	public EmployeeDTO submitValues() {
		model.setUsername(usernameTf.getText());
		model.setFullName(fullNameTf.getText());
		model.setEmail(emailTf.getText());
		model.setPhoneNum(phoneNumTf.getText());
		model.setBirthdate(birthdateDp.getValue() == null ? null : new CustomDate(birthdateDp.getValue()));		
		model.setSalary(salaryTf.getText().isBlank() ? 0 : Double.parseDouble(salaryTf.getText()));
				
		if(model.getId() == 0) {
			model.setPassword(passwordTf.getText());
			model.setAccessLvl(accessLvlMenu.getSelectionModel().getSelectedItem());
		} else
			model.setPermissionStatuses(getPermissions());
				
		return model;
	}
	
	public void prefillUpdateForm(EmployeeDTO model) {
		if(model.getId() == 0)
			return;
		
		usernameTf.setText(model.getUsername());
		fullNameTf.setText(model.getFullName());
		emailTf.setText(model.getEmail());
		phoneNumTf.setText(model.getPhoneNum());
		birthdateDp.setValue(model.getBirthdate().getDate());
		
		salaryTf.setText(model.getSalary() + "");
		setExistingPermissions(model.getPermissions());
	}
	
	private boolean[] getPermissions() {
		boolean[] permissionBoxesValues = new boolean[permissionBoxes.getChildren().size()];
		
		for(int i = 0; i < permissionBoxesValues.length; i++)
			permissionBoxesValues[i] = ((CheckBox)(permissionBoxes.getChildren().get(i))).isSelected();
		
		return permissionBoxesValues;
	}
	
	private void setExistingPermissions(Map<Role, Boolean> userPermissions) {
		for(Map.Entry<Role, Boolean> permission: userPermissions.entrySet()) {
			CheckBox box = new CheckBox(permission.getKey().toString());
			box.setSelected(permission.getValue());
			
			permissionBoxes.getChildren().add(box);
		}
	}
}
