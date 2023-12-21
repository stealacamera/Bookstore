package Bookstore.Bookstore.startup;

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
import Bookstore.Bookstore.commons.utils.Utils;

public class Main extends Application {
		
	public static void main(String[] args) {
		startUp();
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
	
	public static void startUp() {
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
}
