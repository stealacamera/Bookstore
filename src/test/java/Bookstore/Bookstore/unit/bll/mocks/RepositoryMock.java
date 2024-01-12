package Bookstore.Bookstore.unit.bll.mocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Bookstore.Bookstore.dal.repositories.irepositories.IRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class RepositoryMock<T extends Serializable> implements IRepository<T> {
	protected List<T> dummyData = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public void addDummyData(T... models) {
		for(T model: models)		
			dummyData.add(model);
	}
	
	@Override
	public ObservableList<T> getAll() {
		return FXCollections.observableArrayList(dummyData);
	}

	@Override
	public T get(int index) {
		return dummyData.get(index);
	}

	@Override
	public boolean add(T instance) {
		return dummyData.add(instance);
	}

	@Override
	public void add(int index, T instance) {
		dummyData.add(index, instance);
	}

	@Override
	public void remove(int index) {
		dummyData.remove(index);
	}

	@Override
	public int count() {
		return dummyData.size();
	}

	@Override
	public void saveChanges() {
		return;
	}

}
