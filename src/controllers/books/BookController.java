package controllers.books;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Book;
import models.BookPurchase;
import models.CashFlow;
import models.CategoryList;
import models.helpers.ListIO;
import views.books.AddBookView;

public class BookController {
	
	public ObservableList<Book> getBooks() {
		return new ReadOnlyListWrapper<>(Book.getAll());
	}
	
	public void setStock(Book book, int stock) throws NonPositiveInputException {
		book.setStock(stock);
		BookPurchase.add(book.getSellingPrice() * stock);
		CashFlow.addBookPurchase(new BookPurchase(book.getSellingPrice() * stock));
		
		ListIO.writeToFile(BookPurchase.FILE_NAME, BookPurchase.getAll());
		ListIO.writeToFile(Book.FILE_NAME, new ArrayList<>(Book.getAll()));
		CashFlow.writeToFile();
	}
	
	public void add() {
		AddBookView	view = new AddBookView();
		Stage newStage = new Stage();
		
		view.setCategoryLv(new ReadOnlyListWrapper<>(FXCollections.observableArrayList(CategoryList.getAll())));
		setSubmitBookListener(view);
		
		newStage.setTitle("Add new category");
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.setScene(new Scene(view.getView()));
		newStage.show();
	}
	
	public void delete(int index) {
		try {
			Book.remove(index);
			ListIO.writeToFile(Book.FILE_NAME, new ArrayList<>(Book.getAll()));
		} catch(IndexOutOfBoundsException ex) {
			//do nothing
		}
	}
	
	private void setSubmitBookListener(AddBookView view) {
		view.getSubmitBt().setOnAction(e -> {
			try {
				Book.add(new Book(createBookInputFile(view)));
				ListIO.writeToFile(Book.FILE_NAME, new ArrayList<>(Book.getAll()));
				
				double purchaseCost = Double.parseDouble(view.getSellingPrice()) * view.getStock();
				BookPurchase.add(purchaseCost);
				ListIO.writeToFile(BookPurchase.FILE_NAME, BookPurchase.getAll());
				
				CashFlow.addBookPurchase(new BookPurchase(purchaseCost));				
				CashFlow.writeToFile();
				
				Node node = (Node) e.getSource();
				Stage currentStage = (Stage) node.getScene().getWindow();
				currentStage.close();
			} catch(FileNotFoundException | ExistingObjectException | EmptyInputException | WrongFormatException | NonPositiveInputException ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
	}
	
	private File createBookInputFile(AddBookView view) {
		File newBookFile = new File("New Book.txt");
		
		try(PrintWriter write = new PrintWriter(newBookFile)) {
			write.println(view.getIsbn() + "\n" + view.getBookTitle() + "\n" + view.getAuthor());
			write.println(view.getCategory() + "\n" + view.getSupplier() + "\n" + view.getPurchasedDate());
			write.println(view.getPurchasedPrice() + "\n" + view.getOriginalPrice() + "\n" + view.getSellingPrice() + "\n" + view.getStock());
		} catch(FileNotFoundException ex) {
			view.displayError(ex.getLocalizedMessage());
		}
		
		return newBookFile;
	}
}
