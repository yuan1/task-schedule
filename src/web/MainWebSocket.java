package web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entity.Computer;
import service.ComputerService;
import util.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

@ServerEndpoint("/endpoint")
public class MainWebSocket {
    private ComputerService computerService = new ComputerService();

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
    }
}