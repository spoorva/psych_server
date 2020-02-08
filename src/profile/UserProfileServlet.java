package profile;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.Constant;
import common.Location;
import common.Sessions;
import common.UserProfile;
import dao.UserProfileDAO;
import fieldValidation.CommonFieldsVal;
import fieldValidation.LocationFieldsVal;
import fieldValidation.UserProfileFieldsVal;

/**
 * Servlet implementation class UserProfile
 */
@WebServlet("/UserProfile")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("application/json;charset=UTF-8");
		JSONObject returnJSON = new JSONObject();
		
		HttpSession session = request.getSession(false);
		
		if(Sessions.isValidAdminSession(session)){
			String participant = request.getParameter(Constant.PARTICIPANT);
			if (participant != null && participant.equals("all")){
				returnJSON = UserProfileDAO.getAllParticipants();
			}
			else{
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Arguments missing.");
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
		String email="";
		String newEmail="";
		
		HttpSession session = request.getSession(false);
		
		if(Sessions.isValidAdminSession(session)){
			
			email= (String) session.getAttribute(Constant.EMAIL);
			
			try {
				obj = parser.parse(sb.toString());
				JSONObject jsonObject = (JSONObject) obj;
				newEmail = (String) jsonObject.get(Constant.NEW_EMAIL);
				
				boolean isUpdated = false;
				if(isValidInputData(jsonObject)){
					
					UserProfile userProfile = parseUserDetails(jsonObject);
					
					boolean isDuplicate = false;
					if(!email.equals(newEmail)){
						isDuplicate = UserProfileDAO.isDuplicateEmail(userProfile.getEmail());
					}
				
					if(!isDuplicate){
						isUpdated = UserProfileDAO.udpateUserProfile(userProfile, email);
						
						if(isUpdated){
							
							returnJSON.put(Constant.FIRST_NAME, userProfile.getFirstName());
							returnJSON.put(Constant.LAST_NAME, userProfile.getLastName());
							returnJSON.put(Constant.EMAIL, newEmail);
							
							session.setAttribute(Constant.FIRST_NAME, userProfile.getFirstName());
							session.setAttribute(Constant.LAST_NAME, userProfile.getLastName());
							session.setAttribute(Constant.EMAIL, newEmail);
							
							returnJSON.put(Constant.STATUS, Constant.OK_200);
						}
					}
				}
				if(!isUpdated){
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				}
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
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
		
		String newEmail = (String) jsonObject.get(Constant.NEW_EMAIL);
		String newPassword = (String) jsonObject.get(Constant.NEW_PASSWORD);
		String newFirstName = (String) jsonObject.get(Constant.NEW_FIRST_NAME);
		String newLastName = (String) jsonObject.get(Constant.NEW_LAST_NAME);

		if(UserProfileFieldsVal.validateEmail(newEmail) && UserProfileFieldsVal.validateName(newFirstName) && 
				UserProfileFieldsVal.validateName(newLastName)){
			
			if(!newPassword.equals("")){
				if(UserProfileFieldsVal.validatePassword(newPassword))
					return true;
				else
					return false;
			}
			return true;
		}
		return false;
		
	}
	
	private UserProfile parseUserDetails(JSONObject jsonObject){
		
		UserProfile userProfile = new UserProfile();
		
		String newEmail = (String) jsonObject.get(Constant.NEW_EMAIL);
		String newPassword = (String) jsonObject.get(Constant.NEW_PASSWORD);
		String newFirstName = (String) jsonObject.get(Constant.NEW_FIRST_NAME);
		String newLastName = (String) jsonObject.get(Constant.NEW_LAST_NAME);
		
		//Mandatory Fields
		userProfile.setEmail(newEmail);
		userProfile.setFirstName(newFirstName);
		userProfile.setLastName(newLastName);
		userProfile.setPassword(newPassword);
		
		return userProfile;
	}

}
