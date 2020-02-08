package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Statement;

public class SessionDAO {
	
	private static Logger slf4jLogger = LoggerFactory.getLogger(SessionDAO.class);
	public static long getLatestSessionNumber(String participantId){
		
		long defaultSessionNumber = 0;
		slf4jLogger.info("Within getLatestSessionNumber");
		String selectQuery = "SELECT * FROM userSession WHERE participantId = ? order by sessionNumber";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setLong(1, Long.parseLong(participantId));
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				if(rs.last()){
					defaultSessionNumber = rs.getLong("sessionNumber");
				}
			}
			connection.close();
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return defaultSessionNumber;
	}
	
	public static long createNewSession(String participantId, Long newSessionNumber){
		
		slf4jLogger.info("Within createNewSession");
		String insertQuery = "INSERT INTO userSession (participantId, sessionDate, sessionNumber) VALUES (?, ?, ?)";
		Connection connection = null;
		Long sessionId = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, Long.parseLong(participantId));
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("EST"));
			
			java.util.Date currentDate = calendar.getTime();
			java.sql.Date date = new java.sql.Date(currentDate.getTime());
			preparedStatement.setDate(2, date);
			preparedStatement.setLong(3, newSessionNumber);
			
			// execute select SQL statement
			slf4jLogger.info("Query :" +preparedStatement.toString());
			preparedStatement.executeUpdate();
			
			ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
            	sessionId = rs.getLong(1);
            }
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return sessionId;
	}
	
	public static boolean createNewParameters(Long sessionId, String participantId, String[] colors){
		
		String insertQuery = "INSERT INTO parameter (participantId, sessionId, posColor, negColor) VALUES (?, ?, ?, ?)";
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setLong(1, Long.parseLong(participantId));
			preparedStatement.setLong(2, sessionId);
			preparedStatement.setString(3, colors[0]);
			preparedStatement.setString(4, colors[1]);
			
			// execute select SQL statement
			int inserted = preparedStatement.executeUpdate();
			
            if(inserted == 1)
            {
            	connection.close();
            	return true;
            }
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}
	
	public static void deleteParameters(Long sessionId, String participantId){
		
		slf4jLogger.info("Entered into deleteParameters");
		String selectQuery = "DELETE FROM parameter WHERE participantId = ? and sessionId = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setLong(1, Long.parseLong(participantId));
			preparedStatement.setLong(2, sessionId);
			preparedStatement.executeUpdate();
			connection.close();
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	public static boolean deleteSession(Long sessionId){
		
		slf4jLogger.info("Entered into deleteSession");
		String selectQuery = "DELETE FROM userSession WHERE id = ?";
		
		Connection connection = null;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setLong(1, sessionId);
			//preparedStatement.setLong(2, sessionNumber);
			int udpated = preparedStatement.executeUpdate();
			connection.close();
			if(udpated == 1){
				return true;
			}
			return false;
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
}
