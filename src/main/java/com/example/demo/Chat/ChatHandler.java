package com.example.demo.Chat;

import com.example.demo.Chat.ChatLog;
import com.example.demo.Chat.ChatLogRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final ChatLogRepository chatLogRepository;

    public ChatHandler(ChatLogRepository chatLogRepository) {
        this.chatLogRepository = chatLogRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String[] parts = message.getPayload().split(":");
        if (parts.length == 2) {
            String nickname = parts[0];
            String chatMessage = parts[1];

            // Create ChatLog entity
            ChatLog chatLog = new ChatLog();
            chatLog.setNickname(nickname);
            chatLog.setMessage(chatMessage);
            chatLog.setTimestamp(LocalDateTime.now());

            // Save ChatLog to MySQL
            chatLogRepository.save(chatLog);

            String processedMessage = nickname + ": " + chatMessage;

            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(new TextMessage(processedMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
