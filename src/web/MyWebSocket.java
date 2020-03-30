package web;

import util.WebSocketSession;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class MyWebSocket {
    
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen::" + session.getId());
        WebSocketSession.save(session);
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
        WebSocketSession.remove(session.getId());
    }
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message);
    }
}