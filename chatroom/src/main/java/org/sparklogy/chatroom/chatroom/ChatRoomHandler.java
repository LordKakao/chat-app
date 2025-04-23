package org.sparklogy.chatroom.chatroom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparklogy.chatroom.weather.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatRoomHandler extends TextWebSocketHandler {

    private final WeatherService weatherService;
    private final ObjectMapper objectMapper;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> users = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        sendMessage(session, "Welcome to the chat room!");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            ChatMessage msg = objectMapper.readValue(message.getPayload(), ChatMessage.class);
            if(!users.containsKey(session.getId())) {
                users.put(session.getId(), msg.getUsername());
            }

            if (msg.getMessage().startsWith("@weather")) {
                String city = msg.getMessage().substring("@weather".length()).trim();
                String weatherInfo = weatherService.getWeather(city);
                sendMessageToAllConnections(weatherInfo);
            } else {
                // Broadcast the message to all users
                sendMessageToAllConnections(msg.getUsername() + ": " + msg.getMessage());
            }
        } catch (JsonProcessingException e) {
            log.error("Unable to read message ", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sendMessageToAllConnections(users.get(session.getId()) + " has left the chat.");
        sessions.remove(session.getId());
    }

    private void sendMessageToAllConnections(String message) {
        sessions.values().forEach(session -> sendMessage(session, message));
    }

    private void sendMessage(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("Unable to send message ", e);
        }
    }
}