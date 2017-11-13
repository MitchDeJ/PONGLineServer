package server.sql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ResultList {
	
	private List<LinkedHashMap<String, QueryResult>> results = new ArrayList<LinkedHashMap<String, QueryResult>>();
	
	public void add(LinkedHashMap<String, QueryResult> result) {
		results.add(result);
	}
	
	public LinkedHashMap<String, QueryResult> get(int index) {
		return results.get(index);
	}

	public int size() {
		return results.size();
	}
}
