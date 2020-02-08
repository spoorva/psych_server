package questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
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
import dao.QuestionDAO;

public class QuestionServletTest {
	
	QuestionServlet questionServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  questionServlet = new QuestionServlet();
	}
	
	
	@Test
	public void testValidQuestionCreate() throws ServletException, IOException, ParseException{
		
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
		
		String newQuestionName = "How do you feel? -" + randomNumber;
		String newnewQuestionDescription = "";
		long newQuestionCategoryId = 1;
		
		jsonObj.put(Constant.NEW_QUESTION_NAME, newQuestionName);
		jsonObj.put(Constant.NEW_QUESTION_DESCRIPTION, newnewQuestionDescription);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_ID, newQuestionCategoryId);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
		
		boolean deleted = QuestionDAO.deleteQuestion(jsonObject.get(Constant.NEW_QUESTION_NAME).toString());
		
		assertEquals(true, deleted);
	}
	
	@Test
	public void testInvalidQuestionCreate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		String newQuestionName = "";
		String newnewQuestionDescription = "";
		long newQuestionCategoryId = 1;
		
		jsonObj.put(Constant.NEW_QUESTION_NAME, newQuestionName);
		jsonObj.put(Constant.NEW_QUESTION_DESCRIPTION, newnewQuestionDescription);
		jsonObj.put(Constant.NEW_QUESTION_CATEGORY_ID, newQuestionCategoryId);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.BADREQUEST_400);
	}
	
	@Test
	public void testValidQuestionUpdate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		long questionId = 1;
		String newQuestionName = "How do you feel today?";
		String newnewQuestionDescription = "";
		long newQuestionCategoryId = 1;
		
		jsonObj.put(Constant.QUESTION_NAME, newQuestionName);
		jsonObj.put(Constant.QUESTION_DESCRIPTION, newnewQuestionDescription);
		jsonObj.put(Constant.QUESTION_CATEGORY_ID, newQuestionCategoryId);
		jsonObj.put(Constant.QUESTION_ID, questionId);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testInvalidQuestionUpdate() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		long questionId = 1;
		String newQuestionName = " ";
		String newnewQuestionDescription = "";
		long newQuestionCategoryId = 1;
		
		jsonObj.put(Constant.QUESTION_NAME, newQuestionName);
		jsonObj.put(Constant.QUESTION_DESCRIPTION, newnewQuestionDescription);
		jsonObj.put(Constant.QUESTION_CATEGORY_ID, newQuestionCategoryId);
		jsonObj.put(Constant.QUESTION_ID, questionId);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.BADREQUEST_400);
	}
	
//	@Test
//	public void testGetAllQuestions() throws ServletException, IOException, ParseException{
//		
//		request = mock(HttpServletRequest.class);
//		response = mock(HttpServletResponse.class);
//		session = mock(HttpSession.class);
//		
//		StringWriter stringWriter = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(stringWriter);
//		
//		BufferedReader bufferedReader = mock(BufferedReader.class);
//		when(request.getReader()).thenReturn(bufferedReader);
//		
//		
//		
//		when(response.getWriter()).thenReturn(printWriter);
//		when(request.getSession(false)).thenReturn(session);
//		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
//		when(session.getAttribute(Constant.USER_ID)).thenReturn(4l);
//		
//		questionServlet.doGet(request, response);
//		
//		JSONParser parser = new JSONParser();
//		Object obj = parser.parse(stringWriter.getBuffer().toString());
//		JSONObject jsonObject = (JSONObject) obj;
//		
//		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
//		System.out.println(jsonObject.get(Constant.RESULTS));
//		JSONArray results = (JSONArray) jsonObject.get(Constant.RESULTS);
//		assertTrue(results.size() >= 0);
//		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
//	}
	
	@Test
	public void testGetAllQuestionsByTargetGroupId() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(request.getParameter(Constant.TARGET_GROUP_ID)).thenReturn("1");
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		
		questionServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println(jsonObject.get(Constant.RESULTS));
		JSONArray results = (JSONArray) jsonObject.get(Constant.RESULTS);
		assertTrue(results.size() > 0);
		
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testGetAllQuestionsByTargetGroupIdNotInTable() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(request.getParameter(Constant.TARGET_GROUP_ID)).thenReturn("1000000000");
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		when(request.getParameter(Constant.SOURCE)).thenReturn(Constant.ANDROID);
		
		questionServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println(jsonObject.get(Constant.RESULTS));
		JSONArray results = (JSONArray) jsonObject.get(Constant.RESULTS);
		
		assertTrue(results.size() == 0);
		
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
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
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		questionServlet.doPost(request, response);
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
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		questionServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}

}
