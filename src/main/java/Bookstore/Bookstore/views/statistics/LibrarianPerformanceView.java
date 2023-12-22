package Bookstore.Bookstore.views.statistics;

import java.time.LocalDate;
import java.util.List;

import Bookstore.Bookstore.bll.dto.LibrarianPerformanceDTO;
import Bookstore.Bookstore.views.IView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LibrarianPerformanceView extends IView {
	private VBox pane = new VBox();
	private DatePicker startDateDp = new DatePicker(LocalDate.now()), endDateDp = new DatePicker(LocalDate.now());
	private Button submitBt = new Button("Submit");
	
	private TableView<LibrarianPerformanceDTO> performanceDataTv = new TableView<>();
	private TableColumn<LibrarianPerformanceDTO, String> tcEmployee = new TableColumn<>("Employee");
	private	TableColumn<LibrarianPerformanceDTO, Integer> tcBills = new TableColumn<>("No. of bills"), tcBooks = new TableColumn<>("No. of books sold");
	private TableColumn<LibrarianPerformanceDTO, Double> tcSalesAmount = new TableColumn<>("Sales amount");
	
	public LibrarianPerformanceView() {
		createLayout();
		super.getChildren().add(pane);
	}
	
	public LocalDate getStartDate() { return startDateDp.getValue(); }
	public LocalDate getEndDate() { return endDateDp.getValue(); }
	
	public void setSubmitListener(EventHandler<ActionEvent> action) { submitBt.setOnAction(action); }
	
	public void setPerformanceList(List<LibrarianPerformanceDTO> list) {
		performanceDataTv.setItems(FXCollections.observableArrayList(list));
	}
	
	private void setPerformanceDataTv() {		
		tcEmployee.setCellValueFactory(new PropertyValueFactory<LibrarianPerformanceDTO, String>("employeeDescription"));
		tcBills.setCellValueFactory(new PropertyValueFactory<LibrarianPerformanceDTO, Integer>("numOfBills"));
		tcBooks.setCellValueFactory(new PropertyValueFactory<LibrarianPerformanceDTO, Integer>("numOfBooks"));
		
		tcSalesAmount.setCellValueFactory(new PropertyValueFactory<LibrarianPerformanceDTO, Double>("salesAmount"));
		tcSalesAmount.setCellFactory(cell -> new SalesAmountTableCellFactory());
		
		performanceDataTv.getColumns().add(tcEmployee);
		performanceDataTv.getColumns().add(tcBills);
		performanceDataTv.getColumns().add(tcBooks);
		performanceDataTv.getColumns().add(tcSalesAmount);
		
		performanceDataTv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		performanceDataTv.setPrefWidth(500);
	}
	
	private void createLayout() {
		GridPane dpPane = new GridPane();
		HBox fieldsPane = new HBox(dpPane, submitBt);
		setPerformanceDataTv();
		
		dpPane.add(new Label("Start date:"), 0, 0);
		dpPane.add(new Label("End date:"), 1, 0);
		dpPane.add(startDateDp, 0, 1);
		dpPane.add(endDateDp, 1, 1);
		
		dpPane.setHgap(10);
		dpPane.setVgap(5);
		
		fieldsPane.setSpacing(30);
		fieldsPane.setAlignment(Pos.BOTTOM_CENTER);
		
		pane.setPadding(new Insets(20));
		pane.setSpacing(20);
		
		pane.getChildren().addAll(fieldsPane, performanceDataTv);
	}
	
	private static class SalesAmountTableCellFactory extends TableCell<LibrarianPerformanceDTO, Double> {
		@Override
		protected void updateItem(Double sales, boolean empty) {
			super.updateItem(sales, empty);
			
			if(empty || sales == null) {
				setText(null);
				setGraphic(null);
			} else
				setText(String.format("%.3f", sales));
		}
	}
}
