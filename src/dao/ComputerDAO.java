package dao;

import entity.Computer;
import util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAO {
    private Connection connection = null;    //数据库链接
    private PreparedStatement statement = null;    //用于 实例包含已编译的 SQL 语句
    private ResultSet set = null;    //数据库查询结果集

    public List<Computer> selectAll() {
        List<Computer> computerList = new ArrayList<>();
        connection = new DBHelper().getConn();
        String sql = "select * from computer"; //定义SQL语句
        try {
            statement = connection.prepareStatement(sql);
            set = statement.executeQuery();    //执行SQL语句并取得结果集
            while (set.next()) {    //遍历结果集
                Computer computer = new Computer();
                computer.setId(set.getInt("id"));
                computer.setName(set.getString("name"));
                computer.setCpu(set.getString("cpu"));
                computer.setCpuUsage(set.getLong("cpu_usage"));
                computer.setMemory(set.getString("memory"));
                computer.setMemoryUsage(set.getLong("memory_usage"));
                computer.setDisk(set.getString("disk"));
                computer.setDiskUsage(set.getLong("disk_usage"));
                computer.setNetwork(set.getString("network"));
                computer.setNetworkUsage(set.getLong("network_usage"));
                computerList.add(computer);    //封装对象添加到List中
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                set.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return computerList;
    }
}
