package parameter;

import authentication.BuildStaticParameters;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Servlet implementation class InitialParameter
 */
@WebServlet("/InitialParameter")
public class InitialParameter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitialParameter() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		String userID = request.getParameter("userID");
		
		if (BuildStaticParameters.conn == null) {
			BuildStaticParameters.buildConnectionWithSQL();
		}
		
		String sql = "select count(*) from background";
		
		String numberColor = "";
		
		Set<Integer> uniqueSet = null;
		
		try {
			ResultSet rs = BuildStaticParameters.stmt.executeQuery(sql);
			int maximum_number = 0;
			while(rs.next()) {
				maximum_number = rs.getInt(1);
			}
			uniqueSet = new HashSet<Integer>();
			uniqueSet = generateRandomUniqueNumber(maximum_number);
			int j = 0;
			for (Integer i : uniqueSet) {
				if (j == uniqueSet.size()-1)
					numberColor = numberColor + i.toString();
				else
					numberColor = numberColor + i.toString() + ",";
				j++;
			}
			
			String sqlColor = "select bgColor from background where backgroundId in (" + numberColor + ")";
			
			ResultSet rs1 = BuildStaticParameters.stmt.executeQuery(sqlColor);
			String color[] = new String[2];
			int i = 0;
			while(rs1.next()){
				color[i] = rs1.getString(1);
				i++;
			}
			
			String sql3 = "select userAge from user where userId = '" + userID + "'"; 
			ResultSet rs2 = BuildStaticParameters.stmt.executeQuery(sql3);
			int age = 0;
			while (rs2.next()){
				age = rs2.getInt(1);
			}
			Float timeInterval = getTimeInterval(age);
			
			String sql4 = "select usSessionNumber from userSession where usUserId = '" + userID + "'";
			
			ResultSet rs3 = BuildStaticParameters.stmt.executeQuery(sql4);
			Date sessionDate = new Date();
			int sessionID = 0;
			if(rs3.next()){
				while(rs3.next()) {
					if(rs3.isLast()) {
						sessionID = rs3.getInt(1);
					}
				}
			}
			sessionID++;
			
			String sql6 = "insert into userSession(usUserId, usSessionNumber, usSessionDate) values (?,?,?)";
			PreparedStatement sessionStmt = BuildStaticParameters.conn.prepareStatement(sql6);
			sessionStmt.setString(1, userID);
			sessionStmt.setInt(2, sessionID);
			sessionStmt.setString(3, sessionDate.toString());
			sessionStmt.executeUpdate();
			
			String sql7 = "insert into parameter(paramUser, paramSessionId, paramSessionDate, paramColorOne, paramColorTwo, paramColorOneType, paramColorTwoType, paramTimeInterval) values (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement paramStmt = BuildStaticParameters.conn.prepareStatement(sql7);
			paramStmt.setString(1, userID);
			paramStmt.setInt(2, sessionID);
			paramStmt.setString(3, sessionDate.toString());
			paramStmt.setString(4, color[0]);
			paramStmt.setString(5, color[1]);
			paramStmt.setString(6, "positive");
			paramStmt.setString(7, "negative");
			paramStmt.setFloat(8, timeInterval);
			paramStmt.executeUpdate();
			
			String result = getJSONStringParameters(color, timeInterval, sessionID);
			response.getWriter().write(result);
		} catch (SQLException e) {
			String result = "{\"status\":\"error\"; \"error\":" + e.getMessage() + numberColor + uniqueSet + uniqueSet.size() + "}";
			response.getWriter().write(result);
			e.printStackTrace();
		}
		
	}

	private float getTimeInterval(int age) {
		return 10;
	}

	private String getJSONStringParameters(String[] colors, Float timeInterval, int sessionId) {
		String result = "";
		String time = timeInterval.toString();
			result = "{\"positiveColor\":\"" + colors[0] + 
					"\",\"negativeColor\":\"" + colors[1] + 
					"\",\"timeInterval\":\"" + time +   
					"\",\"totalGames\": \"7\"" +
					",\"sessionID\":\"" + sessionId + "\"}";
		return result;
	}

	private Set<Integer> generateRandomUniqueNumber(int maximum_number) {
		int set_size_required = 2;
		int set_maximum_size = maximum_number;
		
		Random randomGen = new Random();
		Set<Integer> set = new HashSet<Integer>(set_size_required);
		
		while(set.size() < set_size_required) {
			int i = randomGen.nextInt(set_maximum_size);
			if (i != 0)
				set.add(i);
		}
		return set;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
