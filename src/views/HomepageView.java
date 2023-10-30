package views;

import java.util.List;
import java.util.Map;

import controllers.HomepageController;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.helpers.Role;
import models.helpers.Session;
import ui.BaseView;

public class HomepageView extends BaseView {
	private HomepageController controller;
	private BorderPane pane = new BorderPane();
	private MenuBar menuBar = new MenuBar();
	private Button backBt = new Button("Go back");
	
	public HomepageView(HomepageController controller) {
		this.controller = controller;
		
		setMenuBar();
		createLayout();
		showStockWarning();
	}
	
	private void showStockWarning() {
		if(Session.getAccessLvl() == 2) {
			List<String> bookTitles = controller.getLowStockBooks();
			
			if(bookTitles.size() != 0) {
				Alert stockWarning = new Alert(AlertType.WARNING);
				StringBuilder message = new StringBuilder("These books need to be restocked:\n");
				
				for(String bookTitle: controller.getLowStockBooks())
					message.append(bookTitle + "\n");
				
				stockWarning.setContentText(message.toString());
				stockWarning.showAndWait();
			}
		}
	}
		
	private void createLayout() {	
		Text greetingText = new Text("Welcome back, "), usernameText = new Text(Session.getUsername());
		TextFlow welcomeText = new TextFlow(greetingText, usernameText);
		HBox bottomPane = new HBox(backBt);
		VBox middlePane = new VBox(welcomeText, createButtons());
		
		greetingText.setFont(Font.font(15));
		usernameText.setFont(Font.font(15));
		usernameText.setStyle("-fx-font-weight: bold");			
				
		middlePane.setSpacing(15);
		middlePane.setPadding(new Insets(20));
		bottomPane.setPadding(new Insets(30));
		pane.setPrefWidth(700);
		
		pane.setTop(menuBar);
		pane.setCenter(middlePane);
		pane.setBottom(bottomPane);
		BorderPane.setAlignment(middlePane, Pos.CENTER);
		
		backBt.setVisible(false);
		backBt.setOnAction(e -> {
			Node node = (Node) e.getSource();
			Stage currentStage = (Stage) node.getScene().getWindow();
			
			currentStage.setTitle("Homepage");
			pane.setCenter(middlePane);
			backBt.setVisible(false);
		});
	}
	
	private void setMenuBar() {
		Menu propertiesM = new Menu("Properties");
		MenuItem changePasswordMi = new MenuItem("Change password");
		
		changePasswordMi.setOnAction(e -> controller.showChangePasswordForm());
		
		propertiesM.getItems().add(changePasswordMi);
		menuBar.getMenus().addAll(propertiesM);
		
		if(Session.getAccessLvl() != 1) {
			MenuItem addCategoryMi = new MenuItem("Add new category");
			
			addCategoryMi.setOnAction(e -> controller.showAddCategoryForm());
			propertiesM.getItems().add(addCategoryMi);
		}
	}
	
	private FlowPane createButtons() {
		FlowPane buttonsPane = new FlowPane(Orientation.HORIZONTAL);
		Map<Role, Boolean> userPermissionStatus = Session.getUserPermissionStatus();
		
		for(Map.Entry<Role, Boolean> permission : userPermissionStatus.entrySet()) {
			if(permission.getKey().equals(Role.ADD_NEW_CATEGORY))
				continue;
			
			Button actionBtn = new Button(permission.getKey().toString());
			
			if(permission.getValue())
				controller.setBtListener(actionBtn, permission.getKey(), pane, backBt);
			
			actionBtn.setDisable(!permission.getValue());
			actionBtn.setPrefSize(150, 150);
			actionBtn.setFont(Font.font(15));
			actionBtn.setWrapText(true);
			
			buttonsPane.getChildren().add(actionBtn);
		}
		
		buttonsPane.setAlignment(Pos.CENTER);
		buttonsPane.setHgap(10);
		buttonsPane.setVgap(10);
		return buttonsPane;
	}
}
