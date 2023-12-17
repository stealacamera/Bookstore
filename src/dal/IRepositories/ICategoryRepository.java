package dal.IRepositories;

import dal.models.Category;

public interface ICategoryRepository extends IRepository<Category> {

	Category getById(int id);

	Category getByName(String name);

}
