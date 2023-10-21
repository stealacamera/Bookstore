package models;

import java.util.ArrayList;

import exceptions.ExistingObjectException;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.ListIO;

public class CategoryList {
	public static final String FILE_NAME = "categories.dat";
	private static ObservableList<String> categories;
	
	private CategoryList() {};
	
	public static ObservableList<String> getAll() {
		return new ReadOnlyListWrapper<>(categories);
	}
	
	public static void setList() {
		ArrayList<String> currentList = ListIO.readFromFile(FILE_NAME);
		categories = currentList == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(currentList);
	}
	
	public static void add(String category) throws ExistingObjectException {
		if(categories.contains(category))
			throw new ExistingObjectException("category name");
		
		categories.add(category);
	}	
}