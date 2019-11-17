package com.example.musicplayer.chat.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.chat.model.Chat;
import com.example.musicplayer.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void createPrivateChat(User user, int companionId, String title) {
        chatRepository.createPrivateChat(user.getId(), companionId, title);
    }

    public List<Chat> getAllChatsByUserId(int userId) {
        return chatRepository.getAllChatsByUserId(userId);
    }

    public Chat getChatByChatTitle(String title) {
        return chatRepository.getChatByChatTitle(title);
    }

    public Integer isFriendHasInChat(int userId) {
        return chatRepository.isFriendHasInChat(userId);
    }
}