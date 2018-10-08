package net.asher.book.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import net.asher.book.util.SessionUtil;

@Component
public class AsherWebSocketHandler extends TextWebSocketHandler {

	private List<WebSocketSession> connectedUsers;
	
	@Autowired
	public AsherWebSocketHandler() {
		connectedUsers = new ArrayList<WebSocketSession>();
	}
	/**
	 * 2가지 이벤트 처리
	 * 1. Send : 클라이언트가 서버에게 메시지 보냄
	 * 2. Emit : 서버에 연결되어 있는 클라이언트들에게 메시지 보냄
	 * @param WebSocketSession 메시지를 보낸 클라이언트
	 * @param TextMessage 메시지의 내용
	 **/
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TODO Auto-generated method stub
		for(WebSocketSession webSocketSession :  connectedUsers) {
			webSocketSession.sendMessage(new TextMessage(message.getPayload()));
		}
	}

	/**
	 * 접속 관련 Event Method
	 * @param WebSocketSession 접속한 사용자 
	 **/
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(session.getId() + " connected....");
		//session.sendMessage(new TextMessage(session.getId()));
		connectedUsers.add(session);
	}


	/**
	 * 클라이언트가 서버와 연결 종료
	 * @param WebSocketSession 연결을 끊은 클라이언트
	 * @param CloseStatus 연결 상태(확인 필요) 
	 **/
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println(session.getId() + " closed....");
		connectedUsers.remove(session);
	}
	
	public void sendDatabaseMsg(String msg) throws IOException {
		if(connectedUsers.size() > 0) {
			for(WebSocketSession webSocketSession :  connectedUsers) {
				webSocketSession.sendMessage(new TextMessage(msg));
			}
		}
	}

}
