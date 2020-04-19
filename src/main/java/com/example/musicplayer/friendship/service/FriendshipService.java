package com.example.musicplayer.friendship.service;

import com.example.musicplayer.conversation.chat.service.ChatService;
import com.example.musicplayer.friendship.repository.FriendshipRepository;
import com.example.musicplayer.sign.authentication.model.User;
import com.example.musicplayer.sign.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final ChatService chatService;

    @Autowired
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

    public List<User> getAllUsers(User currentUser) {
        List<User> allUsersWithoutCurrentUser = new ArrayList<>();

        for (User user : userService.getAllUsers()) {
            if (!user.getUsername().equals(currentUser.getUsername())) {
                allUsersWithoutCurrentUser.add(user);
            }
        }
        return allUsersWithoutCurrentUser;
    }

    public List<User> getFriendsByUserIdForFriend(int userId) {
        List<User> getFriends = new ArrayList<>();

        List<Integer> friendsByUserId = friendshipRepository.getFriendsByUserId(userId);

        for (Integer user : friendsByUserId) {
            getFriends.add(userService.getUserByUserId(user));
        }

        return getFriends;
    }
}