package views.books;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ui.BaseView;

public class AddCategoryView extends BaseView {
	private VBox pane = new VBox();
	private TextField categoryTf = new TextField();
	private Button addBt = new Button("Add");
	
	public AddCategoryView() {
		createLayout();
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
	
	public String getCategory() {
		return categoryTf.getText();
	}
	
	public Button getAddBtn() {
		return addBt;
	}

	@Override
	public Pane getView() {
		return pane;
	}
}
