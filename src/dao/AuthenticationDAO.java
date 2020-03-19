package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.AdminDetails;


public class AuthenticationDAO{
	
	private static Logger slf4jLogger = LoggerFactory.getLogger(AuthenticationDAO.class);
	
	public static AdminDetails validateUser(String email, String password){
		
		slf4jLogger.info("Entered into validateUser");
		
		String selectQuery = "SELECT ID, FIRSTNAME, LASTNAME, EMAIL, ROLE FROM ADMIN WHERE EMAIL = ? AND PASSWORD = ?";
		
		Connection connection = null;
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.first()) {
				AdminDetails adminDetails = new AdminDetails();
				adminDetails.setFirstName(rs.getString("FIRSTNAME"));
				adminDetails.setLastName(rs.getString("LASTNAME"));
				adminDetails.setUserId(rs.getLong("ID"));
				
				int roleFieldID = rs.getInt("ROLE");
				
				String roleValue = AuthenticationDAO.getFieldNameByFieldId(roleFieldID);
				adminDetails.setRole(roleValue);
				slf4jLogger.info(roleValue);
				connection.close();
				return adminDetails;
			}
			
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while finding user information");
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
	
		return null;
	}
	
	public static String getFieldNameByFieldId(int fieldId) throws SQLException{
		
		Connection connection = null;
		
		slf4jLogger.info("Entered into getFieldNameByFieldId");
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
		
			PreparedStatement preparedStatement = null;
			
			String selectQuery = "SELECT FIELDNAME FROM FIELDLOOKUP WHERE ID = ?";
	
			preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, fieldId);
			
			// execute select SQL statement
			ResultSet rs = preparedStatement.executeQuery();
			//System.out.println(rs.getFetchSize());
			String fieldName = null;
			while (rs.next()) {
				fieldName = rs.getString("FIELDNAME");
			}
			connection.close();
			return fieldName;
			
		}catch(SQLException e){
			slf4jLogger.info("SQL Exception while fetching field information");
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
		return "";
	}

}