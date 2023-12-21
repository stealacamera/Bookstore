package Bookstore.Bookstore.bll.services.iservices;

import Bookstore.Bookstore.bll.dto.CategoryDTO;

public interface ICategoryService extends IService<CategoryDTO> {

	CategoryDTO getById(int id);

}
