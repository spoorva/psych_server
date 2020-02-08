package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import authentication.BuildStaticParameters;
import common.Constant;
import common.Question;

public class QuestionDAO {
	
	public static JSONObject createQuestion(Question q){
		String insertQuery = "INSERT INTO QUESTION (name, description, categoryId) VALUES (?, ?, ?)";
		Connection connection = null;
		JSONObject returnJSON = new JSONObject();
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			
			preparedStatement.setString(1, q.getName());
			preparedStatement.setString(2, q.getDescription());
			preparedStatement.setLong(3, q.getCategoryId());
			
			// execute select SQL stetement
			int rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected == 1){
				
				returnJSON.put(Constant.NEW_QUESTION_NAME, q.getName());
				returnJSON.put(Constant.NEW_QUESTION_DESCRIPTION, q.getDescription());
				returnJSON.put(Constant.NEW_QUESTION_CATEGORY_ID, q.getCategoryId());
				
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				returnJSON.put(Constant.USER_MESSAGE, "Added question successfully!");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Added question successfully!");
				
			}
			else{
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.USER_MESSAGE, "Could not add to database.");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Failed at inserting record.");
			}
			connection.close();
			return returnJSON;
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Could not insert question");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Could not insert question: " + e.getMessage());
			return returnJSON;
		}
	}

	
	public static boolean deleteQuestion(String name){
		
		String deleteQuery = "DELETE FROM QUESTION WHERE NAME = ?";
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

	public static JSONObject updateQuestion(Question question) {
		
		String updateQuery = "UPDATE QUESTION SET name=?, description=?, categoryId=? WHERE ID=?;";
		
		JSONObject returnJSON = new JSONObject();

		Connection connection = null;
		
		try{
		
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			
			preparedStatement.setString(1, question.getName());
			preparedStatement.setString(2, question.getDescription());
			preparedStatement.setLong(3, question.getCategoryId());
			preparedStatement.setLong(4, question.getId());
			
			// execute select SQL statement
			int rowsAffected = preparedStatement.executeUpdate();
						
			if (rowsAffected == 1){
				JSONObject updated = new JSONObject();
				
				updated.put(Constant.NEW_QUESTION_NAME, question.getName());
				updated.put(Constant.NEW_QUESTION_DESCRIPTION, question.getDescription());
				updated.put(Constant.NEW_QUESTION_CATEGORY_ID, question.getCategoryId());
				updated.put(Constant.QUESTION_ID, question.getId());
				
				returnJSON.put(Constant.RESULTS, updated);
				
				returnJSON.put(Constant.STATUS, Constant.OK_200);
				returnJSON.put(Constant.USER_MESSAGE, "Updated question successfully!");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Updated question successfully!");
			}
			else{
				returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
				returnJSON.put(Constant.USER_MESSAGE, "Could not update question");
				returnJSON.put(Constant.DEVELOPER_MESSAGE, "Could not update question, rowsAffected not 1.");
			}
			
			connection.close();
		
		}catch(SQLException e){
			System.out.println(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Could not update question");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Could not update question: " + e.getMessage());
			
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
	
	
	public static JSONObject getAll(){
		
		String selectQuery = "SELECT A.ID AS qId, B.ID AS qCategoryId, A.NAME AS qName, IFNULL(A.DESCRIPTION, '') AS qDescription," +
								"B.NAME AS catName " + 
								"FROM psych.QUESTION AS A "+
								"INNER JOIN psych.QUESTIONCATEGORY AS B "+
								"ON A.categoryId = B.id;";
		
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
				
				object.put(Constant.QUESTION_ID, rows.getString("qId"));
				object.put(Constant.QUESTION_NAME, rows.getString("qName"));
				object.put(Constant.QUESTION_DESCRIPTION, rows.getString("qDescription"));
				object.put(Constant.QUESTION_CATEGORY_NAME, rows.getString("catName"));
				object.put(Constant.QUESTION_CATEGORY_ID, rows.getString("qCategoryId"));
				results.add(object);
			}
			returnJSON.put(Constant.RESULTS, results);
			returnJSON.put(Constant.STATUS, Constant.OK_200);
			returnJSON.put(Constant.USER_MESSAGE, "Successfully retrieved all questions!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Successfully retrieved all questions");
			
			connection.close();
			
			return returnJSON;
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Error in retrieving all questions!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Error retrieving all questions: " + e.getMessage());
			
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


	public static boolean checkDuplicate(String name) {
		String selectQuery = "SELECT * FROM QUESTION WHERE NAME = ?";
		
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
	
	public static JSONObject getAllQuestionsByTargetGroupId(Long targetGroupId){
		
		String selectQuery = "SELECT D.NAME AS questionName, F.FIELDNAME AS responseType, "
				+ "D.ID AS questionId, E.STARTLABEL AS startLabel, E.ENDLABEL AS endLabel "
				+ "FROM PSYCH.TARGETGROUP AS A INNER JOIN PSYCH.TRAINING AS B ON A.TRAININGID = B.ID "
				+ "INNER JOIN PSYCH.TRAININGQUESTIONMAP AS C ON B.ID = C.TRAININGID "
				+ "INNER JOIN PSYCH.QUESTION AS D ON D.ID = C.QUESTIONID "
				+ "INNER JOIN PSYCH.QUESTIONCATEGORY AS E ON E.ID = D.CATEGORYID "
				+ "INNER JOIN PSYCH.FIELDLOOKUP AS F ON F.ID = E.RESPONSETYPE "
				+ "WHERE A.ID = ?;";

			JSONObject returnJSON = new JSONObject();
			JSONArray results = new JSONArray();
			
			
			Connection connection = null;
			
			try{
			
			connection = DBSource.getConnectionPool().getConnection();
			
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setLong(1, targetGroupId);
			
			// execute select SQL stetement
			ResultSet rows = preparedStatement.executeQuery();
			
			while (rows.next()){
				JSONObject object = new JSONObject();
				
				object.put(Constant.QUESTION_ID, rows.getLong("questionId"));
				object.put(Constant.QUESTION_NAME, rows.getString("questionName"));
				object.put(Constant.QUESTION_CATEGORY_START_LABEL, rows.getString("startLabel"));
				object.put(Constant.QUESTION_CATEGORY_END_LABEL, rows.getString("endLabel"));
				object.put(Constant.QUESTION_CATEGORY_RESPONSE_TYPE, rows.getString("responseType"));
				
				results.add(object);
			}
			returnJSON.put(Constant.RESULTS, results);
			returnJSON.put(Constant.STATUS, Constant.OK_200);
			returnJSON.put(Constant.USER_MESSAGE, "Successfully retrieved all questions for targetGroupId!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Successfully retrieved all questions for targetGroupId");
			
			connection.close();
			
			return returnJSON;
			
			}catch(SQLException e){
			System.out.println(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Error in retrieving all questions for targetGroupId!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Error retrieving all questions for targetGroupId: " + e.getMessage());
			
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
	
	
	public static boolean saveQuestionResponse(ArrayList<HashMap<String, String>> responseList, Long sessionId, String participantId,
			String questoinStage){
		
		String insertQuery = "INSERT INTO questionResponse (sessionId, participantId, questionId, response, questionStage) "
				+ "VALUES (?, ?, ?, ?, ?)";
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			int length = responseList.size();
			for(int i=0; i< length; i++){
				HashMap<String, String> response = responseList.get(i);
				preparedStatement.setLong(1, sessionId);
				preparedStatement.setLong(2, Long.parseLong(participantId));
				preparedStatement.setLong(3, Long.parseLong(response.get(Constant.QUESTION_ID)));
				preparedStatement.setString(4, response.get(Constant.RESPONSE));
				preparedStatement.setString(5, questoinStage);
				// execute select SQL statement
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected != 1){
					System.out.println("Issue while insert");
					System.out.println(preparedStatement.toString());
					return false;
				}
			}
			connection.close();
			return true;
		}catch(SQLException e){
			System.out.println(e.getMessage());
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean deleteQuestionResponses(Long sessionId){
		
		String deleteQuery = "DELETE FROM questionresponse WHERE sessionId = ?";
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setLong(1, sessionId);
			
			// execute select SQL statement
			int rowsAffected = preparedStatement.executeUpdate();
			
			connection.close();
			if (rowsAffected >= 1){
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
	
	public static boolean deleteSessionParameterAndSession(Long sessionId){
		
		String deleteParameterQuery = "DELETE FROM parameter WHERE sessionId = ?";
		String deleteSessionQuery = "DELETE FROM usersession WHERE id = ?";
		Connection connection = null;
		
		try{
			
			connection = DBSource.getConnectionPool().getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(deleteParameterQuery);
			preparedStatement.setLong(1, sessionId);
			
			// execute select SQL statement
			int rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected >= 1){
				
				preparedStatement = connection.prepareStatement(deleteSessionQuery);
				preparedStatement.setLong(1, sessionId);
				
				// execute select SQL statement
				rowsAffected = preparedStatement.executeUpdate();
				
				if (rowsAffected >= 1){
					connection.close();
					return true;
				}
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
	
}
