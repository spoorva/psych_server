package questionnaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.NetworkChannel;

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

import common.Constant;
import common.QuestionCategory;
import common.Sessions;
import dao.QuestionCategoryDAO;
import dao.UserProfileDAO;
import fieldValidation.QuestionCategoryFieldsVal;
import fieldValidation.UserProfileFieldsVal;

/**
 * Servlet implementation class QuestionCategoryServlet
 */
@WebServlet("/questionCategory")
public class QuestionCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionCategoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.addHeader("Access-Control-Allow-Origin", Constant.ACCESS_CONTROL_ALLOW_ORIGIN);
		response.addHeader("Access-Control-Allow-Headers", Constant.ACCESS_CONTROL_ALLOW_HEADERS);
		response.addHeader("Access-Control-Allow-Methods", Constant.ACCESS_CONTROL_ALLOW_METHODS);
		response.addIntHeader("Access-Control-Max-Age", Constant.ACCESS_CONTROL_ALLOW_MAX_AGE);
		
		
		
		String name = request.getParameter("name");
		
		JSONObject returnJSON = new JSONObject();
		
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
		
			if (name != null){
				
				System.out.println("Name: " + name);
				
				boolean duplicate = QuestionCategoryDAO.checkDuplicate(name);
				
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				returnJSON.put(Constant.RESULTS, duplicate);
				returnJSON.put(Constant.USER_MESSAGE, "Searched for duplicate successfully!");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Searched for duplicate successfully");
			}
			else{
				
				returnJSON = QuestionCategoryDAO.getAll();
			}
		}else{
			returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
		}
		
		response.getWriter().print(returnJSON);
		
		//System.out.println(name);
		
		response.setContentType("application/json;charset=UTF-8");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
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
		
		String newName;
		String newDescrition;
		long responseType; 
		String newStartLabel;
		String newEndLabel;
		
		HttpSession session = request.getSession(false);
		
		if(Sessions.isValidGlobalAdminSession(session)){
			
			try {
				obj = parser.parse(sb.toString());
				JSONObject jsonObject = (JSONObject) obj;
				newName = (String) jsonObject.get(Constant.NEW_QUESTION_CATEGORY_NAME);
				newDescrition = (String) jsonObject.get(Constant.NEW_QUESTION_CATEGORY_DESCRIPTION);
				responseType = (long) jsonObject.get(Constant.NEW_QUESTION_CATEGORY_RESPONSE_TYPE);
				newStartLabel = (String) jsonObject.get(Constant.NEW_QUESTION_CATEGORY_START_LABEL);
				newEndLabel = (String) jsonObject.get(Constant.NEW_QUESTION_CATEGORY_END_LABEL);
			
				if(QuestionCategoryFieldsVal.validateQuestionCategoryName(newName) 
						&& QuestionCategoryFieldsVal.validateDescription(newDescrition)
						&& QuestionCategoryFieldsVal.validateLabel(newStartLabel)
						&& QuestionCategoryFieldsVal.validateLabel(newEndLabel)){
				
						
					//System.out.println(newFirstName+ ":"+newLastName + ":"+newEmail + ":" + newPassword +" :" + email);
					boolean isCreated = QuestionCategoryDAO.createQuestionCategory(new QuestionCategory(1, newName, newDescrition, responseType, newStartLabel, newEndLabel));
					//System.out.println("isUpdated : " + isUpdated);
					if(isCreated){
						
						returnJSON.put(Constant.NEW_QUESTION_CATEGORY_NAME, newName);
						returnJSON.put(Constant.NEW_QUESTION_CATEGORY_DESCRIPTION, newDescrition);
						returnJSON.put(Constant.NEW_QUESTION_CATEGORY_RESPONSE_TYPE, responseType);
						returnJSON.put(Constant.NEW_QUESTION_CATEGORY_START_LABEL, newStartLabel);
						returnJSON.put(Constant.NEW_QUESTION_CATEGORY_END_LABEL, newEndLabel);
						
						returnJSON.put(Constant.STATUS, Constant.OK_200);
						returnJSON.put(Constant.USER_MESSAGE, "Added question category successfully!");
						returnJSON.put(Constant.DEVELOPER_MESSAGE, "Added question category successfully!");
						
					}else{
						returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
						returnJSON.put(Constant.USER_MESSAGE, "Could not add to database.");
						returnJSON.put(Constant.DEVELOPER_MESSAGE, "Failed at inserting question category.");
					}
				}else{
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
					returnJSON.put(Constant.USER_MESSAGE, "Invalid data in fields");
					returnJSON.put(Constant.DEVELOPER_MESSAGE, "Invalid data in fields");
				}
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.DEVELOPER_MESSAGE, Constant.JSON_PARSE_ERROR + sb.toString());
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
		
		String newName;
		String newDescrition;
		long responseType; 
		String newStartLabel;
		String newEndLabel;
		long id;
		
		HttpSession session = request.getSession(false);
		
		if(Sessions.isValidGlobalAdminSession(session)){
			
			try {
				obj = parser.parse(sb.toString());
				JSONObject jsonObject = (JSONObject) obj;
				id = (long) jsonObject.get(Constant.QUESTION_CATEGORY_ID);
				newName = (String) jsonObject.get(Constant.QUESTION_CATEGORY_NAME);
				newDescrition = (String) jsonObject.get(Constant.QUESTION_CATEGORY_DESCRIPTION);
				responseType = (long) jsonObject.get(Constant.QUESTION_CATEGORY_RESPONSE_TYPE);
				newStartLabel = (String) jsonObject.get(Constant.QUESTION_CATEGORY_START_LABEL);
				newEndLabel = (String) jsonObject.get(Constant.QUESTION_CATEGORY_END_LABEL);
			
				if(QuestionCategoryFieldsVal.validateQuestionCategoryName(newName) 
						&& QuestionCategoryFieldsVal.validateDescription(newDescrition)
						&& QuestionCategoryFieldsVal.validateLabel(newStartLabel)
						&& QuestionCategoryFieldsVal.validateLabel(newEndLabel)){
				
					returnJSON = QuestionCategoryDAO.udpateQuestionCategory(new 
							QuestionCategory(id, newName, newDescrition, responseType, newStartLabel, newEndLabel));
				}else{
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
					returnJSON.put(Constant.USER_MESSAGE, "Invalid data in fields");
					returnJSON.put(Constant.DEVELOPER_MESSAGE, "Invalid data in fields");
				}
				
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.DEVELOPER_MESSAGE, Constant.JSON_PARSE_ERROR + sb.toString());
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
}
