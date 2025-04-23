package org.sparklogy.chatroom.chat;

import lombok.RequiredArgsConstructor;
import org.sparklogy.chatroom.weather.WeatherService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final WeatherService weatherService;

    public String handleMessage(ChatMessage message) {
        if (message.getMessage().startsWith("@weather")) {
            String city = message.getMessage().substring("@weather".length()).trim();
            return weatherService.getWeather(city);
        }
        return message.getUsername() + ": " + message.getMessage();
    }
}

