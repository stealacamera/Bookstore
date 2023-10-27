package views.stats;

import java.text.DateFormatSymbols;
import java.time.LocalDate;

import controllers.StatisticsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ui.BaseView;

public class BookExpensesView extends BaseView {
	private StatisticsController controller;
	private BorderPane pane = new BorderPane();
	private FlowPane chartPane = new FlowPane();
	private PieChart dailyChart = new PieChart(), totalChart = new PieChart();
	private BarChart<String, Number> monthlyChart;
	
	private DatePicker dateDp = new DatePicker(LocalDate.now());
	private RadioButton dailyBox = new RadioButton("Daily"), monthlyBox = new RadioButton("Monthly"), totalBox = new RadioButton("Total");
	private boolean clickedRepeatedlyDaily = false, clickedRepeatedlyMonthly = false, clickedRepeatedlyTotal = false;
	
	public BookExpensesView(StatisticsController controller) {
		this.controller = controller;
		
		ToggleGroup radios = new ToggleGroup();
		dailyBox.setToggleGroup(radios);
		monthlyBox.setToggleGroup(radios);
		totalBox.setToggleGroup(radios);
		
		dateDp.valueProperty().addListener((observable, oldValue, newValue) ->
			setDailyChart(controller.getDailySales(newValue), controller.getDailyPurchases(newValue)));
		
		setRadioListeners();
		createLayout();
	}
	
	private void createLayout() {
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
	
	private void setRadioListeners() {
		dailyBox.setOnAction(e -> {
			if(!clickedRepeatedlyDaily) {
				setDailyChart(controller.getDailySales(dateDp.getValue()), controller.getDailyPurchases(dateDp.getValue()));
				clickedRepeatedlyDaily = true;
			}
			
			pane.setCenter(dailyChart);
		});
		
		monthlyBox.setOnAction(e -> {
			if(!clickedRepeatedlyMonthly) {
				setMonthlyChart(controller.getMonthlySales(), controller.getMonthlyPurchases());
				clickedRepeatedlyMonthly = true;
			}
			
			pane.setCenter(monthlyChart);
		});
		
		totalBox.setOnAction(e -> {
			if(!clickedRepeatedlyTotal) {
				setTotalChart(controller.getTotalSales(), controller.getTotalPurchases());
				clickedRepeatedlyTotal = true;
			}
			
			pane.setCenter(totalChart);
		});
	}
	
	private void setDailyChart(double sales, double purchases) {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
				new PieChart.Data("Book sales", sales),
				new PieChart.Data("Book stock costs", purchases));
		
		dailyChart.setData(data);
		dailyChart.setTitle("Daily cash flow");
	}
	
	private void setMonthlyChart(double[] sales, double[] purchases) {
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
		monthlyChart.setTitle("Monthly cash flow");
		
		monthlyChart.getData().add(salesSeries);
		monthlyChart.getData().add(purchasesSeries);		
	}
	
	private void setTotalChart(double sales, double purchases) {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
				new PieChart.Data("Book sales", sales),
				new PieChart.Data("Book stock purchases", purchases));
		
		totalChart = new PieChart(data);
		totalChart.setTitle("Total cash flow");
	}
}
