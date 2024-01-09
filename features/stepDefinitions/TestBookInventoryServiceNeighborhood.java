import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TestBookInventoryServiceNeighborhood {
	private static String tempDataDirPath = Utils.testingUserDataDirPath;
	private IBookInventoryRepository repository = new BookInventoryRepository(tempDataDirPath, new DbContext());
	private IBookInventoryService service = new BookInventoryService(repository);
	
	private ArrayList<BookInventory> data;
	private BookInventoryDTO dtoModelInUse;
	
	@AfterAll
	public static void deleteFolder() {
		File dataDir = new File(tempDataDirPath);
		File parentDir = dataDir.getParentFile();
		
		dataDir.delete();
		parentDir.delete();
	}
	
	@After
	public void deleteData() {
		new File(tempDataDirPath).delete();
		repository = new EmployeeRepository(tempDataDirPath, new DbContext());
	}
	
	@Given("these existing Books saved in the database")
	public void these_existing_employees_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			try {
				Book book = new Book(
						data.get("isbn"), data.get("title"), 
						data.get("author"), data.get("supplier"), 
						Integer.parseInt(data.get("phone number"))
					);
					
					BookInventory BI = new BookInventory(book, Double.parseDouble(data.get("purchasedPrice")),Double.parseDouble(data.get("originalPrice")),Double.parseDouble(data.get("sellingPrice")),Integer.parseInt(data.get("stock")),new CustomDate(data.get("purchasedDate")) );
					
				repository.add(BI);
				this.data.add(BI);
			} catch (EmptyInputException | NonPositiveInputException | WrongFormatException e) {
				e.printStackTrace();
			}
		}
	}
	
	@When("All Books Requested")
	public void All_Books_Requested() {
		data.add(service.getAll());
	}
	@Then("Return all titles")
	public void All_titles_returned() {
		for (int i=0;i<data.size;i++) {
			BookInventory book1 = service.get(i);
		    AssertEquals(data.getTitle(),book1.getTitle())
		}
	}
	@Given("ISBN {string} of a book")
	public void Given_ISBN_OF_BOOK(String isbn) {
		dtoModelInUse=service.getByIsbn(isbn);
	}
	@Then("return title {string} of the book")
	public void Return_title_ofBook(String title) {
		assertEquals(title,dtoModelInUse.getTitle());
	}
	@Given("a valid DTO of {string} {string} {string} {string} {string} {string} {string} {string} {string} {string}")
	public void a_Valid_dto(String isbn,String title,String author,String supplier ,String categoryId,String purchasedPrice,String originalPrice,String sellingPrice,String stock,String purchasedDate){
		BookTDO book = new BookTDO (isbn,title,author,supplier,Integer.parseInt(categoryId));
		dtoModelInUse = new BookInventory(book,Double.parseDouble(data.get("purchasedPrice")),Double.parseDouble(data.get("originalPrice")),Double.parseDouble(data.get("sellingPrice")),Integer.parseInt(data.get("stock")),new CustomDate(data.get("purchasedDate")));
	}
	@Then("add item to inventory")
	public void add_item_to_inventory(){
		repository.saveChanges();
	    assertNotNull(service.getByIsbn(dtoModelInUse.getIsbn()));
	}
	@Then("update the title to {string}")
	public void Udpate_title_to_string(String title){
		BookInventory model = dtoModelInUse.getIsbn();
		BookDTO b = new BookDTO(model.getIsbn(),model.getTitle(),model.getAuthor(),model.getSupplier(),model.getCatgoryId());
		BookInventoryDTO binv = new BookInventoryDTO(model.getBook(),model.getPurchasedPrice(),model.getOriginalPrice(),model.getSellingPrice(),model.getStock(),model.getDate());
		dtoModelInUse = new BookInventoryDTO(binv);
		dtoModelInUse.setTitle(title);
		service.update(binv,dtoModelInUse);
	}
	
	@Then("update stock to {string}")
	public void Update_stock_to_string(String stock) {
		int parseStock=Integer.parseInt(stock);
		BookInventory model = dtoModelInUse.getIsbn();
		BookDTO b = new BookDTO(model.getIsbn(),model.getTitle(),model.getAuthor(),model.getSupplier(),model.getCatgoryId());
		BookInventoryDTO binv = new BookInventoryDTO(model.getBook(),model.getPurchasedPrice(),model.getOriginalPrice(),model.getSellingPrice(),model.getStock(),model.getDate());
		dtoModelInUse = new BookInventoryDTO(binv);
		dtoModelInUse.setStock(stock);
		service.update(binv,dtoModelInUse);
	}
	@Then("remove the book from database")
	public void Remove_book_from_db(){
		service.remove(dtoModelInUse);
	}
	@Then("Save the removal")
	public void Save_The_Removal() {
		repository.saveChanges();
	    assertNull(service.getByIsbn(dtoModelInUse.getIsbn()));
	}

}
