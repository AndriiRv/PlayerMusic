package com.example.musicplayer.conversation.message.controller;

import com.example.musicplayer.conversation.message.model.MessageDto;
import com.example.musicplayer.conversation.message.service.MessageService;
import com.example.musicplayer.sign.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<MessageDto> getMessage(Integer chatId, @AuthenticationPrincipal User user) {
        return messageService.getMessageByChatId(chatId);
    }

    @PostMapping
    public void createMessage(Integer chatId, @AuthenticationPrincipal User user, String message) {
        messageService.saveMessage(chatId, message, user);
    }
}