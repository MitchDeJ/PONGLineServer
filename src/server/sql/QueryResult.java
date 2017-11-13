package server.sql;

public class QueryResult {

	private Object result;
	
	public QueryResult(Object result) {
		this.result = result;
	}
	
	public String getString() {
		return (String) result;
	}
	
	public boolean getBoolean() {
		return (boolean) result;
	}
	
	public int getInt() {
		return (int) result;
	}
	
	public long getLong() {
		return (long) result;
	}
}
