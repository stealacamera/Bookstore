package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controllers.books.BillController;
import controllers.books.BookController;
import exceptions.ExistingObjectException;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Bill;
import models.Book;
import models.BookPurchase;
import models.CashFlow;
import models.CategoryList;
import models.Employee;
import models.helpers.ListIO;
import models.helpers.Role;
import models.helpers.Session;
import views.ChangePasswordView;
import views.books.AddCategoryView;
import views.books.CreateBillView;
import views.books.ManageBooksView;
import views.employees.ManageEmployeesView;
import views.stats.BookExpensesView;
import views.stats.CashFlowStatsView;
import views.stats.LibrarianPerformanceView;

public class HomepageController {
	
	public HomepageController() {
		Employee.setList();
		Bill.setList();
		Book.setList();
		BookPurchase.setList();
		CashFlow.readFromFile();
		CategoryList.setList();
	}
	
	public List<String> getLowStockBooks() {
		ArrayList<String> bookTitles = new ArrayList<>();
		
		for(Book book: Book.getAll())
			if(book.getStock() <= 5)
				bookTitles.add(book.getTitle());
		
		return Collections.unmodifiableList(bookTitles);
	}	
	
	public void showChangePasswordForm() {
		ChangePasswordView view = new ChangePasswordView();
		Stage newStage = new Stage();

		view.setSubmitAction(e -> {
			try {				
				if(Session.changePassword(view.getCurrentPassword(), view.getNewPassword()))
					newStage.close();
				else
					view.displayError("Incorrect current password");
			} catch(Exception ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
		
		newStage.setTitle("Change password");
		newStage.setScene(new Scene(view));
		newStage.showAndWait();
	}
	
	public void showAddCategoryForm() {
		AddCategoryView view = new AddCategoryView();
		Stage newStage = new Stage();
		
		view.setAddAction(e -> {
			try {
				CategoryList.add(view.getCategory());
				ListIO.writeToFile(CategoryList.FILE_NAME, new ArrayList<>(CategoryList.getAll()));
				view.clearForm();
			} catch(ExistingObjectException ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
		
		newStage.setTitle("Add new category");
		newStage.setScene(new Scene(view));
		newStage.show();
	}
	
	public void setBtListener(Button button, Role permission, BorderPane mainPane, Button backButton) {
		switch(permission) {
			case CREATE_BILL: 
				button.setOnAction(e -> {
					CreateBillView view = new CreateBillView(new BillController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Create a bill");
					mainPane.setCenter(view);
					backButton.setVisible(true);
				});
				break;
			case MANAGE_BOOKS:
				button.setOnAction(e -> {
					ManageBooksView view = new ManageBooksView(new BookController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Manage books");
					mainPane.setCenter(view);
					backButton.setVisible(true);
				});
				break;
			case GET_BOOK_STATS:
				button.setOnAction(e -> {
					BookExpensesView view = new BookExpensesView(new StatisticsController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Book cash flow");
					mainPane.setCenter(view);
					backButton.setVisible(true);
				});
				break;
			case GET_REVENUE_STATS: 
				button.setOnAction(e -> {
					CashFlowStatsView view = new CashFlowStatsView(new StatisticsController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Bookstore cash flow");
					mainPane.setCenter(view);
					backButton.setVisible(true);
				});
				break;
			case GET_LIBR_PERFORMANCE: 
				button.setOnAction(e -> {
					LibrarianPerformanceView view = new LibrarianPerformanceView(new StatisticsController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Librarians' performance");
					mainPane.setCenter(view);
					backButton.setVisible(true);
				});
				break;
			default:
				button.setOnAction(e -> {
					ManageEmployeesView view = new ManageEmployeesView(new EmployeesController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Manage employees");
					mainPane.setCenter(view);
					backButton.setVisible(true);
				});
			}
	}
}
