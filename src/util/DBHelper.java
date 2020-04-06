package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库工具
 */
public class DBHelper {
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    /**
     * 获得数据源（连接池）
     *
     * @return
     */
    public static ComboPooledDataSource getDataSource() {
        return dataSource;
    }

    /**
     * 获得连接
     *
     * @return
     */
    public static Connection getConn() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 归还连接
     *
     * @param conn
     */
    public static void closeConn(Connection conn) {
        try {
            if (conn != null && conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
