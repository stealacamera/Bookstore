package dal.IRepositories;

import models.Category;

public interface ICategoryRepository extends IRepository<Category> {

	Category getById(int id);

}
