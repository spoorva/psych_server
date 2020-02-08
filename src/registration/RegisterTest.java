package registration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import common.Constant;
import dao.LocationDAO;
import dao.ParticipantDAO;
import location.LocationServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@FixMethodOrder(MethodSorters.JVM)
public class RegisterTest {
	
	Register register;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	String username1 = "james";
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  register = new Register();
	}	
	
	
	@Test
	public void testToCheckForRegisteredUsersAndDuplicatedParticipants() throws ServletException, IOException, ParseException{
		testValidParticipantRegisterReqest();
		testDuplicateParticipants();
		
	}
	
	public void testValidParticipantRegisterReqest()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.USERNAME)).thenReturn(username1);
		when(request.getParameter(Constant.PASSWORD)).thenReturn("bond");
		when(request.getParameter(Constant.AGE)).thenReturn("28");
		when(request.getParameter(Constant.ETHNICITY)).thenReturn("Asian");
		when(request.getParameter(Constant.GENDER)).thenReturn("Male");
		when(request.getParameter(Constant.DISABILITY)).thenReturn("true");
		when(request.getParameter(Constant.MOBILE_EXPE)).thenReturn("Average");
		when(request.getParameter(Constant.PSYCOMEDS)).thenReturn("None");
		when(request.getParameter(Constant.COLOR)).thenReturn("true");
		when(request.getParameter(Constant.EDUCATION)).thenReturn("Graduate");
		when(request.getParameter(Constant.REGCODE)).thenReturn("ABCD120001");
			
		when(response.getWriter()).thenReturn(printWriter);
		
		register.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("1", (String) jsonObject.get(Constant.STATUS));
		
	}
	
	public void testDuplicateParticipants() throws ServletException, IOException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.USERNAME)).thenReturn(username1);
		when(request.getParameter(Constant.PASSWORD)).thenReturn("bond");
		when(request.getParameter(Constant.AGE)).thenReturn("28");
		when(request.getParameter(Constant.ETHNICITY)).thenReturn("Asian");
		when(request.getParameter(Constant.GENDER)).thenReturn("Male");
		when(request.getParameter(Constant.DISABILITY)).thenReturn("true");
		when(request.getParameter(Constant.MOBILE_EXPE)).thenReturn("Average");
		when(request.getParameter(Constant.PSYCOMEDS)).thenReturn("None");
		when(request.getParameter(Constant.COLOR)).thenReturn("true");
		when(request.getParameter(Constant.EDUCATION)).thenReturn("Graduate");
		when(request.getParameter(Constant.REGCODE)).thenReturn("ABCD120001");
			
		when(response.getWriter()).thenReturn(printWriter);
		
		register.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("0", (String) jsonObject.get(Constant.STATUS));
		ParticipantDAO.deleteParticipant(username1);
	}
	
	@Test
	public void testInvalidParticipantsRegCode() throws ServletException, IOException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.USERNAME)).thenReturn("kiran");
		when(request.getParameter(Constant.PASSWORD)).thenReturn("abcd");
		when(request.getParameter(Constant.AGE)).thenReturn("26");
		when(request.getParameter(Constant.GENDER)).thenReturn("Male");
		when(request.getParameter(Constant.ETHNICITY)).thenReturn("Asian");
		when(request.getParameter(Constant.DISABILITY)).thenReturn("true");
		when(request.getParameter(Constant.EDUCATION)).thenReturn("Graduate");
		when(request.getParameter(Constant.MOBILE_EXPE)).thenReturn("Average");
		when(request.getParameter(Constant.PSYCOMEDS)).thenReturn("None");
		when(request.getParameter(Constant.COLOR)).thenReturn("true");
		when(request.getParameter(Constant.REGCODE)).thenReturn("PQRST&*(");
		
		when(response.getWriter()).thenReturn(printWriter);
		
		register.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("0", (String) jsonObject.get(Constant.STATUS));
		
	}
	
		
}
