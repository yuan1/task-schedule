package util;

import com.alibaba.fastjson.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * websocket session 工具
 */
public class WebSocketSession {
    public static Map<String, Session> sessions = new HashMap<>();

    public static void save(Session session) {
        sessions.put(session.getId(), session);
    }

    public static void remove(String id) {
        sessions.remove(id);
    }

    public static void sendMsgToAll(String data) {
        sessions.values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void sendMsgById(String id, String data) {
        Session session = sessions.get(id);
        if (session == null) {
            System.out.println("not found session " + id);
            return;
        }
        try {
            session.getBasicRemote().sendText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String buildResponse(String action ,Boolean isSuccess, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("success", isSuccess);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject.toJSONString();
    }

    public static String buildResponse(String action ,Boolean isSuccess, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("success", isSuccess);
        jsonObject.put("msg", msg);
        jsonObject.put("data", null);
        return jsonObject.toJSONString();
    }

    public static String buildResponse(String action ,Boolean isSuccess, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("success", isSuccess);
        jsonObject.put("msg", "");
        jsonObject.put("data", data);
        return jsonObject.toJSONString();
    }

    public static String buildResponse(String action ,Boolean isSuccess) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", action);
        jsonObject.put("success", isSuccess);
        jsonObject.put("msg", null);
        jsonObject.put("data", null);
        return jsonObject.toJSONString();
    }
}
