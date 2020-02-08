package registration;

import authentication.BuildStaticParameters;
import common.Constant;
import common.ParticipantDetails;
import dao.ParticipantDAO;
import dao.TargetGroupDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class Register
 * for getting the data from the android application
 * and store it into the database
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String type = request.getParameter("queryType");
		JSONObject returnJSON = new JSONObject();
		try {
			response.setCharacterEncoding("UTF-8");
			if(type != null && type.equalsIgnoreCase("getAgreement")){
				response.setContentType("plain/text");
				ServletContext ctx = getServletContext();
				File file=new File(ctx.getRealPath("/"));
				File f = new File(file.getParent()+"/appData/agreement.txt");
				@SuppressWarnings("resource")
				FileInputStream fis = new FileInputStream(f);
				byte[] data = new byte[(int) file.length()];
				fis.read(data);
				
				String agreement=new String(data,"UTF-8");
				response.getWriter().write(agreement);
				return;
			}
			
		}catch(Exception e){
			returnJSON.put(Constant.MESSAGE, e.getMessage());
			response.getWriter().print(returnJSON);
			return;
		}
		
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		ParticipantDetails participantDetails = parseInput(request);
		JSONObject returnJSON = new JSONObject();
		try{
			Long targetGroupId = TargetGroupDAO.getTargetGroupIdByRegCode(participantDetails.getRegCode());
			if(targetGroupId != null){
				participantDetails.setTargetGroupId(targetGroupId);
				boolean isDuplicate = ParticipantDAO.isUserDuplicate(participantDetails.getUsername());
				if(!isDuplicate){
					ParticipantDAO.insertUserDetails(participantDetails);
					
					returnJSON.put(Constant.STATUS, "1");
					returnJSON.put(Constant.PARTICIPANTID, participantDetails.getId());
					returnJSON.put(Constant.MESSAGE, "Successfully Registered");
				//	result =  "{\"status\":1,\"userId\":" + participantDetails.getId() + ",\"message\":\"Successfully Registered.\"}";
					response.getWriter().print(returnJSON);
					return;
				}else{
					
					returnJSON.put(Constant.STATUS, "0");
					returnJSON.put(Constant.MESSAGE, "User Already Exists");
					response.getWriter().print(returnJSON);
					return;
				}
			}else{
				returnJSON.put(Constant.STATUS, "0");
				returnJSON.put(Constant.MESSAGE, "Invalid Registration Code");
				response.getWriter().print(returnJSON);
				return;
			}
		
			
		} catch(Exception e) {
		
			ParticipantDAO.deleteParticipant(request.getParameter(Constant.USERNAME));
			returnJSON.put(Constant.STATUS, "0");
			returnJSON.put(Constant.MESSAGE, e.getMessage());
			response.getWriter().print(returnJSON);
			return;
		}
	}
		

	private ParticipantDetails parseInput(HttpServletRequest request){
		
		ParticipantDetails participantDetails = new ParticipantDetails();
		
		participantDetails.setUsername(request.getParameter(Constant.USERNAME));
		participantDetails.setPassword(request.getParameter(Constant.PARTICIPANT_PASSWORD));
		participantDetails.setAge(request.getParameter(Constant.AGE));
		participantDetails.setGender(request.getParameter(Constant.GENDER));
		participantDetails.setEthnicity( request.getParameter(Constant.ETHNICITY));
		participantDetails.setDisability(request.getParameter(Constant.DISABILITY));
		participantDetails.setEducation(request.getParameter(Constant.EDUCATION));
		participantDetails.setMobileHandlingExperience(request.getParameter(Constant.MOBILE_EXPE));
		participantDetails.setPsycothereputicMedications(request.getParameter(Constant.PSYCOMEDS));
		participantDetails.setColorblind(request.getParameter(Constant.COLOR));
		participantDetails.setRegCode(request.getParameter(Constant.REGCODE));
		return participantDetails;
		
	}
	
}
