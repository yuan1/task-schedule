package service;

import dao.TaskDAO;
import entity.Task;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TaskService {
    private TaskDAO taskDAO = new TaskDAO();

    public List<Task> selectAll() {
        return taskDAO.selectAll();
    }

    public List<Task> selectAllNotCompleteByComputerId(int id) {
        return taskDAO.selectAllNotCompleteByComputerId(id);
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

    public Map<String, Long> countTaskComplete() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

        List<Task> tasks = this.selectAll();
        // 获取七天前
        Instant before7 = Instant.now().minus(7, ChronoUnit.DAYS);

        List<Task> completeTasks = tasks.stream().filter(task -> task.getComplete() == 1).filter(task -> {
            Instant taskTime = Instant.ofEpochSecond(task.getCompleteTime());
            return taskTime.isAfter(before7);
        }).collect(Collectors.toList());

        Map<String, Long> map = new TreeMap<>(Comparator.reverseOrder());
        for (int i = 0; i < 7; i++) {
            Instant before = Instant.now().minus(i, ChronoUnit.DAYS);
            String date = formatter.format(before);
            map.put(date, 0L);
        }
        completeTasks.forEach(task -> {
            String date = formatter.format(Instant.ofEpochSecond(task.getCompleteTime()));
            Long old = map.get(date);
            if (old == null) {
                old = 0L;
            }
            map.put(date, old + 1);
        });
        System.out.println("count task complete is " + map);
        return map;
    }

    public Map<String, Long> countTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

        List<Task> tasks = this.selectAll();
        // 获取三十天前
        Instant before30 = Instant.now().minus(30, ChronoUnit.DAYS);

        List<Task> createdTasks = tasks.stream().filter(task -> {
            Instant taskTime = Instant.ofEpochSecond(task.getCreateTime());
            return taskTime.isAfter(before30);
        }).collect(Collectors.toList());

        Map<String, Long> map = new TreeMap<>(Comparator.reverseOrder());
        for (int i = 0; i < 30; i++) {
            Instant before = Instant.now().minus(i, ChronoUnit.DAYS);
            String date = formatter.format(before);
            map.put(date, 0L);
        }
        createdTasks.forEach(task -> {
            String date = formatter.format(Instant.ofEpochSecond(task.getCreateTime()));
            Long old = map.get(date);
            if (old == null) {
                old = 0L;
            }
            map.put(date, old + 1);
        });
        System.out.println("count task is " + map);
        return map;
    }

}
