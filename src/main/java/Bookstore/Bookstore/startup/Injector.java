package Bookstore.Bookstore.startup;

import java.util.HashMap;
import java.util.Map;

import Bookstore.Bookstore.dal.repositories.DbContext;

public class Injector {
	private static DbContext dbContext = null;
	private static Map<Class<?>, Object> dependencySolver = new HashMap<>();
	
	public static void addDbContext(DbContext obj) {
		dbContext = obj;
	}
	
	public static DbContext getDbContext() {
		return dbContext;
	}
	
	public static <T> void addDependency(Class<?> dependency, T solution) {
		dependencySolver.put(dependency, solution);
	}
	
	public static <T> T getDependency(Class<?> dependency) {
		return (T) dependencySolver.get(dependency);
	}
	
}
