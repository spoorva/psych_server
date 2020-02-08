package dao;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebServlet("/DBSourceRegistrationListener")
public class DBSourceRegistrationListener implements ServletContextListener {

	private static Logger slf4jLogger = LoggerFactory.getLogger(DBSourceRegistrationListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		// 1. Go fetch the DataSource and close it.
        // 2. DeRegister Driver
		slf4jLogger.info("Called DB Source connection closer listener");
        try {
        	DBSource.closeConnectionPool();
            java.sql.Driver mySqlDriver = DriverManager.getDriver("jdbc:mysql://localhost:3306/");
            DriverManager.deregisterDriver(mySqlDriver);
        } catch (SQLException ex) {
        	slf4jLogger.info("Could not deregister driver:".concat(ex.getMessage()));
        } 

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}
	
}
