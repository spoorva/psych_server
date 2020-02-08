package authentication;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BuildStaticParameters {
	static final String configXML = "config.xml";
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String DB_URL;
	static String USER;
	static String PASS;
	public static Connection conn = null;
	public static Statement stmt =null;

	public static void buildConnectionWithSQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt=BuildStaticParameters.conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getDBConnection() {

		Connection dbConnection = null;
		File file = new File(configXML);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		        .newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			USER = document.getElementsByTagName("user").item(0).getTextContent();
			PASS = document.getElementsByTagName("password").item(0).getTextContent();
			DB_URL = document.getElementsByTagName("db-url").item(0).getTextContent();

		} catch (ParserConfigurationException | SAXException | IOException e1) {
			e1.printStackTrace();
			return dbConnection;
		}

		try {
			Class.forName(JDBC_DRIVER).newInstance();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			dbConnection = DriverManager.getConnection(DB_URL, USER,PASS);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return dbConnection;

	}
}
