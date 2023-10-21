package views.books;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import models.helpers.CustomDate;
import ui.BaseView;
import ui.Form;

public class AddBookView extends BaseView implements Form {
	private VBox pane = new VBox();
	private ListView<String> categoryLv = new ListView<>();
	private Button submitBt = new Button("Submit");
	
	private DatePicker purchasedDateDp = new DatePicker(LocalDate.now());
	private TextField isbnTf = new TextField(), titleTf = new TextField(), authorTf = new TextField(), 
			supplierTf = new TextField(), purchasedPriceTf = new TextField(), originalPriceTf = new TextField(), 
			sellingPriceTf = new TextField(), stockTf = new TextField();
	
	public AddBookView() {
		stockTf.setTextFormatter(getPositiveNumberFormatter());
		createLayout();
	}
	
	private void createLayout() {
		categoryLv.setMaxHeight(100);
		
		pane = createPane("New book form", new GridPane(), 
				new Control[] {isbnTf, titleTf, authorTf, categoryLv, supplierTf, purchasedDateDp, purchasedPriceTf, originalPriceTf, sellingPriceTf, stockTf}, 
				new String[] {"ISBN:", "Title:", "Author:", "Category:", "Supplier:", "Purchased date:", "Purchased price:", "Original price:", "Selling price:", "Stock:"});
		pane.getChildren().add(submitBt);
	}
	
	public Button getSubmitBt() {
		return submitBt;
	}
	
	public void setCategoryLv(ObservableList<String> categories) {
		categoryLv.setItems(categories);
	}
	
	public String getIsbn() {
		return isbnTf.getText();
	}
	
	public String getBookTitle() {
		return titleTf.getText();
	}
	
	public String getAuthor() {
		return authorTf.getText();
	}
	
	public String getCategory() {
		String category = categoryLv.getSelectionModel().getSelectedItem();
		return category == null ? "" : category;
	}
	
	public String getSupplier() {
		return supplierTf.getText();
	}
	
	public String getPurchasedDate() {
		return purchasedDateDp.getValue() == null ? "" : CustomDate.format(purchasedDateDp.getValue());
	}
	
	public String getPurchasedPrice() {
		return purchasedPriceTf.getText();
	}
	
	public String getOriginalPrice() {
		return originalPriceTf.getText();
	}
	
	public String getSellingPrice() {
		return sellingPriceTf.getText();
	}
	
	public int getStock() {
		return stockTf.getText().isBlank() ? 0 : Integer.parseInt(stockTf.getText());
	}

	@Override
	public Pane getView() {
		return pane;
	}
}
