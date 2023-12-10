package bll;

import java.io.Serializable;

import bll.IServices.IService;
import dal.IRepositories.IRepository;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Service<DAO extends Serializable, DTO> implements IService<DTO> {
	protected final IRepository<DAO> db;
	
	public Service(IRepository<DAO> db) {
		this.db = db;
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
		return convertToDTO(db.get(index));
	}

	@Override
	public void remove(int index) {
		if(index < 0 || index > db.count())
			throw new IndexOutOfBoundsException();
		
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
