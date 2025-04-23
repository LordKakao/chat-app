package org.sparklogy.chatroom.chatroom;

import lombok.RequiredArgsConstructor;
import org.sparklogy.chatroom.weather.WeatherService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public String sendMessage(@Payload ChatMessage chatMessage) {
        return chatRoomService.processMessage(chatMessage);
    }
}
