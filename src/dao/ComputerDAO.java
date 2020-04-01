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

    public boolean save(Computer computer) {
        connection = new DBHelper().getConn();
        String sql = "insert into computer(name,cpu,disk,memory,network,cpu_usage,disk_usage,memory_usage,network_usage) values(?,?,?,?,?,?,?,?,?) ";    //使用?做占位符
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, computer.getName());    //为第1个?号赋值
            statement.setString(2, computer.getCpu());
            statement.setString(3, computer.getDisk());
            statement.setString(4, computer.getMemory());
            statement.setString(5, computer.getNetwork());
            statement.setLong(6, computer.getCpuUsage());
            statement.setLong(7, computer.getDiskUsage());
            statement.setLong(8, computer.getMemoryUsage());
            statement.setLong(9, computer.getNetworkUsage());
            int rs = statement.executeUpdate();    //执行并返回影响条数
            if (rs > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Computer selectOne(int id) {
        Computer computer = null;
        connection = new DBHelper().getConn();
        String sql = "select * from computer where id=?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            set = statement.executeQuery();
            while (set.next()) {
                computer = new Computer();
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
        return computer;
    }

    public Boolean update(Computer computer) {
        connection = new DBHelper().getConn();
        String sql = "update computer set name=?,cpu=?,disk=?,memory=?,network=?,cpu_usage=?,disk_usage=?,memory_usage=?,network_usage=?  where id=?";    //使用?做占位符
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, computer.getName());    //为第1个?号赋值
            statement.setString(2, computer.getCpu());
            statement.setString(3, computer.getDisk());
            statement.setString(4, computer.getMemory());
            statement.setString(5, computer.getNetwork());
            statement.setLong(6, computer.getCpuUsage());
            statement.setLong(7, computer.getDiskUsage());
            statement.setLong(8, computer.getMemoryUsage());
            statement.setLong(9, computer.getNetworkUsage());
            statement.setLong(10, computer.getId());
            int rs = statement.executeUpdate();    //执行并返回影响条数
            if (rs > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
