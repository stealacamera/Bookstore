package Bookstore.Bookstore.integration.serviceNeighborhoods.stepDefinitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.services.BookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.BookPurchase;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.BookPurchaseRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.irepositories.IBookPurchaseRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestBookPurchaseServiceNeighborhood {
	private IBookPurchaseRepository repository = new BookPurchaseRepository(Utils.testDataDirPath, new DbContext());
	private IBookPurchaseService service = new BookPurchaseService(repository);

	private BookPurchaseDTO dtoModelInUse;
	private List<BookPurchaseDTO> dtos;

	@Before
	public void recreateDatabase() {		
		repository = new BookPurchaseRepository(Utils.testDataDirPath, new DbContext());
		service = new BookPurchaseService(repository);
	}
	
	@After
	public void tearDownDatabase() {
		TestingUtils.deleteTestDatabase(null);
	}
	
	@Given("these existing purchases saved in the database")
	public void these_existing_purchases_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) throws Exception {
		List<Map<String, String>> rowData = dataTable.asMaps();
		
		for(Map<String, String> data: rowData) {
			BookPurchase bookpurchase = new BookPurchase(Integer.parseInt(data.get("amount")), new CustomDate(data.get("date")));
			repository.add(bookpurchase);
		}
	}

	@When("all purchases requested")
	public void all_purchases_requested() {
	    dtos = service.getAll();
	}

	@Then("purchases {string} {string}, {string} {string}, {string} {string} should be returned")
	public void purchases_should_be_returned(String amount1, String date1, String amount2, String date2, String amount3, String date3) {
	    List<BookPurchaseDTO> expectedData = Arrays.asList(
    		new BookPurchaseDTO(Double.parseDouble(amount1), LocalDate.parse(date1, CustomDate.dateFormat)),
    		new BookPurchaseDTO(Double.parseDouble(amount2), LocalDate.parse(date2, CustomDate.dateFormat)),
    		new BookPurchaseDTO(Double.parseDouble(amount3), LocalDate.parse(date3, CustomDate.dateFormat))
		);
		
	    assertIterableEquals(expectedData, dtos);
	}

	@When("amount {string} is given")
	public void amount_is_given(String amount) throws Exception {
	    dtoModelInUse = new BookPurchaseDTO(Double.parseDouble(amount), LocalDate.now());
	    service.add(dtoModelInUse);
	}

	@Then("new book purchase should be saved in database with the current date")
	public void new_book_purchase_should_be_saved_in_database_with_the_current_date() throws Exception {
		BookPurchase expectedEntity = new BookPurchase(dtoModelInUse.getAmount());
		
		assertEquals(expectedEntity, repository.get(repository.count() - 1));
	}
}
