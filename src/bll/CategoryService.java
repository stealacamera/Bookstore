package bll;

import bll.IServices.ICategoryService;
import bll.dto.CategoryDTO;
import dal.IRepositories.ICategoryRepository;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import dal.models.Category;

public class CategoryService extends Service<Category, CategoryDTO> implements ICategoryService {
	private final ICategoryRepository db;
	
	public CategoryService(ICategoryRepository db) {
		super(db);
		this.db = db;
	}

	@Override
	public boolean add(CategoryDTO model) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		Category existingName = db.getAll().stream().filter(e -> e.getName().equals(model.getName())).findFirst().orElse(null);
		
		if(existingName != null)
			throw new ExistingObjectException();
		
		return db.add(convertToDAO(model));
	}
	
	@Override
	public CategoryDTO getById(int id) {
		return convertToDTO(db.getById(id));
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
