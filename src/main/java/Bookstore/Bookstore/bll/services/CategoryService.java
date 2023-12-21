package Bookstore.Bookstore.bll.services;

import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.dal.repositories.irepositories.ICategoryRepository;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.bll.dto.CategoryDTO;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;

public class CategoryService extends Service<Category, CategoryDTO> implements ICategoryService {
	private final ICategoryRepository db;
	
	public CategoryService(ICategoryRepository db) {
		super(db);
		this.db = db;
	}

	@Override
	public boolean add(CategoryDTO model) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		if(model == null)
			return false;
		
		Category existingName = db.getByName(model.getName());
		
		if(existingName != null)
			throw new ExistingObjectException();
		
		return db.add(convertToDAO(model));
	}
	
	@Override
	public CategoryDTO getById(int id) {
		Category model = db.getById(id);
		return model == null ? null : convertToDTO(model);
	}
	
	@Override
	protected CategoryDTO convertToDTO(Category model) {
		return new CategoryDTO(model.getId(), model.getName());
	}

	@Override
	protected Category convertToDAO(CategoryDTO model) throws EmptyInputException {
		return model.getId() == 0 ? new Category(model.getName()) : new Category(model.getId(), model.getName());
	}
}
