package com.example.musicplayer.conversation.chat.controller;

import com.example.musicplayer.conversation.chat.model.Chat;
import com.example.musicplayer.conversation.chat.service.ChatService;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<Chat> getAllChatByUser(@AuthenticationPrincipal User user) {
        return chatService.getAllChatsByUserId(user.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void removeChatById(int chatId) {
        chatService.removeChatById(chatId);
    }
}