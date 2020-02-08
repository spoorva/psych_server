package fieldValidation;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class QuestionCategoryFieldsValTest {
	
	QuestionCategoryFieldsVal questionCategory;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  questionCategory = new QuestionCategoryFieldsVal();
	}
	
	@Test
	public void testValidateQuestionCategoryName(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df" 
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "Fitness 1";
	
		
		assertEquals(true, QuestionCategoryFieldsVal.validateQuestionCategoryName(correctName1));
		assertEquals(true, QuestionCategoryFieldsVal.validateQuestionCategoryName(numbers));
		
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionCategoryName(empty2));
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionCategoryName(badCharacter));
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionCategoryName(bigName));
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionCategoryName(emtpyName));
		
	}
	
	@Test
	public void testValidateQuestionCategoryDescription(){
		
		String goodCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df" 
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "Fitness 1";
	
		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		while (i < 1200){
			sb.append("a");
			i++;
		}
		
		String largeDesc = sb.toString();
		
		assertEquals(true, QuestionCategoryFieldsVal.validateDescription(correctName1));
		assertEquals(true, QuestionCategoryFieldsVal.validateDescription(numbers));
		
		assertEquals(true,  QuestionCategoryFieldsVal.validateDescription(empty2));
		assertEquals(true,  QuestionCategoryFieldsVal.validateDescription(goodCharacter));
		assertEquals(true,  QuestionCategoryFieldsVal.validateDescription(bigName));
		assertEquals(true,  QuestionCategoryFieldsVal.validateDescription(emtpyName));
		assertEquals(false,  QuestionCategoryFieldsVal.validateDescription(largeDesc));
		
	}
	
	@Test
	public void testValidateQuestionLabel(){
		
		String badCharacter = "Thomas __ &(!@ ASDF";
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj ";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctLabel1 = "Very happy";
		String correctLabel2 = "Very sad";
	
		
		assertEquals(true, QuestionCategoryFieldsVal.validateLabel(correctLabel1));
		assertEquals(true, QuestionCategoryFieldsVal.validateLabel(correctLabel2));
		assertEquals(true, QuestionCategoryFieldsVal.validateLabel(numbers));
		
		assertEquals(false,  QuestionCategoryFieldsVal.validateLabel(empty2));
		assertEquals(false,  QuestionCategoryFieldsVal.validateLabel(badCharacter));
		assertEquals(false,  QuestionCategoryFieldsVal.validateLabel(bigName));
		assertEquals(false,  QuestionCategoryFieldsVal.validateLabel(emtpyName));
		
	}
	
	@Test
	public void testValidateQuestionName(){
		
		String bigName = "Thomaskjasdlkfok asldfiuk Sdfjaosiduf osudflkjasdfliu ASDKFJlasdjflksjdlfkj "
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df" 
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df"
				+ "lkasjdflkjlkj ksdjf lkjasd fjalksdjflkajsdlkfj lskjdflksjdl flskjflkjsdlkfj aklsjdflksj df";
		String emtpyName = "";
		String empty2 = " ";
		
		String numbers = "asd012987 laksjdf7034";
		String correctName1 = "Fitness 1?";
	
		
		assertEquals(true, QuestionCategoryFieldsVal.validateQuestionName(correctName1));
		assertEquals(true, QuestionCategoryFieldsVal.validateQuestionName(numbers));
		
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionName(empty2));
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionName(bigName));
		assertEquals(false,  QuestionCategoryFieldsVal.validateQuestionName(emtpyName));
		
	}

}
