package Bookstore.Bookstore.integration;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/Bookstore/Bookstore/integration/features",
		glue = "Bookstore.Bookstore.integration.features.stepDefinitions",
		tags = "@ServiceNeighborhood"
	)
public class RunnerTest { }
