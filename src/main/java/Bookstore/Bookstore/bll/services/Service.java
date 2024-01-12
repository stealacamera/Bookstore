package Bookstore.Bookstore.bll.services;

import java.io.Serializable;

import Bookstore.Bookstore.dal.repositories.irepositories.IRepository;
import Bookstore.Bookstore.bll.services.iservices.IService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Service<DAO extends Serializable, DTO> implements IService<DTO> {
	protected final IRepository<DAO> db;
	
	public Service(IRepository<DAO> db) {
		this.db = db;
	}
	
	@Override
	public int count() {
		return db.count();
	}
	
	@Override
	public ObservableList<DTO> getAll() {
		ObservableList<DTO> list = FXCollections.observableArrayList();
		
		for(DAO instance: db.getAll())
			list.add(convertToDTO(instance));
		
		return new ReadOnlyListWrapper<>(list);
	}

	@Override
	public DTO get(int index) {
		DAO model = db.get(index);		
		return model == null ? null : convertToDTO(model);
	}

	@Override
	public void remove(int index) {
		db.remove(index);
		db.saveChanges();
	}

	@Override
	public boolean add(DTO instance) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		if(instance == null)
			return false;
		
		boolean result = db.add(convertToDAO(instance));
		db.saveChanges();
		
		return result;
	}

	protected abstract DTO convertToDTO(DAO model);
	protected abstract DAO convertToDAO(DTO model) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException;
}
