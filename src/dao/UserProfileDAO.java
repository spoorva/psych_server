package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.Constant;
import common.UserProfile;

public class UserProfileDAO {
	
	private static Logger slf4jLogger = LoggerFactory.getLogger(UserProfileDAO.class);
	
	public static boolean udpateUserProfile(UserProfile userProfile, String currentEmail){
		
		slf4jLogger.info("Entered into udpateUserProfile");
		
		String selectQuery;
		if(!userProfile.getPassword().equals(""))
			selectQuery = "UPDATE ADMIN SET FIRSTNAME = ?, LASTNAME = ?,  EMAIL= ?,  PASSWORD = ? WHERE EMAIL = ?";
		else
			selectQuery = "UPDATE ADMIN SET FIRSTNAME = ?, LASTNAME = ?,  EMAIL= ? WHERE EMAIL = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, userProfile.getFirstName());
			preparedStatement.setString(2, userProfile.getLastName());
			preparedStatement.setString(3, userProfile.getEmail());
			if(!userProfile.getPassword().equals("")){
				preparedStatement.setString(4, userProfile.getPassword());
				preparedStatement.setString(5, currentEmail);
			}else{
				preparedStatement.setString(4, currentEmail);
			}
			
			// execute select SQL statement
			int affectedLines = preparedStatement.executeUpdate();
			connection.close();
			if(affectedLines == 1) {
				return true;
			}
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
			return false;
		}
		
	}
	
	public static boolean isDuplicateEmail(String email){
		
		
		slf4jLogger.info("Entered into isDuplicateEmail");
		String selectQuery = "SELECT * FROM ADMIN WHERE EMAIL = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, email);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.first()) {
				connection.close();
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
			return false;
		} 
	}
	
	
	public static JSONObject getAllParticipants(){
		
		slf4jLogger.info("Entered into getAllParticipants");
		String selectQuery = "SELECT id AS pId, username AS pUserName, targetGroupId AS pTgId FROM PSYCH.PARTICIPANT;";
		
		Connection connection = null;
		
		JSONObject returnJSON = new JSONObject();
		
		JSONArray jsonArray = new JSONArray();
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constant.PARTICIPANTID,rs.getString("pId"));
				jsonObject.put(Constant.USERNAME, rs.getString("pUserName"));
				jsonObject.put(Constant.TARGET_GROUP_ID, rs.getString("pTgId"));
				jsonArray.add(jsonObject);
			}
			
			returnJSON.put(Constant.STATUS, Constant.OK_200);
			returnJSON.put(Constant.USER_MESSAGE, "Retrieved all participants successfully!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Retrieved all participants successfully.");
			
			connection.close();
			
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while extracting field information");
			slf4jLogger.info(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Error retrieving all participants");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, e.getMessage());
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} 
		returnJSON.put(Constant.RESULTS, jsonArray);
		
		return returnJSON;
	}

}
