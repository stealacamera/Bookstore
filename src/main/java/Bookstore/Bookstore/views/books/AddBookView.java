package Bookstore.Bookstore.views.books;

import java.time.LocalDate;

import Bookstore.Bookstore.commons.utils.Forms;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.CategoryDTO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class AddBookView extends IView {
	private VBox pane = new VBox();
	private ListView<CategoryDTO> categoryLv = new ListView<>();
	private Button submitBt = new Button("Submit");
	
	private DatePicker purchasedDateDp = new DatePicker(LocalDate.now());
	private TextField isbnTf = new TextField(), titleTf = new TextField(), authorTf = new TextField(), 
			supplierTf = new TextField(), purchasedPriceTf = new TextField(), originalPriceTf = new TextField(), 
			sellingPriceTf = new TextField(), stockTf = new TextField();
	
	public AddBookView() {
		stockTf.setTextFormatter(Forms.getPositiveNumberFormatter());
		purchasedPriceTf.setTextFormatter(Forms.getDecimalNumberFormatter());
		originalPriceTf.setTextFormatter(Forms.getDecimalNumberFormatter());
		sellingPriceTf.setTextFormatter(Forms.getDecimalNumberFormatter());
		
		createLayout();
		super.getChildren().add(pane);
	}
	
	private void createLayout() {		
		Control[] fields = {isbnTf, titleTf, authorTf, categoryLv, supplierTf, purchasedDateDp, purchasedPriceTf, originalPriceTf, sellingPriceTf, stockTf};
		String[] fieldLabels = {"ISBN:", "Title:", "Author:", "Category:", "Supplier:", "Purchased date:", "Purchased price:", "Original price:", "Selling price:", "Stock:"};
		
		categoryLv.setMaxHeight(100);
		pane = Forms.createPane("New book form", new GridPane(), fields, fieldLabels);
		pane.getChildren().add(submitBt);
	}
	
	public BookInventoryDTO submitValues() {
		CategoryDTO selectedCategory = categoryLv.getSelectionModel().getSelectedItem();
		
		BookDTO instanceBase = new BookDTO(
				isbnTf.getText(), titleTf.getText(), 
				authorTf.getText(), supplierTf.getText(), 
				selectedCategory == null ? 0 : selectedCategory.getId());
		
		BookInventoryDTO instance = new BookInventoryDTO(
				instanceBase, Double.parseDouble(purchasedPriceTf.getText()), 
				Double.parseDouble(originalPriceTf.getText()), Double.parseDouble(sellingPriceTf.getText()),
				Integer.parseInt(stockTf.getText()), purchasedDateDp.getValue());
		
		return instance;
	}
	
	public void setSubmitAction(EventHandler<ActionEvent> action) {
		submitBt.setOnAction(action);
	}
	
	public void setCategoryLv(ObservableList<CategoryDTO> categories) {
		categoryLv.setItems(categories);
	}
}
