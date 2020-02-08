package imageData;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import common.Constant;
import dao.ImageDAO;
import dao.SessionDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class ImageDataServletTest {
	
	ImageDataServlet image;	
	String sessionId;
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  image = new ImageDataServlet();
	  sessionId = null;
	}

	@Test
	public void testValidImageResponseSaveRequest() throws IOException, ServletException, ParseException {
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		
		Long newSessionId = SessionDAO.createNewSession("1", 1l);
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(mockImageResponseData());
		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("1");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn(Long.toString(newSessionId));
		
		image.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should have processed the request to insert image response", Constant.SUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
		assertTrue(ImageDAO.deleteImageResponses(newSessionId));
		assertTrue(SessionDAO.deleteSession(newSessionId));
	
	}
	
	
	@Test
	public void testInValidImageResponseSaveRequest() throws IOException, ServletException, ParseException {
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		when(response.getWriter()).thenReturn(printWriter);
		
		when(request.getParameterValues(Constant.RESPONSES)).thenReturn(mockImageResponseData());
		when(request.getParameter(Constant.PARTICIPANTID)).thenReturn("1");
		when(request.getParameter(Constant.SESSION_ID)).thenReturn("");
		
		image.doPost(request, response);

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals("System should not have processed the request to insert image response", Constant.UNSUCCESSFUL,
				(String) jsonObject.get(Constant.SAVE));
	
	}
	
public static String[] mockImageResponseData(){
		
		JSONObject obj1 = new JSONObject();
		obj1.put(Constant.IMAGE_ID, "1");
		obj1.put(Constant.IMAGE_CATEGORY_ID, "1");
		obj1.put(Constant.IMAGE_TYPE_ID, "55");
		obj1.put(Constant.BACKGROUND_COLOR, "#5377fd");
		obj1.put(Constant.RESPONSE_TIME, "2300");
		obj1.put(Constant.CORRECTNESS, Constant.TRUE);		
		obj1.put(Constant.IS_ATTEMPTED, Constant.FALSE);
		
		
		JSONObject obj2 = new JSONObject();
		obj2.put(Constant.IMAGE_ID, "2");
		obj2.put(Constant.IMAGE_CATEGORY_ID, "1");
		obj2.put(Constant.IMAGE_TYPE_ID, "55");
		obj2.put(Constant.BACKGROUND_COLOR, "#5377fd");
		obj2.put(Constant.RESPONSE_TIME, "3200");
		obj2.put(Constant.CORRECTNESS, Constant.FALSE);
		obj2.put(Constant.IS_ATTEMPTED, Constant.TRUE);
		
		
		JSONObject obj3 = new JSONObject();
		obj3.put(Constant.IMAGE_ID, "3");
		obj3.put(Constant.IMAGE_CATEGORY_ID, "2");
		obj3.put(Constant.IMAGE_TYPE_ID, "56");
		obj3.put(Constant.BACKGROUND_COLOR, "#a3597b");
		obj3.put(Constant.RESPONSE_TIME, "1800");
		obj3.put(Constant.CORRECTNESS, Constant.TRUE);
		obj3.put(Constant.IS_ATTEMPTED, Constant.FALSE);
		
		
		JSONObject obj4 = new JSONObject();
		obj4.put(Constant.IMAGE_ID, "4");
		obj4.put(Constant.IMAGE_CATEGORY_ID, "2");
		obj4.put(Constant.IMAGE_TYPE_ID, "56");
		obj4.put(Constant.BACKGROUND_COLOR, "#a3597b");
		obj4.put(Constant.RESPONSE_TIME, "2200");
		obj4.put(Constant.CORRECTNESS, Constant.FALSE);
		obj4.put(Constant.IS_ATTEMPTED, Constant.TRUE);	

		
		ArrayList<String> aList = new ArrayList<>();
		aList.add(obj1.toString());
		aList.add(obj2.toString());
		aList.add(obj3.toString());
		aList.add(obj4.toString());
	
			
		//System.out.println(aList.toString());
		String[] images = new String[1];
		images[0] = aList.toString();
		return images;
		
	}
	
	


}
