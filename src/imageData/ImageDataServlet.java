package imageData;

import authentication.BuildStaticParameters;
import common.Constant;
import dao.ColorDAO;
import dao.ImageDAO;
import dao.QuestionDAO;
import dao.SessionDAO;
import fieldValidation.CommonFieldsVal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Servlet implementation class ImageDataServlet
 */
@WebServlet("/ImageDataServlet")
public class ImageDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageDataServlet() {
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
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=UTF-8");
		JSONObject returnJSON = new JSONObject();
		try{
	        //String questionSession = request.getParameter("questionSession");
	        String participantId = request.getParameter(Constant.PARTICIPANTID);
	        String sessionIdStr = request.getParameter(Constant.SESSION_ID);
	        String[] responses = request.getParameterValues(Constant.RESPONSES);
	        
	       	boolean success = false;
	       	if(responses!= null && responses.length > 0){
	       		try{
					Gson gson = new Gson();
					ArrayList<HashMap<String, String>> responseList = gson.fromJson(responses[0], new TypeToken<ArrayList<HashMap<String, String>>>() {}.getType());
					
					if(responseList.size() != 0 && sessionIdStr != null && participantId != null &&
							CommonFieldsVal.validateFieldId(sessionIdStr) &&
							CommonFieldsVal.validateFieldId(participantId)){
						
						// Save the question response in the table.
						success =  ImageDAO.saveImageResponse(responseList, Long.parseLong(sessionIdStr), participantId);
					}
	       		}catch(Exception e){
	       			e.printStackTrace();
	       			returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
	       		}
			}
	       	if(success){
	       		returnJSON.put(Constant.SAVE, Constant.SUCCESSFUL);
			}else{
				returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
			}
		}catch(Exception e){
			e.printStackTrace();
			returnJSON.put(Constant.SAVE, Constant.UNSUCCESSFUL);
		}
		response.getWriter().print(returnJSON);
	}

}
