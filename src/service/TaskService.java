package service;

import dao.TaskDAO;
import entity.Task;

import java.time.Instant;
import java.util.*;

public class TaskService {
    private TaskDAO taskDAO = new TaskDAO();

    public List<Task> selectAll() {
        return taskDAO.selectAll();
    }

    public Task selectOne(int id) {
        return taskDAO.selectOne(id);
    }

    public Boolean save(Task task) {
        task.setCreateTime(Instant.now().getEpochSecond());
        task.setComputerName("");
        task.setStated(0);
        task.setWaited(0);
        task.setComplete(0);
        task.setComputerId(0);
        task.setCompleteTime(0L);
        task.setOverTime(0L);
        task.setStartedTime(0L);
        task.setStatus("创建完成");
        return taskDAO.save(task);
    }

    public Boolean update(Task task) {
        return taskDAO.update(task);
    }

    public Boolean delete(int id) {
        return taskDAO.delete(id);
    }


    public void deleteCompleted() {
        taskDAO.deleteCompleted();

    }
}
