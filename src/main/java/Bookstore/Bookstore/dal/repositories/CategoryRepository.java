package Bookstore.Bookstore.dal.repositories;

import Bookstore.Bookstore.dal.repositories.irepositories.ICategoryRepository;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.dal.models.Category;

public class CategoryRepository extends Repository<Category> implements ICategoryRepository {
	
	public CategoryRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, Category.class));
		seedData();
	}
	
	@Override
	public Category getById(int id) {
		return instances.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
	}
	
	@Override
	public Category getByName(String name) {
		return instances.stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
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