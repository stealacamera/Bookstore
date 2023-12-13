package dal.models;

import java.io.Serializable;

import exceptions.EmptyInputException;

public class Category implements Serializable {
	private static final long serialVersionUID = 2213319713236715241L;
	private static volatile int idOrder = 1;
	
	private final int id;
	private String name;
	
	public Category(int id, String name) throws EmptyInputException {
		this.id = id;
		setName(name);
	}
	
	public Category(String name) throws EmptyInputException {
		this(incrementId(), name);
	};
	
	private static int incrementId() {
		return Category.idOrder++;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws EmptyInputException {
		if(name == null || name.isBlank())
			throw new EmptyInputException("name");
			
		this.name = name;
	}
}