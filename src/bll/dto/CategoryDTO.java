package bll.dto;

public class CategoryDTO {
	private int id;
	private String name;
	
	public CategoryDTO(int id, String name) {
		setId(id);
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
