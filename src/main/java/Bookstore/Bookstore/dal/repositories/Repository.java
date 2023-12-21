package Bookstore.Bookstore.dal.repositories;

import java.io.Serializable;
import java.util.ArrayList;

import Bookstore.Bookstore.dal.repositories.irepositories.IRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Repository<T extends Serializable> implements IRepository<T> {
	protected final DbTable<T> dbTable;
	protected ObservableList<T> instances;
	
	public Repository(DbTable<T> dbTable) {
		this.dbTable = dbTable;
		instances = FXCollections.observableArrayList(dbTable.readTable());
	}
			
	@Override
	public ObservableList<T> getAll() {
		return FXCollections.observableList(instances);
	}

	@Override
	public T get(int index) {
		return instances.get(index);
	}

	@Override
	public boolean add(T instance) {
		return instances.add(instance);
	}

	@Override
	public void add(int index, T instance) {
		instances.add(index, instance);
	}
		
	@Override
	public void remove(int index) {
		instances.remove(index);
	}

	@Override
	public int count() {
		return instances.size();
	}
	
	@Override
	public void saveChanges() {
		dbTable.saveToTable(new ArrayList<T>(instances));
	}
}
