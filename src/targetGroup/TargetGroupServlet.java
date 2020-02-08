package targetGroup;

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
import common.TargetGroup;
import dao.LocationDAO;
import dao.TargetGroupDAO;
import fieldValidation.CommonFieldsVal;
import fieldValidation.LocationFieldsVal;
import training.TrainingServlet;

/**
 * Servlet implementation class TargetGroupServlet
 */
@WebServlet("/targetGroup")
public class TargetGroupServlet extends HttpServlet {
	private static Logger slf4jLogger = LoggerFactory.getLogger(TargetGroupServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TargetGroupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		slf4jLogger.info("Entered in doGet method of TargetGroupServlet");
		JSONObject returnJSON = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
			if((request.getParameter(Constant.TG_NAME) != null)){
				
				String targetGroupName = request.getParameter(Constant.TG_NAME);
				returnJSON.put(Constant.RESULTS, TargetGroupDAO.isDuplicateTargetGroup(targetGroupName));
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				
			}else{
				//Extract all location information
				JSONArray jsonArray = TargetGroupDAO.fetchAllTargetGroup();
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
		slf4jLogger.info("Entered in doPost method of TrainingServlet");
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
					String tgName =  ((String) jsonObject.get(Constant.TG_NAME)).trim();
					boolean duplicated = TargetGroupDAO.isDuplicateTargetGroup(tgName);
					if(!duplicated){
						TargetGroup targetGroup = parseTargetGroup(jsonObject);
						String locCode = LocationDAO.getLocationCodeByLocationId(targetGroup.getLocId());
						targetGroup.setLocCode(locCode);
						Long targetGroupCountPerLocation = TargetGroupDAO.getTargetGroupCountPerLocation(targetGroup.getLocId());
						targetGroup.setTrainingCount(targetGroupCountPerLocation+1);
						created = TargetGroupDAO.createTargetGroup(targetGroup);
						returnJSON.put(Constant.STATUS, Constant.OK_200);
						returnJSON.put(Constant.TG_REG_CODE, targetGroup.getRegCode());
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
					TargetGroup targetGroup = parseTargetGroup(jsonObject);
					updated = TargetGroupDAO.updateTargetGroup(targetGroup);
					if(updated){
						JSONArray jsonArray = TargetGroupDAO.fetchAllTargetGroup();
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
		
		String name = ((String) jsonObject.get(Constant.TG_NAME)).trim();
		String desc = ((String) jsonObject.get(Constant.TG_DESCRIPTION)).trim();
		String keywords = ((String) jsonObject.get(Constant.TG_KEYWORDS)).trim();
		//String locationCode = ((String) jsonObject.get(Constant.TG_LOCATION_CODE)).trim();
		String locationId = ((String) jsonObject.get(Constant.TG_LOCATION_ID)).trim();
		String trainingId = ((String) jsonObject.get(Constant.TG_TRAINING_ID)).trim();

		if(LocationFieldsVal.validateName(name) && CommonFieldsVal.validateDescription(desc) && 
				CommonFieldsVal.validateKeywords(keywords) && CommonFieldsVal.validateFieldId(locationId) &&
				 CommonFieldsVal.validateFieldId(trainingId)){
			return true;
		}
		return false;
		}catch(Exception e){
			slf4jLogger.info("Parsing issue in target group details");
			return false;
		}
		
	}
	
	private TargetGroup parseTargetGroup(JSONObject jsonObject){
		
		TargetGroup targetGroup = new TargetGroup();
		
		slf4jLogger.info("Entered into parseTargetGroup");
		
		String name = ((String) jsonObject.get(Constant.TG_NAME)).trim();
		String desc = ((String) jsonObject.get(Constant.TG_DESCRIPTION)).trim();
		String keywords = ((String) jsonObject.get(Constant.TG_KEYWORDS)).trim();
		//String locationCode = ((String) jsonObject.get(Constant.TG_LOCATION_CODE)).trim();
		String locationId = ((String) jsonObject.get(Constant.TG_LOCATION_ID)).trim();
		String trainingId = ((String) jsonObject.get(Constant.TG_TRAINING_ID)).trim();
		
		if(jsonObject.get(Constant.TG_REG_CODE) != null && jsonObject.get(Constant.TG_REG_CODE) != ""){
			targetGroup.setRegCode(((String) jsonObject.get(Constant.TG_REG_CODE)).trim());
		}
		if(jsonObject.get(Constant.TG_ID) != null && jsonObject.get(Constant.TG_ID) != ""){
			targetGroup.setId(Long.parseLong(((String) jsonObject.get(Constant.TG_ID)).trim()));
		}
		
		//Mandatory Fields
		targetGroup.setName(name);
		targetGroup.setDesc(desc);
		targetGroup.setKeywords(keywords);
		//targetGroup.setLocCode(locationCode);
		targetGroup.setLocId(Long.parseLong(locationId));
		targetGroup.setTrainingId(Long.parseLong(trainingId));
		
		return targetGroup;
	}

}
