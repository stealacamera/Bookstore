package Bookstore.Bookstore.dal.repositories;

import Bookstore.Bookstore.dal.models.Category;
import Bookstore.Bookstore.dal.repositories.irepositories.ICategoryRepository;

public class CategoryRepository extends Repository<Category> implements ICategoryRepository {
	
	public CategoryRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, Category.class));
	}
	
	@Override
	public Category getById(int id) {
		return instances.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
	
	@Override
	public Category getByName(String name) {
		return instances.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
	}
}