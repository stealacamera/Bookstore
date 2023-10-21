package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import controllers.books.BillController;
import controllers.books.BookController;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.WrongFormatException;
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
import views.ChangePasswordView;
import views.books.AddCategoryView;
import views.books.CreateBillView;
import views.books.ManageBooksView;
import views.employees.ManageEmployeesView;
import views.stats.BookExpensesView;
import views.stats.CashFlowStatsView;
import views.stats.LibrarianPerformanceView;

public class HomepageController {
	private Employee currentUser;
	
	public HomepageController(Employee currentUser) {
		this.currentUser = currentUser;
		
		Employee.setList();
		Bill.setList();
		Book.setList();
		BookPurchase.setList();
		CashFlow.readFromFile();
		CategoryList.setList();
	}
	
	public String getUsername() {
		return currentUser.getUsername();
	}
	
	public int getAccessLvl() {
		return currentUser.getAccessLvl();
	}
	
	public Map<Role, Boolean> getUserPermissionStatus() {
		return Collections.unmodifiableMap(currentUser.getPermissions());
	}
	
	public ArrayList<String> getLowStockBooks() {
		ArrayList<String> bookTitles = new ArrayList<>();
		
		for(Book book: Book.getAll())
			if(book.getStock() <= 5)
				bookTitles.add(book.getTitle());
		
		return bookTitles;
	}	
	
	public void showChangePasswordForm() {
		ChangePasswordView view = new ChangePasswordView();
		Stage newStage = new Stage();
		
		view.getSubmitBt().setOnAction(e -> {
			try {
				if(changePassword(view.getCurrentPassword(), view.getNewPassword()))
					newStage.close();
				else
					view.displayError("Incorrect current password");
			} catch(EmptyInputException | WrongFormatException ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
		
		newStage.setTitle("Change password");
		newStage.setScene(new Scene(view.getView()));
		newStage.showAndWait();
	}
	
	private boolean changePassword(String currentPassword, String newPassword) throws EmptyInputException, WrongFormatException {
		if(currentUser.isCorrectPassword(currentPassword)) {
			currentUser.setPassword(newPassword);
			ListIO.writeToFile(Employee.FILE_NAME, new ArrayList<>(Employee.getAll()));
			
			return true;
		}
		
		return false;
	}
	
	public void showAddCategoryForm() {
		AddCategoryView view = new AddCategoryView();
		Stage newStage = new Stage();
		
		view.getAddBtn().setOnAction(e -> {
			try {
				CategoryList.add(view.getCategory());
				ListIO.writeToFile(CategoryList.FILE_NAME, new ArrayList<>(CategoryList.getAll()));
				view.clearForm();
			} catch(ExistingObjectException ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
		
		newStage.setTitle("Add new category");
		newStage.setScene(new Scene(view.getView()));
		newStage.show();
	}
	
	public void setBtListener(Button button, Role permission, BorderPane mainPane, Button backButton) {
		switch(permission) {
			case CREATE_BILL: 
				button.setOnAction(e -> {
					CreateBillView view = new CreateBillView(new BillController(currentUser));
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Create a bill");
					mainPane.setCenter(view.getView());
					backButton.setVisible(true);
				});
				break;
			case MANAGE_BOOKS:
				button.setOnAction(e -> {
					ManageBooksView view = new ManageBooksView(new BookController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Manage books");
					mainPane.setCenter(view.getView());
					backButton.setVisible(true);
				});
				break;
			case GET_BOOK_STATS:
				button.setOnAction(e -> {
					BookExpensesView view = new BookExpensesView(new StatisticsController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Book cash flow");
					mainPane.setCenter(view.getView());
					backButton.setVisible(true);
				});
				break;
			case GET_REVENUE_STATS: 
				button.setOnAction(e -> {
					CashFlowStatsView view = new CashFlowStatsView(new StatisticsController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Bookstore cash flow");
					mainPane.setCenter(view.getView());
					backButton.setVisible(true);
				});
				break;
			case GET_LIBR_PERFORMANCE: 
				button.setOnAction(e -> {
					LibrarianPerformanceView view = new LibrarianPerformanceView(new StatisticsController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Librarians' performance");
					mainPane.setCenter(view.getView());
					backButton.setVisible(true);
				});
				break;
			default:
				button.setOnAction(e -> {
					ManageEmployeesView view = new ManageEmployeesView(new EmployeesController());
					Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					
					currentStage.setTitle("Manage employees");
					mainPane.setCenter(view.getView());
					backButton.setVisible(true);
				});
			}
	}
}
