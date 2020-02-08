package fieldValidation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class UserProfileFieldsTest {
	
	UserProfileFieldsVal userProfile;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  userProfile = new UserProfileFieldsVal();
	}
	
	@Test
	public void testFirstLastNameFieldValidation(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "Mathias d'Arras";
		String correctName2 = "Martin Luther King, Jr.";
		String correctName3 = "Hector Sausage-Hausen";
		
		assertEquals(true, UserProfileFieldsVal.validateName(correctName1) );
		assertEquals(true, UserProfileFieldsVal.validateName(correctName2));
		assertEquals(true, UserProfileFieldsVal.validateName(correctName3));
		
		assertEquals(false, UserProfileFieldsVal.validateName(badCharacter));
		assertEquals(false, UserProfileFieldsVal.validateName(bigName));
		assertEquals(false, UserProfileFieldsVal.validateName(numbers));
		
	}
	
	@Test
	public void testPasswordValidation(){
		
		String noUpperCase = "abcde&2";
		String noNumber = "Alaksdf&";
		String noSpecialChar = "Alkad234";
		
		String correctPwd1 = "Abcde@12345";
		String correctPwd2 = "Psych@C.M.1";
		
		assertEquals(true, UserProfileFieldsVal.validatePassword(correctPwd1));
		assertEquals(true, UserProfileFieldsVal.validatePassword(correctPwd2));
		
		assertEquals(false, UserProfileFieldsVal.validatePassword(noUpperCase));
		assertEquals(false, UserProfileFieldsVal.validatePassword(noNumber));
		assertEquals(false, UserProfileFieldsVal.validatePassword(noSpecialChar));
		
	}
	
	@Test
	public void testEmailValidation(){
		
		String normalName = "abcdef";
		String noDotCom = "abcder@asdfsdf";
		String noAt = "asdkflj.google.com";
		
		String validEmail1 = "abcd@google.com";
		String validEmail2 = "something@neu.edu";
		String validEmail3 = "something.123_123@google.com";
		
		assertEquals(true, UserProfileFieldsVal.validateEmail(validEmail1));
		assertEquals(true, UserProfileFieldsVal.validateEmail(validEmail2));
		assertEquals(true, UserProfileFieldsVal.validateEmail(validEmail3));
		
		assertEquals(false, UserProfileFieldsVal.validateEmail(normalName));
		assertEquals(false, UserProfileFieldsVal.validateEmail(noDotCom));
		assertEquals(false, UserProfileFieldsVal.validateEmail(noAt));
	}
}
