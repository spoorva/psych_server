package authentication;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import common.Constant;

public class AdminAuthenticationServletTest {
	
	AdminAuthenticationServlet adminAuthentication;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  adminAuthentication = new AdminAuthenticationServlet();
	}
	
	@Test
	public void testValidAdminLoginAuthentication() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.EMAIL, "patel.dars@husky.neu.edu");
		jsonObj.put(Constant.PASSWORD, "Abcde@12345");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(true)).thenReturn(session);
		//when(request.getParameter(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		//when(request.getParameter(Constant.PASSWORD)).thenReturn("abcd");
		
		adminAuthentication.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have allowed the user to login as the credentials are valide",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		verify(session).setAttribute(Constant.EMAIL, "patel.dars@husky.neu.edu");
		verify(session).setAttribute(Constant.ROLE, "GlobalAdministrator");
		verify(session).setAttribute(Constant.FIRST_NAME, "Darshan");
		verify(session).setAttribute(Constant.LAST_NAME, "Patel");
		
	}
	
	@Test
	public void testInvalidAdminLoginAuthentication() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(true)).thenReturn(session);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.EMAIL, "woeura#$%#$nssknlkj");
		jsonObj.put(Constant.PASSWORD, "weoiru2l3kn4234908");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		//when(request.getParameter(Constant.EMAIL)).thenReturn("woeura#$%#$nssknlkj");
		//when(request.getParameter(Constant.PASSWORD)).thenReturn("weoiru2l3kn4234908");
		
		adminAuthentication.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have unauthorized the user to login",
				Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testValidSessionPageRefresh() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.EMAIL, "woeura#$%#$nssknlkj");
		jsonObj.put(Constant.PASSWORD, "weoiru2l3kn4234908");
		
		when(request.getParameter(Constant.LOGGED_IN)).thenReturn(Constant.YES);
		String email = "patel.dars@husky.neu.edu";
		String role = "GlobalAdministrator";
		String firstName = "Darshan";
		String lastName = "Patel";
		String userId = "1";
		
		when(session.getAttribute(Constant.EMAIL)).thenReturn(email);
		when(session.getAttribute(Constant.ROLE)).thenReturn(role);
		when(session.getAttribute(Constant.FIRST_NAME)).thenReturn(firstName);
		when(session.getAttribute(Constant.LAST_NAME)).thenReturn(lastName);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(userId);
		
		//when(request.getParameter(Constant.PASSWORD)).thenReturn("weoiru2l3kn4234908");
		
		adminAuthentication.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have unauthorized the user to login",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		assertEquals(email, (String) jsonObject.get(Constant.EMAIL));
		assertEquals(role, (String) jsonObject.get(Constant.ROLE));
		assertEquals(firstName, (String) jsonObject.get(Constant.FIRST_NAME));
		assertEquals(lastName, (String) jsonObject.get(Constant.LAST_NAME));
		assertEquals(userId, (String) jsonObject.get(Constant.USER_ID));
		
	}
	
	@Test
	public void testInValidSessionPageRefresh() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.EMAIL, "woeura#$%#$nssknlkj");
		jsonObj.put(Constant.PASSWORD, "weoiru2l3kn4234908");
		
		when(request.getParameter(Constant.LOGGED_IN)).thenReturn(Constant.YES);
		
		adminAuthentication.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System shouldn't have unauthorized the user to login",
				Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testInValidPageRefreshRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		when(response.getWriter()).thenReturn(printWriter);

		String email = "patel.dars@husky.neu.edu";
		String role = "GlobalAdministrator";
		String firstName = "Darshan";
		String lastName = "Patel";
		String userId = "1";
		
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.EMAIL)).thenReturn(email);
		when(session.getAttribute(Constant.ROLE)).thenReturn(role);
		when(session.getAttribute(Constant.FIRST_NAME)).thenReturn(firstName);
		when(session.getAttribute(Constant.LAST_NAME)).thenReturn(lastName);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(userId);
		
		adminAuthentication.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System shouldn't have unauthorized the user to login",
				Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testValidLogoutRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		
		JSONObject jsonObj = new JSONObject();
		
		String email = "patel.dars@husky.neu.edu";
		String role = "GlobalAdministrator";
		String firstName = "Darshan";
		String lastName = "Patel";
		String userId = "1";
		
		when(session.getAttribute(Constant.EMAIL)).thenReturn(email);
		when(session.getAttribute(Constant.ROLE)).thenReturn(role);
		when(session.getAttribute(Constant.FIRST_NAME)).thenReturn(firstName);
		when(session.getAttribute(Constant.LAST_NAME)).thenReturn(lastName);
		when(session.getAttribute(Constant.USER_ID)).thenReturn(userId);
		
		when(request.getParameter(Constant.LOGOUT)).thenReturn(Constant.YES);
		
		adminAuthentication.doGet(request, response);
		
		verify(session).invalidate();
		
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
		
		when(request.getParameter(Constant.TG_NAME)).thenReturn("ASDFJA KASDFJAE RASJFASDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		adminAuthentication.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
	

}
