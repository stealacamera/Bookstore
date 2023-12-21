package Bookstore.Bookstore.unit.dal.repositories.mocks;

import Bookstore.Bookstore.dal.repositories.DbContext;
import Bookstore.Bookstore.dal.repositories.Repository;

public class RepositoryStub extends Repository<String> {	
	public RepositoryStub(DbContext dbContext, String dataDirPath) {
		super(dbContext.table(dataDirPath, String.class));
	}
}
