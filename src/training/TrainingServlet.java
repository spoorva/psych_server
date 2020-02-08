package training;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

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
import common.Question;
import common.Sessions;
import common.Training;
import common.TrainingImage;
import dao.LocationDAO;
import dao.QuestionDAO;
import dao.TargetGroupDAO;
import dao.TrainingDAO;
import fieldValidation.CommonFieldsVal;
import fieldValidation.LocationFieldsVal;
import fieldValidation.QuestionCategoryFieldsVal;
import location.LocationServlet;

/**
 * Servlet implementation class TrainingServlet
 */
@WebServlet("/training")
public class TrainingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger slf4jLogger = LoggerFactory.getLogger(TrainingServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrainingServlet() {
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
		
		String trainingId = request.getParameter(Constant.TRAINING_ID);
		
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
			if((request.getParameter(Constant.TRAINING_DROPDOWN) != null && 
					request.getParameter(Constant.TRAINING_DROPDOWN).equals(Constant.YES))){
				
				returnJSON.put(Constant.RESULTS, TrainingDAO.fetchAllTrainingName());
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				
			}
			else if (trainingId != null){
				returnJSON = TrainingDAO.fetchTrainingDetailsById(Long.parseLong(trainingId));
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
		String newKeywords;
		ArrayList<Long> questions = new ArrayList<Long>();
		ArrayList<TrainingImage> images = new ArrayList<TrainingImage>();
		
		HttpSession session = request.getSession(false);
		
//		if(session != null && session.getAttribute(Constant.ROLE) != null &&
//				session.getAttribute(Constant.EMAIL) != null &&
//				(session.getAttribute(Constant.ROLE).equals(Constant.GLOBAL_ADMIN) ||
//				(session.getAttribute(Constant.ROLE).equals(Constant.LOCAL_ADMIN)))){
			
			try {
				obj = parser.parse(sb.toString());
				JSONObject jsonObject = (JSONObject) obj;
				newName = (String) jsonObject.get(Constant.TRAINING_NAME);
				newDescrition = (String) jsonObject.get(Constant.TRAINING_DESCRIPTION);
				newKeywords = (String) jsonObject.get(Constant.TRAINING_KEYWORDS);
				JSONArray questionsArray = (JSONArray) jsonObject.get(Constant.TRAINING_QUESTIONS);
				JSONArray imageArray = (JSONArray) jsonObject.get(Constant.TRAINING_IMAGES);
				
				
				if(QuestionCategoryFieldsVal.validateQuestionName(newName) 
						&& CommonFieldsVal.validateDescription(newDescrition)
						&& CommonFieldsVal.validateKeywords(newKeywords)){
				
						
					//System.out.println(newFirstName+ ":"+newLastName + ":"+newEmail + ":" + newPassword +" :" + email);
					for (int i = 0; i < imageArray.size(); i++){
						JSONObject image = (JSONObject) imageArray.get(i);
						Long imageCategoryId = Long.parseLong(image.get(Constant.TRG_IMAGE_MAP_IMAGE_CAT).toString());
						Long imageTypeId = Long.parseLong(image.get(Constant.TRG_IMAGE_MAP_IMAGE_TYPE).toString());
						Integer imageCount = Integer.parseInt(image.get(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES).toString());
						Integer duration = Integer.parseInt(image.get(Constant.TRG_IMAGE_MAP_DURATION).toString());
						
						images.add(new TrainingImage(imageCategoryId, imageTypeId, imageCount, duration));
					}
					
					for (int j = 0; j < questionsArray.size(); j++){
						JSONObject question = (JSONObject) questionsArray.get(j);
						Long questionId = Long.parseLong(question.get(Constant.QUESTION_ID).toString());
						questions.add(questionId);
					}
					
					
					returnJSON  = TrainingDAO.createTraining(new Training(1, newName, newDescrition, newKeywords, questions, images));
					//System.out.println("isUpdated : " + isUpdated);
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
			
//		}else{
//			returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
//			returnJSON.put(Constant.USER_MESSAGE, Constant.UNAUTHORIZED_401);
//			returnJSON.put(Constant.DEVELOPER_MESSAGE, Constant.UNAUTHORIZED_401);
//		}
		
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
		
		Long id;
		String newName;
		String newDescrition;
		String newKeywords;
		ArrayList<Long> questions = new ArrayList<Long>();
		ArrayList<TrainingImage> images = new ArrayList<TrainingImage>();
		
		HttpSession session = request.getSession(false);
		
//		if(session != null && session.getAttribute(Constant.ROLE) != null &&
//				session.getAttribute(Constant.EMAIL) != null &&
//				(session.getAttribute(Constant.ROLE).equals(Constant.GLOBAL_ADMIN) ||
//				(session.getAttribute(Constant.ROLE).equals(Constant.LOCAL_ADMIN)))){
			
			try {
				obj = parser.parse(sb.toString());
				JSONObject jsonObject = (JSONObject) obj;
				id = (Long) jsonObject.get(Constant.TRAINING_ID);
				newName = (String) jsonObject.get(Constant.TRAINING_NAME);
				newDescrition = (String) jsonObject.get(Constant.TRAINING_DESCRIPTION);
				newKeywords = (String) jsonObject.get(Constant.TRAINING_KEYWORDS);
				JSONArray questionsArray = (JSONArray) jsonObject.get(Constant.TRAINING_QUESTIONS);
				JSONArray imageArray = (JSONArray) jsonObject.get(Constant.TRAINING_IMAGES);
				
				
				if(QuestionCategoryFieldsVal.validateQuestionName(newName) 
						&& CommonFieldsVal.validateDescription(newDescrition)
						&& CommonFieldsVal.validateKeywords(newKeywords)){
				
						
					//System.out.println(newFirstName+ ":"+newLastName + ":"+newEmail + ":" + newPassword +" :" + email);
					for (int i = 0; i < imageArray.size(); i++){
						JSONObject image = (JSONObject) imageArray.get(i);
						Long imageCategoryId = Long.parseLong(image.get(Constant.TRG_IMAGE_MAP_IMAGE_CAT).toString());
						Long imageTypeId = Long.parseLong(image.get(Constant.TRG_IMAGE_MAP_IMAGE_TYPE).toString());
						Integer imageCount = Integer.parseInt(image.get(Constant.TRG_IMAGE_MAP_NO_OF_IMAGES).toString());
						Integer duration = Integer.parseInt(image.get(Constant.TRG_IMAGE_MAP_DURATION).toString());
						
						images.add(new TrainingImage(imageCategoryId, imageTypeId, imageCount, duration));
					}
					System.out.println(questionsArray);
					
					for (int j = 0; j < questionsArray.size(); j++){
						JSONObject question = (JSONObject) questionsArray.get(j);
						Long questionId = Long.parseLong(question.get(Constant.QUESTION_ID).toString());
						questions.add(questionId);
					}
					
					
					returnJSON  = TrainingDAO.updateTraining(new Training(id, newName, newDescrition, newKeywords, questions, images));
					//System.out.println("isUpdated : " + isUpdated);
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
			
//		}else{
//			returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
//			returnJSON.put(Constant.USER_MESSAGE, Constant.UNAUTHORIZED_401);
//			returnJSON.put(Constant.DEVELOPER_MESSAGE, Constant.UNAUTHORIZED_401);
//		}
		
		response.getWriter().print(returnJSON);
		response.addHeader("Access-Control-Allow-Origin", Constant.ACCESS_CONTROL_ALLOW_ORIGIN);
		response.addHeader("Access-Control-Allow-Headers", Constant.ACCESS_CONTROL_ALLOW_HEADERS);
		response.addHeader("Access-Control-Allow-Methods", Constant.ACCESS_CONTROL_ALLOW_METHODS);
		response.addIntHeader("Access-Control-Max-Age", Constant.ACCESS_CONTROL_ALLOW_MAX_AGE);
	}
	
}
