package com.example.musicplayer.conversation.chat.service;

import com.example.musicplayer.conversation.chat.model.Chat;
import com.example.musicplayer.conversation.chat.repository.ChatRepository;
import com.example.musicplayer.sign.authentication.model.User;
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
        List<Chat> allChatsByUserId = chatRepository.getAllChatsByUserId(userId);
        for (Chat chat : allChatsByUserId) {
            User friend = getFriendByCurrentUserIdAndChatId(chat.getId(), userId);
            chat.setTitle(friend.getName() + " " + friend.getSurname());
        }
        return allChatsByUserId;
    }

    private User getFriendByCurrentUserIdAndChatId(int chatId, int userId) {
        return chatRepository.getFriendByCurrentUserIdAndChatId(chatId, userId);
    }

    public void removeChatById(int chatId) {
        chatRepository.removeChatById(chatId);
    }

    public void removeChatByRemovedFriendship(int userId, int friendId) {
        chatRepository.removeChatByRemovedFriendship(userId, friendId);
    }
}