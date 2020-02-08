package imageData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import common.Constant;
import dao.ImageDAO;


public class ImageUploadServletTest {
	

	ImageUploadServlet imageUploadServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;
	ServletContext servletContext;
	DiskFileItemFactory diskFileItemFactory;
	String inputFolder = "/tmp/imageInput";
	String outputFolder = "/tmp/imageOutput";
	String name;
	String updateName;
	String imagePath;
	
	@Before
	public void setUp() throws Exception {
	  MockitoAnnotations.initMocks(this);
	  imageUploadServlet = new ImageUploadServlet();
	  createFolder(inputFolder);
	  createFolder(outputFolder);
	  name = "Test Image Test Image Name";
	  updateName = name;
	}
	
	@Test
	public void testInOrder() throws Exception{
		testValidImageUploadRequest();
		testValidImageDuplicateSearchRequest();
		testGetAllImages();
		testFetchSingleImage();
		testValidImageUpdateReqeustNoImageUpdate();
		testValidImageUpdateReqeustWithImageUpdate();
		testInValidSessionImageUpdateReqeustWithImageUpdate();
		Long imageIdLong = ImageDAO.getImageIdByImageName(updateName);
		ImageDAO.deleteImageById(imageIdLong);
	}
	
	public void testValidImageUploadRequest() throws Exception {
	
		generateTestImage("test.jpg");
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		servletContext = mock(ServletContext.class);
		diskFileItemFactory = mock(DiskFileItemFactory.class);
		
		ServletConfig config = mock(ServletConfig.class);
		when(config.getServletContext()).thenReturn(servletContext);
        ServletFileUpload fileUpload = mock(ServletFileUpload.class);
        List<FileItem> files = new ArrayList<FileItem>();
        
        FileItem imageFileItem = mock(FileItem.class);
        when(imageFileItem.getFieldName()).thenReturn(Constant.IMAGE_FILE);
        when(imageFileItem.isFormField()).thenReturn(false);
        when(imageFileItem.getInputStream()).thenReturn(new FileInputStream(inputFolder+"/test.jpg"));
        when(imageFileItem.getName()).thenReturn("test.jpg");
        when(servletContext.getMimeType("test.jpg")).thenReturn("image/");
        
        FileItem imageName = mock(FileItem.class);
        when(imageName.getString()).thenReturn(name);
        when(imageName.getFieldName()).thenReturn(Constant.IMAGE_NAME);
        when(imageName.isFormField()).thenReturn(true);
        
        FileItem imageDescription = mock(FileItem.class);
        when(imageDescription.getString()).thenReturn("Test Image Description");
        when(imageDescription.getFieldName()).thenReturn(Constant.IMAGE_DESCRIPTION);
        when(imageDescription.isFormField()).thenReturn(true);
        
        FileItem imageCategoryId = mock(FileItem.class);
        when(imageCategoryId.getString()).thenReturn("1");
        when(imageCategoryId.getFieldName()).thenReturn(Constant.IMAGE_CATEGORY_ID);
        when(imageCategoryId.isFormField()).thenReturn(true);
        
        FileItem imageIntensityId = mock(FileItem.class);
        when(imageIntensityId.getString()).thenReturn("2");
        when(imageIntensityId.getFieldName()).thenReturn(Constant.IMAGE_INTENSITY);
        when(imageIntensityId.isFormField()).thenReturn(true);
        
        FileItem imageTypeId = mock(FileItem.class);
        when(imageTypeId.getString()).thenReturn("55");
        when(imageTypeId.getFieldName()).thenReturn(Constant.IMAGE_TYPE_ID);
        when(imageTypeId.isFormField()).thenReturn(true);
        
        files.add(imageFileItem);
        files.add(imageName);
        files.add(imageDescription);
        files.add(imageCategoryId);
        files.add(imageIntensityId);
        files.add(imageTypeId);
        
        when(fileUpload.parseRequest(request)).thenReturn(files);
        imageUploadServlet.setImageFolder(outputFolder);
        imageUploadServlet.setFileUpload(fileUpload);
        imageUploadServlet.doPost(request, response);
        
        JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		imagePath = (String) jsonObject.get(Constant.IMAGE_PATH);
		assertTrue(doesFileExists((outputFolder+"/"+imagePath)));
		
	}
	
	@Test
	public void testInValidImageCreateRequest() throws Exception {
	
		generateTestImage("test.jpg");
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		servletContext = mock(ServletContext.class);
		diskFileItemFactory = mock(DiskFileItemFactory.class);
		
		ServletConfig config = mock(ServletConfig.class);
		when(config.getServletContext()).thenReturn(servletContext);
        ServletFileUpload fileUpload = mock(ServletFileUpload.class);
        List<FileItem> files = new ArrayList<FileItem>();
        
        FileItem imageFileItem = mock(FileItem.class);
        when(imageFileItem.getName()).thenReturn("");
        when(imageFileItem.getFieldName()).thenReturn(Constant.IMAGE_FILE);
        when(imageFileItem.isFormField()).thenReturn(false);
        when(imageFileItem.getInputStream()).thenReturn(null);
        when(servletContext.getMimeType("test.jpg")).thenReturn("image/");
        
        FileItem imageName = mock(FileItem.class);
        when(imageName.getString()).thenReturn("");
        when(imageName.getFieldName()).thenReturn(Constant.IMAGE_NAME);
        when(imageName.isFormField()).thenReturn(true);
        
        FileItem imageDescription = mock(FileItem.class);
        when(imageDescription.getString()).thenReturn("Test Image Description");
        when(imageDescription.getFieldName()).thenReturn(Constant.IMAGE_DESCRIPTION);
        when(imageDescription.isFormField()).thenReturn(true);
        
        FileItem imageCategoryId = mock(FileItem.class);
        when(imageCategoryId.getString()).thenReturn("alksjdf");
        when(imageCategoryId.getFieldName()).thenReturn(Constant.IMAGE_CATEGORY_ID);
        when(imageCategoryId.isFormField()).thenReturn(true);
        
        FileItem imageIntensityId = mock(FileItem.class);
        when(imageIntensityId.getString()).thenReturn("2");
        when(imageIntensityId.getFieldName()).thenReturn(Constant.IMAGE_INTENSITY);
        when(imageIntensityId.isFormField()).thenReturn(true);
        
        FileItem imageTypeId = mock(FileItem.class);
        when(imageTypeId.getString()).thenReturn("55");
        when(imageTypeId.getFieldName()).thenReturn(Constant.IMAGE_TYPE_ID);
        when(imageTypeId.isFormField()).thenReturn(true);
        
        files.add(imageFileItem);
        files.add(imageName);
        files.add(imageDescription);
        files.add(imageCategoryId);
        files.add(imageIntensityId);
        files.add(imageTypeId);
        
        when(fileUpload.parseRequest(request)).thenReturn(files);
        imageUploadServlet.setImageFolder(outputFolder);
        imageUploadServlet.setFileUpload(fileUpload);
        imageUploadServlet.doPost(request, response);
        
        JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		deleteFile(inputFolder+"/test.jpg");
		
		assertEquals(Constant.BADREQUEST_400, (String) jsonObject.get(Constant.STATUS));

	}
	
	
	@Test
	public void testInValidSessionImageUploadRequest() throws Exception {
	
		generateTestImage("test.jpg");
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		when(request.getSession(false)).thenReturn(null);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		servletContext = mock(ServletContext.class);
		diskFileItemFactory = mock(DiskFileItemFactory.class);
		
		ServletConfig config = mock(ServletConfig.class);
		when(config.getServletContext()).thenReturn(servletContext);
        ServletFileUpload fileUpload = mock(ServletFileUpload.class);
        List<FileItem> files = new ArrayList<FileItem>();
        
        FileItem imageFileItem = mock(FileItem.class);
        when(imageFileItem.getFieldName()).thenReturn(Constant.IMAGE_FILE);
        when(imageFileItem.isFormField()).thenReturn(false);
        when(imageFileItem.getInputStream()).thenReturn(new FileInputStream(inputFolder+"/test.jpg"));
        when(imageFileItem.getName()).thenReturn("test.jpg");
        when(servletContext.getMimeType("test.jpg")).thenReturn("image/");
        
        FileItem imageName = mock(FileItem.class);
        when(imageName.getString()).thenReturn("Test Iamge");
        when(imageName.getFieldName()).thenReturn(Constant.IMAGE_NAME);
        when(imageName.isFormField()).thenReturn(true);
        
        FileItem imageDescription = mock(FileItem.class);
        when(imageDescription.getString()).thenReturn("Test Image Description");
        when(imageDescription.getFieldName()).thenReturn(Constant.IMAGE_DESCRIPTION);
        when(imageDescription.isFormField()).thenReturn(true);
        
        FileItem imageCategoryId = mock(FileItem.class);
        when(imageCategoryId.getString()).thenReturn("1");
        when(imageCategoryId.getFieldName()).thenReturn(Constant.IMAGE_CATEGORY_ID);
        when(imageCategoryId.isFormField()).thenReturn(true);
        
        FileItem imageIntensityId = mock(FileItem.class);
        when(imageIntensityId.getString()).thenReturn("2");
        when(imageIntensityId.getFieldName()).thenReturn(Constant.IMAGE_INTENSITY);
        when(imageIntensityId.isFormField()).thenReturn(true);
        
        FileItem imageTypeId = mock(FileItem.class);
        when(imageTypeId.getString()).thenReturn("55");
        when(imageTypeId.getFieldName()).thenReturn(Constant.IMAGE_TYPE_ID);
        when(imageTypeId.isFormField()).thenReturn(true);
        
        files.add(imageFileItem);
        files.add(imageName);
        files.add(imageDescription);
        files.add(imageCategoryId);
        files.add(imageIntensityId);
        files.add(imageTypeId);
        
        when(fileUpload.parseRequest(request)).thenReturn(files);
        imageUploadServlet.setImageFolder(outputFolder);
        imageUploadServlet.setFileUpload(fileUpload);
        imageUploadServlet.doPost(request, response);
        
        JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
	}
	
	public void generateTestImage(String imageName){
		
		try {
			 //image dimension
			 int width = 640;
			 int height = 320;
			 //create buffered image object img
			 BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			 //file object
			 File f = null;
			 //create random image pixel by pixel
			 for(int y = 0; y < height; y++){
				 for(int x = 0; x < width; x++){
			     int a = (int)(Math.random()*256); //alpha
			     int r = (int)(Math.random()*256); //red
			     int g = (int)(Math.random()*256); //green
			     int b = (int)(Math.random()*256); //blue
			 
		         int p = (a<<24) | (r<<16) | (g<<8) | b; //pixel
		         img.setRGB(x, y, p);
		         }
			}
			//write image
			File file = new File(inputFolder+"/"+imageName);
			ImageIO.write(img, "jpg", file);
		     
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void createFolder(String folderPath){
		File f = new File(folderPath);
		if(f.mkdir()){
			System.out.println(folderPath + " Directory Created");
		}
	}
	
	public boolean doesFileExists(String filePath){
		File f = new File(filePath);
		return f.exists();
	}
	
	public void deleteFile(String filePath){
		File fInput = new File(filePath);
		fInput.delete();
	}
	
	public void testValidImageDuplicateSearchRequest() throws ServletException, IOException, ParseException{
		
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
		
		when(request.getParameter(Constant.IMAGE_NAME)).thenReturn(name);
		imageUploadServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals((Boolean) jsonObject.get(Constant.RESULTS), true);
		assertEquals(Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
	}
	
	
	
	@Test
	public void testInValidImageDuplicateSearchRequest() throws ServletException, IOException, ParseException{
		
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
		
		when(request.getParameter(Constant.IMAGE_NAME)).thenReturn("#$%@DSSFD");
		imageUploadServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals((Boolean) jsonObject.get(Constant.RESULTS), false);
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	
	public void testGetAllImages() throws ServletException, IOException, ParseException{
		
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
		imageUploadServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		JSONArray results = (JSONArray) jsonObject.get(Constant.RESULTS);
		assertTrue(results.size() > 0);
		assertEquals((String) jsonObject.get(Constant.STATUS), Constant.OK_200);
	}
	
	@Test
	public void testInvalidSessionGetAllImages() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(null);
		
		imageUploadServlet.doGet(request, response);
		
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
	}
	
	public void testFetchSingleImage() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		ServletOutputStream servletOutputStream = mock(StubServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(servletOutputStream);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		when(request.getParameter(Constant.IMAGE_PATH)).thenReturn(imagePath);
		imageUploadServlet.doGet(request, response);
		
		//Mockito.verify(servletOutputStream, Mockito.times(1)).write();
		Mockito.verify(response, Mockito.times(1)).setHeader("Content-Type", "image/jpg");
		//servletOutputStream.close();
		//assertEquals(null, jsonObject.get(Constant.STATUS));
	}
	
	public void testFetchSingleImageAndroid() throws ServletException, IOException, ParseException{
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		ServletOutputStream servletOutputStream = mock(StubServletOutputStream.class);
		when(response.getOutputStream()).thenReturn(servletOutputStream);
		
		when(response.getWriter()).thenReturn(printWriter);
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		when(request.getParameter(Constant.IMAGE_PATH)).thenReturn(imagePath);
		when(request.getParameter(Constant.SOURCE)).thenReturn(Constant.ANDROID);
		imageUploadServlet.doGet(request, response);
		
		//Mockito.verify(servletOutputStream, Mockito.times(1)).write();
		Mockito.verify(response, Mockito.times(1)).setHeader("Content-Type", "image/jpg");
		//servletOutputStream.close();
		//assertEquals(null, jsonObject.get(Constant.STATUS));
	}

	public void testValidImageUpdateReqeustNoImageUpdate() throws Exception {
	
		Long imageIdLong = ImageDAO.getImageIdByImageName(name);
		System.out.println("imageIdLong is :"+imageIdLong);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		servletContext = mock(ServletContext.class);
		diskFileItemFactory = mock(DiskFileItemFactory.class);
		
		ServletConfig config = mock(ServletConfig.class);
		when(config.getServletContext()).thenReturn(servletContext);
        ServletFileUpload fileUpload = mock(ServletFileUpload.class);
        List<FileItem> files = new ArrayList<FileItem>();
        
        FileItem imageId = mock(FileItem.class);
        when(imageId.getString()).thenReturn(Long.toString(imageIdLong));
        when(imageId.getFieldName()).thenReturn(Constant.IMAGE_ID);
        when(imageId.isFormField()).thenReturn(true);
        
        FileItem imageName = mock(FileItem.class);
        when(imageName.getString()).thenReturn(name);
        when(imageName.getFieldName()).thenReturn(Constant.IMAGE_NAME);
        when(imageName.isFormField()).thenReturn(true);
        
        FileItem imageDescription = mock(FileItem.class);
        when(imageDescription.getString()).thenReturn("Test Image Description Updated");
        when(imageDescription.getFieldName()).thenReturn(Constant.IMAGE_DESCRIPTION);
        when(imageDescription.isFormField()).thenReturn(true);
        
        FileItem imageCategoryId = mock(FileItem.class);
        when(imageCategoryId.getString()).thenReturn("1");
        when(imageCategoryId.getFieldName()).thenReturn(Constant.IMAGE_CATEGORY_ID);
        when(imageCategoryId.isFormField()).thenReturn(true);
        
        FileItem imageIntensityId = mock(FileItem.class);
        when(imageIntensityId.getString()).thenReturn("2");
        when(imageIntensityId.getFieldName()).thenReturn(Constant.IMAGE_INTENSITY);
        when(imageIntensityId.isFormField()).thenReturn(true);
        
        FileItem imageTypeId = mock(FileItem.class);
        when(imageTypeId.getString()).thenReturn("55");
        when(imageTypeId.getFieldName()).thenReturn(Constant.IMAGE_TYPE_ID);
        when(imageTypeId.isFormField()).thenReturn(true);
        
        FileItem imagePathFileItem = mock(FileItem.class);
        when(imagePathFileItem.getString()).thenReturn(imagePath);
        when(imagePathFileItem.getFieldName()).thenReturn(Constant.IMAGE_PATH);
        when(imagePathFileItem.isFormField()).thenReturn(true);
        
        files.add(imageId);
        files.add(imageName);
        files.add(imageDescription);
        files.add(imageCategoryId);
        files.add(imageIntensityId);
        files.add(imageTypeId);
        files.add(imagePathFileItem);
        
        when(fileUpload.parseRequest(request)).thenReturn(files);
        imageUploadServlet.setImageFolder(outputFolder);
        imageUploadServlet.setFileUpload(fileUpload);
        imageUploadServlet.doPut(request, response);
        
        JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
	}
	
	public void testValidImageUpdateReqeustWithImageUpdate() throws Exception {
		
		generateTestImage("test_updated.jpg");
		Long imageIdLong = ImageDAO.getImageIdByImageName(name);
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		when(request.getSession(false)).thenReturn(session);
		when(session.getAttribute(Constant.ROLE)).thenReturn(Constant.GLOBAL_ADMIN);
		when(session.getAttribute(Constant.EMAIL)).thenReturn("patel.dars@husky.neu.edu");
		when(session.getAttribute(Constant.USER_ID)).thenReturn(1l);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		servletContext = mock(ServletContext.class);
		diskFileItemFactory = mock(DiskFileItemFactory.class);
		
		ServletConfig config = mock(ServletConfig.class);
		when(config.getServletContext()).thenReturn(servletContext);
        ServletFileUpload fileUpload = mock(ServletFileUpload.class);
        List<FileItem> files = new ArrayList<FileItem>();
        
        FileItem imageId = mock(FileItem.class);
        when(imageId.getString()).thenReturn(Long.toString(imageIdLong));
        when(imageId.getFieldName()).thenReturn(Constant.IMAGE_ID);
        when(imageId.isFormField()).thenReturn(true);
        
        FileItem imageFileItem = mock(FileItem.class);
        when(imageFileItem.getFieldName()).thenReturn(Constant.IMAGE_FILE);
        when(imageFileItem.isFormField()).thenReturn(false);
        when(imageFileItem.getInputStream()).thenReturn(new FileInputStream(inputFolder+"/test_updated.jpg"));
        when(imageFileItem.getName()).thenReturn("test_updated.jpg");
        
        FileItem imageName = mock(FileItem.class);
        updateName = name+" Updated Image";
        when(imageName.getString()).thenReturn(updateName);
        when(imageName.getFieldName()).thenReturn(Constant.IMAGE_NAME);
        when(imageName.isFormField()).thenReturn(true);
        
        FileItem imageDescription = mock(FileItem.class);
        when(imageDescription.getString()).thenReturn("Test Image Description Updated Image");
        when(imageDescription.getFieldName()).thenReturn(Constant.IMAGE_DESCRIPTION);
        when(imageDescription.isFormField()).thenReturn(true);
        
        FileItem imageCategoryId = mock(FileItem.class);
        when(imageCategoryId.getString()).thenReturn("1");
        when(imageCategoryId.getFieldName()).thenReturn(Constant.IMAGE_CATEGORY_ID);
        when(imageCategoryId.isFormField()).thenReturn(true);
        
        FileItem imageIntensityId = mock(FileItem.class);
        when(imageIntensityId.getString()).thenReturn("2");
        when(imageIntensityId.getFieldName()).thenReturn(Constant.IMAGE_INTENSITY);
        when(imageIntensityId.isFormField()).thenReturn(true);
        
        FileItem imageTypeId = mock(FileItem.class);
        when(imageTypeId.getString()).thenReturn("55");
        when(imageTypeId.getFieldName()).thenReturn(Constant.IMAGE_TYPE_ID);
        when(imageTypeId.isFormField()).thenReturn(true);
        
        files.add(imageId);
        files.add(imageName);
        files.add(imageDescription);
        files.add(imageCategoryId);
        files.add(imageIntensityId);
        files.add(imageTypeId);
        files.add(imageFileItem);
        
        when(fileUpload.parseRequest(request)).thenReturn(files);
        imageUploadServlet.setImageFolder(outputFolder);
        imageUploadServlet.setFileUpload(fileUpload);
        System.out.println("Called doPut method");
        imageUploadServlet.doPut(request, response);
        
        JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.OK_200, (String) jsonObject.get(Constant.STATUS));
		assertTrue(doesFileExists((outputFolder+"/"+(String) jsonObject.get(Constant.IMAGE_PATH))));
		
		deleteFile(outputFolder+"/"+(String) jsonObject.get(Constant.IMAGE_PATH));
		deleteFile(inputFolder+"/test_updated.jpg");
		deleteFile(inputFolder+"/test.jpg");
	}
	
	
	public void testInValidSessionImageUpdateReqeustWithImageUpdate() throws Exception {
		
		generateTestImage("test_updated.jpg");
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		when(request.getSession(false)).thenReturn(null);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		BufferedReader bufferedReader = mock(BufferedReader.class);
		when(request.getReader()).thenReturn(bufferedReader);
		
		servletContext = mock(ServletContext.class);
		diskFileItemFactory = mock(DiskFileItemFactory.class);
		
		ServletConfig config = mock(ServletConfig.class);
		when(config.getServletContext()).thenReturn(servletContext);
        ServletFileUpload fileUpload = mock(ServletFileUpload.class);
        List<FileItem> files = new ArrayList<FileItem>();
        
        FileItem imageId = mock(FileItem.class);
        when(imageId.getString()).thenReturn("");
        when(imageId.getFieldName()).thenReturn(Constant.IMAGE_ID);
        when(imageId.isFormField()).thenReturn(true);
        
        FileItem imageFileItem = mock(FileItem.class);
        when(imageFileItem.getFieldName()).thenReturn(Constant.IMAGE_FILE);
        when(imageFileItem.isFormField()).thenReturn(false);
        when(imageFileItem.getInputStream()).thenReturn(new FileInputStream(inputFolder+"/test_updated.jpg"));
        when(imageFileItem.getName()).thenReturn("test_updated.jpg");
        
        FileItem imageName = mock(FileItem.class);
        when(imageName.getString()).thenReturn(name+" Updated Image ");
        when(imageName.getFieldName()).thenReturn(Constant.IMAGE_NAME);
        when(imageName.isFormField()).thenReturn(true);
        
        FileItem imageDescription = mock(FileItem.class);
        when(imageDescription.getString()).thenReturn("Test Image Description Updated Image");
        when(imageDescription.getFieldName()).thenReturn(Constant.IMAGE_DESCRIPTION);
        when(imageDescription.isFormField()).thenReturn(true);
        
        FileItem imageCategoryId = mock(FileItem.class);
        when(imageCategoryId.getString()).thenReturn("1");
        when(imageCategoryId.getFieldName()).thenReturn(Constant.IMAGE_CATEGORY_ID);
        when(imageCategoryId.isFormField()).thenReturn(true);
        
        FileItem imageIntensityId = mock(FileItem.class);
        when(imageIntensityId.getString()).thenReturn("2");
        when(imageIntensityId.getFieldName()).thenReturn(Constant.IMAGE_INTENSITY);
        when(imageIntensityId.isFormField()).thenReturn(true);
        
        FileItem imageTypeId = mock(FileItem.class);
        when(imageTypeId.getString()).thenReturn("55");
        when(imageTypeId.getFieldName()).thenReturn(Constant.IMAGE_TYPE_ID);
        when(imageTypeId.isFormField()).thenReturn(true);
        
        files.add(imageId);
        files.add(imageName);
        files.add(imageDescription);
        files.add(imageCategoryId);
        files.add(imageIntensityId);
        files.add(imageTypeId);
        files.add(imageFileItem);
        
        when(fileUpload.parseRequest(request)).thenReturn(files);
        imageUploadServlet.setImageFolder(outputFolder);
        imageUploadServlet.setFileUpload(fileUpload);
        imageUploadServlet.doPut(request, response);
        
        JSONParser parser = new JSONParser();
		Object obj = parser.parse(stringWriter.getBuffer().toString());
		JSONObject jsonObject = (JSONObject) obj;
		
		assertEquals(Constant.UNAUTHORIZED_401, (String) jsonObject.get(Constant.STATUS));
		deleteFile(inputFolder+"/test_updated.jpg");
	}
	
	
}

class StubServletOutputStream extends ServletOutputStream {
	 public ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   public void write(int i) throws IOException {
	    baos.write(i);
	 }
	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub
		
	}
}

