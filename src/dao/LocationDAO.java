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

public class LocationDAO {
	
	private static Logger slf4jLogger = LoggerFactory.getLogger(LocationDAO.class);
	
	public static boolean createLocation(Location location){
			
		slf4jLogger.info("Entered into createLocation");
		
		String insertQuery = "INSERT INTO LOCATION (location.locCode, location.locName, location.description, location.keywords, "
				+ "location.addressLine1, location.addressLine2,  location.city, location.state, location.zipcode, location.phoneNumber, "
				+ "location.faxNumber, location.email) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, location.getCode());
			preparedStatement.setString(2, location.getName());
			preparedStatement.setString(3, location.getDesc());
			preparedStatement.setString(4, location.getKeywords());
			preparedStatement.setString(5, location.getAddressLine1());
			preparedStatement.setString(6, location.getAddressLine2());
			preparedStatement.setString(7, location.getCity());
			preparedStatement.setLong(8, location.getStateId());
			preparedStatement.setString(9, location.getZipCode());
			preparedStatement.setString(10, location.getPhoneNumber());
			preparedStatement.setString(11, location.getFaxNumber());
			preparedStatement.setString(12, location.getEmail());
			
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
	
	
	public static boolean isDuplicateLocation(String name){
		
		slf4jLogger.info("Entered into isDuplicateLocation");
		
		String selectQuery = "SELECT * FROM LOCATION WHERE LOCNAME = ?";
		
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
	
	public static void deleteLocation(String name){
		
		slf4jLogger.info("Entered into deleteLocation");
		String selectQuery = "DELETE FROM LOCATION WHERE LOCNAME = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);
			slf4jLogger.info("Delete query :" + preparedStatement.toString());
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

	public static boolean isDuplicateLocationCode(String code){
		
		slf4jLogger.info("Entered into isDuplicateLocationCode");
		String selectQuery = "SELECT * FROM LOCATION WHERE LOCCODE = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, code);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.first()) {
				connection.close();
				return true;
			}
			connection.close();
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
	
	public static JSONArray fetchAllLocation(){
		
		slf4jLogger.info("Entered into fetchAllLocation");
		String selectQuery = "SELECT * FROM location join fieldlookup on location.state = fieldlookup.id";
		
		Connection connection = null;
		JSONArray jsonArray = new JSONArray();
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constant.LOCATION_ID,Long.toString(rs.getLong("id")));
				jsonObject.put(Constant.LOCATION_NAME,rs.getString("locName"));
				jsonObject.put(Constant.LOCATION_DESCRIPTION,rs.getString("description"));
				jsonObject.put(Constant.LOCATION_KEYWORDS,rs.getString("keywords"));
				jsonObject.put(Constant.LOCATION_CODE,rs.getString("locCode"));
				jsonObject.put(Constant.LOCATION_ADDRESS_LINE_1,rs.getString("addressLine1"));
				jsonObject.put(Constant.LOCATION_ADDRESS_LINE_2,rs.getString("addressLine2"));
				jsonObject.put(Constant.LOCATION_CITY,rs.getString("city"));
				jsonObject.put(Constant.LOCATION_STATE,rs.getString("fieldName"));
				jsonObject.put(Constant.LOCATION_STATE_ID,Long.toString(rs.getLong("state")));
				jsonObject.put(Constant.LOCATION_ZIPCODE,rs.getString("zipcode"));
				jsonObject.put(Constant.LOCATION_PHONE_NUMBER,rs.getString("phoneNumber"));
				jsonObject.put(Constant.LOCATION_FAX_NUMBER,rs.getString("faxNumber"));
				jsonObject.put(Constant.LOCATION_EMAIL,rs.getString("email"));
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
	
	public static boolean updateLocation(Location location){
		
		slf4jLogger.info("Entered into updateLocation");
		
		String updateQuery = "UPDATE LOCATION SET location.locCode=?, location.locName=?, location.description=?, location.keywords=?, "
				+ "location.addressLine1=?, location.addressLine2=?,  location.city=?, location.state=?, location.zipcode=?, location.phoneNumber=?, "
				+ "location.faxNumber=?, location.email=? WHERE location.id = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			
			preparedStatement.setString(1, location.getCode());
			preparedStatement.setString(2, location.getName());
			preparedStatement.setString(3, location.getDesc());
			preparedStatement.setString(4, location.getKeywords());
			preparedStatement.setString(5, location.getAddressLine1());
			preparedStatement.setString(6, location.getAddressLine2());
			preparedStatement.setString(7, location.getCity());
			preparedStatement.setLong(8, location.getStateId());
			preparedStatement.setString(9, location.getZipCode());
			preparedStatement.setString(10, location.getPhoneNumber());
			preparedStatement.setString(11, location.getFaxNumber());
			preparedStatement.setString(12, location.getEmail());
			
			preparedStatement.setLong(13, location.getId());
			
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
	
	public static String getLocationCodeByLocationId(Long locationId){
		
		slf4jLogger.info("Entered into isDuplicateLocationCode");
		String selectQuery = "SELECT locCode FROM location WHERE id = ?";
		
		Connection connection = null;
		String locationCode = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setLong(1, locationId);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.first()) {
				locationCode = rs.getString("locCode");
			}
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
		return locationCode;
	}
	

}
