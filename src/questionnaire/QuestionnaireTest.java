package questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import common.Constant;
import dao.QuestionDAO;

public class QuestionnaireTest {

	Questionnaire question;
	String sessionID;

	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  question = new Questionnaire();
	  sessionID = "";
	}

	@Test
	public void testInOrderQuestionResponseSaveReqeust() throws IOException, ServletException, ParseException{
		testValidStartQuestionResponseSaveRequest();
		testValidEndQuestionResponseSaveRequest();
		assertTrue(QuestionDAO.deleteQuestionResponses(Long.parseLong(sessionID)));
		assertTrue(QuestionDAO.deleteSessionParameterAndSession(Long.parseLong(sessionID)));
	}
	
	public void testValidStartQuestionResponseSaveRequest() throws IOException, ServletException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		
		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("1");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn("0");
		when(request.getParameter(Constant.TG_ID)).thenReturn("1");
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(mockResponseData());
		when(request.getParameter(Constant.QUESTION_SESSION)).thenReturn(Constant.DEFAULT_QUESTION_SESSION);
		question.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have processed the request to insert question response", Constant.SUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
		
		sessionID = (String) jsonObject.get(Constant.SESSION_ID);
		assertTrue(sessionID!=null);
		assertTrue(!sessionID.equals("-1"));
	}

	public void testValidEndQuestionResponseSaveRequest() throws IOException, ServletException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);

		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("1");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn(sessionID);
		when(request.getParameter(Constant.TG_ID)).thenReturn("1");
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(mockResponseData());
		when(request.getParameter(Constant.QUESTION_SESSION)).thenReturn("1");
		
		question.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		assertEquals("System should have processed the request to insert question response", Constant.SUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
	}

	@Test
	public void testInvalidQuestionResoponseSaveRequest() throws IOException, ServletException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("1");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn("0");
		when(request.getParameter(Constant.TG_ID)).thenReturn("1");
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(new String[0]);

		when(response.getWriter()).thenReturn(printWriter);

		question.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		assertEquals("System should not processess the request to insert question response", Constant.UNSUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
	}
	
	@Test
	public void testInvalidQuestionResoponseSaveRequestWhenTargetIdIsInvalid() throws IOException, ServletException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);

		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("1");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn("0");
		when(request.getParameter(Constant.TG_ID)).thenReturn("");
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(mockResponseData());

		question.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		assertEquals("System should not processess the request to insert question response when target id is invalid", Constant.UNSUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
	}	
	
	@Test
	public void testInvalidQuestionResoponseSaveRequestWhenParticipantIdIsInvalid() throws IOException, ServletException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);

		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn("0");
		when(request.getParameter(Constant.TG_ID)).thenReturn("1");
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(mockResponseData());

		question.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		assertEquals("System should not processess the request to insert question response when participant id is invalid", Constant.UNSUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
	}
	
	public static String[] mockResponseData(){
		
		JSONObject obj1 = new JSONObject();
		obj1.put(Constant.RESPONSE_TYPE, "Continuous");
		obj1.put(Constant.RESPONSE, "3");
		obj1.put(Constant.QUESTION_ID, "1");

		JSONObject obj2 = new JSONObject();
		obj2.put(Constant.RESPONSE_TYPE, "Categorical");
		obj2.put(Constant.RESPONSE, "StartLabel2");
		obj2.put(Constant.QUESTION_ID, "2");

		JSONObject obj3 = new JSONObject();
		obj3.put(Constant.RESPONSE_TYPE, "Categorical");
		obj3.put(Constant.RESPONSE, "Very Happy");
		obj3.put(Constant.QUESTION_ID, "3");
		
		JSONObject obj4 = new JSONObject();
		obj4.put(Constant.RESPONSE_TYPE, "Categorical");
		obj4.put(Constant.RESPONSE, "Very Happy");
		obj4.put(Constant.QUESTION_ID, "4");
		
		JSONObject obj5 = new JSONObject();
		obj5.put(Constant.RESPONSE_TYPE, "Categorical");
		obj5.put(Constant.RESPONSE, "Very Happy");
		obj5.put(Constant.QUESTION_ID, "5");
		
		JSONObject obj6 = new JSONObject();
		obj6.put(Constant.RESPONSE_TYPE, "Categorical");
		obj6.put(Constant.RESPONSE, "Very Happy");
		obj6.put(Constant.QUESTION_ID, "6");
		
		JSONObject obj7 = new JSONObject();
		obj7.put(Constant.RESPONSE_TYPE, "Categorical");
		obj7.put(Constant.RESPONSE, "EndLabel2");
		obj7.put(Constant.QUESTION_ID, "7");
		
		JSONObject obj8 = new JSONObject();
		obj8.put(Constant.RESPONSE_TYPE, "Categorical");
		obj8.put(Constant.RESPONSE, "Very Happy");
		obj8.put(Constant.QUESTION_ID, "8");
		
		JSONObject obj9 = new JSONObject();
		obj9.put(Constant.RESPONSE_TYPE, "Categorical");
		obj9.put(Constant.RESPONSE, "EndLabel2");
		obj9.put(Constant.QUESTION_ID, "9");	
		
		JSONObject obj10 = new JSONObject();
		obj10.put(Constant.RESPONSE_TYPE, "Categorical");
		obj10.put(Constant.RESPONSE, "Very Happy");
		obj10.put(Constant.QUESTION_ID, "10");
		
		JSONObject obj11 = new JSONObject();
		obj11.put(Constant.RESPONSE_TYPE, "Categorical");
		obj11.put(Constant.RESPONSE, "Very Happy");
		obj11.put(Constant.QUESTION_ID, "11");
		
		ArrayList<String> aList = new ArrayList<>();
		aList.add(obj1.toString());
		aList.add(obj2.toString());
		aList.add(obj3.toString());
		aList.add(obj4.toString());
		aList.add(obj5.toString());
		aList.add(obj6.toString());
		aList.add(obj7.toString());
		aList.add(obj8.toString());
		aList.add(obj9.toString());
		aList.add(obj10.toString());
		aList.add(obj11.toString());
			
		//System.out.println(aList.toString());
		String[] responses = new String[1];
		responses[0] = aList.toString();
		return responses;
		
	}
}
