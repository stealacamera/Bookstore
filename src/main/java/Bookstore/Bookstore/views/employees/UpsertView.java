package Bookstore.Bookstore.views.employees;

import java.util.Map;

import Bookstore.Bookstore.commons.utils.Forms;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
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
import Bookstore.Bookstore.dal.models.utils.Role;

public class UpsertView extends IView {
	private int modelId, modelAccessLvl;
	private VBox pane = new VBox();
	
	private DatePicker birthdateDp = new DatePicker();
	private Button submitBtn = new Button("Submit");
	private TextField usernameTf = new TextField(), fullNameTf = new TextField(), 
			emailTf = new TextField(), phoneNumTf = new TextField(), salaryTf = new TextField();
	
	// Specific to insertion
	private PasswordField passwordTf = new PasswordField();
	private ComboBox<Integer> accessLvlMenu = new ComboBox<>();
	
	// Specific to update
	private FlowPane permissionBoxes;
	
	public UpsertView(EmployeeDTO model) {
		this.modelId = model.getId();
		this.modelAccessLvl = model.getAccessLvl();
		
		createLayout(modelId == 0);
		
		if(modelId != 0)
			prefillUpdateForm(model);
		
		super.getChildren().add(pane);
		setInputBoxesIds();
	}
	
	public void setSubmitAction(EventHandler<ActionEvent> action) {
		submitBtn.setOnAction(action);
	}
	
	private void setInputBoxesIds() {
		setId("upsert_employee_view");
		
		birthdateDp.setId("birthdate");
		submitBtn.setId("submit-btn");
		usernameTf.setId("username");
		fullNameTf.setId("full-name");
		emailTf.setId("email");
		phoneNumTf.setId("phone-nr");
		salaryTf.setId("salary");
		passwordTf.setId("password");
		accessLvlMenu.setId("access-lvl");
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
		EmployeeDTO model = new EmployeeDTO();
		
		model.setId(modelId);
		model.setUsername(usernameTf.getText());
		model.setFullName(fullNameTf.getText());
		model.setEmail(emailTf.getText());
		model.setBirthdate(birthdateDp.getValue());		
		model.setPhoneNum(phoneNumTf.getText());
		model.setSalary(salaryTf.getText().isBlank() ? 0 : Double.parseDouble(salaryTf.getText()));

		if(model.getId() == 0) {
			model.setPassword(passwordTf.getText());
			model.setAccessLvl(accessLvlMenu.getSelectionModel().getSelectedItem());
		} else {
			model.setAccessLvl(modelAccessLvl);
			model.setHashedPassword(passwordTf.getText());
			model.setPermissionStatuses(getPermissions());
		}		
		return model;
	}
	
	public void prefillUpdateForm(EmployeeDTO model) {
		if(model.getId() == 0)
			return;
		
		usernameTf.setText(model.getUsername());
		fullNameTf.setText(model.getFullName());
		emailTf.setText(model.getEmail());
		phoneNumTf.setText(model.getPhoneNum());
		birthdateDp.setValue(model.getBirthdate());
		accessLvlMenu.setValue(model.getAccessLvl());
		passwordTf.setText(model.getHashedPassword());
		
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
			box.setId(permission.getKey().name());
			
			permissionBoxes.getChildren().add(box);
		}
	}
}
