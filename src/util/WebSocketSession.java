package util;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebSocketSession {
    public static Map<String, Session> sessions = new HashMap<>();

    public static void save(Session session) {
        sessions.put(session.getId(), session);
    }

    public static void remove(String id) {
        sessions.remove(id);
    }
    public static void sendMsgToAll(String data){
        sessions.values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
