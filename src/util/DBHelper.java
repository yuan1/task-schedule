package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库工具
 *
 */
public class DBHelper {
	private String dbUrl = "jdbc:mysql://123.206.18.117:3309/task-schedule?useUnicode=true&characterEncoding=utf8";
	private String dbUser = "root";
	private String dbPassword = "1371271347";
	private String jdbcName = "com.mysql.jdbc.Driver";

	public Connection getConn() {
		Connection conn = null;
		try {
			Class.forName(jdbcName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return conn;
	}
}
