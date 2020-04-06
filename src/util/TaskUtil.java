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

    // 等待中的任务（计算机资源未找到）
    private static List<Task> waitedTask = new ArrayList<>();


    /**
     * 开始一个任务
     *
     * @param task 任务信息
     */
    public static boolean startTask(Task task) {
        System.out.println("start task " + task.getName());
        ComputerService computerService = new ComputerService();
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
            return canUseCpu >= task.getCpuUsage() && canUseNetwork >= task.getNetworkUsage() && canUseMemory >= task.getMemoryUsage() && canUseDisk >= task.getDiskUsage();
        }).collect(Collectors.toList());

        TaskService taskService = new TaskService();
        if (canUseComputer.size() == 0) {
            System.out.println("not find can use computer");
            task.setStatus("等待中");
            task.setStated(0);
            task.setWaited(1);
            taskService.update(task);
            waitedTask.add(task);
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
        // 获取当前时间
        Instant now = Instant.now();
        // 加任务时长，获得结束时间
        Instant taskOver = now.plusSeconds(time);

        // 设置任务信息
        task.setStartedTime(now.getEpochSecond());
        task.setOverTime(taskOver.getEpochSecond());

        task.setStatus("开始执行");
        task.setStated(1);
        task.setComputerId(computer.getId());
        task.setComputerName(computer.getName());
        task.setWaited(0);
        taskService.update(task);

        System.out.println("task " + task.getName() + " is started in computer " + computer.getName());
        // 存储该任务
        startedTask.put(task.getId(), task);
        // 更新页面显示
        updateSession();
        return true;
    }

    private static void updateSession() {
        System.out.println("update session to page all");
        ComputerService computerService = new ComputerService();
        List<Computer> computers = computerService.selectAll();
        String result = WebSocketSession.buildResponse("loadComputer", true, computers);
        WebSocketSession.sendMsgToAll(result);

        TaskService taskService = new TaskService();
        List<Task> tasks = taskService.selectAll();
        String result1 = WebSocketSession.buildResponse("loadTask", true, tasks);
        WebSocketSession.sendMsgToAll(result1);
    }

    public static void initTimer() {
        Timer timer = new Timer();
        // 每秒钟 检查
        timer.schedule(new TaskTimer(), 0, 1 * 1000);
    }

    // 每分钟 检查 任务是否执行完毕
    public static void checkTaskIsOver() {
        System.out.println("check task is over");
        Instant now = Instant.now();
        List<Task> list = new ArrayList<>(startedTask.values());
        list.forEach(task -> {
            Instant taskOver = Instant.ofEpochSecond(task.getOverTime());
            if (taskOver.isBefore(now)) {
                setTaskOver(task, taskOver);
                startedTask.remove(task.getId());
            }
        });

    }

    public static void initStartedTask() {
        System.out.println("init started task by db");
        TaskService taskService = new TaskService();
        List<Task> tasks = taskService.selectAll();
        // 过滤数组 获取 开始的任务
        tasks.stream().filter(task -> (task.getStated() == 1 && task.getComplete() == 0)).forEach(task -> {
            // 获取任务应该结束的时间
            Instant taskOver = Instant.ofEpochSecond(task.getOverTime());
            // 结束时间在当前时间之前 表示该任务已经结束
            if (taskOver.isBefore(Instant.now())) {
                setTaskOver(task, taskOver);
            } else {
                startedTask.put(task.getId(), task);
            }
        });
        // 过滤数组 获取 等待中的任务
        // 开始等待的任务
        tasks.stream().filter(task -> (task.getWaited() == 1)).forEach(TaskUtil::startTask);
    }

    private static void setTaskOver(Task task, Instant overTime) {
        System.out.println("task " + task.getName() + " is to set over");
        TaskService taskService = new TaskService();
        task.setComplete(1);
        task.setStated(0);
        task.setWaited(0);
        task.setStatus("执行完毕");
        task.setCompleteTime(overTime.getEpochSecond());
        taskService.update(task);

        ComputerService computerService = new ComputerService();
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
        // 更新页面显示
        updateSession();

        // 检查等待中的任务 执行一个
        checkWaitedTask();
    }

    public static void checkWaitedTask() {
        System.out.println("check waited task");
        if (waitedTask.size() > 0) {
            Task task1 = waitedTask.get(0);
            boolean res = startTask(task1);
            if (res) {
                System.out.println("task " + task1.getName() + " is started now delete from [Started Failed Task]");
                waitedTask.remove(0);
            }
        }
    }

    public static void deleteTask(Task task) {
        if (task.getComplete() == 1) {
            return;
        }
        if (task.getWaited() == 1) {
            waitedTask.remove(task);
        }
        if (task.getStated() == 1) {
            startedTask.remove(task.getId());
        }
        setTaskOver(task, Instant.now());
    }

    public static void deleteComputer(Computer computer) {
        TaskService taskService = new TaskService();
        List<Task> taskList = taskService.selectAllNotCompleteByComputerId(computer.getId());
        // 结束任务
        taskList.forEach(TaskUtil::deleteTask);
    }
}


