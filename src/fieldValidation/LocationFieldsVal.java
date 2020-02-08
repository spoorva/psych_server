package fieldValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationFieldsVal {
	
	static String locationCodeRegex = "^[a-zA-Z0-9]{6}$";
	static Pattern locationCodePattern = Pattern.compile(locationCodeRegex);
	
	public static boolean validateLocationCode(String locationCode){
		Matcher matcher = locationCodePattern.matcher(locationCode.trim());
		return matcher.matches();
	}
	
	public static boolean validateAddressLine1(String addressLine1){
		int length = addressLine1.trim().length();
		if(length >0 && length <= 200){
			return true;
		}
		return false;
	}
	
	public static boolean validateAddressLine2(String addressLine2){
		int length = addressLine2.trim().length();
		if(length <= 200){
			return true;
		}
		return false;
	}
	
	static String locNameRegex = "^[a-zA-Z0-9 ,.'&-]{1,150}$";
	static Pattern locNamePattern = Pattern.compile(locNameRegex);
	public static boolean validateName(String locName){
		Matcher matcher = locNamePattern.matcher(locName.trim());
		return matcher.matches();
	}
	
	static String phoneNumberRegex = "^[0-9]{10}$";
	static Pattern phoneNumberPattern = Pattern.compile(phoneNumberRegex);
	public static boolean validatePhoneNumber(String phoneNumber){
		Matcher matcher = phoneNumberPattern.matcher(phoneNumber.trim());
		return matcher.matches();
	}
	
	
	public static boolean validateFaxNumber(String faxNumber){
		if(faxNumber.trim().equals("")){
			return true;
		}
		Matcher matcher = phoneNumberPattern.matcher(faxNumber.trim());
		return matcher.matches();
	}
	
	static String zipcodeRegex = "^[0-9]{5}(?:-[0-9]{4})?$";
	static Pattern zipCodePattern = Pattern.compile(zipcodeRegex);
	public static boolean validateZipCode(String zipCode){
		Matcher matcher = zipCodePattern.matcher(zipCode.trim());
		return matcher.matches();
	}
	
	static String stateRegex = "^[a-zA-Z ]{2,100}$";
	static Pattern statePattern = Pattern.compile(stateRegex);
	public static boolean validateState(String state){
		Matcher matcher = statePattern.matcher(state.trim());
		return matcher.matches();
	}
	
	static String cityRegex = "[a-zA-Z ]{2,100}$";
	static Pattern cityPattern = Pattern.compile(cityRegex);
	public static boolean validateCity(String city){
		Matcher matcher = cityPattern.matcher(city.trim());
		return matcher.matches();
	}
	
	public static boolean validateEmail(String email){
		if(email.trim().equals("")){
			return true;
		}else{
			return UserProfileFieldsVal.validateEmail(email);
		}
	}

}
