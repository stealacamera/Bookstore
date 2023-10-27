package views.stats;

import java.time.LocalDate;

import controllers.StatisticsController;
import javafx.collections.FXCollections;
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
import models.LibrarianPerformance;
import ui.BaseView;

public class LibrarianPerformanceView extends BaseView {
	private StatisticsController controller;
	private VBox pane = new VBox();
	private DatePicker startDateDp = new DatePicker(LocalDate.now()), endDateDp = new DatePicker(LocalDate.now());
	private Button submitBt = new Button("Submit");
	
	private TableView<LibrarianPerformance> performanceDataTv = new TableView<>();
	private TableColumn<LibrarianPerformance, String> tcEmployee = new TableColumn<>("Employee");
	private	TableColumn<LibrarianPerformance, Integer> tcBills = new TableColumn<>("No. of bills"), tcBooks = new TableColumn<>("No. of books sold");
	private TableColumn<LibrarianPerformance, Double> tcSalesAmount = new TableColumn<>("Sales amount");
	
	public LibrarianPerformanceView(StatisticsController controller) {
		this.controller = controller;
		setPerformanceDataTv();
		setSubmitBt();
		createLayout();
	}
	
	private void setSubmitBt() {
		submitBt.setOnAction(e -> performanceDataTv.setItems(FXCollections.
				observableArrayList(controller.getLibrariansPerformance(startDateDp.getValue(), endDateDp.getValue()))));
	}
	
	private void setPerformanceDataTv() {		
		tcEmployee.setCellValueFactory(new PropertyValueFactory<LibrarianPerformance, String>("employee"));
		tcBills.setCellValueFactory(new PropertyValueFactory<LibrarianPerformance, Integer>("numOfBills"));
		tcBooks.setCellValueFactory(new PropertyValueFactory<LibrarianPerformance, Integer>("numOfBooks"));
		
		tcSalesAmount.setCellValueFactory(new PropertyValueFactory<LibrarianPerformance, Double>("salesAmount"));
		tcSalesAmount.setCellFactory(cell -> new TableCell<LibrarianPerformance, Double>() {
			@Override
			protected void updateItem(Double sales, boolean empty) {
				super.updateItem(sales, empty);
				
				if(empty || sales == null) {
					setText(null);
					setGraphic(null);
				} else
					setText(String.format("%.3f", sales.doubleValue()));
			}
		});
		
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
}
