package Bookstore.Bookstore.views.statistics;

import java.text.DateFormatSymbols;
import java.time.LocalDate;

import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.views.IView;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BookExpensesView extends IView {
	private BorderPane pane = new BorderPane();
	private FlowPane chartPane = new FlowPane();
	private PieChart dailyChart = new PieChart(), totalChart = new PieChart();
	private BarChart<String, Number> monthlyChart;
	
	private DatePicker dateDp = new DatePicker(LocalDate.now());
	private RadioButton dailyBox = new RadioButton("Daily"), monthlyBox = new RadioButton("Monthly"), totalBox = new RadioButton("Total");
	
	public BookExpensesView() {
		createLayout();
		
		dateDp.setId("date-picker");
		dailyBox.setId("daily-toggle");
		monthlyBox.setId("monthly-toggle");
		totalBox.setId("total-toggle");

		super.getChildren().add(pane);
	}
	
	public LocalDate getDateValue() { return dateDp.getValue(); }
	
	public void setDateListener(ChangeListener<LocalDate> action) { 
		dateDp.valueProperty().addListener(action); 
		dateDp.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER)
				dateDp.setValue(LocalDate.parse(dateDp.getEditor().getText(), CustomDate.dateFormat));
		});
	}
	
	private void createLayout() {
		ToggleGroup radios = new ToggleGroup();
		dailyBox.setToggleGroup(radios);
		monthlyBox.setToggleGroup(radios);
		totalBox.setToggleGroup(radios);

		dailyBox.setOnAction(e -> pane.setCenter(dailyChart));
		monthlyBox.setOnAction(e -> pane.setCenter(monthlyChart));
		totalBox.setOnAction(e -> pane.setCenter(totalChart));
		
		HBox dailyOptionPane = new HBox(dailyBox, dateDp);
		VBox optionsPane = new VBox(dailyOptionPane, monthlyBox, totalBox);
		
		dailyOptionPane.setAlignment(Pos.BASELINE_CENTER);
		dailyOptionPane.setSpacing(10);
		optionsPane.setSpacing(10);
		
		pane.setLeft(optionsPane);
		pane.setCenter(chartPane);
		pane.setPadding(new Insets(20));
		pane.setPrefSize(600, 600);
	}
	
	public void setDailyChart(double sales, double purchases) {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
				new PieChart.Data("Book sales", sales),
				new PieChart.Data("Book stock costs", purchases));
		
		dailyChart.setData(data);
		dailyChart.setTitle("Daily cash flow");
		dailyChart.setId("daily-chart");
	}
	
	public void setMonthlyChart(double[] sales, double[] purchases) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Month");
		yAxis.setLabel("Cash flow");
		
		XYChart.Series<String, Number> salesSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> purchasesSeries = new XYChart.Series<>();
		salesSeries.setName("Sales");
		purchasesSeries.setName("Purchases");
		
		for(int i = 0; i < sales.length; i++) {
			salesSeries.getData().add(new XYChart.Data<String, Number>(new DateFormatSymbols().getMonths()[i], sales[i]));
			purchasesSeries.getData().add(new XYChart.Data<String, Number>(new DateFormatSymbols().getMonths()[i], purchases[i]));
		}
		
		monthlyChart = new BarChart<String, Number>(xAxis, yAxis);
		monthlyChart.setId("monthly-chart");
		monthlyChart.setTitle("Monthly cash flow");
		
		monthlyChart.getData().add(salesSeries);
		monthlyChart.getData().add(purchasesSeries);		
	}
	
	public void setTotalChart(double sales, double purchases) {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
				new PieChart.Data("Book sales", sales),
				new PieChart.Data("Book stock purchases", purchases));
		
		totalChart = new PieChart(data);
		totalChart.setId("total-chart");
		totalChart.setTitle("Total cash flow");
	}
}
