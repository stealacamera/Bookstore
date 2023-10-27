package views.books;

import controllers.books.BookController;
import exceptions.NonPositiveInputException;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;
import models.Book;
import ui.BaseView;

public class ManageBooksView extends BaseView {
	private BookController controller;
	private VBox pane = new VBox();
	private Button addBt = new Button("Add new book"), deleteBt = new Button("Delete");
	
	private TableView<Book> booksTv = new TableView<Book>();
	private TableColumn<Book, String> isbnTc = new TableColumn<>("ISBN"), titleTc = new TableColumn<>("Title");
	private TableColumn<Book, Integer> tcStock = new TableColumn<>("Stock");
	
	public ManageBooksView(BookController controller) {
		this.controller = controller;
		setTvBooks();
		setBtListeners();
		createLayout();
	}
	
	private void setTvBooks() {		
		isbnTc.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
		titleTc.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		
		tcStock.setCellValueFactory(new PropertyValueFactory<Book, Integer>("stock"));
		tcStock.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		tcStock.setOnEditCommit(e -> {
			try {
				controller.setStock(e.getRowValue(), e.getNewValue());
			} catch(NonPositiveInputException ex) {
				displayError(ex.getLocalizedMessage());
			}
		});
		
		booksTv.getColumns().add(isbnTc);
		booksTv.getColumns().add(titleTc);
		booksTv.getColumns().add(tcStock);
		booksTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		booksTv.setPrefWidth(350);
		booksTv.setEditable(true);
		booksTv.setItems(controller.getBooks());
	}
	
	private void setBtListeners() {
		addBt.setOnAction(e -> controller.add());
		deleteBt.setOnAction(e -> controller.delete(booksTv.getSelectionModel().getSelectedIndex()));
	}
	
	private void createLayout() {
		addBt.setPrefHeight(40);
		deleteBt.setPrefHeight(40);
		
		HBox btPane = new HBox(addBt, deleteBt);
		btPane.setSpacing(10);
		
		Text instructionTxt = new Text("*Double-click on a book's stock to edit stock.");
		instructionTxt.setStyle("-fx-font-style: italic");
		
		pane.getChildren().addAll(btPane, booksTv, instructionTxt);
		pane.setSpacing(20);
		pane.setPadding(new Insets(30));
	}
}
