package report;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import common.Constant;
import common.Sessions;
import dao.ReportDAO;

/**
 * Servlet implementation class ReportServlet
 */
@WebServlet("/report")
public class ReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportServlet() {
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
		
		JSONObject returnJSON = new JSONObject();
		
		String participant = request.getParameter(Constant.PARTICIPANT);
		String reportType = request.getParameter(Constant.REPORT_TYPE);
		
		String targetGroupId = request.getParameter(Constant.TARGET_GROUP_ID);
		
		HttpSession session = request.getSession(false);
		if(Sessions.isValidGlobalAdminSession(session)){
		
			if (participant != null && reportType != null){
				
				response.setContentType("application/json;charset=UTF-8");
				
				Long participantId = Long.parseLong(participant);
				if(reportType.equals("1")){
					returnJSON = ReportDAO.getAvgResponseTimeForImageResponses(participantId);
				}
				else{
					returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
					returnJSON.put(Constant.DEVELOPER_MESSAGE, "Report type not found.");
				}
			}
			else if (targetGroupId != null && reportType != null){
				
				if(reportType.equals("2")){
					Long tgId = Long.parseLong(targetGroupId);
					
					response.setContentType("application/csv");
					
			        PrintWriter w = response.getWriter();
			        
			        w.println(ReportDAO.generateImageReportFileForTargetGroup(tgId));

			        w.flush();
			        w.close();
				}
				else if(reportType.equals("3")){
					Long tgId = Long.parseLong(targetGroupId);
					
					response.setContentType("application/csv");
					
			        PrintWriter w = response.getWriter();
			        
			        w.println(ReportDAO.generateQuestionReportFileForTargetGroup(tgId));

			        w.flush();
			        w.close();
				}
				
			}
			else{
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Paramaters missing.");
			}
		}
		else{
			returnJSON.put(Constant.STATUS, Constant.UNAUTHORIZED_401);
		}
		
		response.getWriter().print(returnJSON);
	}

}
