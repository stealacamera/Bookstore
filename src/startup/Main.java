package startup;

import bll.BillService;
import bll.BookInventoryService;
import bll.BookPurchaseService;
import bll.CategoryService;
import bll.EmployeeService;
import bll.IServices.IBillService;
import bll.IServices.IBookInventoryService;
import bll.IServices.IBookPurchaseService;
import bll.IServices.ICategoryService;
import bll.IServices.IEmployeeService;
import controllers.HomepageController;
import controllers.LoginController;
import dal.BillRepository;
import dal.BookInventoryRepository;
import dal.BookPurchaseRepository;
import dal.CategoryRepository;
import dal.DbContext;
import dal.EmployeeRepository;
import dal.IRepositories.IBillRepository;
import dal.IRepositories.IBookInventoryRepository;
import dal.IRepositories.IBookPurchaseRepository;
import dal.IRepositories.ICategoryRepository;
import dal.IRepositories.IEmployeeRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
		Injector.addDependency(IBillRepository.class, new BillRepository(Injector.getDbContext()));
		Injector.addDependency(IBookPurchaseRepository.class, new BookPurchaseRepository(Injector.getDbContext()));
		Injector.addDependency(IBookInventoryRepository.class, new BookInventoryRepository(Injector.getDbContext()));
		Injector.addDependency(ICategoryRepository.class, new CategoryRepository(Injector.getDbContext()));
		Injector.addDependency(IEmployeeRepository.class, new EmployeeRepository(Injector.getDbContext()));
		
		// Services
		Injector.addDependency(IBillService.class, new BillService(Injector.getDependency(IBillRepository.class)));
		Injector.addDependency(IBookPurchaseService.class, new BookPurchaseService(Injector.getDependency(IBookPurchaseRepository.class)));
		Injector.addDependency(IBookInventoryService.class, new BookInventoryService(Injector.getDependency(IBookInventoryRepository.class)));
		Injector.addDependency(ICategoryService.class, new CategoryService(Injector.getDependency(ICategoryRepository.class)));
		Injector.addDependency(IEmployeeService.class, new EmployeeService(Injector.getDependency(IEmployeeRepository.class)));
	}
}
