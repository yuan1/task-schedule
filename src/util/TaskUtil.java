package util;

import entity.Computer;
import entity.Task;
import service.ComputerService;
import service.TaskService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TaskUtil {
    // 储存所有数据库加载任务
    public static Map<Integer, Task> taskMap = new HashMap<>();
    // 储存所有数据库加载计算机
    public static Map<Integer, Computer> computerMap = new HashMap<>();
    // 存储已经开始的任务
    private static Map<Integer, Task> startedTask = new HashMap<>();
    // 等待中的任务（计算机资源未找到）
    private static Map<Integer, Task> waitedTask = new HashMap<>();

    public static Boolean canStart = false;

    /**
     * 开始一个任务
     *
     * @param task 任务信息
     */
    public static boolean startTask(Task task) {
        if (!canStart) {
            System.out.println("task start to await " + task.getName());
            return true;
        }
        System.out.println("start task " + task.getName());
        // 根据任务 获取 对应可用的计算机资源
        List<Computer> canUseComputer = getComputers().stream().filter(computer -> {
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

        if (canUseComputer.size() == 0) {
            System.out.println("not find can use computer");
            task.setStatus("等待中");
            task.setStated(0);
            task.setWaited(1);
            // 先删除 再添加 执行更新操作
            removeTaskFromMap(task.getId());
            taskMap.put(task.getId(), task);

            waitedTask.put(task.getId(), task);
            return false;
        }

        Computer computer = canUseComputer.get(0);
        System.out.println("find can use computer" + computer.getId());
        // 设置计算机 占用
        computer.setCpuUsage(computer.getCpuUsage() + task.getCpuUsage());
        computer.setNetworkUsage(computer.getNetworkUsage() + task.getNetworkUsage());
        computer.setDiskUsage(computer.getDiskUsage() + task.getDiskUsage());
        computer.setMemoryUsage(computer.getMemoryUsage() + task.getMemoryUsage());
        // 先删除 再添加 执行更新操作
        removeComputerFromMap(computer.getId());
        computerMap.put(computer.getId(), computer);

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
        // 先删除 再添加 执行更新操作
        removeTaskFromMap(task.getId());
        taskMap.put(task.getId(), task);

        System.out.println("task " + task.getName() + " is started in computer " + computer.getName());
        // 存储该任务
        startedTask.put(task.getId(), task);
        // 更新页面显示
        updateSession();
        return true;
    }

    /**
     * 设置任务可以开始
     *
     * @param val 是否可以
     */
    public static void setCanStart(Boolean val) {
        canStart = val;
        if (canStart) {
            // 过滤数组 获取 没开始执行的任务
            getTasks().stream().filter(task -> task.getStated() == 0 && task.getComplete() == 0).forEach(TaskUtil::startTask);
        }
    }

    private static void updateSession() {
        System.out.println("update session to page all");
        String result = WebSocketSession.buildResponse("loadComputer", true, getComputers());
        WebSocketSession.sendMsgToAll(result);
        String result1 = WebSocketSession.buildResponse("loadTask", true,getTasks());
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
        List<Task> tasks = new ArrayList<>(startedTask.values());
        tasks.forEach(task -> {
            Instant taskOver = Instant.ofEpochSecond(task.getOverTime());
            if (taskOver.isBefore(now)) {
                setTaskOver(task, taskOver);
                //startedTask.remove(ele);    //出错 修改了映射结构 影响了迭代器遍历
                //用迭代器删除 则不会出错
                System.out.println("task is over now remove task on [startedTask]");
                startedTask.keySet().removeIf(ele -> ele == task.getId());
            }
        });
    }

    public static void initTask() {
        System.out.println("init task by db");
        TaskService taskService = new TaskService();
        List<Task> tasks = taskService.selectAll();
        tasks.forEach(task -> {
            // 储存到内存
            taskMap.put(task.getId(), task);
            //获取 开始的任务
            if (task.getStated() == 1 && task.getComplete() == 0) {
                // 获取任务应该结束的时间
                Instant taskOver = Instant.ofEpochSecond(task.getOverTime());
                // 结束时间在当前时间之前 表示该任务已经结束
                if (taskOver.isBefore(Instant.now())) {
                    setTaskOver(task, taskOver);
                } else {
                    startedTask.put(task.getId(), task);
                }
            }
            //获取 等待中的任务
            if (task.getWaited() == 1) {
                // 开始等待的任务
                startTask(task);
            }
        });
    }

    public static void initComputer() {
        System.out.println("init computer");
        ComputerService computerService = new ComputerService();
        List<Computer> computers = computerService.selectAll();
        computers.forEach(computer -> {
            // 重置不是0 的计算机
            computer.setCpuUsage(0L);
            computer.setNetworkUsage(0L);
            computer.setMemoryUsage(0L);
            computer.setDiskUsage(0L);
            // 储存到内存
            computerMap.put(computer.getId(), computer);
        });
    }

    private static void setTaskOver(Task task, Instant overTime) {
        System.out.println("task " + task.getName() + " is to set over");
        task.setComplete(1);
        task.setStated(0);
        task.setWaited(0);
        task.setStatus("执行完毕");
        task.setCompleteTime(overTime.getEpochSecond());
        // 先删除 再添加 执行更新操作
        removeTaskFromMap(task.getId());
        taskMap.put(task.getId(), task);

        Computer computer = computerMap.get(task.getComputerId());
        if (computer == null) {
            System.out.println("not found computer on [setTaskOver] in "+task.getName());
            return;
        }
        // 解除该任务的计算机 占用
        computer.setCpuUsage(computer.getCpuUsage() - task.getCpuUsage());
        computer.setNetworkUsage(computer.getNetworkUsage() - task.getNetworkUsage());
        computer.setDiskUsage(computer.getDiskUsage() - task.getDiskUsage());
        computer.setMemoryUsage(computer.getMemoryUsage() - task.getMemoryUsage());
        if(computer.getCpuUsage()<0){
            computer.setCpuUsage(0L);
        }
        if(computer.getNetworkUsage()<0){
            computer.setNetworkUsage(0L);
        }
        if(computer.getDiskUsage()<0){
            computer.setDiskUsage(0L);
        }
        if(computer.getMemoryUsage()<0){
            computer.setMemoryUsage(0L);
        }
        // 先删除 再添加 执行更新操作
        removeComputerFromMap(computer.getId());
        computerMap.put(computer.getId(), computer);
        // 更新页面显示
        updateSession();

        // 检查等待中的任务 执行一个
        checkWaitedTask();
    }

    public static void checkWaitedTask() {
        System.out.println("check waited task");
        System.out.println(waitedTask);
        List<Task> tasks = new ArrayList<>(waitedTask.values());
        if (tasks.size() > 0) {
            Task task1 = tasks.get(0);
            boolean res = startTask(task1);
            if (res) {
                System.out.println("task " + task1.getName() + " is started now delete from [Started Failed Task]");
//                waitedTask.remove(0);
                waitedTask.keySet().removeIf(ele -> ele == task1.getId());
            }
        }
    }

    public static void deleteTask(Task task) {
        if (task.getWaited() == 1) {
//            waitedTask.remove(task);
            waitedTask.keySet().removeIf(ele -> ele == task.getId());
        }
        if (task.getStated() == 1) {
//            startedTask.remove(task.getId());
            startedTask.keySet().removeIf(ele -> ele == task.getId());
        }
        if (task.getComplete() == 0) {
            setTaskOver(task, Instant.now());
        }
        // 删除内存中存储的
        removeTaskFromMap(task.getId());
    }

    public static void deleteComputer(Computer computer) {
        // 结束任务
        getTasks().stream().filter(task -> task.getComplete() == 0 && task.getComputerId() == computer.getId()).forEach(TaskUtil::deleteTask);
        // 删除内存中存储的
        removeComputerFromMap(computer.getId());
    }

    public static Map<String, Long> countTaskComplete() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

        // 获取七天前
        Instant before7 = Instant.now().minus(7, ChronoUnit.DAYS);

        List<Task> completeTasks = getTasks().stream().filter(task -> task.getComplete() == 1).filter(task -> {
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

    public static Map<String, Long> countTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

        // 获取三十天前
        Instant before30 = Instant.now().minus(30, ChronoUnit.DAYS);

        List<Task> createdTasks = getTasks().stream().filter(task -> {
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

    public static void saveTask(Task task) {
        taskMap.put(task.getId(), task);
    }

    public static void saveComputer(Computer computer) {
        computerMap.put(computer.getId(), computer);
    }

    public static void updateAll() {

        TaskService taskService = new TaskService();
        // 异步更新所有
        getTasks().forEach(taskService::update);

        ComputerService computerService = new ComputerService();
        // 异步更新所有
        getComputers().forEach(computerService::update);
    }
    public static List<Task> getTasks(){
        System.out.println("get all tasks map is "+taskMap);
        return new ArrayList<>(taskMap.values());
    }
    public static List<Computer> getComputers(){
        System.out.println("get all computer map is "+computerMap);
        return new ArrayList<>(computerMap.values());
    }

    private static void removeTaskFromMap(int id){
        taskMap.keySet().removeIf(ele -> ele == id);
    }
    private static void removeComputerFromMap(int id){
        computerMap.keySet().removeIf(ele -> ele == id);
    }

    public static void deleteTaskCompleted() {
        getTasks().forEach(task -> {
            if(task.getComplete()==1){
                removeTaskFromMap(task.getId());
            }
        });
    }
}


