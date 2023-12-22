package Bookstore.Bookstore.views;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Bookstore.Bookstore.commons.utils.Forms;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

public class CreateBillView extends IView {
	private HBox pane = new HBox();
	private TextField quantityTf = new TextField();
	private Button addBt = new Button("Add book"), removeBt = new Button("Remove"), submitBt = new Button("Submit");
	
	private TableView<BookInventoryDTO> booksTv = new TableView<>();
	private TableView<Map.Entry<BookInventoryDTO, Integer>> billTv = new TableView<>();
	TableColumn<Map.Entry<BookInventoryDTO, Integer>, Integer> tcQuantity = new TableColumn<>("Quantity");
	
	public CreateBillView(ObservableList<BookInventoryDTO> books) {
		quantityTf.setTextFormatter(Forms.getPositiveNumberFormatter());
		
		setbooksTv(books);
		setbillTv();
		createLayout();
		
		super.getChildren().add(pane);
	}
	
	public void setInsertListener(EventHandler<ActionEvent> action) { addBt.setOnAction(action); }
	public void setSubmitBillListener(EventHandler<ActionEvent> action) { submitBt.setOnAction(action); }
	public void setDeleteListener(EventHandler<ActionEvent> action) { removeBt.setOnAction(action); }
	
	public void setBillItemStockListener(EventHandler<CellEditEvent<Entry<BookInventoryDTO, Integer>, Integer>> action) {
		tcQuantity.setOnEditCommit(action);
	}
	
	public int getQuantity() { return Integer.parseInt(quantityTf.getText()); }
	
	public int getSelectedBillItemIndex() { return billTv.getSelectionModel().getSelectedIndex(); }
	
	public BookInventoryDTO getSelectedBookItem() { return booksTv.getSelectionModel().getSelectedItem(); }
	
	public List<Map.Entry<BookInventoryDTO, Integer>> getBill() { return billTv.getItems(); }
	
	public boolean billContainsBook(BookInventoryDTO instance) {
		for(Map.Entry<BookInventoryDTO, Integer> entry: billTv.getItems())
			if(entry.getKey().equals(instance))
				return true;
		
		return false;
	}
	
	public void addItemToBill(BookInventoryDTO instance, int quantity) { 
		billTv.getItems().add(new AbstractMap.SimpleEntry<BookInventoryDTO, Integer>(instance, quantity)); 
	}
	
	public void removeBillItem(int index) {
		billTv.getItems().remove(index);
	}
	
	public void refreshBillView() {
		billTv.refresh();
	}
	
	public void refreshView(ObservableList<BookInventoryDTO> list) {
		billTv.getItems().clear();
		booksTv.getItems().clear();
		booksTv.setItems(list);
	}
	
	private void setbooksTv(ObservableList<BookInventoryDTO> books) {
		TableColumn<BookInventoryDTO, String> tcIsbn = new TableColumn<>("ISBN");
		TableColumn<BookInventoryDTO, String> tcTitle = new TableColumn<>("Title");
		TableColumn<BookInventoryDTO, Integer> tcStock = new TableColumn<>("Stock");
		
		tcIsbn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getBook().getIsbn()));
		tcTitle.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getBook().getTitle()));
		tcStock.setCellValueFactory(new PropertyValueFactory<BookInventoryDTO, Integer>("stock"));
		
		booksTv.getColumns().add(tcIsbn);
		booksTv.getColumns().add(tcTitle);
		booksTv.getColumns().add(tcStock);
		
		booksTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		booksTv.setPrefSize(350, 500);
		booksTv.setItems(books);
	}
	
	private void setbillTv() {
		TableColumn<Map.Entry<BookInventoryDTO, Integer>, String> tcIsbn = new TableColumn<>("ISBN");
		TableColumn<Map.Entry<BookInventoryDTO, Integer>, String> tcTitle = new TableColumn<>("Title");

		tcIsbn.setCellValueFactory(new BookISBNTableColumnCellFactory());		
		tcTitle.setCellValueFactory(new BookTitleTableColumnCellFactory());
		tcQuantity.setCellValueFactory(new BookQuantityTableColumnCellFactory());
		
		tcQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		
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
	
	private static class BookISBNTableColumnCellFactory implements Callback<TableColumn.CellDataFeatures<Entry<BookInventoryDTO,Integer>,String>, ObservableValue<String>> {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Entry<BookInventoryDTO, Integer>, String> book) {
			return new SimpleStringProperty(book.getValue().getKey().getBook().getIsbn());
		}
	}
	
	private static class BookTitleTableColumnCellFactory implements Callback<TableColumn.CellDataFeatures<Entry<BookInventoryDTO,Integer>, String>, ObservableValue<String>> {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Entry<BookInventoryDTO, Integer>, String> book) {
			return new SimpleStringProperty(book.getValue().getKey().getBook().getTitle());
		}
	}
	
	private static class BookQuantityTableColumnCellFactory implements Callback<TableColumn.CellDataFeatures<Entry<BookInventoryDTO,Integer>,Integer>, ObservableValue<Integer>> {
		@Override
		public ObservableValue<Integer> call(CellDataFeatures<Entry<BookInventoryDTO, Integer>, Integer> value) {
			return new SimpleIntegerProperty(value.getValue().getValue()).asObject();
		}
	}
}
