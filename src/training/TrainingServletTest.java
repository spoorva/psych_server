package training;

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
import dao.TrainingDAO;

public class TrainingServletTest {
	
	TrainingServlet trainingServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  trainingServlet = new TrainingServlet();
	}
	
	@Test
	public void testInValidSessionTrainingDropDownRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		trainingServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have blocked the request with invalid session"
				,Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	
	@Test
	public void testValidTrainingDropDownRequest() throws ServletException, IOException, ParseException{
		
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
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		when(request.getParameter(Constant.TRAINING_DROPDOWN)).thenReturn(Constant.YES);
		
		trainingServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have successfully processed the search request with valid session and search parameter",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		assertTrue("System should have returned at least one location information.",((JSONArray)jsonObject.get(Constant.RESULTS)).size() >= 1);
		
	}
	
	
	
	@Test
	public void testValidInputCreateTraining() throws ServletException, IOException, ParseException{
		
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
		
		JSONArray questions = new JSONArray();
		JSONObject question = new JSONObject();
		question.put(Constant.QUESTION_ID, 1);
		questions.add(question);
		
		JSONObject question2 = new JSONObject();
		question2.put(Constant.QUESTION_ID, 2);
		questions.add(question2);
		
		JSONArray images = new JSONArray();
		
		JSONObject image1 = new JSONObject();
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 55);
		image1.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image1.put(Constant.TRG_IMAGE_MAP_DURATION, 2000);
		
		JSONObject image2 = new JSONObject();
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 56);
		image2.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image2.put(Constant.TRG_IMAGE_MAP_DURATION, 3000);
		
		
		images.add(image1);
		images.add(image2);
		
		String newTrainingName = "TrainingTestName" + randomNumber;
		String newTrainingDescription = "TrainingTestName";
		String newTrainingKeywords = "TrainingKeyword1,Trainingkeyword2";
		
		jsonObj.put(Constant.TRAINING_NAME, newTrainingName);
		jsonObj.put(Constant.TRAINING_DESCRIPTION, newTrainingDescription);
		jsonObj.put(Constant.TRAINING_KEYWORDS, newTrainingKeywords);
		jsonObj.put(Constant.TRAINING_QUESTIONS, questions);
		jsonObj.put(Constant.TRAINING_IMAGES, images);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(4l);
		
		trainingServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
		
		System.out.println(jsonObject.get(Constant.TRAINING_ID));
		boolean deleted = TrainingDAO.deleteTraining((long) jsonObject.get(Constant.TRAINING_ID));
		
		assertEquals(true, deleted);
		
	}
	
	
	@Test
	public void testInvalidInputCreateTraining() throws ServletException, IOException, ParseException{
		
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
		
		JSONArray questions = new JSONArray();
		JSONObject question = new JSONObject();
		question.put(Constant.QUESTION_ID, 1);
		questions.add(question);
		
		JSONObject question2 = new JSONObject();
		question2.put(Constant.QUESTION_ID, 2);
		questions.add(question2);
		
		JSONArray images = new JSONArray();
		
		JSONObject image1 = new JSONObject();
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 55);
		image1.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image1.put(Constant.TRG_IMAGE_MAP_DURATION, 2000);
		
		JSONObject image2 = new JSONObject();
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 56);
		image2.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image2.put(Constant.TRG_IMAGE_MAP_DURATION, 3000);
		
		
		images.add(image1);
		images.add(image2);
		
		String newTrainingName = "";
		String newTrainingDescription = "TrainingTestName";
		String newTrainingKeywords = "TrainingKeyword1,Trainingkeyword2";
		
		jsonObj.put(Constant.TRAINING_NAME, newTrainingName);
		jsonObj.put(Constant.TRAINING_DESCRIPTION, newTrainingDescription);
		jsonObj.put(Constant.TRAINING_KEYWORDS, newTrainingKeywords);
		jsonObj.put(Constant.TRAINING_QUESTIONS, questions);
		jsonObj.put(Constant.TRAINING_IMAGES, images);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(4l);
		
		trainingServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.BADREQUEST_400);		
	}
	
	@Test
	public void testValidInputUpdateTraining() throws ServletException, IOException, ParseException{
		
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
		
		JSONArray questions = new JSONArray();
		JSONObject question = new JSONObject();
		question.put(Constant.QUESTION_ID, 1);
		questions.add(question);
		
		JSONObject question2 = new JSONObject();
		question2.put(Constant.QUESTION_ID, 2);
		questions.add(question2);
		
		JSONArray images = new JSONArray();
		
		JSONObject image1 = new JSONObject();
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 55);
		image1.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image1.put(Constant.TRG_IMAGE_MAP_DURATION, 2000);
		
		JSONObject image2 = new JSONObject();
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 56);
		image2.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image2.put(Constant.TRG_IMAGE_MAP_DURATION, 3000);
		
		
		images.add(image1);
		images.add(image2);
		
		String newTrainingName = "Northeastern University";
		String newTrainingDescription = "TrainingTestName";
		String newTrainingKeywords = "TrainingKeyword1,Trainingkeyword2";
		
		jsonObj.put(Constant.TRAINING_ID, 1);
		jsonObj.put(Constant.TRAINING_NAME, newTrainingName);
		jsonObj.put(Constant.TRAINING_DESCRIPTION, newTrainingDescription);
		jsonObj.put(Constant.TRAINING_KEYWORDS, newTrainingKeywords);
		jsonObj.put(Constant.TRAINING_QUESTIONS, questions);
		jsonObj.put(Constant.TRAINING_IMAGES, images);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(4l);
		
		trainingServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);		
	}
	
	@Test
	public void testInvalidInputUpdateTraining() throws ServletException, IOException, ParseException{
		
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
		
		JSONArray questions = new JSONArray();
		JSONObject question = new JSONObject();
		question.put(Constant.QUESTION_ID, 1);
		questions.add(question);
		
		JSONObject question2 = new JSONObject();
		question2.put(Constant.QUESTION_ID, 2);
		questions.add(question2);
		
		JSONArray images = new JSONArray();
		
		JSONObject image1 = new JSONObject();
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image1.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 55);
		image1.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image1.put(Constant.TRG_IMAGE_MAP_DURATION, 2000);
		
		JSONObject image2 = new JSONObject();
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_CAT, 1);
		image2.put(Constant.TRG_IMAGE_MAP_IMAGE_TYPE, 56);
		image2.put(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES, 2);
		image2.put(Constant.TRG_IMAGE_MAP_DURATION, 3000);
		
		
		images.add(image1);
		images.add(image2);
		
		String newTrainingName = " ";
		String newTrainingDescription = "TrainingTestName";
		String newTrainingKeywords = "TrainingKeyword1,Trainingkeyword2";
		
		jsonObj.put(Constant.TRAINING_ID, 1);
		jsonObj.put(Constant.TRAINING_NAME, newTrainingName);
		jsonObj.put(Constant.TRAINING_DESCRIPTION, newTrainingDescription);
		jsonObj.put(Constant.TRAINING_KEYWORDS, newTrainingKeywords);
		jsonObj.put(Constant.TRAINING_QUESTIONS, questions);
		jsonObj.put(Constant.TRAINING_IMAGES, images);
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(4l);
		
		trainingServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.BADREQUEST_400);		
	}
	
	@Test
	public void testGetTrainingDetailsById() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(request.getParameter(Constant.TRAINING_ID)).thenReturn("1");
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		trainingServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		//System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		System.out.println(jsonObject.get(Constant.DEVELOPER_MESSAGE));
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}

}
