package views.homepage;

import bll.dto.CategoryDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import views.IView;

public class AddCategoryView extends IView {
	private VBox pane = new VBox();
	private TextField categoryTf = new TextField();
	private Button addBt = new Button("Add");
	
	public AddCategoryView() {
		createLayout();
		super.getChildren().add(pane);
	}
	
	private void createLayout() {
		Label categoryL = new Label("Category:");
		VBox fieldsPane = new VBox(categoryL, categoryTf);
		
		categoryL.setTextFill(Color.GRAY);		
		fieldsPane.setSpacing(5);
		
		addBt.setPrefHeight(30);
		addBt.setMaxWidth(Double.MAX_VALUE);
		
		pane.getChildren().addAll(fieldsPane, addBt);
		pane.setSpacing(20);
		pane.setPadding(new Insets(30));
	}

	public void clearForm() {
		categoryTf.clear();
	}
	
	public CategoryDTO submitForm() {
		return new CategoryDTO(0, categoryTf.getText());
	}
	
	public void setAddAction(EventHandler<ActionEvent> action) {
		addBt.setOnAction(action);
	}
}
