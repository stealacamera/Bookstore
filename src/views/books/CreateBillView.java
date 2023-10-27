package views.books;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import controllers.books.BillController;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import models.Book;
import ui.BaseView;

public class CreateBillView extends BaseView {
	private BillController controller;
	private HBox pane = new HBox();
	private TextField quantityTf = new TextField();
	private Button addBt = new Button("Add book"), removeBt = new Button("Remove"), submitBt = new Button("Submit");
	
	private TableView<Book> booksTv = new TableView<>();
	private TableView<Map.Entry<Book, Integer>> billTv = new TableView<>();
	
	public CreateBillView(BillController controller) {
		this.controller = controller;
		quantityTf.setTextFormatter(getPositiveNumberFormatter());
		
		setbooksTv();
		setbillTv();
		setBtListener();
		createLayout();
	}
	
	private void setBtListener() {
		addBt.setOnAction(e -> {			
			try {
				Book bookSelected = booksTv.getSelectionModel().getSelectedItem();
				int quantity = Integer.parseInt(quantityTf.getText());
				
				if(bookSelected == null)
					throw new Exception("Please select a book");
				
				if(!inStock(bookSelected.getStock(), quantity))
					throw new Exception("Not enough stock");
				
				for(Map.Entry<Book, Integer> entry: billTv.getItems())
					if(entry.getKey().equals(bookSelected))
						throw new ExistingObjectException("book entity");
					
				billTv.getItems().add(new AbstractMap.SimpleEntry<Book, Integer>(bookSelected, quantity));
			} catch(NumberFormatException ex) {
				displayError(new EmptyInputException("stock").getLocalizedMessage());
			} catch(Exception ex) {
				displayError(ex.getLocalizedMessage());
			}
		});
				
		submitBt.setOnAction(e -> {
			try {
				controller.submitBill(new ArrayList<>(billTv.getItems()));
				billTv.getItems().clear();
				booksTv.refresh();
			} catch(FileNotFoundException ex) {
				this.displayError("Something went wrong: file could not be created. Try again later");
			} catch (IOException e1) {
				this.displayError("Illegal/unrecognizable character(s) was used");
			}
			
		});
		
		removeBt.setOnAction(e -> billTv.getItems().removeAll(billTv.getSelectionModel().getSelectedItems()));
	}
	
	private void setbooksTv() {
		TableColumn<Book, String> tcIsbn = new TableColumn<>("ISBN");
		TableColumn<Book, String> tcTitle = new TableColumn<>("Title");
		TableColumn<Book, Integer> tcStock = new TableColumn<>("Stock");
		
		tcIsbn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
		tcTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		tcStock.setCellValueFactory(new PropertyValueFactory<Book, Integer>("stock"));
		
		booksTv.getColumns().add(tcIsbn);
		booksTv.getColumns().add(tcTitle);
		booksTv.getColumns().add(tcStock);
		
		booksTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		booksTv.setPrefSize(350, 500);
		booksTv.setItems(controller.getBooks());
	}
	
	private void setbillTv() {		
		TableColumn<Map.Entry<Book, Integer>, String> tcIsbn = new TableColumn<>("ISBN");
		TableColumn<Map.Entry<Book, Integer>, String> tcTitle = new TableColumn<>("Title");
		TableColumn<Map.Entry<Book, Integer>, Integer> tcQuantity = new TableColumn<>("Quantity");
		
		tcIsbn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<Book,Integer>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Entry<Book, Integer>, String> book) {
				return new SimpleStringProperty(book.getValue().getKey().getIsbn());
			}
		});
		
		tcTitle.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<Book,Integer>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Entry<Book, Integer>, String> book) {
				return new SimpleStringProperty(book.getValue().getKey().getTitle());
			}
		});
		
		tcQuantity.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry<Book,Integer>,Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Entry<Book, Integer>, Integer> value) {
				return new SimpleIntegerProperty(value.getValue().getValue()).asObject();
				}
			});
		
		tcQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		
		tcQuantity.setOnEditCommit(e -> {
			try {
				inStock(e.getRowValue().getKey().getStock(), e.getNewValue());
			} catch(Exception ex) {
				billTv.refresh();
				displayError(ex.getLocalizedMessage());
			}
		});
		
		billTv.getColumns().add(tcIsbn);
		billTv.getColumns().add(tcTitle);
		billTv.getColumns().add(tcQuantity);
		
		billTv.setEditable(true);
		billTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		billTv.setPrefSize(350, 500);
	}
	
	private void createLayout() {
		HBox quantityPane = new HBox(quantityTf, addBt), billButtonsPane = new HBox(removeBt, submitBt);
		VBox bookListPane = new VBox(booksTv, quantityPane), billPane = new VBox(billTv, billButtonsPane);
		
		quantityPane.setSpacing(5);
		billButtonsPane.setSpacing(10);
		bookListPane.setSpacing(20);
		billPane.setSpacing(20);
		billButtonsPane.setAlignment(Pos.BASELINE_RIGHT);
		
		pane.setPadding(new Insets(20));
		pane.setSpacing(10);
		pane.getChildren().addAll(bookListPane, billPane);
	}
	
	private boolean inStock(int stock, int requestQuantity) throws NonPositiveInputException {
		if(requestQuantity <= 0)
			throw new NonPositiveInputException("quantity");
		
		return stock < requestQuantity ? false : true;
	}
}
