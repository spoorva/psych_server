package targetGroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

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
import dao.LocationDAO;
import dao.TargetGroupDAO;
import location.LocationServlet;

public class TargetGroupServletTest {
	
	TargetGroupServlet targetGroupServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	String tgName = "Drug Addicted";
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  targetGroupServlet = new TargetGroupServlet();
	}
	
	@Test
	public void testTargetGroupCreateRequestInOrder() throws ServletException, IOException, ParseException {
		testValidTargetGroupCreateRequest(); 
		testValidDuplicateTargetGroupName();
		testInValidDuplicateTargetGroupCreateRequest();
	}
	
	public void testValidTargetGroupCreateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(Constant.TG_NAME, tgName);
		jsonObj.put(Constant.TG_DESCRIPTION, "");
		String[] locationKeywords = new String[] {"TargetGroup", "Psychology"};
		jsonObj.put(Constant.TG_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.TG_LOCATION_ID, "1");
		jsonObj.put(Constant.TG_TRAINING_ID, "1");
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have processed the request to create location",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		assertTrue("System should have generated 10 digit registration code",
				((String)jsonObject.get(Constant.TG_REG_CODE)).length() == 10);
		
	}
	
	public void testInValidDuplicateTargetGroupCreateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(Constant.TG_NAME, tgName);
		jsonObj.put(Constant.TG_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "TargetGroup", "Psychology"};
		jsonObj.put(Constant.TG_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.TG_LOCATION_ID, "1");
		jsonObj.put(Constant.TG_TRAINING_ID, "1");
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should not have processed the request to create location",
				Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
		// Delete the records after test is completed
		TargetGroupDAO.deleteTargetGroup(tgName);
	}
	
	@Test
	public void testInValidTargetGroupCreateRequestMissingMandatoryFields() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.TG_NAME, "");
		jsonObj.put(Constant.TG_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "TargetGroup", "Psychology"};
		jsonObj.put(Constant.TG_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.TG_LOCATION_ID, "");
		jsonObj.put(Constant.TG_TRAINING_ID, "");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have ignored the request with bad parameter",
				Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
	}
	
	@Test
	public void testInValidSessionTargetGroupSearchRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		targetGroupServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have blocked the request with invalid session"
				,Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testValidTargetGroupSearchRequest() throws ServletException, IOException, ParseException{
		
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
		
		targetGroupServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have successfully processed the search request with valid session and search parameter",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		assertTrue("System should have returned at least one location information.",((JSONArray)jsonObject.get(Constant.RESULTS)).size() >= 1);
		
	}
	
	@Test
	public void testInValidSessionTargetGroupUpdateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.TG_ID, "1");
		jsonObj.put(Constant.TG_NAME, "Violence Edited");
		jsonObj.put(Constant.TG_DESCRIPTION, "");
		jsonObj.put(Constant.TG_KEYWORDS, "");
		jsonObj.put(Constant.TG_LOCATION_ID, "1");
		jsonObj.put(Constant.TG_TRAINING_ID, "1");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		targetGroupServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have blocked the request with invalid session"
				,Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	
	@Test
	public void testValidTargetGroupUpdateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.TG_ID, "1");
		jsonObj.put(Constant.TG_NAME, "Drug Addicted Edited");
		jsonObj.put(Constant.TG_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "TargetGroup", "Psychology"};
		jsonObj.put(Constant.TG_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.TG_LOCATION_ID, "1");
		jsonObj.put(Constant.TG_TRAINING_ID, "1");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have updated the target group information"
				,Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testInValidTargetGroupUpdateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.TG_ID, "1");
		jsonObj.put(Constant.TG_NAME, "Drug Addicted Edited");
		jsonObj.put(Constant.TG_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "TargetGroup", "Psychology"};
		jsonObj.put(Constant.TG_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.TG_LOCATION_ID, "");
		jsonObj.put(Constant.TG_TRAINING_ID, "");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System shouldn't have updated the target group information."
				,Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	public void testValidDuplicateTargetGroupName()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.TG_NAME)).thenReturn(tgName);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(true, (Boolean) jsonObject.get(Constant.RESULTS));
		
	}
	
	@Test
	public void testNotDuplicateTargetGroupName()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.TG_NAME)).thenReturn("ASDFJA KASDFJAE RASJFASDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(false, (Boolean) jsonObject.get(Constant.RESULTS));
	}
	
	@Test
	public void testBadParsingDataPostError()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(bufferedReader.readLine()).thenReturn("@#$@#$@#$@#$@#$@#$@#$").thenReturn(null);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.TG_NAME)).thenReturn("ASDFJA KASDFJAE RASJFASDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPost(request, response);
		
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
		when(bufferedReader.readLine()).thenReturn("@#$@#$@#$@#$@#$@#$@#$").thenReturn(null);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.TG_NAME)).thenReturn("ASDFJA KASDFJAE RASJFASDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		targetGroupServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
}
