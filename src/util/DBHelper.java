package util;


import com.alibaba.druid.pool.DruidDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库工具
 */
public class DBHelper {
    // 静态数据源变量，供全局操作且用于静态代码块加载资源。
    private static  DruidDataSource dataSource = new DruidDataSource();
    static {
        //2,为数据库添加配置文件
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://123.206.18.117:3309/task-schedule?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("1371271347");
        dataSource.setMaxActive(50);
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
}
