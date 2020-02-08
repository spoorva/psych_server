package fieldValidation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class LocationFieldsTest {
	
	@Test
	public void testValidateLocationName(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df" 
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "John & John Corp.";
		String correctName2 = "Martin Luther King, Jr.";
		String correctName3 = "Hector Sausage-Hausen";
	
		
		assertEquals(true, LocationFieldsVal.validateName(correctName1));
		assertEquals(true, LocationFieldsVal.validateName(correctName2));
		assertEquals(true, LocationFieldsVal.validateName(correctName3));
		assertEquals(true, LocationFieldsVal.validateName(numbers));
		
		assertEquals(false,  LocationFieldsVal.validateName(empty2));
		assertEquals(false,  LocationFieldsVal.validateName(badCharacter));
		assertEquals(false,  LocationFieldsVal.validateName(bigName));
		assertEquals(false,  LocationFieldsVal.validateName(emtpyName));
		
	}
	
	@Test
	public void testValidateLocationCode(){
		
		String badCodeSpace = "ABC 123";
		String bigCode ="ASDFW1233";
		String specialChar = "!@#$@#";
		String emtpyName = "";
		String empty2 = " ";
		
		String goodCode1 = "ABC123";
		String goodCode2 = "CHARAC";
		String goodCode3 = "123456";
	
		
		assertEquals(true, LocationFieldsVal.validateLocationCode(goodCode1));
		assertEquals(true, LocationFieldsVal.validateLocationCode(goodCode2));
		assertEquals(true, LocationFieldsVal.validateLocationCode(goodCode3));
		
		assertEquals(false,  LocationFieldsVal.validateLocationCode(badCodeSpace));
		assertEquals(false,  LocationFieldsVal.validateLocationCode(emtpyName));
		assertEquals(false,  LocationFieldsVal.validateLocationCode(bigCode));
		assertEquals(false,  LocationFieldsVal.validateLocationCode(specialChar));
		assertEquals(false,  LocationFieldsVal.validateLocationCode(empty2));
		
	}
	
	@Test
	public void testValidateAddressLine1(){
		
		String bigAddressLine1 = "HANDUNL ALSKDJFL ASLKF ASDKJFALKSJDF  ASLKDJFLASKJDF ALKSJDFL ASLKDFJ"
				+ "ASLKDJFALSKFDJLA ALSJKD FLKASJ DFLK SLKFJ LKAJS DFLKJA SLKDFJ LAKSJ DF"
				+ "KLSAJDFLKJSDLKFJ ALKSDJFLKAJ SDFLKJ ALSKDJ FLKASJD FLKJ ALKSDJF LKAJSDF ALSDFJ"
				+ "LAKSJDF LKAJSDFL ALKSJDF LKAJ DFLK ALKSDJF LKASJFD  ALKSJF LKASJ FD";
		String emtpyName = "";
		
		String goodCode1 = "360 HUNTINGTON AVE";
		String goodCode2 = "12 - B @ Chapper hill";
		String empty2 = " ";
		
		assertEquals(true, LocationFieldsVal.validateAddressLine1(goodCode1));
		assertEquals(true, LocationFieldsVal.validateAddressLine1(goodCode2));
		
		assertEquals(false,  LocationFieldsVal.validateAddressLine1(bigAddressLine1));
		assertEquals(false,  LocationFieldsVal.validateAddressLine1(emtpyName));
		assertEquals(false,  LocationFieldsVal.validateAddressLine1(empty2));
		
	}
	
	@Test
	public void testValidateAddressLine2(){
		
		String bigAddressLine2 = "HANDUNL ALSKDJFL ASLKF ASDKJFALKSJDF  ASLKDJFLASKJDF ALKSJDFL ASLKDFJ"
				+ "ASLKDJFALSKFDJLA ALSJKD FLKASJ DFLK SLKFJ LKAJS DFLKJA SLKDFJ LAKSJ DF"
				+ "KLSAJDFLKJSDLKFJ ALKSDJFLKAJ SDFLKJ ALSKDJ FLKASJD FLKJ ALKSDJF LKAJSDF ALSDFJ"
				+ "LAKSJDF LKAJSDFL ALKSJDF LKAJ DFLK ALKSDJF LKASJFD  ALKSJF LKASJ FD";
		String emtpyName = "";
		String empty2 = " ";
		
		String goodCode1 = "OPP. place";
		String goodCode2 = "12 - B @ Chapper hill";
	
		
		assertEquals(true, LocationFieldsVal.validateAddressLine2(goodCode1));
		assertEquals(true, LocationFieldsVal.validateAddressLine2(goodCode2));
		assertEquals(true,  LocationFieldsVal.validateAddressLine2(emtpyName));
		assertEquals(true,  LocationFieldsVal.validateAddressLine2(empty2));
		
		assertEquals(false,  LocationFieldsVal.validateAddressLine2(bigAddressLine2));
		
	}
	
	@Test
	public void testValidateCity(){
		
		String cityWithNumber = "Boston 123";
		String specialChars = "Boston !!";
		String emtpyName = "";
		String empty2 = " ";
		String bigName = "HANDUNL ALSKDJFL ASLKF ASDKJFALKSJDF  ASLKDJFLASKJDF ALKSJDFL ASLKDFJ"
				+ "ASLKDJFALSKFDJLA ALSJKD FLKASJ DFLK SLKFJ LKAJS DFLKJA SLKDFJ LAKSJ DF"
				+ "KLSAJDFLKJSDLKFJ ALKSDJFLKAJ SDFLKJ ALSKDJ FLKASJD FLKJ ALKSDJF LKAJSDF ALSDFJ"
				+ "LAKSJDF LKAJSDFL ALKSJDF LKAJ DFLK ALKSDJF LKASJFD  ALKSJF LKASJ FD";
		
		String goodCode1 = "Boston";
		String goodCode2 = "New York";
	
		
		assertEquals(true, LocationFieldsVal.validateCity(goodCode1));
		assertEquals(true, LocationFieldsVal.validateCity(goodCode2));
		
		assertEquals(false,  LocationFieldsVal.validateCity(emtpyName));
		assertEquals(false,  LocationFieldsVal.validateCity(empty2));
		assertEquals(false,  LocationFieldsVal.validateCity(cityWithNumber));
		assertEquals(false,  LocationFieldsVal.validateCity(specialChars));
		assertEquals(false,  LocationFieldsVal.validateCity(bigName));
		
	}
	
	@Test
	public void testValidateState(){
		
		String number = "New York 123";
		String specialChars = "Colorado !!";
		String emtpyName = "";
		String empty2 = " ";
		String bigName = "HANDUNL ALSKDJFL ASLKF ASDKJFALKSJDF  ASLKDJFLASKJDF ALKSJDFL ASLKDFJ"
				+ "ASLKDJFALSKFDJLA ALSJKD FLKASJ DFLK SLKFJ LKAJS DFLKJA SLKDFJ LAKSJ DF"
				+ "KLSAJDFLKJSDLKFJ ALKSDJFLKAJ SDFLKJ ALSKDJ FLKASJD FLKJ ALKSDJF LKAJSDF ALSDFJ"
				+ "LAKSJDF LKAJSDFL ALKSJDF LKAJ DFLK ALKSDJF LKASJFD  ALKSJF LKASJ FD";
		
		String goodCode1 = "Georgia";
		String goodCode2 = "California";
	
		
		assertEquals(true, LocationFieldsVal.validateState(goodCode1));
		assertEquals(true, LocationFieldsVal.validateState(goodCode2));
		
		assertEquals(false,  LocationFieldsVal.validateState(emtpyName));
		assertEquals(false,  LocationFieldsVal.validateState(empty2));
		assertEquals(false,  LocationFieldsVal.validateState(number));
		assertEquals(false,  LocationFieldsVal.validateState(specialChars));
		assertEquals(false,  LocationFieldsVal.validateState(bigName));
		
	}
	
	@Test
	public void testValidateZipCode(){
		
		String longNumber = "123923097";
		String specialChars = "12345!";
		String emtpyName = "";
		String empty2 = " ";
		String withChars = "12345ABCD";
		
		String goodCode1 = "02120";
		String goodCode2 = "12345";
	
		assertEquals(true, LocationFieldsVal.validateZipCode(goodCode1));
		assertEquals(true, LocationFieldsVal.validateZipCode(goodCode2));
		
		assertEquals(false,  LocationFieldsVal.validateZipCode(longNumber));
		assertEquals(false,  LocationFieldsVal.validateZipCode(specialChars));
		assertEquals(false,  LocationFieldsVal.validateZipCode(emtpyName));
		assertEquals(false,  LocationFieldsVal.validateZipCode(empty2));
		assertEquals(false,  LocationFieldsVal.validateZipCode(withChars));
		
	}
	
	
	@Test
	public void testValidatePhoneNumber(){
		
		String digit11 = "12345678911";
		String digit9 = "123456789";
		String emtpyName = "";
		String empty2 = " ";
		String withChars = "123456789A";
		String withSpec = "123-456-7891";
		
		String goodCode1 = "1234567891";
		String goodCode2 = "2167864567";
	
		assertEquals(true, LocationFieldsVal.validatePhoneNumber(goodCode1));
		assertEquals(true, LocationFieldsVal.validatePhoneNumber(goodCode2));
		
		assertEquals(false,  LocationFieldsVal.validatePhoneNumber(withSpec));
		assertEquals(false,  LocationFieldsVal.validatePhoneNumber(digit11));
		assertEquals(false,  LocationFieldsVal.validatePhoneNumber(digit9));
		assertEquals(false,  LocationFieldsVal.validatePhoneNumber(emtpyName));
		assertEquals(false,  LocationFieldsVal.validatePhoneNumber(empty2));
		assertEquals(false,  LocationFieldsVal.validatePhoneNumber(withChars));
		
	}
	@Test
	public void testValidateFaxNumber(){
		
		String digit11 = "12345678911";
		String digit9 = "123456789";
		String emtpyName = "";
		String empty2 = " ";
		String withChars = "123456789A";
		String withSpec = "123-456-7891";
		
		String goodCode1 = "1234567891";
		String goodCode2 = "2167864567";
	
		assertEquals(true, LocationFieldsVal.validateFaxNumber(goodCode1));
		assertEquals(true, LocationFieldsVal.validateFaxNumber(goodCode2));
		assertEquals(true,  LocationFieldsVal.validateFaxNumber(emtpyName));
		assertEquals(true,  LocationFieldsVal.validateFaxNumber(empty2));
		
		assertEquals(false,  LocationFieldsVal.validateFaxNumber(withSpec));
		assertEquals(false,  LocationFieldsVal.validateFaxNumber(digit11));
		assertEquals(false,  LocationFieldsVal.validateFaxNumber(digit9));
		assertEquals(false,  LocationFieldsVal.validateFaxNumber(withChars));
		
	}
	
	@Test
	public void testEmailValidation(){
		
		String normalName = "abcdef";
		String noDotCom = "abcder@asdfsdf";
		String noAt = "asdkflj.google.com";
		
		String blank = "";
		String validEmail1 = "abcd@google.com";
		String validEmail2 = "something@neu.edu";
		String validEmail3 = "something.123_123@google.com";
		
		assertEquals(true, LocationFieldsVal.validateEmail(blank));
		assertEquals(true, LocationFieldsVal.validateEmail(validEmail1));
		assertEquals(true, LocationFieldsVal.validateEmail(validEmail2));
		assertEquals(true, LocationFieldsVal.validateEmail(validEmail3));
		
		assertEquals(false, LocationFieldsVal.validateEmail(normalName));
		assertEquals(false, LocationFieldsVal.validateEmail(noDotCom));
		assertEquals(false, LocationFieldsVal.validateEmail(noAt));
	}
	
}
