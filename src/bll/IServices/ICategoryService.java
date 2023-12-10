package bll.IServices;

import bll.dto.CategoryDTO;

public interface ICategoryService extends IService<CategoryDTO> {

	CategoryDTO getById(int id);

}
