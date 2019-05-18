package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

	private ConnectionFactory() {}

	public static Connection getInstance() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/unibiodb?useUnicode=true&amp;characterEncoding=UTF-8&amp;characterSetResults=UTF-8",
					"unibio", "root");
					//user	  //pwd
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println("erro no conector");
		}
		return conn;
	}

}
