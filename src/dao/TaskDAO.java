package dao;

import entity.Task;
import util.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    private Connection connection = null;    //数据库链接
    private PreparedStatement statement = null;    //用于 实例包含已编译的 SQL 语句
    private ResultSet set = null;    //数据库查询结果集

    public List<Task> selectAll() {
        List<Task> taskList = new ArrayList<>();
        connection = new DBHelper().getConn();
        String sql = "select * from task"; //定义SQL语句
        try {
            statement = connection.prepareStatement(sql);
            set = statement.executeQuery();    //执行SQL语句并取得结果集
            while (set.next()) {    //遍历结果集
                Task task = new Task();
                task.setId(set.getInt("id"));
                task.setName(set.getString("name"));
                task.setCpuUsage(set.getLong("cpu_usage"));
                task.setMemoryUsage(set.getLong("memory_usage"));
                task.setDiskUsage(set.getLong("disk_usage"));
                task.setNetworkUsage(set.getLong("network_usage"));
                task.setTimeUsage(set.getLong("time_usage"));
                task.setCreateTime(set.getLong("create_time"));
                task.setCompleteTime(set.getLong("complete_time"));
                task.setComplete(set.getInt("complete"));
                task.setComputerId(set.getInt("computer_id"));
                task.setComputerName(set.getString("computer_name"));
                task.setStatus(set.getString("status"));
                taskList.add(task);    //封装对象添加到List中
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
        return taskList;
    }

    public boolean save(Task task) {
        connection = new DBHelper().getConn();
        String sql = "insert into task(name,cpu_usage,disk_usage,memory_usage,network_usage,time_usage,create_time,complete_time,complete,computer_id,computer_name,status) values(?,?,?,?,?,?,?,?,?,?,?,?) ";    //使用?做占位符
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, task.getName());    //为第1个?号赋值
            statement.setLong(2, task.getCpuUsage());
            statement.setLong(3, task.getDiskUsage());
            statement.setLong(4, task.getMemoryUsage());
            statement.setLong(5, task.getNetworkUsage());
            statement.setLong(6, task.getTimeUsage());
            statement.setLong(7, task.getCreateTime());
            statement.setLong(8, task.getCompleteTime());
            statement.setInt(9, task.getComplete());
            statement.setInt(10, task.getComputerId());
            statement.setString(11, task.getComputerName());
            statement.setString(12, task.getStatus());
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

    public Task selectOne(int id) {
        Task task = null;
        connection = new DBHelper().getConn();
        String sql = "select * from task where id=?";
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            set = statement.executeQuery();
            while (set.next()) {
                task = new Task();
                task.setId(set.getInt("id"));
                task.setName(set.getString("name"));
                task.setCpuUsage(set.getLong("cpu_usage"));
                task.setMemoryUsage(set.getLong("memory_usage"));
                task.setDiskUsage(set.getLong("disk_usage"));
                task.setNetworkUsage(set.getLong("network_usage"));
                task.setTimeUsage(set.getLong("time_usage"));
                task.setCreateTime(set.getLong("create_time"));
                task.setCompleteTime(set.getLong("complete_time"));
                task.setComplete(set.getInt("complete"));
                task.setComputerId(set.getInt("computer_id"));
                task.setComputerName(set.getString("computer_name"));
                task.setStatus(set.getString("status"));
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
        return task;
    }
}
