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
import common.Location;
import common.TargetGroup;

public class TargetGroupDAO {

	private static Logger slf4jLogger = LoggerFactory.getLogger(LocationDAO.class);
	
	public static void deleteTargetGroup(String name){
		
		slf4jLogger.info("Entered into deleteTargetGroup");
		
		String selectQuery = "DELETE FROM targetgroup WHERE name = ?";
		
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
	
	public static boolean createTargetGroup(TargetGroup targetGroup){
			
		slf4jLogger.info("Entered into createTargetGroup");
		
		String insertQuery = "INSERT INTO targetgroup (name, description, keywords, "
				+ "locationId, trainingId, registrationCode) values (?, ?, ?, ?, ?, ?)";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, targetGroup.getName());
			preparedStatement.setString(2, targetGroup.getDesc());
			preparedStatement.setString(3, targetGroup.getKeywords());
			preparedStatement.setLong(4, targetGroup.getLocId());
			preparedStatement.setLong(5, targetGroup.getTrainingId());
			String formattedNumber = String.format("%04d", targetGroup.getTrainingCount());
			String regCode = targetGroup.getLocCode()+formattedNumber;
			preparedStatement.setString(6, regCode);
			targetGroup.setRegCode(regCode);
			
			slf4jLogger.info(preparedStatement.toString());
			int updated = preparedStatement.executeUpdate();
			connection.close();
			if(updated == 1) {
				return true;
			}
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
	
	public static long getTargetGroupCountPerLocation(Long locId){
		
		slf4jLogger.info("Entered into getTargetGroupCountPerLocation");
		
		String selectQuery = "SELECT * FROM targetgroup where locationId = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setLong(1,locId);
			slf4jLogger.info(preparedStatement.toString());
			
			ResultSet rs = preparedStatement.executeQuery();
			int size =0;  
			if (rs != null)   
			{  
				rs.beforeFirst();  
				rs.last();  
				size = rs.getRow();
			}
			connection.close();
			return size;
			
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
		return 0;
	}
	
	public static boolean isDuplicateTargetGroup(String name){
		
		slf4jLogger.info("Entered into isDuplicateTargetGroup");
		
		String selectQuery = "SELECT * FROM targetgroup WHERE name = ?";
		
		Connection connection = null;
		
		boolean isDuplicate = false;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);
			
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
	
	public static JSONArray fetchAllTargetGroup(){
		
		slf4jLogger.info("Entered into fetchAllTargetGroup");
		
		String selectQuery = "SELECT tg.id,  tg.name, tg.description, tg.keywords, tg.locationid, tg.trainingid, loc.locName, train.name, "
				+ "tg.registrationCode FROM "
				+ "targetgroup as tg join location as loc on loc.id = tg.locationId join training as train on train.id = tg.trainingId";
		
		Connection connection = null;
		JSONArray jsonArray = new JSONArray();
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constant.TG_ID, Long.toString(rs.getLong(1)));
				jsonObject.put(Constant.TG_NAME, rs.getString(2));
				jsonObject.put(Constant.TG_DESCRIPTION, rs.getString(3));
				jsonObject.put(Constant.TG_KEYWORDS, rs.getString(4));
				jsonObject.put(Constant.TG_LOCATION_ID, Long.toString(rs.getLong(5)));
				jsonObject.put(Constant.TG_TRAINING_ID, Long.toString(rs.getLong(6)));
				jsonObject.put(Constant.TG_LOCATION_NAME, rs.getString(7));
				jsonObject.put(Constant.TG_TRAINING_NAME, rs.getString(8));
				jsonObject.put(Constant.TG_REG_CODE, rs.getString(9));
				jsonArray.add(jsonObject);
		
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
		return jsonArray;
	}
	
	public static boolean updateTargetGroup(TargetGroup targetGroup){
		
		slf4jLogger.info("Entered into updateTargetGroup");
		
		String updateQuery = "UPDATE TARGETGROUP SET TARGETGROUP.NAME=?, TARGETGROUP.DESCRIPTION=?, TARGETGROUP.KEYWORDS=?, TARGETGROUP.LOCATIONID=?, "
				+ "TARGETGROUP.TRAININGID=? WHERE TARGETGROUP.id = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			
			preparedStatement.setString(1, targetGroup.getName());
			preparedStatement.setString(2, targetGroup.getDesc());
			preparedStatement.setString(3, targetGroup.getKeywords());
			preparedStatement.setLong(4, targetGroup.getLocId());
			preparedStatement.setLong(5, targetGroup.getTrainingId());
			preparedStatement.setLong(6, targetGroup.getId());
			
			slf4jLogger.info(preparedStatement.toString());
			int updated = preparedStatement.executeUpdate();
			connection.close();
			if(updated == 1) {
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
	
	public static Long getTargetGroupIdByRegCode(String regCode){
		
		slf4jLogger.info("Entered into getTargetGroupIdByRegCode");
		
		String selectQuery = "SELECT * FROM targetgroup WHERE registrationCode = ?";
		Connection connection = null;
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, regCode);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			Long id = null;
			if(rs.first()) {
				id = rs.getLong("id");
			}
			connection.close();
			return id;
		}catch(SQLException e){
			try {
				if (connection != null){
					connection.close();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
	
}
