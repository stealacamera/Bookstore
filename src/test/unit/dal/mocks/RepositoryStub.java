package test.unit.dal.mocks;

import dal.DbContext;
import dal.Repository;

public class RepositoryStub extends Repository<String> {	
	public RepositoryStub(DbContext dbContext, String dataDirPath) {
		super(dbContext.table(dataDirPath, String.class));
	}
}
