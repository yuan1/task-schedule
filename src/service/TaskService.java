package service;

import dao.TaskDAO;
import entity.Task;

import java.util.Date;
import java.util.List;

public class TaskService {
    private TaskDAO taskDAO = new TaskDAO();

    public List<Task> selectAll() {
        return taskDAO.selectAll();
    }

    public Task selectOne(int id) {
        return taskDAO.selectOne(id);
    }

    public Boolean save(Task task) {
        task.setCreateTime(new Date().getTime());
        task.setComputerName("");
        task.setComplete(0);
        task.setComputerId(0);
        task.setCompleteTime(0L);
        task.setStatus("创建完成");
        return taskDAO.save(task);
    }

}
