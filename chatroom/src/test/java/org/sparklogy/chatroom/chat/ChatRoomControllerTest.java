package org.sparklogy.chatroom.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatRoomControllerTest {

    @Mock
    private ChatRoomService chatRoomService;

    private ChatRoomController chatRoomController;

    @BeforeEach
    void setUp() {
        chatRoomController = new ChatRoomController(chatRoomService);
    }

    @Test
    void shouldReturnProcessedMessageFromService() {
        ChatMessage inputMessage = new ChatMessage("user", "Hello!");
        String expectedResponse = "user: Hello!";

        when(chatRoomService.handleMessage(inputMessage)).thenReturn(expectedResponse);

        String result = chatRoomController.sendMessage(inputMessage);

        assertEquals(expectedResponse, result);
        verify(chatRoomService).handleMessage(inputMessage);
    }
}

