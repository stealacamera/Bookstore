package views.homepage;

import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import dal.models.utilities.Role;
import startup.Session;
import utils.TriFunction;
import utils.Utils;
import views.IView;

public class HomepageView extends IView {
	private BorderPane pane = new BorderPane();
	private MenuBar menuBar = new MenuBar();
	private Button backBt = new Button("Go back");
	
	private FlowPane buttonsPane = new FlowPane(Orientation.HORIZONTAL);
	private MenuItem changePasswordMi = new MenuItem("Change password"), addCategoryMi = new MenuItem("Add new category");
	
	public HomepageView() {
		setMenuBar();
		createLayout();
		
		super.getChildren().add(pane);
	}
	
	public void showLowStockWarning(List<String> lowStockBookTitles) {
		Alert stockWarning = new Alert(AlertType.WARNING);
		StringBuilder message = new StringBuilder("These books need to be restocked:\n");
		
		for(String bookTitle: lowStockBookTitles)
			message.append(bookTitle + "\n");
		
		stockWarning.setContentText(message.toString());
		stockWarning.showAndWait();
	}
	
	public void addActionBt(Button actionBtn) {
		buttonsPane.getChildren().add(actionBtn);
	}
	
	public void setChangePasswordListener(EventHandler<ActionEvent> action) { changePasswordMi.setOnAction(action); }
	public void setCategoryFormListener(EventHandler<ActionEvent> action) { addCategoryMi.setOnAction(action); }
	
	private void createLayout() {	
		Text greetingText = new Text("Welcome back, "), usernameText = new Text(Session.getCurrentUser().getUsername());
		TextFlow welcomeText = new TextFlow(greetingText, usernameText);
		HBox bottomPane = new HBox(backBt);
		VBox middlePane = new VBox(welcomeText, buttonsPane);
		
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
		
		buttonsPane.setAlignment(Pos.CENTER);
		buttonsPane.setHgap(10);
		buttonsPane.setVgap(10);
		
		backBt.setVisible(false);
		backBt.setOnAction(e -> {
			Utils.getCurrentStage(e).setTitle("Homepage");
			pane.setCenter(middlePane);
			backBt.setVisible(false);
		});
	}
	
	private void setMenuBar() {
		Menu propertiesM = new Menu("Properties");
		propertiesM.getItems().add(changePasswordMi);
		menuBar.getMenus().addAll(propertiesM);
		
		if(Session.getCurrentUser().getAccessLvl() != 1)
			propertiesM.getItems().add(addCategoryMi);
	}
	
	
	public void createButtons(Map<Role, Boolean> userPermissions, TriFunction<Role, BorderPane, Button, EventHandler<ActionEvent>> getBtnListener) {		
		for(Map.Entry<Role, Boolean> permission : userPermissions.entrySet()) {
			if(permission.getKey().equals(Role.ADD_NEW_CATEGORY))
				continue;
			
			Button actionBtn = new Button(permission.getKey().toString());
						
			if(permission.getValue())
				actionBtn.setOnAction(getBtnListener.apply(permission.getKey(), pane, backBt));
			
			actionBtn.setDisable(!permission.getValue());
			actionBtn.setPrefSize(150, 150);
			actionBtn.setFont(Font.font(15));
			actionBtn.setWrapText(true);
			
			buttonsPane.getChildren().add(actionBtn);
		}
		
		
	}
}
