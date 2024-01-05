package Bookstore.Bookstore.dal.repositories;

import java.io.Serializable;

public class DbContext {
	public <T extends Serializable> DbTable<T> table(String dataDirPath, Class<T> type) {
		return new DbTable<>(dataDirPath, type);
	}
}