package Bookstore.Bookstore.integration.serviceNeighborhoods.stepDefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BookDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.services.BookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.Book;
import Bookstore.Bookstore.dal.models.BookInventory;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.BookInventoryRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.irepositories.IBookInventoryRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class TestBookInventoryServiceNeighborhood {
	private IBookInventoryRepository repository = new BookInventoryRepository(Utils.testDataDirPath, new DbContext());
	private IBookInventoryService service = new BookInventoryService(repository);
	
	private BookInventoryDTO dtoModelInUse;
	private BookInventory expectedEntity;
	
	@Before
	public void recreateDatabase() {		
		repository = new BookInventoryRepository(Utils.testDataDirPath, new DbContext());
		service = new BookInventoryService(repository);
	}
	
	@After
	public void tearDownDatabase() {
		TestingUtils.deleteTestDatabase(null);
	}
	
	@Given("these existing books saved in the database")
	public void these_existing_books_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		
		for(Map<String, String> data: rowData) {
			try {
				Book book = new Book(
						data.get("isbn"), data.get("title"), 
						data.get("author"), data.get("supplier"), 
						Integer.parseInt(data.get("categoryId"))
					);
					
				BookInventory bookInventory = new BookInventory(
					book, 
					Double.parseDouble(
						data.get("purchasedPrice")),
						Double.parseDouble(data.get("originalPrice")),
						Double.parseDouble(data.get("sellingPrice")),
						Integer.parseInt(data.get("stock")),
						new CustomDate(data.get("purchasedDate")
					)
				);
				
				repository.add(bookInventory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Given("ISBN {string} of a book")
	public void isbn_of_a_book(String isbn) throws Exception {
	    dtoModelInUse = service.getByISBN(isbn);
	}
	
	@Then("return book with title {string}, author {string}, supplier {string}, category id {string}, purchased price {string}, original price {string}, selling price {string}, stock {string}, purchased date {string}")
	public void return_book_with_title_author_supplier_category_id_purchased_price_original_price_selling_price_stock_purchased_date(String title, String author, String supplier, String categoryId, String purchasedPrice, String originalPrice, String sellingPrice, String stock, String purchasedDate) {
	    BookInventoryDTO expectedModel = new BookInventoryDTO(
    		new BookDTO(dtoModelInUse.getBook().getIsbn(), title, author, supplier, Integer.parseInt(categoryId)), 
    		Double.parseDouble(purchasedPrice), 
    		Double.parseDouble(originalPrice), 
    		Double.parseDouble(sellingPrice), 
    		Integer.parseInt(stock),
    		LocalDate.parse(purchasedDate, CustomDate.dateFormat)
		);
	    
		assertEquals(expectedModel, dtoModelInUse);
	}

	@Given("a valid DTO of {string} {string} {string} {string} {string} {string} {string} {string} {string} {string}")
	public void a_valid_dto_of(String isbn, String title, String author, String supplier, String categoryId, String purchasedPrice, String originalPrice, String sellingPrice, String stock, String purchasedDate) throws Exception {
	    int categoryIdParsed = Integer.parseInt(categoryId),
	    	stockParsed = Integer.parseInt(stock);
	    
		double purchasedPriceParsed = Double.parseDouble(purchasedPrice),
			originalPriceParsed = Double.parseDouble(originalPrice),
			sellingPriceParsed = Double.parseDouble(sellingPrice);
	    
		LocalDate purchasedDateParsed = LocalDate.parse(purchasedDate, CustomDate.dateFormat);
		
		dtoModelInUse = new BookInventoryDTO(
	    	new BookDTO(isbn, title, author, supplier, categoryIdParsed), 
    		purchasedPriceParsed, 
    		originalPriceParsed, 
    		sellingPriceParsed, 
    		stockParsed, 
    		purchasedDateParsed
		);
	    
	    expectedEntity = new BookInventory(
    		new Book(isbn, title, author, supplier, categoryIdParsed), 
    		purchasedPriceParsed, 
    		originalPriceParsed, 
    		sellingPriceParsed, 
    		stockParsed, 
    		new CustomDate(purchasedDateParsed)
		);
	}

	@Then("the entity should be saved in the database")
	public void the_entity_should_be_saved_in_the_database() throws Exception {
	    service.add(dtoModelInUse);
	    
	    repository.saveChanges();
	    assertEquals(expectedEntity, repository.getByISBN(dtoModelInUse.getBook().getIsbn()));
	}

	@Given("an ISBN {string} of a book")
	public void an_isbn_of_a_book(String isbn) throws Exception {
	    dtoModelInUse = service.getByISBN(isbn);
	    
	    BookDTO dtoBase = dtoModelInUse.getBook();
	    expectedEntity = new BookInventory(
    		new Book(isbn, dtoBase.getTitle(), dtoBase.getAuthor(), dtoBase.getSupplier(), dtoBase.getCategoryId()), 
    		dtoModelInUse.getPurchasedPrice(), 
    		dtoModelInUse.getOriginalPrice(), 
    		dtoModelInUse.getSellingPrice(), 
    		dtoModelInUse.getStock(), 
    		new CustomDate(dtoModelInUse.getPurchasedDate())
		);
	}

	@Then("update the stock to {string}")
	public void update_the_stock_to(String stock) throws Exception {
		int stockParsed = Integer.parseInt(stock);
		
		service.updateStock(dtoModelInUse, stockParsed);
		expectedEntity.setStock(stockParsed);
	}

	@Then("the stock of the database entity should be updated")
	public void the_stock_of_the_database_entity_should_be_updated() throws Exception {
		repository.saveChanges();
		
		assertEquals(expectedEntity, repository.getByISBN(dtoModelInUse.getBook().getIsbn()));
	}
}
