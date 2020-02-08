package authentication;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import common.Constant;
import registration.Register;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AuthenticatingUserTest {
	AuthenticatingUser authenticatingUser;
	HttpServletRequest request;
	HttpServletResponse response;
	String username1 = "parasar";
	String password = "parasar";
	String username2 = "abcd";
	String password2 = "efgh";
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  authenticatingUser = new AuthenticatingUser();
	}	
	
	@Test
	public void IsParticipantValid()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.USERNAME)).thenReturn(username1);
		when(request.getParameter(Constant.PASSWORD)).thenReturn(password);
			
		when(response.getWriter()).thenReturn(printWriter);
		
		authenticatingUser.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("1", (String) jsonObject.get(Constant.SUCCESS));
		
	}
	
	@Test
	public void WhenParticipantInValid()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.USERNAME)).thenReturn(username2);
		when(request.getParameter(Constant.PASSWORD)).thenReturn(password2);
			
		when(response.getWriter()).thenReturn(printWriter);
		
		authenticatingUser.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("0", (String) jsonObject.get(Constant.SUCCESS));
		
	}
	
	
	
	
	
	
	
	
	
	
}