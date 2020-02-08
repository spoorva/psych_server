package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.Constant;
import common.QuestionCategory;

public class QuestionCategoryDAO {
	
	public static boolean createQuestionCategory(QuestionCategory qc){
		
		String insertQuery = "INSERT INTO QUESTIONCATEGORY "
								+ "(name, description, responseType, startLabel, endLabel) "
								+ "VALUES (?, ?, ?, ?, ?)";
		
		Connection connection = null;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, qc.getName());
			preparedStatement.setString(2, qc.getDescription());
			preparedStatement.setLong(3, qc.getResponseType());
			preparedStatement.setString(4, qc.getStartLabel());
			preparedStatement.setString(5, qc.getEndLabel());
			
			// execute select SQL stetement
			int rowsAffected = preparedStatement.executeUpdate();
			
			connection.close();
			return rowsAffected == 1;
			
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
	
	public static boolean deleteQuestionCategory(String name){
		
		String deleteQuery = "DELETE FROM QUESTIONCATEGORY WHERE NAME = ?";
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

	public static boolean checkDuplicate(String name){
	
		String selectQuery = "SELECT * FROM QUESTIONCATEGORY WHERE NAME = ?";
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, name);
			
			// execute select SQL stetement
			ResultSet rows = preparedStatement.executeQuery();
			int count = 0;
			
			while (rows.next()){
				count++;
			}
			
			connection.close();
			
			return count != 0;
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return false;
		}
		
	}
	
	public static JSONObject getAll(){
		
		String selectQuery = "SELECT A.ID AS qCatId, A.name AS qCatName, "
				+ "B.id AS fieldId, IFNULL(A.description, '') AS qCatDescription, "
				+ "B.fieldName AS responseType, A.startLabel AS startLabel, "
				+ "A.endLabel AS endLabel "
				+ "FROM QUESTIONCATEGORY AS A "
				+ "INNER JOIN FIELDLOOKUP AS B "
				+ "ON A.RESPONSETYPE = B.ID;";
		
		JSONObject returnJSON = new JSONObject();
		JSONArray results = new JSONArray();
		
		
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			// execute select SQL stetement
			ResultSet rows = preparedStatement.executeQuery();
			
			while (rows.next()){
				JSONObject object = new JSONObject();
				
				object.put(Constant.QUESTION_CATEGORY_ID, rows.getString("qCatId"));
				object.put(Constant.QUESTION_CATEGORY_NAME, rows.getString("qCatName"));
				object.put(Constant.QUESTION_CATEGORY_DESCRIPTION, rows.getString("qCatDescription"));
				object.put(Constant.QUESTION_CATEGORY_RESPONSE_TYPE, rows.getString("responseType"));
				object.put(Constant.QUESTION_CATEGORY_START_LABEL, rows.getString("startLabel"));
				object.put(Constant.QUESTION_CATEGORY_END_LABEL, rows.getString("endLabel"));
				object.put(Constant.RESPONSE_TYPE_FIELD_ID, rows.getString("fieldId"));
				
				results.add(object);
			}
			returnJSON.put(Constant.RESULTS, results);
			returnJSON.put(Constant.STATUS, Constant.OK_200);
			returnJSON.put(Constant.USER_MESSAGE, "Successfully retrieved all question categories!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Successfully retrieved all question categories");
			
			connection.close();
			
			return returnJSON;
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Error in retrieving all question categories!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Error retrieving all question categories: " + e.getMessage());
			
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return returnJSON;
		}
		
	}

	public static JSONObject udpateQuestionCategory(QuestionCategory qc) {
		String updateQuery = "UPDATE QUESTIONCATEGORY SET name=?, description=?, responseType=?, startLabel=?, endLabel=? WHERE ID=?";
		
		JSONObject returnJSON = new JSONObject();

		Connection connection = null;
		
		try{
		
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			
			preparedStatement.setString(1, qc.getName());
			preparedStatement.setString(2, qc.getDescription());
			preparedStatement.setLong(3, qc.getResponseType());
			preparedStatement.setString(4, qc.getStartLabel());
			preparedStatement.setString(5, qc.getEndLabel());
			preparedStatement.setLong(6, qc.getId());
			
			// execute select SQL stetement
			int rowsAffected = preparedStatement.executeUpdate();
			
			
			if (rowsAffected == 1){
				JSONObject updated = new JSONObject();
				
				updated.put(Constant.NEW_QUESTION_CATEGORY_NAME, qc.getName());
				updated.put(Constant.NEW_QUESTION_CATEGORY_DESCRIPTION, qc.getDescription());
				updated.put(Constant.NEW_QUESTION_CATEGORY_RESPONSE_TYPE, qc.getResponseType());
				updated.put(Constant.NEW_QUESTION_CATEGORY_START_LABEL, qc.getStartLabel());
				updated.put(Constant.NEW_QUESTION_CATEGORY_END_LABEL, qc.getEndLabel());
				updated.put(Constant.QUESTION_CATEGORY_ID, qc.getId());
				
				returnJSON.put(Constant.RESULTS, updated);
				
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				returnJSON.put(Constant.USER_MESSAGE, "Updated question category successfully!");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Updated question category successfully!");
			}
			else{
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.USER_MESSAGE, "Could not update question category");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Could not update question category, rowsAffected not 1.");
			}
			
			connection.close();
		
		}catch(SQLException e){
			System.out.println(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Could not update question category");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Could not update question category: " + e.getMessage());
			
			try {
				if (connection != null){
					connection.close();
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return returnJSON;
		}
		
		return returnJSON;
		
	}

}
