package fieldValidation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ImageFieldsTest {
	
	@Test
	public void testValidateImageCategoryName(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "John & John Corp.";
		String correctName2 = "Martin Luther King, Jr.";
		String correctName3 = "Hector Sausage-Hausen";
	
		
		assertEquals(true, ImageFieldsVal.validateImageCategoryName(correctName1));
		assertEquals(true, ImageFieldsVal.validateImageCategoryName(correctName2));
		assertEquals(true, ImageFieldsVal.validateImageCategoryName(correctName3));
		assertEquals(true,  ImageFieldsVal.validateImageCategoryName(numbers));
		
		assertEquals(false,  ImageFieldsVal.validateImageCategoryName(empty2));
		assertEquals(false,  ImageFieldsVal.validateImageCategoryName(badCharacter));
		assertEquals(false,  ImageFieldsVal.validateImageCategoryName(bigName));
		assertEquals(false,  ImageFieldsVal.validateImageCategoryName(emtpyName));
		
		
	}
	
	@Test
	public void testValidateImageName(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "John & John Corp.";
		String correctName2 = "Martin Luther King, Jr.";
		String correctName3 = "Hector Sausage-Hausen";
	
		assertEquals(true, ImageFieldsVal.validateImageName(correctName1));
		assertEquals(true, ImageFieldsVal.validateImageName(correctName2));
		assertEquals(true, ImageFieldsVal.validateImageName(correctName3));
		assertEquals(true,  ImageFieldsVal.validateImageName(numbers));
		
		assertEquals(false,  ImageFieldsVal.validateImageName(empty2));
		assertEquals(false,  ImageFieldsVal.validateImageName(badCharacter));
		assertEquals(false,  ImageFieldsVal.validateImageName(bigName));
		assertEquals(false,  ImageFieldsVal.validateImageName(emtpyName));
		
		
	}
	
	@Test
	public void testValidateImageIntesity(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj";
		String emtpyName = "";
		String empty2 = " ";
		
		String correctName1 = "1";
		String correctName2 = "2";
		String correctName3 = "3";
	
		assertEquals(true, ImageFieldsVal.validateImageIntensity(correctName1));
		assertEquals(true, ImageFieldsVal.validateImageIntensity(correctName2));
		assertEquals(true, ImageFieldsVal.validateImageIntensity(correctName3));
		
		assertEquals(false,  ImageFieldsVal.validateImageIntensity(empty2));
		assertEquals(false,  ImageFieldsVal.validateImageIntensity(badCharacter));
		assertEquals(false,  ImageFieldsVal.validateImageIntensity(bigName));
		assertEquals(false,  ImageFieldsVal.validateImageIntensity(emtpyName));
		
		
	}

}
