package Bookstore.Bookstore.integration.serviceNeighborhoods;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/Bookstore/Bookstore/integration/serviceNeighborhoods/features",
		glue = "Bookstore.Bookstore.integration.serviceNeighborhoods.stepDefinitions",
		tags = "@ServiceNeighborhood"
	)
public class TestServiceNeighborhoodsRunner { }
