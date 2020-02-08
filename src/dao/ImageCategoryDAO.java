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
import common.ImageCategory;
import common.Location;

public class ImageCategoryDAO {

	private static Logger slf4jLogger = LoggerFactory.getLogger(ImageCategoryDAO.class);
	
	public static boolean deleteImageCategory(String name){
		
		String deleteQuery = "DELETE FROM IMAGECATEGORY WHERE NAME = ?";
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			
			preparedStatement.setString(1, name);
			
			// execute select SQL stetement
			int rowsAffected = preparedStatement.executeUpdate();
			
			connection.close();
			
			if (rowsAffected == 1){
				return true;
			}
			else{
				return false;
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
			return false;
		}
		
	}
	
	public static Long getImageCategoryIdByName(String name){
		
		String selectQuery = "SELECT id FROM imagecategory WHERE name = ?";
		
		Connection connection = null;
		Long id = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, name);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.first()) {
				id = rs.getLong("id");
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
		return id;
	}
	
	public static boolean createImageCategory(ImageCategory imageCategory){
		
		slf4jLogger.info("Entered into createImageCategory");
		
		String insertQuery = "INSERT INTO IMAGECATEGORY (IMAGECATEGORY.NAME, IMAGECATEGORY.DESCRIPTION) "
				+ "values (?, ?)";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, imageCategory.getName());
			preparedStatement.setString(2, imageCategory.getDesc());
			
			slf4jLogger.info(preparedStatement.toString());
			int created = preparedStatement.executeUpdate();
			connection.close();
			if(created == 1) {
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
	
	public static boolean isDuplicateImageCategory(String name, String id){
		
		slf4jLogger.info("Entered into isDuplicateImageCategory");
		
		String selectQuery;
		if(id == null)
			selectQuery = "SELECT * FROM IMAGECATEGORY WHERE NAME = ?";
		else
			selectQuery = "SELECT * FROM IMAGECATEGORY WHERE NAME = ? AND ID <> ?";
		Connection connection = null;
		
		boolean isDuplicate = false;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setString(1, name);
			
			if(id != null)
				preparedStatement.setLong(2, Long.parseLong(id));
			
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
	
	
	public static JSONArray fetchAllImageCategory(){
		
		slf4jLogger.info("Entered into fetchAllImageCategory");
		String selectQuery = "SELECT * FROM IMAGECATEGORY";
		
		Connection connection = null;
		JSONArray jsonArray = new JSONArray();
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Constant.IMAGE_CATEGORY_ID, rs.getLong("id"));
				jsonObject.put(Constant.IMAGE_CATEGORY_NAME,rs.getString("name"));
				jsonObject.put(Constant.IMAGE_CATEGORY_DESCRIPTION,rs.getString("description"));
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
	
	public static boolean updateImageCategory(ImageCategory imageCategory){
		
		slf4jLogger.info("Entered into updateImageCategory");
		
		String updateQuery = "UPDATE IMAGECATEGORY SET IMAGECATEGORY.NAME=?, IMAGECATEGORY.description=?"
				+ " WHERE IMAGECATEGORY.id = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			
			preparedStatement.setString(1, imageCategory.getName());
			preparedStatement.setString(2, imageCategory.getDesc());
			preparedStatement.setLong(3, imageCategory.getId());
			
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
}
