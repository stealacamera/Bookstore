package views.stats;

import java.text.DecimalFormat;
import java.time.LocalDate;

import controllers.StatisticsController;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ui.BaseView;

public class CashFlowStatsView extends BaseView {
	private BorderPane pane = new BorderPane();
	private StatisticsController controller;
	
	private DatePicker startDateDp = new DatePicker(LocalDate.now()), endDateDp = new DatePicker(LocalDate.now());
	private Text totalIncomeTxt = new Text(), totalCostsTxt = new Text();
	private Button submitBt = new Button("Submit");
	
	private BarChart<String, Number> cashFlowChart;
	private XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>(), costsSeries = new XYChart.Series<>();
	
	public CashFlowStatsView(StatisticsController controller) {
		this.controller = controller;
		setCashFlowChart();
		createLayout();
		
		submitBt.setOnAction(e -> {
			LocalDate startDate = startDateDp.getValue(), endDate = endDateDp.getValue();
			
			if(startDate != null && endDate != null)
				setData(startDate, endDate);
		});
	}
	
	private void setCashFlowChart() {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("$");
		
		incomeSeries.setName("Income");
		costsSeries.setName("Costs");
		
		cashFlowChart = new BarChart<>(xAxis, yAxis);
		cashFlowChart.setTitle("Cash flow");
		cashFlowChart.setAnimated(false);
				
		cashFlowChart.getData().add(incomeSeries);
		cashFlowChart.getData().add(costsSeries);
	}
	
	private void setData(LocalDate startDate, LocalDate endDate) {
		double totalBookSales = controller.getTotalBookSales(startDate, endDate), 
				totalBookPurchases = controller.getTotalBookPurchases(startDate, endDate),
				totalSalaries = controller.getTotalSalaries();
		
		incomeSeries.getData().clear();
		costsSeries.getData().clear();
		
		incomeSeries.getData().add(new XYChart.Data<String, Number>("Book sales", totalBookSales));
		costsSeries.getData().add(new XYChart.Data<String, Number>("Book purchases", totalBookPurchases));
		costsSeries.getData().add(new XYChart.Data<String, Number>("Salaries", totalSalaries));
		
		//Data grid
		DecimalFormat decimal = new DecimalFormat("#.000");
		
		totalIncomeTxt.setText(decimal.format(totalBookSales) + "");
		totalCostsTxt.setText(decimal.format(totalBookPurchases + totalSalaries) + "");
	}
	
	private void createLayout() {
		GridPane totalsPane = new GridPane();
		VBox startDatePane = new VBox(new Label("Start date:"), startDateDp),
				endDatePane = new VBox(new Label("End date:"), endDateDp),
				dpPane = new VBox(startDatePane, endDatePane),
				inputPane = new VBox(dpPane, submitBt),
				fieldsPane = new VBox(inputPane, totalsPane);
		
		totalsPane.add(new Label("Total income:"), 0, 0);
		totalsPane.add(totalIncomeTxt, 1, 0);
		totalsPane.add(new Label("Total costs:"), 0, 1);
		totalsPane.add(totalCostsTxt, 1, 1);
		
		startDatePane.setSpacing(3);
		endDatePane.setSpacing(3);
		
		dpPane.setSpacing(3);
		inputPane.setSpacing(20);
		totalsPane.setHgap(20);
		totalsPane.setVgap(3);
		fieldsPane.setSpacing(70);
		
		pane.setLeft(fieldsPane);;
		pane.setCenter(cashFlowChart);
		pane.setPadding(new Insets(25));
	}
}
