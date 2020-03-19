package imageData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Constant;
import common.ImageInfo;
import common.Sessions;
import dao.ImageCategoryDAO;
import dao.ImageDAO;
import fieldValidation.CommonFieldsVal;
import fieldValidation.ImageFieldsVal;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/imageUpload")
public class ImageUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger slf4jLogger = LoggerFactory.getLogger(ImageUploadServlet.class);
	ServletFileUpload fileUpload;
	String imageFolder;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	public ServletFileUpload getFileUpload() {
		return fileUpload;
	}


	public void setFileUpload(ServletFileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}
	
	public String getImageFolder() {
		return imageFolder;
	}

	public void setImageFolder(String imageFolder) {
		this.imageFolder = imageFolder;
	}

	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(repository);
        fileUpload = new ServletFileUpload(factory);
        imageFolder = servletContext.getInitParameter(Constant.IMAGE_FOLDER);
      }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		slf4jLogger.info("Entered in doGet method of ImageCategoryServlet");
		JSONObject returnJSON = new JSONObject();
		
		String source = request.getParameter(Constant.SOURCE);
		Boolean isAndroid = (source!=null)?source.equals(Constant.ANDROID)?true:false:false;
		
		if(isAndroid){
			String imagePath = request.getParameter(Constant.IMAGE_PATH);
			try{
				File f = new File(imageFolder+"/"+imagePath);
				FileInputStream fis = new FileInputStream(f);
				int b = 0;
				while ((b = fis.read()) != -1) {
					response.getOutputStream().write(b);
				}
				fis.close();
		        //response.setHeader("Content-Type", getServletContext().getMimeType(f.toString()));
				response.setHeader("Content-Type", "image/"+FilenameUtils.getExtension(f.toString()));
		        response.setHeader("Content-Length", String.valueOf(f.length()));
		        response.setHeader("Content-Disposition", "inline; filename=\"" + f.getName() + "\"");
				return;
			}catch(FileNotFoundException e){
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "File doen't exists");
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			}
		}
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
			
			String imageName = request.getParameter(Constant.IMAGE_NAME);
			Boolean checkDuplicate = (imageName!=null)?true:false;
			
			String imagePath = request.getParameter(Constant.IMAGE_PATH);
			Boolean fetchImage = (imagePath!=null)?true:false;
			
			if(checkDuplicate){
				returnJSON.put(Constant.RESULTS, ImageDAO.isDuplicateImage(imageName));
				returnJSON.put(Constant.STATUS, Constant.OK_200);
			}else if(fetchImage){
				
				try{
					File f = new File(imageFolder+"/"+imagePath);
					FileInputStream fis = new FileInputStream(f);
					int b = 0;
					while ((b = fis.read()) != -1) {
						response.getOutputStream().write(b);
					}
					fis.close();
			        //response.setHeader("Content-Type", getServletContext().getMimeType(f.toString()));
					response.setHeader("Content-Type", "image/"+FilenameUtils.getExtension(f.toString()));
			        response.setHeader("Content-Length", String.valueOf(f.length()));
			        response.setHeader("Content-Disposition", "inline; filename=\"" + f.getName() + "\"");
			        response.addHeader("Access-Control-Allow-Origin", Constant.ACCESS_CONTROL_ALLOW_ORIGIN);
					response.addHeader("Access-Control-Allow-Headers", Constant.ACCESS_CONTROL_ALLOW_HEADERS);
					response.addHeader("Access-Control-Allow-Methods", Constant.ACCESS_CONTROL_ALLOW_METHODS);
					response.addIntHeader("Access-Control-Max-Age", Constant.ACCESS_CONTROL_ALLOW_MAX_AGE);
					return;
				}catch(FileNotFoundException e){
					returnJSON.put(Constant.DEVELOPER_MESSAGE, "File doen't exists");
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				}
			}else{
				//Extract all location information
				JSONArray jsonArray = ImageDAO.fetchAllImage();
				returnJSON.put(Constant.RESULTS, jsonArray);
				returnJSON.put(Constant.STATUS, Constant.OK_200);
			}
		}else{
			returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
		}
		
		response.getWriter().print(returnJSON);
		response.addHeader("Access-Control-Allow-Origin", Constant.ACCESS_CONTROL_ALLOW_ORIGIN);
		response.addHeader("Access-Control-Allow-Headers", Constant.ACCESS_CONTROL_ALLOW_HEADERS);
		response.addHeader("Access-Control-Allow-Methods", Constant.ACCESS_CONTROL_ALLOW_METHODS);
		response.addIntHeader("Access-Control-Max-Age", Constant.ACCESS_CONTROL_ALLOW_MAX_AGE);
		
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject returnJSON = new JSONObject();
        try {
        	
        	HttpSession session = request.getSession(false);
        	if(Sessions.isValidGlobalAdminSession(session)){
        		slf4jLogger.info("Valid session");
	        	ImageInfo imageInfo = new ImageInfo();
	            List<FileItem> files = fileUpload.parseRequest(request);
	            boolean badInput = false;
	            boolean folderCreationIssue = false;
	            boolean isImageUpdated = false;
	            if (files != null && !files.isEmpty()) {
	            	
	                for (FileItem item : files) {
	                	
	                	if(item.isFormField()){
	                		if(!validateInput(item.getFieldName(), item.getString())){
	                			slf4jLogger.info("Bad input field Name"+ item.getFieldName());
	                			slf4jLogger.info("Bad input field value"+ item.getString());
		            			badInput = true;
		            			break;
		            		}
	                		setInputInfo(item.getFieldName(), item.getString(), imageInfo);
	                	}else{
	                		
	                		if(!validateInput(item.getFieldName(), item.getName())){
	                			slf4jLogger.info("Bad input"+ item.getFieldName());
		            			badInput = true;
		            			break;
		            		}
	                		
	                		Random random = new Random();
	                    	int folderId = random.nextInt(20);
	                    	if(!validateFolder(Integer.toString(folderId))){
	                    		folderCreationIssue = true;
	                    		break;
	                    	}
	                    	String extension = FilenameUtils.getExtension(item.getName());
	                		UUID uuid = UUID.randomUUID();
	                		
	                		String filePath = Integer.toString(folderId) +"/"+ uuid.toString()+"."+extension;
	                		imageInfo.setUuid(uuid.toString());
	                		imageInfo.setInputStream(item.getInputStream());
	                		imageInfo.setImageShortPath(filePath);
	                		imageInfo.setImageFullPath(imageFolder+"/"+filePath);
	                		isImageUpdated = true;
	                	}
	                }
	            }
	            if(!badInput && !folderCreationIssue){
	            	if(isImageUpdated){
	            		System.out.println("Will update image info with image");
	            		saveInputFile(imageInfo);
	            	}else{
	            		System.out.println(imageInfo.getOldImageShortPath());
	            		imageInfo.setImageShortPath(imageInfo.getOldImageShortPath());
	            	}
            		boolean created = ImageDAO.updateImage(imageInfo);
            		if(created){
            			// Delete the existing file.
            			if(isImageUpdated){
            				deleteFile(imageFolder+"/"+imageInfo.getOldImageShortPath());
            			}
            			returnJSON.put(Constant.STATUS, Constant.OK_200);
            			returnJSON.put(Constant.IMAGE_PATH, imageInfo.getImageShortPath());
            			returnJSON.put(Constant.IMAGE_UUID, imageInfo.getUuid());
            		}else{
            			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
            			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue while saving the image info in database");
            		}
            	}else if (badInput){
	            	returnJSON.put(Constant.DEVELOPER_MESSAGE, "Bad input data");
	            	returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
	            }else if(folderCreationIssue){
	            	returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue is creating folder");
	            	returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
	            }
        	}else{
        		returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
        	}
            
        } catch (FileUploadException e) {
            e.printStackTrace();
            returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
            returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue in Parsing the input data");
        }
        response.getWriter().print(returnJSON);
		response.addHeader("Access-Control-Allow-Origin", Constant.ACCESS_CONTROL_ALLOW_ORIGIN);
		response.addHeader("Access-Control-Allow-Headers", Constant.ACCESS_CONTROL_ALLOW_HEADERS);
		response.addHeader("Access-Control-Allow-Methods", Constant.ACCESS_CONTROL_ALLOW_METHODS);
		response.addIntHeader("Access-Control-Max-Age", Constant.ACCESS_CONTROL_ALLOW_MAX_AGE);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if (ServletFileUpload.isMultipartContent(request)) {
		JSONObject returnJSON = new JSONObject();
        try {
        	HttpSession session = request.getSession(false);
        	if(Sessions.isValidGlobalAdminSession(session)){
        		
	        	ImageInfo imageInfo = new ImageInfo();
	            List<FileItem> files = fileUpload.parseRequest(request);
	            boolean badInput = false;
	            boolean folderCreationIssue = false;
	            if (files != null && !files.isEmpty()) {
	            	
	                for (FileItem item : files) {
	                	
	                	if(item.isFormField()){
	                		if(!validateInput(item.getFieldName(), item.getString())){
		            			badInput = true;
		            			break;
		            		}
	                		setInputInfo(item.getFieldName(), item.getString(), imageInfo);
	                	}else{
	                		
	                		if(!validateInput(item.getFieldName(), item.getName())){
		            			badInput = true;
		            			break;
		            		}
	                		Random random = new Random();
	                    	int folderId = random.nextInt(20);
	                    	if(!validateFolder(Integer.toString(folderId))){
	                    		folderCreationIssue = true;
	                    		break;
	                    	}
	                    	String extension = FilenameUtils.getExtension(item.getName());
	                		UUID uuid = UUID.randomUUID();
	                		
	                		String filePath = Integer.toString(folderId) +"/"+ uuid.toString()+"."+extension;
	                		imageInfo.setUuid(uuid.toString());
	                		imageInfo.setInputStream(item.getInputStream());
	                		imageInfo.setImageShortPath(filePath);
	                		imageInfo.setImageFullPath(imageFolder+"/"+filePath);
	                	}
	                }
	            }
	            if(!badInput && !folderCreationIssue){
	            	// First save file on disk
	            	if(saveInputFile(imageInfo)){
	            		//Second save into database
	            		boolean created = ImageDAO.createImage(imageInfo);
	            		if(created){
	            			returnJSON.put(Constant.STATUS, Constant.OK_200);
	            			returnJSON.put(Constant.IMAGE_PATH, imageInfo.getImageShortPath());
	            			returnJSON.put(Constant.IMAGE_UUID, imageInfo.getUuid());
	            		}else{
	            			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
	            			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue while saving the image info in database");
	            		}
	            	}else{
	            		returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
	            		returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue in saving the file");
	            	}
	            }else if(badInput){
	            	returnJSON.put(Constant.DEVELOPER_MESSAGE, "Bad input data");
	            	returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
	            }else if(folderCreationIssue){
	            	returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue is creating folder");
	            	returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
	            }
        	}else{
        		returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
        	}
            
        } catch (FileUploadException e) {
            e.printStackTrace();
            returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
            returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue in Parsing the input data");
        }
        response.getWriter().print(returnJSON);
		response.addHeader("Access-Control-Allow-Origin", Constant.ACCESS_CONTROL_ALLOW_ORIGIN);
		response.addHeader("Access-Control-Allow-Headers", Constant.ACCESS_CONTROL_ALLOW_HEADERS);
		response.addHeader("Access-Control-Allow-Methods", Constant.ACCESS_CONTROL_ALLOW_METHODS);
		response.addIntHeader("Access-Control-Max-Age", Constant.ACCESS_CONTROL_ALLOW_MAX_AGE);
	}
	
	private boolean validateInput(String fieldName, String fieldValue){
		
		if(fieldName.equals(Constant.IMAGE_NAME)){
			return ImageFieldsVal.validateImageName(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_DESCRIPTION)){
			return CommonFieldsVal.validateDescription(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_CATEGORY_ID)){
			return CommonFieldsVal.validateFieldId(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_INTENSITY)){
			return ImageFieldsVal.validateImageIntensity(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_TYPE_ID)){
			return CommonFieldsVal.validateFieldId(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_FILE)){
			if(!fieldValue.equals("")){
				String extension = FilenameUtils.getExtension(fieldValue);
				if(extension.toLowerCase().equals("jpeg") || extension.toLowerCase().equals("png") ||
						extension.toLowerCase().equals("jpg")){
					return true;
				}
			}
			return false;
		}else if(fieldName.equals(Constant.IMAGE_PATH)){
			return true;
		}
		else if(fieldName.equals(Constant.IMAGE_ID)){
			return CommonFieldsVal.validateFieldId(fieldValue);
		}
		return false;
	}
	
	private void setInputInfo(String fieldName, String fieldValue, ImageInfo imageInfo){
		if(fieldName.equals(Constant.IMAGE_NAME)){
			imageInfo.setImageName(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_DESCRIPTION)){
			imageInfo.setImageDesc(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_CATEGORY_ID)){
			imageInfo.setImageCategoryId(Long.parseLong(fieldValue));
		}else if(fieldName.equals(Constant.IMAGE_INTENSITY)){
			imageInfo.setImageIntensity(Long.parseLong(fieldValue));
		}else if(fieldName.equals(Constant.IMAGE_TYPE_ID)){
			imageInfo.setImageTypeId(Long.parseLong(fieldValue));
		}else if(fieldName.equals(Constant.IMAGE_PATH)){
			imageInfo.setOldImageShortPath(fieldValue);
		}else if(fieldName.equals(Constant.IMAGE_ID)){
			imageInfo.setId(Long.parseLong(fieldValue));
		}
		
	}
	
	private boolean saveInputFile(ImageInfo imageInfo){
		
		InputStream in = new BufferedInputStream(imageInfo.getInputStream());
		OutputStream out = null;
		try {
			System.out.println(imageInfo.getImageFullPath());
			out = new FileOutputStream(new File(imageInfo.getImageFullPath()));
			IOUtils.copy(in,out);
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	        IOUtils.closeQuietly(out);
	        IOUtils.closeQuietly(in);
	    }
		
		return false;
	}
	
	private boolean validateFolder(String folderName){
		File f = new File(imageFolder+"/"+folderName);
		try{
		  if(f.mkdir()) { 
		    return true;
		  } else {
		      return true;
		  }
		 } catch(Exception e){
		  e.printStackTrace();
		}
		return false;
	}
	
	private void deleteFile(String filePath){
		File fInput = new File(filePath);
		fInput.delete();
	}
}
