package imageData;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Constant;
import common.ImageCategory;
import common.Location;
import common.Sessions;
import dao.ImageCategoryDAO;
import dao.LocationDAO;
import fieldValidation.CommonFieldsVal;
import fieldValidation.ImageFieldsVal;

/**
 * Servlet implementation class ImageCategoryServlet
 */
@WebServlet("/imageCategory")
public class ImageCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger slf4jLogger = LoggerFactory.getLogger(ImageCategoryServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageCategoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		slf4jLogger.info("Entered in doGet method of ImageCategoryServlet");
		JSONObject returnJSON = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
			String imageCategoryName = request.getParameter(Constant.IMAGE_CATEGORY_NAME);
			Boolean checkDuplicate = (imageCategoryName!=null)?true:false;
			
			if(checkDuplicate){
				String imageCategoryId = request.getParameter(Constant.IMAGE_CATEGORY_ID);
				returnJSON.put(Constant.RESULTS, ImageCategoryDAO.isDuplicateImageCategory(imageCategoryName, imageCategoryId));
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				
			}else{
				//Extract all location information
				JSONArray jsonArray = ImageCategoryDAO.fetchAllImageCategory();
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		slf4jLogger.info("Entered in doPost method of ImageCategoryServlet");
		response.setContentType("application/json;charset=UTF-8");
		
		JSONObject returnJSON = new JSONObject();
		
		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        
        JSONParser parser = new JSONParser();
		Object obj;
		
		HttpSession session = request.getSession(false);
		
		if(Sessions.isValidGlobalAdminSession(session)){
			
			try {
				
				obj = parser.parse(sb.toString());
				
				JSONObject jsonObject = (JSONObject) obj;
				boolean created = false;
				if(isValidInputData(jsonObject)){
					String imageCategoryName =  ((String) jsonObject.get(Constant.IMAGE_CATEGORY_NAME)).trim();
					boolean duplicated = ImageCategoryDAO.isDuplicateImageCategory(imageCategoryName, null);
					if(!duplicated){
						ImageCategory imageCategory = parseImageCategory(jsonObject);
						created = ImageCategoryDAO.createImageCategory(imageCategory);
						returnJSON.put(Constant.STATUS, Constant.OK_200);
					}
				}
				if(!created){
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
					returnJSON.put(Constant.DEVELOPER_MESSAGE, "Input is not valid or issue with database insert");
				}
				
			}catch(ParseException e){
				e.printStackTrace();
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue with Parsing");
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
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		slf4jLogger.info("Entered in doPut method of LocationServlet");
		response.setContentType("application/json;charset=UTF-8");
		
		JSONObject returnJSON = new JSONObject();
		
		StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        
        JSONParser parser = new JSONParser();
		Object obj;
		
		HttpSession session = request.getSession(false);
		
		if(Sessions.isValidGlobalAdminSession(session)){
			
			try {
				
				obj = parser.parse(sb.toString());
				
				JSONObject jsonObject = (JSONObject) obj;
				boolean updated = false;
				if(isValidInputData(jsonObject)){
					String imageCategoryName =  ((String) jsonObject.get(Constant.IMAGE_CATEGORY_NAME)).trim();
					String imageCategoryId =  ((String) jsonObject.get(Constant.IMAGE_CATEGORY_ID)).trim();
					boolean duplicated = ImageCategoryDAO.isDuplicateImageCategory(imageCategoryName, imageCategoryId);
					ImageCategory location = parseImageCategory(jsonObject);
					updated = ImageCategoryDAO.updateImageCategory(location);
					if(updated){
						JSONArray jsonArray = LocationDAO.fetchAllLocation();
						returnJSON.put(Constant.RESULTS, jsonArray);
					}
					returnJSON.put(Constant.STATUS, Constant.OK_200);
				}
				if(!updated){
					
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
					returnJSON.put(Constant.DEVELOPER_MESSAGE, "Input is not valid or issue with database insert");
				}
				
			}catch(ParseException e){
				e.printStackTrace();
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Issue with Parsing");
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
	
	private boolean isValidInputData(JSONObject jsonObject){
		
		slf4jLogger.info("Entered into isValidInputData");
		try{
		
		String name = ((String) jsonObject.get(Constant.IMAGE_CATEGORY_NAME)).trim();
		String desc = ((String) jsonObject.get(Constant.IMAGE_CATEGORY_DESCRIPTION)).trim();

		if(ImageFieldsVal.validateImageCategoryName(name) && CommonFieldsVal.validateDescription(desc)){
			return true;
		}
		return false;
		}catch(Exception e){
			slf4jLogger.info("Parsing issue in imamge category details");
			return false;
		}
		
	}
	
	private ImageCategory parseImageCategory(JSONObject jsonObject){
		
		ImageCategory imageCategory = new ImageCategory();
		
		slf4jLogger.info("Entered into parseLocation");
		
		String name = ((String) jsonObject.get(Constant.IMAGE_CATEGORY_NAME)).trim();
		String desc = ((String) jsonObject.get(Constant.IMAGE_CATEGORY_DESCRIPTION)).trim();
		
		if(jsonObject.get(Constant.IMAGE_CATEGORY_ID) != null && jsonObject.get(Constant.IMAGE_CATEGORY_ID) != ""){
			imageCategory.setId(Long.parseLong(((String) jsonObject.get(Constant.IMAGE_CATEGORY_ID)).trim()));
		}
		
		//Mandatory Fields
		imageCategory.setName(name);
		imageCategory.setDesc(desc);
		return imageCategory;
	}
	

}
