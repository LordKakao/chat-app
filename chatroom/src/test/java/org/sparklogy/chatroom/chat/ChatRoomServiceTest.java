package org.sparklogy.chatroom.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sparklogy.chatroom.weather.WeatherService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {
    private ChatRoomService chatRoomService;

    @Mock
    WeatherService weatherService;

    @BeforeEach
    void beforeEach() {
        chatRoomService = new ChatRoomService(weatherService);
    }

    @Test
    public void shouldReturnChatMessage() {
        ChatMessage message = new ChatMessage("user", "Hello!");
        String result = chatRoomService.handleMessage(message);

        verify(weatherService, times(0)).getWeather(any());
        assertEquals("user: Hello!", result);
    }

    @Test
    public void shouldReturnWeatherInfoMessage() {
        when(weatherService.getWeather("Prague"))
                .thenReturn("The current temperature in Prague is 20°C. Weather description: Windy.");
        ChatMessage message = new ChatMessage("user", "@weather Prague");
        String result = chatRoomService.handleMessage(message);

        verify(weatherService, times(1)).getWeather("Prague");
        assertEquals("The current temperature in Prague is 20°C. Weather description: Windy.", result);
    }
}
