package Bookstore.Bookstore.dal.repositories.irepositories;

import Bookstore.Bookstore.dal.models.Category;

public interface ICategoryRepository extends IRepository<Category> {

	Category getById(int id);

	Category getByName(String name);

}
