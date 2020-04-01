package util;

import entity.Computer;
import entity.Task;
import service.ComputerService;
import service.TaskService;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TaskUtil {
    // 存储已经开始的任务
    private static Map<Integer, Task> startedTask = new HashMap<>();

    // 执行失败的任务（计算机资源未找到）
    private static List<Task> startedFailedTask = new ArrayList<>();

    private TaskService taskService = new TaskService();
    private ComputerService computerService = new ComputerService();

    /**
     * 开始一个任务
     *
     * @param task 任务信息
     */
    public boolean startTask(Task task) {

        // 根据任务 获取 对应可用的计算机资源
        List<Computer> allComputer = computerService.selectAll();
        List<Computer> canUseComputer = allComputer.stream().filter(computer -> {
            // 可用 CPU资源
            long canUseCpu = 100 - computer.getCpuUsage();
            // 可用 内存资源
            long canUseMemory = 100 - computer.getMemoryUsage();
            // 可用 硬盘资源
            long canUseDisk = 100 - computer.getDiskUsage();
            // 可用 网络资源
            long canUseNetwork = 100 - computer.getNetworkUsage();
            System.out.println("now computer is " + computer.getName() + " canUseCpu is " + canUseCpu + " canUseMemory is " + canUseMemory + " canUseDisk is " + canUseDisk + " canUseNetwork is " + canUseNetwork);
            return canUseCpu < task.getCpuUsage() || canUseNetwork < task.getNetworkUsage() || canUseMemory < task.getMemoryUsage() || canUseDisk < task.getDiskUsage();
        }).collect(Collectors.toList());
        if (canUseComputer.size() == 0) {
            System.out.println("not find can use computer");
            task.setStatus("计算机资源满，等待中");
            task.setStated(0);
            taskService.update(task);
            startedFailedTask.add(task);
            return false;
        }

        Computer computer = canUseComputer.get(0);

        // 设置计算机 占用
        computer.setCpuUsage(computer.getCpuUsage() + task.getCpuUsage());
        computer.setNetworkUsage(computer.getNetworkUsage() + task.getNetworkUsage());
        computer.setDiskUsage(computer.getDiskUsage() + task.getDiskUsage());
        computer.setMemoryUsage(computer.getMemoryUsage() + task.getMemoryUsage());

        computerService.update(computer);

        // 获取任务时长 (分钟)
        Long time = task.getTimeUsage();
        // 获取创建时间
        Instant instant = Instant.ofEpochSecond(task.getCreateTime());
        // 加任务时长，获得结束时间
        Instant taskOver = instant.plusSeconds(time * 60);

        // 设置任务信息
        task.setOverTime(taskOver.getEpochSecond());
        task.setStatus("开始执行");
        task.setStated(1);
        task.setComputerId(computer.getId());
        task.setComputerName(computer.getName());
        taskService.update(task);

        System.out.println("task " + task.getName() + "is started in computer " + computer.getName());
        // 存储该任务
        startedTask.put(task.getId(), task);
        return true;
    }

    // 每分钟 检查 任务是否执行完毕
    private void checkTaskIsOver() {
        Instant now = Instant.now();
        startedTask.values().forEach(task -> {
            Instant taskOver = Instant.ofEpochSecond(task.getOverTime());
            if (taskOver.isBefore(now)) {
                this.setTaskOver(task, taskOver);
                startedTask.remove(task.getId());
            }
        });
    }

    private void initStartedTask() {
        List<Task> tasks = taskService.selectAll();
        // 过滤数组 获取 开始的任务
        tasks.stream().filter(task -> task.getStated() == 1 && task.getComplete() == 0).forEach(task -> {
            // 获取任务时长 (分钟)
            Long time = task.getTimeUsage();
            // 获取创建时间
            Instant instant = Instant.ofEpochSecond(task.getCreateTime());
            // 加任务时长，获得结束时间
            Instant taskOver = instant.plusSeconds(time * 60);
            // 结束时间在当前时间之前 表示该任务已经结束
            if (taskOver.isBefore(Instant.now())) {
                this.setTaskOver(task, taskOver);
            } else {
                startedTask.put(task.getId(), task);
            }
        });
    }

    private void setTaskOver(Task task, Instant overTime) {
        System.out.println("task " + task.getName() + "is over");
        task.setComplete(1);
        task.setStated(0);
        task.setStatus("任务执行完毕");
        task.setCompleteTime(overTime.getEpochSecond());
        taskService.update(task);
        Computer computer = computerService.selectOne(task.getComputerId());
        if (computer == null) {
            return;
        }
        // 解除该任务的计算机 占用
        computer.setCpuUsage(computer.getCpuUsage() - task.getCpuUsage());
        computer.setNetworkUsage(computer.getNetworkUsage() - task.getNetworkUsage());
        computer.setDiskUsage(computer.getDiskUsage() - task.getDiskUsage());
        computer.setMemoryUsage(computer.getMemoryUsage() - task.getMemoryUsage());
        computerService.update(computer);

        // 检查任务执行失败的任务 执行一个
        if (startedFailedTask.size() > 0) {
            Task task1 = startedFailedTask.get(0);
            boolean res = startTask(task1);
            if (res) {
                System.out.println("task "+ task.getName() +"is started now delete from [Started Failed Task]");
                startedFailedTask.remove(0);
            }
        }
    }
}


