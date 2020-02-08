package questionnaire;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import authentication.BuildStaticParameters;
import common.Constant;
import dao.ColorDAO;
import dao.QuestionDAO;
import dao.SessionDAO;
import fieldValidation.CommonFieldsVal;

/**
 * Servlet implementation class Questionnaire
 */
@WebServlet("/Questionnaire")
public class Questionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Questionnaire() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json;charset=UTF-8");
		JSONObject returnJSON = new JSONObject();
		try{
	        //String questionSession = request.getParameter("questionSession");
	        String participantId = request.getParameter(Constant.PARTICIPANTID);
	        String sessionIdStr = request.getParameter(Constant.SESSION_ID);
	        String targetGroupId = request.getParameter(Constant.TG_ID);
	        String questionSession = request.getParameter(Constant.QUESTION_SESSION);
	        String[] responses = request.getParameterValues(Constant.RESPONSES);
	        
	       	boolean success = false;
	       	boolean validInput = false;
	       	long sessionId = -1;
	       	long latestSessionNumber = -2;
	       	if(responses.length > 0){
	       		try{
					Gson gson = new Gson();
					ArrayList<HashMap<String, String>> responseList = gson.fromJson(responses[0], new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType());
					if(responseList.size() != 0  && questionSession != null && targetGroupId != null && participantId != null &&
							CommonFieldsVal.validateFieldId(targetGroupId) &&
							CommonFieldsVal.validateFieldId(participantId)){
						validInput = true;
						if(questionSession.equals(Constant.DEFAULT_QUESTION_SESSION)){
							// Generate Session Number
							latestSessionNumber = SessionDAO.getLatestSessionNumber(participantId);
							latestSessionNumber++;
							// Insert into Session Id
							sessionId = SessionDAO.createNewSession(participantId, latestSessionNumber);
							//Fetch two color randomly
							String[] colors = ColorDAO.getTwoRandomColors();
							// Save into parameter table
							boolean parameterCreated = SessionDAO.createNewParameters(sessionId, participantId, colors);
							if(colors.length != 2 || latestSessionNumber < 1 || parameterCreated == false){
								returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
								return;
							}else{
								System.out.println("save question response");
								// Save the question response in the table.
								success = QuestionDAO.saveQuestionResponse(responseList, sessionId, participantId, Constant.START_STAGE);
								if(success){
									System.out.println("AFter entry session id is:"+sessionId);
									returnJSON.put(Constant.POSITIVE_COLOR, colors[0]);
									returnJSON.put(Constant.NEGATIVE_COLOR, colors[1]);
									returnJSON.put(Constant.SESSION_ID, Long.toString(sessionId));
								}
							}
						}else{
							//Save the question response in the table.
							if(CommonFieldsVal.validateFieldId(sessionIdStr) && sessionIdStr != null){
								validInput = true;
								success =  QuestionDAO.saveQuestionResponse(responseList, Long.parseLong(sessionIdStr), participantId, Constant.END_STAGE);
							}
						}
					}
	       		}catch(Exception e){
	       			e.printStackTrace();
	       			returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
	       			if(questionSession.equals(Constant.DEFAULT_QUESTION_SESSION) && validInput == true){
	       				SessionDAO.deleteParameters(sessionId, participantId);
	       				SessionDAO.deleteSession(sessionId);
	       			}
	       		}
			}
	       	if(success){
	       		returnJSON.put(Constant.SAVE, Constant.SUCCESSFUL);
			}else{
				returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
				if(validInput){
					SessionDAO.deleteParameters(sessionId, participantId);
					SessionDAO.deleteSession(sessionId);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
		}
		response.getWriter().print(returnJSON);
	}

	private String getQuestionJSON(ArrayList<String> questions) {
		String jsonString = "{questions: [";
		int i = 0;
		for (String s:questions) {
			if (i+1 >= questions.size())
				jsonString = jsonString + "{\"question\":\"" + s + "\"}";
			else
				jsonString =  jsonString + "{\"question\":\"" + s + "\"},";
			i++;
		}
		jsonString = jsonString + "]}";
		return jsonString;
	}
}
