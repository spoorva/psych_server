package fieldValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileFieldsVal {
	static String nameRegex = "^[a-zA-Z ,.'-]{1,50}$";
	static Pattern namePattern = Pattern.compile(nameRegex);
	
	public static boolean validateName(String name){
		
		Matcher matcher = namePattern.matcher(name.trim());
		return matcher.matches();
	}
	
	static String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$.%^&+=])[^\\s]{6,30}$";
	static Pattern passwordPattern = Pattern.compile(passwordRegex);
	public static boolean validatePassword(String password){
		
		Matcher matcher = passwordPattern.matcher(password.trim());
		return matcher.matches();
	}
	
	static final String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	static Pattern emailPattern = Pattern.compile(emailRegex);
	public static boolean validateEmail(String email){
		
		Matcher matcher = emailPattern.matcher(email.trim());
		return matcher.matches();
	}
}
