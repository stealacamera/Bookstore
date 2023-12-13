package dal;

import dal.IRepositories.ICategoryRepository;
import exceptions.EmptyInputException;
import dal.models.Category;

public class CategoryRepository extends Repository<Category> implements ICategoryRepository {
	
	public CategoryRepository(DbContext context) {
		super(context.table(Category.class));
		seedData();
	}
	
	@Override
	public Category getById(int id) {
		return instances.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
	
	private void seedData() {
		if(instances.size() != 0)
			return;
		
		try {
			add(new Category("Drama"));
			add(new Category("Comedy"));
			add(new Category("Science fiction"));
			add(new Category("Mystery"));
			add(new Category("Horror"));
			add(new Category("Romance"));
			add(new Category("Fantasy"));
			
			super.saveChanges();
		} catch (EmptyInputException e) {
			e.printStackTrace();
		}
	}
}