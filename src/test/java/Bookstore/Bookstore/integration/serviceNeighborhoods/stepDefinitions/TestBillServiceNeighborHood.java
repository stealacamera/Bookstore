package Bookstore.Bookstore.integration.serviceNeighborhoods.stepDefinitions;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.TestingUtils;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.services.BillService;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.dal.models.Bill;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.BillRepository;
import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.irepositories.IBillRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class TestBillServiceNeighborHood {
	private IBillRepository repository = new BillRepository(Utils.testDataDirPath, new DbContext());
	private IBillService service = new BillService(repository);
	
	private List<Bill> data;
	private List<BillDTO> dtos;
	private BillDTO dtoModelInUse;
	
	@Before
	public void recreateDatabase() {		
		repository = new BillRepository(Utils.testDataDirPath, new DbContext());
		service = new BillService(repository);
	}
	
	@After
	public void tearDownDatabase() {
		TestingUtils.deleteTestDatabase(null);
	}
	
	@Given("these existing bills saved in the database")
	public void these_existing_bills_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			try {
				Bill bill = new Bill(
					Integer.parseInt(data.get("sellerId")),
					Double.parseDouble(data.get("saleAmount")),
					Integer.parseInt(data.get("numOfBooks")), 
					new CustomDate(data.get("date"))
				);
				
				repository.add(bill);
				this.data.add(bill);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@When("all bills requested")
	public void all_bills_requested() {
	    dtos = service.getAll();
	}

	@Then("return all bills")
	public void return_all_bills() {
		List<Bill> returnedData = dtos.stream()
			.map(e -> {
				try {
					return new Bill(e.getSellerId(), e.getSaleAmount(), e.getNumOfBooks(), new CustomDate(e.getDate()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				return null;
			})
			.toList();
		
	    assertIterableEquals(data, returnedData);
	}

	@Given("the seller id {string}, sale amount {string}, nr. of books {string}")
	public void the_seller_id_sale_amount_nr_of_books(String sellerId, String saleAmount, String nrBooks) {
	    dtoModelInUse = new BillDTO(Integer.parseInt(sellerId), Double.parseDouble(saleAmount), Integer.parseInt(nrBooks));
	}

	@Then("add the bill")
	public void add_the_bill() throws Exception {
		service.add(dtoModelInUse);
	}

	@Then("the database should contain the new bill with the current date")
	public void the_database_should_contain_the_new_bill_with_the_current_date() throws Exception {
		repository.saveChanges();
		
		Bill expectedEntity = new Bill(
			dtoModelInUse.getSellerId(), 
			dtoModelInUse.getSaleAmount(), 
			dtoModelInUse.getNumOfBooks(), 
			new CustomDate()
		);
		
		assertTrue(repository.getAll().stream().anyMatch(e-> e.equals(expectedEntity)));
	}
}
