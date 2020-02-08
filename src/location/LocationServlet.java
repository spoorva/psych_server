package location;

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
import common.Location;
import common.Sessions;
import dao.DBSourceRegistrationListener;
import dao.LocationDAO;
import fieldValidation.CommonFieldsVal;
import fieldValidation.LocationFieldsVal;
import fieldValidation.UserProfileFieldsVal;

/**
 * Servlet implementation class LocationServlet
 */
@WebServlet("/location")
public class LocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger slf4jLogger = LoggerFactory.getLogger(LocationServlet.class);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		slf4jLogger.info("Entered in doGet method of LocationServlet");
		boolean checkLocationNameDuplicate = false;
		boolean checkLocaitonCodeDuplicate = false;
		JSONObject returnJSON = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
			if((request.getParameter(Constant.LOCATION_NAME) != null) || (request.getParameter(Constant.LOCATION_CODE) != null)){
				if(request.getParameter(Constant.LOCATION_NAME) != null){
					checkLocationNameDuplicate= true;
				}else{
					checkLocaitonCodeDuplicate = true;
				}
			}
			
			if(checkLocationNameDuplicate){
				// Check location name duplicate
				
				String locationName = request.getParameter(Constant.LOCATION_NAME);
				returnJSON.put(Constant.RESULTS, LocationDAO.isDuplicateLocation(locationName));
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				
			}else if(checkLocaitonCodeDuplicate){
				
				String locationCode = request.getParameter(Constant.LOCATION_CODE);
				slf4jLogger.info("locationCode : "+locationCode + LocationDAO.isDuplicateLocationCode(locationCode));
				returnJSON.put(Constant.RESULTS, LocationDAO.isDuplicateLocationCode(locationCode));
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				
			}else{
				//Extract all location information
				JSONArray jsonArray = LocationDAO.fetchAllLocation();
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
		
		slf4jLogger.info("Entered in doPost method of LocationServlet");
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
					String locationName =  ((String) jsonObject.get(Constant.LOCATION_NAME)).trim();
					boolean duplicated = LocationDAO.isDuplicateLocation(locationName);
					if(!duplicated){
						Location location = parseLocation(jsonObject);
						created = LocationDAO.createLocation(location);
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
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
					Location location = parseLocation(jsonObject);
					updated = LocationDAO.updateLocation(location);
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
		
		String name = ((String) jsonObject.get(Constant.LOCATION_NAME)).trim();
		String desc = ((String) jsonObject.get(Constant.LOCATION_DESCRIPTION)).trim();
		String keywords = ((String) jsonObject.get(Constant.LOCATION_KEYWORDS)).trim();
		String addressLine1 = ((String) jsonObject.get(Constant.LOCATION_ADDRESS_LINE_1)).trim();
		String addressLine2 = ((String) jsonObject.get(Constant.LOCATION_ADDRESS_LINE_2)).trim();
		String city = ((String) jsonObject.get(Constant.LOCATION_CITY)).trim();
		String code = ((String) jsonObject.get(Constant.LOCATION_CODE)).trim();
		String state = ((String) jsonObject.get(Constant.LOCATION_STATE_ID)).trim();
		String zipcode = ((String) jsonObject.get(Constant.LOCATION_ZIPCODE)).trim();
		String phoneNumber = ((String) jsonObject.get(Constant.LOCATION_PHONE_NUMBER)).trim();
		String faxNumber = ((String) jsonObject.get(Constant.LOCATION_FAX_NUMBER)).trim();
		String email = ((String) jsonObject.get(Constant.LOCATION_EMAIL)).trim();

		if(LocationFieldsVal.validateName(name) && CommonFieldsVal.validateDescription(desc) && 
				CommonFieldsVal.validateKeywords(keywords) && LocationFieldsVal.validateAddressLine1(addressLine1) && 
				LocationFieldsVal.validateLocationCode(code) && CommonFieldsVal.validateFieldId(state) && 
				LocationFieldsVal.validateAddressLine2(addressLine2) && LocationFieldsVal.validateCity(city) &&
				LocationFieldsVal.validateZipCode(zipcode) && LocationFieldsVal.validatePhoneNumber(phoneNumber) &&
				LocationFieldsVal.validateFaxNumber(faxNumber) && LocationFieldsVal.validateEmail(email)){
			return true;
		}
		return false;
		}catch(Exception e){
			slf4jLogger.info("Parsing issue in location details");
			return false;
		}
		
	}
	
	private Location parseLocation(JSONObject jsonObject){
		
		Location location = new Location();
		
		slf4jLogger.info("Entered into parseLocation");
		
		String name = ((String) jsonObject.get(Constant.LOCATION_NAME)).trim();
		String desc = ((String) jsonObject.get(Constant.LOCATION_DESCRIPTION)).trim();
		String keywords = ((String) jsonObject.get(Constant.LOCATION_KEYWORDS)).trim();
		String addressLine1 = ((String) jsonObject.get(Constant.LOCATION_ADDRESS_LINE_1)).trim();
		String addressLine2 = ((String) jsonObject.get(Constant.LOCATION_ADDRESS_LINE_2)).trim();
		String city = ((String) jsonObject.get(Constant.LOCATION_CITY)).trim();
		String code = ((String) jsonObject.get(Constant.LOCATION_CODE)).trim();
		String state = ((String) jsonObject.get(Constant.LOCATION_STATE_ID)).trim();
		String zipcode = ((String) jsonObject.get(Constant.LOCATION_ZIPCODE)).trim();
		String phoneNumber = ((String) jsonObject.get(Constant.LOCATION_PHONE_NUMBER)).trim();
		String faxNumber = ((String) jsonObject.get(Constant.LOCATION_FAX_NUMBER)).trim();
		String email = ((String) jsonObject.get(Constant.LOCATION_EMAIL)).trim();
		
		
		if(jsonObject.get(Constant.LOCATION_ID) != null && jsonObject.get(Constant.LOCATION_ID) != ""){
			location.setId(Long.parseLong(((String) jsonObject.get(Constant.LOCATION_ID)).trim()));
		}
		
		//Mandatory Fields
		location.setName(name);
		location.setAddressLine1(addressLine1);
		location.setCity(city);
		location.setStateId(Long.parseLong(state));
		location.setZipCode(zipcode);
		location.setPhoneNumber(phoneNumber);
		location.setCode(code);
		location.setDesc(desc);
		location.setKeywords(keywords);
		location.setAddressLine2(addressLine2);
		location.setFaxNumber(faxNumber);
		location.setEmail(email);
		return location;
	}

}
