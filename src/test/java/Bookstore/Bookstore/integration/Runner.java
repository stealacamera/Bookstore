package Bookstore.Bookstore.integration;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/java/Bookstore/Bookstore/integration/features"},
		glue = {"stepDefinitions"}
	)
public class Runner {

}
