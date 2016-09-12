package generator.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcUtils {

	public static Connection getConnect(){
		Connection connect=null;
		try {
			Class.forName(PropertiesUtils.getProperty("connection.driver_class"));
			connect  =  DriverManager.getConnection(PropertiesUtils.getProperty("connection.url"),PropertiesUtils.getProperty("connection.username") ,PropertiesUtils.getProperty("connection.password") );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connect;
	}
}
