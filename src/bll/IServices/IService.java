package bll.IServices;

import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import javafx.collections.ObservableList;

public interface IService<T> {
	ObservableList<T> getAll();
	T get(int index);
	boolean add(T instance) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException;
	void remove(int index) throws IndexOutOfBoundsException;
}
