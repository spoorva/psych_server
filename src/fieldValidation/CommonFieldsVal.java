package fieldValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.Constant;

public class CommonFieldsVal {

	public static boolean validateDescription(String description){
		int length = description.trim().length();
		if(length <= 1000){
			return true;
		}
		return false;
	}
	
	static String keywordsRegex = "^[a-zA-Z0-9 ]{1,30}$";
	static Pattern keywordPattern = Pattern.compile(keywordsRegex);
	public static boolean validateKeywords(String keywords){
		
		if(keywords.trim().equals("")){
			return true;
		}
		String[] parts = keywords.split(Constant.KEYWORD_SEPERATOR_PARSER);
		for(String part: parts){
			Matcher matcher = keywordPattern.matcher(part.trim());
			if(!matcher.find()){
				return false;
			}
		}
		return true;
	}
	
	static String keyRegex = "^[0-9]{1,20}$";
	static Pattern keyPattern = Pattern.compile(keyRegex);
	public static boolean validateFieldId(String key){
		
		Matcher matcher  = keyPattern.matcher(key.trim());
		return matcher.find();
		
	}
	
}
