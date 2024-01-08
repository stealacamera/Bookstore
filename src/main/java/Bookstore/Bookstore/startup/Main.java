package Bookstore.Bookstore.startup;

import java.time.LocalDate;

import Bookstore.Bookstore.bll.dto.CategoryDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.CategoryService;
import Bookstore.Bookstore.bll.services.EmployeeService;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.controllers.HomepageController;
import Bookstore.Bookstore.controllers.LoginController;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.CategoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.EmployeeRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IBillRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IBookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IBookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.ICategoryRepository;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
		
	public static void main(String[] args) {
		registerDependencies();
		seedData(
			Injector.getDependency(IEmployeeService.class), 
			Injector.getDependency(ICategoryService.class)
		);
		
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		//Set up login page
		LoginController loginCtrl = new LoginController(Injector.getDependency(IEmployeeService.class));
		
		Stage loginStage = new Stage();
		loginStage.setTitle("Log in"); 
		loginStage.setScene(new Scene(loginCtrl.getIndexView()));
		loginStage.showAndWait();
		
		if(Session.getCurrentUser() == null)
			return;
		
		HomepageController homepageCtrl = new HomepageController(
				Injector.getDependency(IBookInventoryService.class),
				Injector.getDependency(ICategoryService.class),
				Injector.getDependency(IBillService.class),
				Injector.getDependency(IBookPurchaseService.class),
				Injector.getDependency(IEmployeeService.class));
				
		primaryStage.setTitle("Homepage");
		primaryStage.setScene(new Scene(homepageCtrl.getIndexView()));
		primaryStage.show();
	}
	
	public static void registerDependencies() {
		Injector.addDbContext(new DbContext());
		
		// Repositories
		Injector.addDependency(IBillRepository.class, new BillRepository(Utils.dataDirPath, Injector.getDbContext()));
		Injector.addDependency(IBookPurchaseRepository.class, new BookPurchaseRepository(Utils.dataDirPath, Injector.getDbContext()));
		Injector.addDependency(IBookInventoryRepository.class, new BookInventoryRepository(Utils.dataDirPath, Injector.getDbContext()));
		Injector.addDependency(ICategoryRepository.class, new CategoryRepository(Utils.dataDirPath, Injector.getDbContext()));
		Injector.addDependency(IEmployeeRepository.class, new EmployeeRepository(Utils.dataDirPath, Injector.getDbContext()));
		
		// Services
		Injector.addDependency(IBillService.class, new BillService(Injector.getDependency(IBillRepository.class)));
		Injector.addDependency(IBookPurchaseService.class, new BookPurchaseService(Injector.getDependency(IBookPurchaseRepository.class)));
		Injector.addDependency(IBookInventoryService.class, new BookInventoryService(Injector.getDependency(IBookInventoryRepository.class)));
		Injector.addDependency(ICategoryService.class, new CategoryService(Injector.getDependency(ICategoryRepository.class)));
		Injector.addDependency(IEmployeeService.class, new EmployeeService(Injector.getDependency(IEmployeeRepository.class)));
	}
	
	public static void seedData(IEmployeeService employeeService, ICategoryService categoryService) {		
		if(employeeService.getAll().size() == 0) {
			UserDTO librarian, manager, admin;
			
			try {
				librarian = new UserDTO("librarian", "Name Surname", "librarian@gmail.com", "Password123", "069 123 1233", LocalDate.now());
				manager = new UserDTO("manager", "Name Surname", "manager@gmail.com", "Password123", "069 123 3123", LocalDate.now());
				admin = new UserDTO("admin", "Name Surname", "admin@gmail.com", "Password123", "069 123 1323", LocalDate.now());
				
				employeeService.add(new EmployeeDTO(admin, 1400.89, 3));
				employeeService.add(new EmployeeDTO(librarian, 500, 1));
				employeeService.add(new EmployeeDTO(manager, 655.45, 2));
			} catch (EmptyInputException | NonPositiveInputException | WrongFormatException | ExistingObjectException | WrongLengthException | IncorrectPermissionsException e) {
				e.printStackTrace();
			}
		}
		
		if(categoryService.getAll().size() == 0) {
			try {
				categoryService.add(new CategoryDTO("Drama"));
				categoryService.add(new CategoryDTO("Comedy"));
				categoryService.add(new CategoryDTO("Science fiction"));
				categoryService.add(new CategoryDTO("Mystery"));
				categoryService.add(new CategoryDTO("Horror"));
				categoryService.add(new CategoryDTO("Romance"));
				categoryService.add(new CategoryDTO("Fantasy"));
			} catch (EmptyInputException | ExistingObjectException | NonPositiveInputException | WrongFormatException | WrongLengthException | IncorrectPermissionsException e) {
				e.printStackTrace();
			}
		}
	}
}
