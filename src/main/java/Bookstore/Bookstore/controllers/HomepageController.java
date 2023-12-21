package Bookstore.Bookstore.controllers;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.dal.models.utils.Role;
import Bookstore.Bookstore.startup.Session;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.views.homepage.AddCategoryView;
import Bookstore.Bookstore.views.homepage.ChangePasswordView;
import Bookstore.Bookstore.views.homepage.HomepageView;

public class HomepageController {
	private IBookInventoryService bookInventoryService;
	private ICategoryService categoryService;
	private IBillService billService;
	private IBookPurchaseService bookPurchaseService;
	private IEmployeeService employeeService;
	
	public HomepageController(
			IBookInventoryService bookInventoryService, ICategoryService categoryService,
			IBillService billService, IBookPurchaseService bookPurchaseService,
			IEmployeeService employeeService) {
		this.bookInventoryService = bookInventoryService;
		this.categoryService = categoryService;
		this.billService = billService;
		this.bookPurchaseService = bookPurchaseService;
		this.employeeService = employeeService;
	}
	
	public IView getIndexView() {
		HomepageView view = new HomepageView();
		
		List<String> lowStockBookTitles = getLowStockBooks();
		if(Session.getCurrentUser().getAccessLvl() == 2 && lowStockBookTitles.size() != 0)
			view.showLowStockWarning(getLowStockBooks());
		
		view.setChangePasswordListener(e -> Utils.createPopupStage("Change password", getChangePasswordView()).showAndWait());
		view.setCategoryFormListener(e -> Utils.createPopupStage("Add new category", getAddCategoryView()).showAndWait());
		
		view.createButtons(
			Session.getCurrentUser().getPermissions(),
			(permission, pane, goBackBtn) -> {
				Map.Entry<String, IView> result = getHomeActionView(permission);
				return (e -> {
					if(view != null) {
						Utils.getCurrentStage(e).setTitle(result.getKey());
						pane.setCenter(result.getValue());
						goBackBtn.setVisible(true);
					}
				});			
			}
		);
		
		return view;
	}
	
	private List<String> getLowStockBooks() {
		ArrayList<String> bookTitles = new ArrayList<>();
		
		for(BookInventoryDTO book: bookInventoryService.getAll())
			if(book.getStock() <= 5)
				bookTitles.add(book.getBook().getTitle());
		
		return Collections.unmodifiableList(bookTitles);
	}
	
	private IView getChangePasswordView() {
		ChangePasswordView view = new ChangePasswordView();

		view.setSubmitAction(e -> {
			try {				
				if(employeeService.changePassword(new EmployeeDTO(Session.getCurrentUser()), view.getCurrentPassword(), view.getNewPassword()))
					Utils.getCurrentStage(e).close();
				else
					view.displayError("Incorrect current password");
			} catch(EmptyInputException | NonPositiveInputException | WrongFormatException | WrongLengthException
					| IncorrectPermissionsException ex) {
				view.displayError(ex.getLocalizedMessage());
			}
		});
		
		return view;
	}
	
	private IView getAddCategoryView() {
		AddCategoryView view = new AddCategoryView();
		
		view.setAddAction(e -> {
			try {
				categoryService.add(view.submitForm());
				view.clearForm();
			} catch(ExistingObjectException | EmptyInputException | NonPositiveInputException | WrongFormatException | WrongLengthException
					| IncorrectPermissionsException ex) {
				view.displayError(ex.getMessage());
			}
		});

		return view;
	}
	
	private Map.Entry<String, IView> getHomeActionView(Role permission) {
		String viewTitle = null;
		IView view = null;
		
		switch(permission) {
			case CREATE_BILL:
				view = new BillController(billService, bookInventoryService).getCreateView();
				viewTitle = "Create a bill";
				break;
			case MANAGE_BOOKS:
				view = new BookController(bookInventoryService, bookPurchaseService, categoryService).getIndexView();
				viewTitle = "Manage books";
				break;
			case GET_BOOK_STATS:
				view = new StatisticsController(billService, employeeService, bookPurchaseService).getBookExpensesView();
				viewTitle = "Book cash flow";
				break;
			case GET_REVENUE_STATS:
				view = new StatisticsController(billService, employeeService, bookPurchaseService).getCashFlowStatsView();
				viewTitle = "Bookstore cash flow";
				break;
			case GET_LIBR_PERFORMANCE:
				view = new StatisticsController(billService, employeeService, bookPurchaseService).getLibrarianPerformanceView();
				viewTitle = "Librarians' performance";
				break;
			case MANAGE_EMPLOYEES:
				view = new EmployeesController(employeeService).getIndexView();
				viewTitle = "Manage employees";
				break;
			default: break;
		}
		
		return new AbstractMap.SimpleEntry<String, IView>(viewTitle, view);
	}
}
