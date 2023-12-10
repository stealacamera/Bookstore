package dal.IRepositories;

import java.io.Serializable;

import javafx.collections.ObservableList;

public interface IRepository<T extends Serializable> {
	ObservableList<T> getAll();
	T get(int index);
	
	boolean add(T instance);
	void add(int index, T instance);
	
	void remove(int index);
	int count();
	
	void saveChanges();
}
