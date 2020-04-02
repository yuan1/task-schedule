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
import java.util.List;
import java.util.Map;

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
        if (action.equals("loadComputer")) {
            List<Computer> computers = computerService.selectAll();
            String result = WebSocketSession.buildResponse(action, true, computers);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("loadTask")) {
            List<Task> tasks = taskService.selectAll();
            String result = WebSocketSession.buildResponse(action, true, tasks);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("loadTaskCount")) {
            Map<String, Long> map = taskService.countTask();
            JSONObject res = new JSONObject();
            res.put("x1", map.keySet());
            res.put("y1", map.values());
            Map<String, Long> map1 = taskService.countTaskComplete();
            res.put("x2", map1.keySet());
            res.put("y2", map1.values());


            String result = WebSocketSession.buildResponse(action, true, res);
            WebSocketSession.sendMsgById(session.getId(), result);
        }
        if (action.equals("selectComputerById")) {
            Integer id = jsonObject.getInteger("data");
            Computer computer = computerService.selectOne(id);
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
            Boolean res = computerService.save(computer);
            String result = WebSocketSession.buildResponse(action, res);
            WebSocketSession.sendMsgById(session.getId(), result);

            // 新分配计算机资源 检查等待的任务
            TaskUtil.checkWaitedTask();
        }
        if (action.equals("addTask")) {
            JSONObject data = jsonObject.getJSONObject("data");
            Task task = new Task();
            task.setName(data.getString("name"));
            task.setCpuUsage(data.getLong("cpuUsage"));
            task.setMemoryUsage(data.getLong("memoryUsage"));
            task.setDiskUsage(data.getLong("diskUsage"));
            task.setNetworkUsage(data.getLong("networkUsage"));
            task.setTimeUsage(data.getLong("timeUsage"));
            Boolean res = taskService.save(task);
            String result = WebSocketSession.buildResponse(action, res);
            WebSocketSession.sendMsgById(session.getId(), result);

            // 开始新任务
            TaskUtil.startTask(task);
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