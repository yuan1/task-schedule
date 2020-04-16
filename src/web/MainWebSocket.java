package web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.Computer;
import entity.Task;
import service.ComputerService;
import service.TaskService;
import util.TaskUtil;
import util.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ServerEndpoint("/endpoint")
public class MainWebSocket {
    private ComputerService computerService = new ComputerService();
    private TaskService taskService = new TaskService();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());
        WebSocketSession.save(session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" + session.getId());
        WebSocketSession.remove(session.getId());
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);

        JSONObject jsonObject = JSON.parseObject(message);
        String action = jsonObject.getString("action");
        if (action == null) {
            return;
        }
        if (action.equals("setTaskCanStart")) {
            Boolean can = jsonObject.getBoolean("data");
            String res = "";
            if (can && TaskUtil.canStart) {
                res = "任务执行已经开始";
            } else if (!can && !TaskUtil.canStart) {
                res = "任务执行已经停止";
            } else {
                TaskUtil.setCanStart(can);
                res = "任务执行" + (can ? "开始" : "停止") + "成功";
            }
            String result = WebSocketSession.buildResponse(action, true, res);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("loadComputer")) {
            String result = WebSocketSession.buildResponse(action, true, TaskUtil.getComputers());
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("loadTask")) {
            String result = WebSocketSession.buildResponse(action, true, TaskUtil.getTasks());
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("loadTaskCount")) {
            Map<String, Long> map = TaskUtil.countTask();
            JSONObject res = new JSONObject();
            res.put("x1", map.keySet());
            res.put("y1", map.values());
            Map<String, Long> map1 = TaskUtil.countTaskComplete();
            res.put("x2", map1.keySet());
            res.put("y2", map1.values());


            String result = WebSocketSession.buildResponse(action, true, res);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("selectComputerById")) {
            Integer id = jsonObject.getInteger("data");
            Computer computer = TaskUtil.computerMap.get(id);
            String result = WebSocketSession.buildResponse(action, true, computer);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("addComputer")) {
            JSONObject data = jsonObject.getJSONObject("data");
            Computer computer = new Computer();
            computer.setName(data.getString("name"));
            computer.setCpu(data.getString("cpu"));
            computer.setMemory(data.getString("memory"));
            computer.setNetwork(data.getString("network"));
            computer.setDisk(data.getString("disk"));
            computerService.save(computer);
            TaskUtil.saveComputer(computer);
            String result = WebSocketSession.buildResponse(action, true);
            WebSocketSession.sendMsgById(session.getId(), result);

            // 新分配计算机资源 检查等待的任务
            TaskUtil.checkWaitedTask();
        }
        if (action.equals("addTask")) {
            JSONObject data = jsonObject.getJSONObject("data");
            // 批量添加
            Long count = data.getLong("count");
            Long countInterval = data.getLong("countInterval");
            Long timeUsage = data.getLong("timeUsage");
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Task task = new Task();
                String name = data.getString("name");
                Long cpuUsage = data.getLong("cpuUsage");
                Long memoryUsage = data.getLong("memoryUsage");
                Long diskUsage = data.getLong("diskUsage");
                Long networkUsage = data.getLong("networkUsage");
                if (i > 0) {
                    name = name + "_" + i;
                    // 批量任务随机
                    cpuUsage = (long) (new Random().nextInt(100) + 1);
                    memoryUsage = (long) (new Random().nextInt(100) + 1);
                    diskUsage = (long) (new Random().nextInt(100) + 1);
                    networkUsage = (long) (new Random().nextInt(100) + 1);
                }
                task.setName(name);
                task.setCpuUsage(cpuUsage);
                task.setMemoryUsage(memoryUsage);
                task.setDiskUsage(diskUsage);
                task.setNetworkUsage(networkUsage);
                task.setTimeUsage(timeUsage);
                taskService.save(task);
                tasks.add(task);
                timeUsage += countInterval;
            }

            tasks.forEach(task -> {
                TaskUtil.saveTask(task);
                // 开始新任务
                TaskUtil.startTask(task);
            });

            String result = WebSocketSession.buildResponse(action, true);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("clearOverTask")) {
            // 删除任务相关
            TaskUtil.deleteTaskCompleted();
            taskService.deleteCompleted();
            String result = WebSocketSession.buildResponse(action, true);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("deleteTask")) {
            Integer id = jsonObject.getInteger("data");
            Task task = taskService.selectOne(id);
            if (task == null) {
                return;
            }
            // 删除任务相关
            TaskUtil.deleteTask(task);
            Boolean res = taskService.delete(id);
            String result = WebSocketSession.buildResponse(action, res);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("deleteComputer")) {
            Integer id = jsonObject.getInteger("data");
            Computer computer = computerService.selectOne(id);
            if (computer == null) {
                return;
            }
            // 删除任务相关
            TaskUtil.deleteComputer(computer);
            Boolean res = computerService.delete(id);
            String result = WebSocketSession.buildResponse(action, res);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
    }
}