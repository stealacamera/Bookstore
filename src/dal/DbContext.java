package dal;

import java.io.Serializable;

public class DbContext {
	public <T extends Serializable> DbTable<T> table(Class<T> type) {
		return new DbTable<>(type);
	}
}
