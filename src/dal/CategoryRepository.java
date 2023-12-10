package dal;

import dal.IRepositories.ICategoryRepository;
import models.Category;

public class CategoryRepository extends Repository<Category> implements ICategoryRepository {
	
	public CategoryRepository(DbContext context) {
		super(context.table(Category.class));
	}
	
	@Override
	public Category getById(int id) {
		return instances.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
}