import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TestBillServiceNeighborHood {
	private static String tempDataDirPath = Utils.testingUserDataDirPath;
	private IBillRepository repository = new BillRepository(tempDataDirPath, new DbContext());
	private IBillService Bill = new BillService(repository);
	
	private ArrayList<Bill> data;
	private ArrayList<String> title;
	private BillDTO dtoModelInUse;
	
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
	
	@Given("these existing Bills saved in the database")
	public void these_existing_Bills_saved_in_the_database(io.cucumber.datatable.DataTable dataTable) {
		List<Map<String, String>> rowData = dataTable.asMaps();
		this.data = new ArrayList<>();
		
		for(Map<String, String> data: rowData) {
			try {
				Bill bill = new Bill(
						Integer.parseInt(data.get("sellerId")),Double.parseDouble(data.get("saleAmount")),Integer.parseInt(data.get("numOfBooks")), new CustomDate(data.get("date"))
					);
				
				repository.add(bill);
				this.data.add(bill);
			} catch (EmptyInputException e) {
				e.printStackTrace();
			}
		}
	}
	@When ("all bills requested")
	public void All_Bills_Requested{
		data.add(Bill.getAll());
	}
	@Then("return all bills")
	public void return_all_bills() {
		for(i=0;i<data.size();i++) {
			AssertEquals(data.get(0),Bill.get(0));
		}
	}
	
	@Given ("SellId {string}")
	public void given_sell_id(String a) {
		int parseInd=Integer.parseInt(a);
		dtoModelInUse=Bill.getStream().filter(e->e.getSellerId()==parseInd).findFirst();
	}
	@Then("remove the bill from database")
	public void remove_from_database() {
		bill.remove(dtoModelInUse);
	}
	@And("save it")
	public void Save_Changes() {
		repository.saveChanges();
		boolean t= repoistory.getStream().anyMatch(i->i.equals(dtoModelInUse))
		AssertTrue(t,true);
	}
	

}
