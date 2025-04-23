package org.sparklogy.chatroom.chatroom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.sparklogy.chatroom.weather.WeatherService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component // Add this annotation to make it a Spring-managed bean
public class ChatRoomHandler extends TextWebSocketHandler {

    private final WeatherService weatherService;
    private final ObjectMapper objectMapper;

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> users = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Generate a default username (you can change this to accept from client)
        sessions.put(session.getId(), session);
        sendMessageToUser(session, "Welcome to the chat room!");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        ChatMessage msg = null;
        try {
            msg = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        } catch (JsonProcessingException ignored) {

        }

        // Handle @weather requests
        if (msg.getMessage().startsWith("@weather")) {
            String city = msg.getMessage().substring("@weather".length()).trim();
            String weatherInfo = weatherService.getWeather(city);
            sendMessageToAllUsers(weatherInfo);
        } else {
            // Broadcast the message to all users
            sendMessageToAllUsers(msg.getUsername() + ": " + msg.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sendMessageToAllUsers(session.getId() + " has left the chat.");
        sessions.remove(session.getId()); // Remove the session
    }

    // Helper method to send a message to all connected users
    private void sendMessageToAllUsers(String message) {
        sessions.values().forEach(session -> {
            try {
                session.sendMessage(new org.springframework.web.socket.TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Helper method to send a message to a specific user
    private void sendMessageToUser(WebSocketSession session, String message) {
        try {
            session.sendMessage(new org.springframework.web.socket.TextMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}