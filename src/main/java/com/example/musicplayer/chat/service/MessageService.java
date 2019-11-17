package com.example.musicplayer.chat.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.chat.model.Message;
import com.example.musicplayer.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          ChatService chatService) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
    }

    public void saveMessage(int chatId, String message, User user) {
        messageRepository.saveMessage(chatId, message, user.getId());
    }

    public List<Message> getMessageByChatId(String chatTitle) {

        return messageRepository.getMessageByChatId(chatService.getChatByChatTitle(chatTitle).getId());
    }

//    public List<Message> getMessagesByUserIdAndChatId(User user, int chatId) {
//        return messageRepository.getMessagesByUserIdAndChatId(user.getId(), chatId);
//    }
}