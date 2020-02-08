package questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import common.Constant;
import dao.QuestionCategoryDAO;
import questionnaire.QuestionCategoryServlet;

public class QuestionCategoryServletTest {
	
	QuestionCategoryServlet questionCategoryServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  questionCategoryServlet = new QuestionCategoryServlet();
	}
	
	
	@Test
	public void testValidQuestionCategoryCreate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		Random random = new Random();
		int randomNumber = random.nextInt(1000);
		
		String newQuestionCategoryName = "Fitness-" + randomNumber;
		String newnewQuestionCategoryDescription = "";
		long newResponseType = 53;
		String newStartLabel = "Very sad";
		String newEndLabel = "Very happy";
		
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_NAME, newQuestionCategoryName);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_DESCRIPTION, newnewQuestionCategoryDescription);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_RESPONSE_TYPE, newResponseType);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_START_LABEL, newStartLabel);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_END_LABEL, newEndLabel);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
		
		boolean deleted = QuestionCategoryDAO.deleteQuestionCategory(jsonObject.get(Constant.NEW_QUESTION_CATEGORY_NAME).toString());
		
		assertEquals(true, deleted);
	}
	
	@Test
	public void testInvalidQuestionCategoryCreate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		String newQuestionCategoryName = "";
		String newnewQuestionCategoryDescription = "Designed for fitness group.";
		long newResponseType = 53;
		String newStartLabel = "Very sad";
		String newEndLabel = "Very happy";
		
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_NAME, newQuestionCategoryName);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_DESCRIPTION, newnewQuestionCategoryDescription);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_RESPONSE_TYPE, newResponseType);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_START_LABEL, newStartLabel);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_END_LABEL, newEndLabel);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.BADREQUEST_400);
	}
	
	
	
	@Test
	public void testDuplicateQuestionCategory() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getParameter("name")).thenReturn("Fitness");
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println("Duplicate?: " + jsonObject.get(Constant.RESULTS));
		assertEquals((Boolean) jsonObject.get(Constant.RESULTS), true);
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testDuplicateQuestionCategoryFalse() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getParameter("name")).thenReturn("adadfdf");
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println("Duplicate?: " + jsonObject.get(Constant.RESULTS));
		assertEquals((Boolean) jsonObject.get(Constant.RESULTS), false);
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testGetAllQuestionCategories() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println(jsonObject.get(Constant.RESULTS));
		JSONArray results = (JSONArray) jsonObject.get(Constant.RESULTS);
		assertTrue(results.size() >= 0);
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testValidDataQuestionCategoryUpdate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		Random random = new Random();
		int randomNumber = random.nextInt(1000);
		
		long questionCategoryId = 1;
		String newQuestionCategoryName = "Fitness";
		String newQuestionCategoryDescription = "Question category description. Update: " + randomNumber;
		long newResponseType = 53;
		String newStartLabel = "Very sad";
		String newEndLabel = "Very happy";
		
		jsonObj.put(Constant.QUESTION_CATEGORY_ID, questionCategoryId);
		jsonObj.put(Constant.QUESTION_CATEGORY_NAME, newQuestionCategoryName);
		jsonObj.put(Constant.QUESTION_CATEGORY_DESCRIPTION, newQuestionCategoryDescription);
		jsonObj.put(Constant.QUESTION_CATEGORY_RESPONSE_TYPE, newResponseType);
		jsonObj.put(Constant.QUESTION_CATEGORY_START_LABEL, newStartLabel);
		jsonObj.put(Constant.QUESTION_CATEGORY_END_LABEL, newEndLabel);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testInvalidDataQuestionCategoryUpdate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		long questionCategoryId = 1;
		String newQuestionCategoryName = "";
		String newQuestionCategoryDescription = "";
		long newResponseType = 53;
		String newStartLabel = "Very sad";
		String newEndLabel = "Very happy";
		
		jsonObj.put(Constant.QUESTION_CATEGORY_ID, questionCategoryId);
		jsonObj.put(Constant.QUESTION_CATEGORY_NAME, newQuestionCategoryName);
		jsonObj.put(Constant.QUESTION_CATEGORY_DESCRIPTION, newQuestionCategoryDescription);
		jsonObj.put(Constant.QUESTION_CATEGORY_RESPONSE_TYPE, newResponseType);
		jsonObj.put(Constant.QUESTION_CATEGORY_START_LABEL, newStartLabel);
		jsonObj.put(Constant.QUESTION_CATEGORY_END_LABEL, newEndLabel);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionCategoryServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.BADREQUEST_400);
	}
	
	@Test
	public void testBadParsingDataPostError()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(bufferedReader.readLine()).thenReturn("SDF465456456456456456sdfgsdfgsdfgasdf").thenReturn(null);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		questionCategoryServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testBadParsingDataPutError()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(bufferedReader.readLine()).thenReturn("SDF465456456456456456sdfgsdfgsdfgasdf").thenReturn(null);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		questionCategoryServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
}
