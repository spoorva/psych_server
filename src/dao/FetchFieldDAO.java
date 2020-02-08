package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.CommonFields;

public class FetchFieldDAO {
	
	private static Logger slf4jLogger = LoggerFactory.getLogger(FetchFieldDAO.class);
	
	public static List<CommonFields> extractFieldValues(String fieldName){
		
		String selectQuery = "SELECT ID, FIELDNAME FROM FIELDLOOKUP WHERE GROUPNAME = ?";
		
		List<CommonFields> fieldValueList = new ArrayList<CommonFields>();
		
		Connection connection = null;
		
		slf4jLogger.info("Entered into extractFieldValues");
		
		try{
			connection = DBSource.getConnectionPool().getConnection();
			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			
			preparedStatement.setString(1, fieldName);
			
			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()){
				CommonFields commonFields = new CommonFields();
				commonFields.setFieldId(rs.getLong("ID"));
				commonFields.setFieldValue(rs.getString("FIELDNAME"));
				fieldValueList.add(commonFields);
			}
			connection.close();
			
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
		return fieldValueList;
	}
	

}
