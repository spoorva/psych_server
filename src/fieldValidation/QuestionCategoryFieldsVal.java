package fieldValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionCategoryFieldsVal {
	
	static String categoryNameRegex = "^[a-zA-Z0-9\\s,.'-]{1,50}$";
	static Pattern categoryNamePattern = Pattern.compile(categoryNameRegex);
	
	static String descriptionRegex = ".{0,1000}$";
	static Pattern descriptionPattern = Pattern.compile(descriptionRegex);
	
	static String labelRegex = "^[a-zA-Z0-9\\s]{1,30}$";
	static Pattern labelPattern = Pattern.compile(labelRegex);
	
	public static boolean validateQuestionCategoryName(String name){
		
		Matcher matcher = categoryNamePattern.matcher(name.trim());
		return matcher.matches();
	}
	
	public static boolean validateQuestionName(String name){
		return name.trim().length() > 0 && name.trim().length() <= 200;
	}
	
	public static boolean validateDescription(String desc){
		Matcher matcher = descriptionPattern.matcher(desc.trim());
		return matcher.matches();
	}
	
	public static boolean validateLabel(String label){
		Matcher matcher = labelPattern.matcher(label.trim());
		return matcher.matches();
	}
}
