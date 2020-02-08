package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.interceptors.ResultSetScannerInterceptor;

import authentication.BuildStaticParameters;
import common.Location;
import common.ParticipantDetails;

public class ParticipantDAO {
	
private static Logger slf4jLogger = LoggerFactory.getLogger(ParticipantDAO.class);
	
	public static boolean isUserDuplicate(String usernamme){
		
		String selectQuery = "SELECT * FROM participant WHERE username = ?";
		
		Connection connection = null;
		
		boolean isDuplicate = false;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, usernamme);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.first()) {
				isDuplicate = true;
			}else{
				isDuplicate = false;
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
			isDuplicate = false;
		}
		
		return isDuplicate;
		
	}
	public static boolean insertUserDetails(ParticipantDetails participantDetails){
		
		String insertQuery = "INSERT INTO participant (username, password, age, gender, ethnicity,"
				+ " disability,education,mobileHandlingExperience,psycothereputicMedications,"
				+ "colorblind,targetGroupId) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, participantDetails.getUsername());
			preparedStatement.setString(2, participantDetails.getPassword());
			preparedStatement.setInt(3, Integer.parseInt(participantDetails.getAge()));
			preparedStatement.setString(4, participantDetails.getGender());
			preparedStatement.setString(5, participantDetails.getEthnicity());
			preparedStatement.setString(6, participantDetails.getDisability());
			preparedStatement.setString(7, participantDetails.getEducation());
			preparedStatement.setString(8, participantDetails.getMobileHandlingExperience());
			preparedStatement.setString(9, participantDetails.getPsycothereputicMedications());
			preparedStatement.setString(10, participantDetails.getColorblind());
			preparedStatement.setLong(11,participantDetails.getTargetGroupId());		
			
			slf4jLogger.info(preparedStatement.toString());
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if(rs.first()){
				participantDetails.setId(rs.getLong(1));
				return true;
			}
			connection.close();
			return false;
			
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while extracting field information");
			slf4jLogger.info(e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}
	
	public static void deleteParticipant(String name){
		
		slf4jLogger.info("Entered into deleteParticipant");
		
		String selectQuery = "DELETE FROM participant WHERE username = ?";
		
		Connection connection = null;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);
			int deleted = preparedStatement.executeUpdate();
			slf4jLogger.info("Deleted : "+ deleted);
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
	
	public static void validateParticipant(ParticipantDetails participantDetails){
		
		slf4jLogger.info("Entered into validateParticipant");
		
		String selectQuery = "SELECT id, targetGroupId FROM participant WHERE username = ? AND password = ?";
		
		boolean isParticipant = false;
		Connection connection = null;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, participantDetails.getUsername());
			preparedStatement.setString(2, participantDetails.getPassword());
			
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				participantDetails.setId(rs.getLong(1));
				participantDetails.setTargetGroupId(rs.getLong(2));
			}else{
				participantDetails.setId(null);
			}
			connection.close();
 			
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while finding user information");
			slf4jLogger.info(e.getMessage());
			participantDetails.setId(null);
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
}
