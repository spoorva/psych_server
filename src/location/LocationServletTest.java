package location;
import org.junit.runners.MethodSorters;
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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import common.Constant;
import dao.LocationDAO;

@FixMethodOrder(MethodSorters.JVM)
public class LocationServletTest {
	
	LocationServlet locationServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	String locationName = "Northeastern University TEST";
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  locationServlet = new LocationServlet();
	}
	
	@Test
	public void testLocationCreateRequestInOrder() throws ServletException, IOException, ParseException {
		testValidLocationCreateRequest(); 
		testInValidDuplicateLocationCreateRequest(); 
	}
	
	public void testValidLocationCreateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(Constant.LOCATION_NAME, locationName);
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "Northeastern", "Psychology"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Avenue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "Boston");
		jsonObj.put(Constant.LOCATION_CODE, "AB12EF");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_EMAIL, "ssp@google.com");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have processed the request to create location",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		
	}
	
	@Test
	public void testValidLocationCreateRequestWithBlankFaxNumber() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(Constant.LOCATION_NAME, locationName);
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "Northeastern", "Psychology"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Avenue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "Boston");
		jsonObj.put(Constant.LOCATION_CODE, "AB12EF");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "");
		jsonObj.put(Constant.LOCATION_EMAIL, "ssp@google.com");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have processed the request to create location",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		LocationDAO.deleteLocation(locationName);
		
	}
	
	public void testInValidDuplicateLocationCreateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.LOCATION_NAME, locationName);
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "Northeastern University is a private "
				+ "institution that was founded in 1898. It has a total undergraduate enrollment of 13,697, "
				+ "its setting is urban, and the campus size is 73 acres. It utilizes a semester-based academic "
				+ "calendar. Northeastern University's ranking in the 2017 edition of Best Colleges is National "
				+ "Universities, 39. Its tuition and fees are $47,655 (2016-17).");
		String[] locationKeywords = new String[] { "Northeastern", "Psychology"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Avenue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "Boston");
		jsonObj.put(Constant.LOCATION_CODE, "ABBDE2");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_EMAIL, "northeastern@neu.edu");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should not have processed the request to create location",
				Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
		// Delete the records after test is completed
		LocationDAO.deleteLocation(locationName);
	}
	
	@Test
	public void testInValidLocationCreateRequestMissingMandatoryFields() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.LOCATION_NAME, "");
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "");
		jsonObj.put(Constant.LOCATION_KEYWORDS, "");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "");
		jsonObj.put(Constant.LOCATION_CODE, "");
		jsonObj.put(Constant.LOCATION_STATE_ID, "");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "");
		jsonObj.put(Constant.LOCATION_EMAIL, "");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have ignored the request with bad parameter",
				Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testInValidSessionLocationCreateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put(Constant.LOCATION_NAME, locationName);
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "Northeastern", "Psychology"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Avenue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "Boston");
		jsonObj.put(Constant.LOCATION_CODE, "AB12EF");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_EMAIL, "ssp@google.com");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		locationServlet.doPost(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have blocked the request with invalid session"
				,Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testInValidSessionLocationSearchRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		locationServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have blocked the request with invalid session"
				,Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testValidLocationSearchRequest() throws ServletException, IOException, ParseException{
		
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
		
		locationServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have successfully processed the search request with valid session and search parameter",
				Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
		assertTrue("System should have returned at least one location information.",((JSONArray)jsonObject.get(Constant.RESULTS)).size() >= 1);
		
	}
	
	@Test
	public void testInValidSessionLocationUpdateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.LOCATION_ID, "4");
		jsonObj.put(Constant.LOCATION_NAME, "Northeastern University TEST");
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "");
		String[] locationKeywords = new String[] { "Northeastern", "Psychology"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Avenue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "Boston");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_EMAIL, "northeastern@neu.edu");
		jsonObj.put(Constant.LOCATION_CODE, "ABCD12");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		locationServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have blocked the request with invalid session"
				,Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	
	@Test
	public void testValidLocationUpdateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.LOCATION_ID, "1");
		jsonObj.put(Constant.LOCATION_NAME, "Northeastern University");
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "Description Added");
		String[] locationKeywords = new String[] { "Northeastern", "Psychology"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Avenue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "Boston");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_EMAIL, "northeastern@neu.edu");
		jsonObj.put(Constant.LOCATION_CODE, "ABCD12");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have updated the location information"
				,Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testInValidLocationUpdateRequest() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(Constant.LOCATION_ID, "20");
		jsonObj.put(Constant.LOCATION_NAME, "Northeastern University TEST");
		jsonObj.put(Constant.LOCATION_DESCRIPTION, "");
		String[] locationKeywords = new String[] {"Northeastern", "Psychology", "aoj3rlkj209lamsfm"};
		jsonObj.put(Constant.LOCATION_KEYWORDS, String.join(Constant.KEYWORD_SEPERATOR, locationKeywords));
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_1, "360 Huntington Aasdlfkjalskdjf venue");
		jsonObj.put(Constant.LOCATION_ADDRESS_LINE_2, "");
		jsonObj.put(Constant.LOCATION_CITY, "SDF lskfj  213123 ");
		jsonObj.put(Constant.LOCATION_STATE_ID, "21");
		jsonObj.put(Constant.LOCATION_ZIPCODE, "02115");
		jsonObj.put(Constant.LOCATION_PHONE_NUMBER, "12asdlfkj34567891");
		jsonObj.put(Constant.LOCATION_FAX_NUMBER, "1234567891");
		jsonObj.put(Constant.LOCATION_EMAIL, "northeasteasdfljkrn@neu.edu");
		jsonObj.put(Constant.LOCATION_CODE, "ABCDED12");
		
		when(bufferedReader.readLine()).thenReturn(jsonObj.toString()).thenReturn(null);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System shouldn't have updated the location information."
				,Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
	
	@Test
	public void testValidDuplicateLocationName()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.LOCATION_NAME)).thenReturn("Northeastern University");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(true, (Boolean) jsonObject.get(Constant.RESULTS));
		
	}
	
	@Test
	public void testNotDuplicateLocationName()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.LOCATION_NAME)).thenReturn("ASDFJA KASDFJAE RASJFASDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(false, (Boolean) jsonObject.get(Constant.RESULTS));
		
	}
	
	@Test
	public void testDuplicateLocationCode()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.LOCATION_CODE)).thenReturn("ABCD12");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(true, (Boolean) jsonObject.get(Constant.RESULTS));
		
	}
	
	@Test
	public void testNotDuplicateLocationCode()  throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(request.getParameter(Constant.LOCATION_CODE)).thenReturn("ADNAJSDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(false, (Boolean)jsonObject.get(Constant.RESULTS));
		
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
		
		locationServlet.doPost(request, response);
		
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
		
		when(request.getParameter(Constant.TG_NAME)).thenReturn("ASDFJA KASDFJAE RASJFASDF");
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1);
		
		locationServlet.doPut(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));
		
	}
}
