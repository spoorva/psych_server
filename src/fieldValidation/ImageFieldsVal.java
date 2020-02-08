package fieldValidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageFieldsVal {

	static String imageCategoryNameRegex = "^[a-zA-Z0-9 ,.'&-]{1,50}$";
	static Pattern imageCategoryNamePattern = Pattern.compile(imageCategoryNameRegex);
	public static boolean validateImageCategoryName(String name){
		Matcher matcher = imageCategoryNamePattern.matcher(name.trim());
		return matcher.matches();
	}
	
	
	static String imageNameRegex = "^[a-zA-Z0-9 ,.'&-]{1,50}$";
	static Pattern imageNamePattern = Pattern.compile(imageNameRegex);
	public static boolean validateImageName(String name){
		Matcher matcher = imageNamePattern.matcher(name.trim());
		return matcher.matches();
	}
	
	static String imageIntensityRegex = "^[1-9]{1}$";
	static Pattern imageIntesityPattern = Pattern.compile(imageIntensityRegex);
	public static boolean validateImageIntensity(String intensity){
		Matcher matcher = imageIntesityPattern.matcher(intensity.trim());
		return matcher.matches();
	}
}
