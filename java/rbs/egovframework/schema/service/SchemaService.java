package rbs.egovframework.schema.service;


public interface SchemaService {

	public int addColumn(String columnName, String columnType) throws Exception;
	//public int renameColumn(String columnName, String newColumnName) throws Exception;
	public int dropColumn(String columnName) throws Exception;
	
}
