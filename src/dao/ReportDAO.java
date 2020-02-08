package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.Constant;
import edu.emory.mathcs.backport.java.util.Arrays;

public class ReportDAO {
	
	public static JSONObject getAvgResponseTimeForImageResponses(Long participantId){
		
		JSONObject returnJSON = new JSONObject();
		
		JSONObject results = new JSONObject();
		
		try{
			JSONObject avgCorrect = getAvgResponseTimeForImageResponsesHelper(participantId, 1);
			JSONObject avgWrong = getAvgResponseTimeForImageResponsesHelper(participantId, 0);
			JSONObject correctAndIncorrectCount = getCorrectAndIncorrectCount(participantId);
			results.put(Constant.AVG_IMAGE_RESPONSE_CORRECT, avgCorrect.get(Constant.RESULTS));
			results.put(Constant.AVG_IMAGE_RESPONSE_WRONG, avgWrong.get(Constant.RESULTS));
			results.put(Constant.CORRECT_AND_INCORRECT_COUNT, correctAndIncorrectCount.get(Constant.RESULTS));
			
			returnJSON.put(Constant.RESULTS, results);
			returnJSON.put(Constant.STATUS, Constant.OK_200);
			returnJSON.put(Constant.USER_MESSAGE, "Successfully retrieved all average response times for image responses!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Successfully retrieved all average response times for image responses");
			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			returnJSON.put(Constant.STATUS, Constant.BADREQUEST_400);
			returnJSON.put(Constant.USER_MESSAGE, "Error in retrieving average response times for image responses!");
			returnJSON.put(Constant.DEVELOPER_MESSAGE, "Error retrieving average response times for image responses!: " + e.getMessage());
		}
		
		return returnJSON;
	}
	
	
	public static JSONObject getAvgResponseTimeForImageResponsesHelper(Long pId, int correctness) throws SQLException{
		
		String selectQuery = "select a.sessionId, IFNULL(d.average, 0) as average, "
				+ "concat(c.name, ', ', e.fieldName) as imageCategoryAndType "
				+ "from psych.imageResponse as a "
				+ "left join psych.image as b on a.imageId = b.id "
				+ "left join psych.imageCategory as c on c.id=b.categoryId "
				+ "left join psych.fieldLookup as e on e.id=b.imageType "
				+ "left join "
				+ "(select c.sessionId, c.participantId, AVG(c.timeTaken) as average, e.name, f.fieldName "
				+ "from psych.imageResponse as c "
				+ "inner join psych.image as b on c.imageId = b.id "
				+ "inner join psych.imageCategory as e on e.id=b.categoryId "
				+ "inner join psych.fieldLookup as f on f.id=b.imageType "
				+ "where c.participantId = ? and c.isAttempted = 1 and c.correctness = ? "
				+ "group by c.sessionId, f.fieldName, e.name) as d on d.sessionId = a.sessionId "
				+ "and d.participantId = a.participantId and c.name=d.name and e.fieldName=d.fieldName "
				+ "where a.participantId = ? group by a.sessionId, d.average, c.name, e.fieldName;";
		
		JSONObject returnJSON = new JSONObject();
		JSONArray series = new JSONArray();
		JSONArray data = new JSONArray();
		
		
		Connection connection = null;
			
		connection = DBSource.getConnectionPool().getConnection();
		
		PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
		preparedStatement.setLong(1, pId);
		preparedStatement.setInt(2, correctness);
		preparedStatement.setLong(3, pId);
		
		// execute select SQL statement
		ResultSet rows = preparedStatement.executeQuery();
		
		String prevSeries = "start";
		
		String prevX = "No Sesssions Recorded";
		
		
		JSONArray Y = new JSONArray();
		int count = 0;
		
		while (rows.next()){
			String currentX = rows.getString("sessionId");
			Double avg = rows.getDouble("average");
			String currentSeries = rows.getString("imageCategoryAndType");
			
			
			if(!currentX.equals(prevX) && !prevX.equals("No Sesssions Recorded")){
				count++;
				
				if(series.indexOf(prevSeries) == -1){
					series.add(prevSeries);
				}
				
				JSONObject object = new JSONObject();
				object.put("x", "Session: " + count);
				object.put("y", Y);
				data.add(object);
				
				Y = new JSONArray();
			}
			
			Y.add(avg);
			
			prevSeries = currentSeries;
			prevX = currentX;
		}
		
		if(count != 0){
			count++;
			JSONObject object = new JSONObject();
			object.put("x", "Session: " + count);
			object.put("y", Y);
			data.add(object);
		}
		
		
		JSONObject results = new JSONObject();
		
		results.put(Constant.SERIES, series);
		results.put(Constant.DATA, data);
		results.put(Constant.NO_OF_SESSIONS, count);
		
		returnJSON.put(Constant.RESULTS, results);
		connection.close();
		
		return returnJSON;
		
	}
	
	public static JSONObject getCorrectAndIncorrectCount(Long pId) throws SQLException{
		
		String selectQuery = "select sessionId, count(sessionId) as totalAttempted, "
				+ "sum(correctness) as correct, "
				+ "(count(sessionId) - sum(correctness)) as incorrect "
				+ "from psych.imageResponse "
				+ "where participantId = ? and isAttempted = 1 "
				+ "group by sessionId;";
		
		
		
		JSONObject returnJSON = new JSONObject();
		JSONArray series = new JSONArray();
		JSONArray data = new JSONArray();
		
		
		Connection connection = null;
			
		connection = DBSource.getConnectionPool().getConnection();
		
		PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
		preparedStatement.setLong(1, pId);
		
		// execute select SQL statement
		ResultSet rows = preparedStatement.executeQuery();
		int count = 1;
		
		while (rows.next()){
			String X = rows.getString("sessionId");
			Long totalAttempted = rows.getLong("totalAttempted");
			Long correct = rows.getLong("correct");
			Long incorrect = rows.getLong("incorrect");
			
			JSONObject object = new JSONObject();
			object.put("x", "Session: " + count);
			JSONArray Y = new JSONArray();
			Y.add(totalAttempted);
			Y.add(correct);
			Y.add(incorrect);
			object.put("y", Y);
			
			data.add(object);
			count++;
			
		}
		series.add(Constant.TOTAL_ATTEMPTED);
		series.add(Constant.CORRECT_RESPONSES);
		series.add(Constant.INCORRECT_RESPONSES);
		
		JSONObject results = new JSONObject();
		
		results.put(Constant.SERIES, series);
		results.put(Constant.DATA, data);
		
		returnJSON.put(Constant.RESULTS, results);
		connection.close();
		
		return returnJSON;
	}
	
	
	public static String generateImageReportFileForTargetGroup(Long tgId){
		StringBuilder returnString = new StringBuilder();
		
		returnString.append(Constant.REPORT_HEADER_IMAGES);
		
		String selectQuery = "select a.name as targetGroup, b.username as user, "
				+ "c.sessionId as sessionId, g.sessionDate as sessionDate, "
				+ "e.name as category, f.fieldName as type, d.name as image, "
				+ "c.correctness as imageResponse "
				+ "from psych.targetGroup as a "
				+ "inner join psych.participant as b on a.id = b.targetGroupId "
				+ "inner join psych.imageResponse as c on c.participantId = b.id "
				+ "inner join psych.image as d on d.id = c.imageId "
				+ "inner join psych.imageCategory as e on e.id = d.categoryId "
				+ "inner join psych.fieldLookup as f on f.id = d.imageType "
				+ "inner join psych.userSession as g on g.id = c.sessionId "
				+ "where a.id = ? "
				+ "order by c.id;";
		
		Connection connection = null;
		

		try {
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setLong(1, tgId);
			
			ResultSet rows = preparedStatement.executeQuery();
			
			while(rows.next()){
				
				String targetGroup = rows.getString("targetGroup");
				String user = rows.getString("user");
				String sessionId = rows.getString("sessionId");
				String sessionDate = rows.getString("sessionDate");
				String category = rows.getString("category");
				String type = rows.getString("type");
				String image = rows.getString("image");
				String imageResponse = rows.getString("imageResponse");
				
				returnString.append(targetGroup + ","
						+ user + ","
						+ sessionId + ","
						+ sessionDate + ","
						+ category + ","
						+ type + ","
						+ image + ","
						+ imageResponse + "\n");
			}
			
			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return returnString.toString();
	}
	
	
	public static String generateQuestionReportFileForTargetGroup(Long tgId){
		StringBuilder returnString = new StringBuilder();
		
		returnString.append(Constant.REPORT_HEADER_QUESTIONS);
		
		String selectQuery = "select a.name as targetGroup, b.username as user, c.sessionId as sessionId, "
				+ "g.sessionDate as sessionDate, e.name as category, f.fieldName as responseType, "
				+ "d.name as question, c.response as questionResponse "
				+ "from psych.targetGroup as a "
				+ "inner join psych.participant as b on a.id = b.targetGroupId "
				+ "inner join psych.questionResponse as c on c.participantId = b.id "
				+ "inner join psych.question as d on d.id = c.questionId "
				+ "inner join psych.questionCategory as e on e.id = d.categoryId "
				+ "inner join psych.fieldLookup as f on f.id = e.responseType "
				+ "inner join psych.userSession as g on g.id = c.sessionId "
				+ "where a.id = ? order by c.id;";
		
		Connection connection = null;
		

		try {
			
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setLong(1, tgId);
			
			ResultSet rows = preparedStatement.executeQuery();
			
			while(rows.next()){
				
				String targetGroup = rows.getString("targetGroup");
				String user = rows.getString("user");
				String sessionId = rows.getString("sessionId");
				String sessionDate = rows.getString("sessionDate");
				String category = rows.getString("category");
				String type = rows.getString("responseType");
				String image = rows.getString("question");
				String imageResponse = rows.getString("questionResponse");
				
				returnString.append(targetGroup + ","
						+ user + ","
						+ sessionId + ","
						+ sessionDate + ","
						+ category + ","
						+ type + ","
						+ image + ","
						+ imageResponse + "\n");
			}
			
			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return returnString.toString();
	}

}
