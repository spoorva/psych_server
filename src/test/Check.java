package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Check {

	public static void main(String args[]){
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("abc", "abc");
		hashMap.put("bcd", "bcd");
		hashMap.put("asdfasdf", "asdfsdf");
		System.out.println(hashMap.toString());
		System.out.println(convertHMToString(hashMap));
	}
	
	public static String convertHMToString(HashMap<String, String> hashMap){
		
		Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
		StringBuilder str = new StringBuilder();
		str.append("{");
		while(iterator.hasNext()){
			Map.Entry<String, String> pair = iterator.next();
			
			str.append("\""+pair.getKey()+"\":");
			str.append("\""+pair.getValue()+"\"");
			str.append(",");
		}
		int length = str.length();
		str.deleteCharAt(length-1);
		str.append("}");
		return str.toString();
	}
}
