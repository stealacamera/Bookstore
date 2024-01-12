package Bookstore.Bookstore.views.books;

import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

public class ManageBooksView extends IView {
	private VBox pane = new VBox();
	private Button addBt = new Button("Add new book"), deleteBt = new Button("Delete");
	
	private TableView<BookInventoryDTO> booksTv = new TableView<>();
	private TableColumn<BookInventoryDTO, String> isbnTc = new TableColumn<>("ISBN"), titleTc = new TableColumn<>("Title");
	private TableColumn<BookInventoryDTO, Integer> tcStock = new TableColumn<>("Stock");
	
	public ManageBooksView(ObservableList<BookInventoryDTO> books) {
		addBt.setId("add-btn");
		booksTv.setId("books-list");
		deleteBt.setId("delete-btn");
		
		setTvBooks(books);
		createLayout();
		super.getChildren().add(pane);
	}
		
	public void setStockListener(EventHandler<CellEditEvent<BookInventoryDTO, Integer>> action) {
		tcStock.setOnEditCommit(action);
	}
	
	public void setAddListener(EventHandler<ActionEvent> action) {
		addBt.setOnAction(action);
	}
	
	public void setDeleteListener(EventHandler<ActionEvent> action) {
		deleteBt.setOnAction(action);
	}
	
	public void refreshTable(ObservableList<BookInventoryDTO> books) {
		if(books != null)
			booksTv.setItems(books);
			
		booksTv.refresh();
	}
	
	public int getSelectedIndex() {
		return booksTv.getSelectionModel().getSelectedIndex();
	}
	
	private void setTvBooks(ObservableList<BookInventoryDTO> books) {		
		isbnTc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getBook().getIsbn()));
		titleTc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getBook().getTitle()));
		
		tcStock.setCellValueFactory(new PropertyValueFactory<BookInventoryDTO, Integer>("stock"));
		tcStock.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		
		booksTv.getColumns().add(isbnTc);
		booksTv.getColumns().add(titleTc);
		booksTv.getColumns().add(tcStock);
		booksTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		
		booksTv.setPrefWidth(350);
		booksTv.setEditable(true);
		booksTv.setItems(books);
	}
	
	private void createLayout() {
		addBt.setPrefHeight(40);
		deleteBt.setPrefHeight(40);
		
		HBox btPane = new HBox(addBt, deleteBt);
		btPane.setSpacing(10);
		
		Text instructionTxt = new Text("*Double-click on a book's stock to edit stock. If stock is increased, it is assumed as a new purchase.");
		instructionTxt.setStyle("-fx-font-style: italic");
		
		pane.getChildren().addAll(btPane, booksTv, instructionTxt);
		pane.setSpacing(20);
		pane.setPadding(new Insets(30));
	}
}
