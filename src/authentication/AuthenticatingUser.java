package authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.Constant;
import common.ParticipantDetails;
import dao.ParticipantDAO;

import java.io.IOException;
import java.sql.ResultSet;

/**
 * Servlet implementation class AuthenticatingUser
 */
@WebServlet("/AuthenticatingUser")
public class AuthenticatingUser extends HttpServlet {
	
	
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthenticatingUser() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject returnJSON = new JSONObject();
		response.setContentType("application/json");
		String id="-1";
		try {
			response.setCharacterEncoding("UTF-8");
			String result = "0";
			ParticipantDetails participantDetails = parseInput(request);			
			ParticipantDAO.validateParticipant(participantDetails);
			if(participantDetails.getId() != null){				
				returnJSON.put(Constant.SUCCESS, "1");
				returnJSON.put(Constant.USER_ID, Long.toString(participantDetails.getId()));
				returnJSON.put(Constant.TG_ID, Long.toString(participantDetails.getTargetGroupId()));
			    response.getWriter().print(returnJSON);
			    return;
			}
			returnJSON.put(Constant.SUCCESS, "0");
			returnJSON.put(Constant.USER_ID,id);
		    response.getWriter().print(returnJSON);
		} catch (Exception e) {
			e.printStackTrace();
			returnJSON.put(Constant.SUCCESS, "0");
			returnJSON.put(Constant.USER_ID,id);
			response.getWriter().print(returnJSON);
		}
		
	}
	private ParticipantDetails parseInput(HttpServletRequest request){
			
			ParticipantDetails participantDetails = new ParticipantDetails();
			participantDetails.setUsername(request.getParameter(Constant.USERNAME));
			participantDetails.setPassword(request.getParameter(Constant.PARTICIPANT_PASSWORD));
			return participantDetails;
			
		}

}
