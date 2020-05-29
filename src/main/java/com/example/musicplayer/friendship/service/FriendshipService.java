package com.example.musicplayer.friendship.service;

import com.example.musicplayer.conversation.chat.service.ChatService;
import com.example.musicplayer.friendship.repository.FriendshipRepository;
import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final ChatService chatService;

    public FriendshipService(FriendshipRepository friendshipRepository,
                             UserService userService,
                             ChatService chatService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    public void createFriendship(User user, int possibleFriendId) {
        String currentUserName = userService.getUserByUserId(user.getId()).getName();
        String possibleFriendUserName = userService.getUserByUserId(possibleFriendId).getName();

        friendshipRepository.createFriendship(user.getId(), possibleFriendId);
        chatService.createPrivateChat(user, possibleFriendId, currentUserName + " - " + possibleFriendUserName);
    }

    public void deleteFriendship(int currentUserId, int secondUserId) {
        friendshipRepository.deleteFriendship(currentUserId, secondUserId);
        chatService.removeChatByRemovedFriendship(currentUserId, secondUserId);
    }

    public Integer isFriendHasByUserId(int friendId, int userId) {
        return friendshipRepository.isFriendHasByUserId(friendId, userId);
    }

    public List<User> getAllUsersExceptCurrentUser(User currentUser) {
        return userService.getAllUsers().stream()
                .filter(user -> !user.getUsername().equals(currentUser.getUsername()))
                .collect(Collectors.toList());
    }

    public List<User> getFriendsByUserId(int userId) {
        return friendshipRepository.getFriendsByUserId(userId).stream()
                .map(userService::getUserByUserId)
                .collect(Collectors.toList());
    }
}