package com.example.musicplayer.friendship.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.chat.service.ChatService;
import com.example.musicplayer.friendship.repository.FriendshipRepository;
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

    public void createFriendship(int currentUserId, int possibleFriendId) {
        friendshipRepository.createFriendship(currentUserId, possibleFriendId);
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

    public List<User> getFriendsByUserIdForChat(int userId) {
        List<User> getFriends = new ArrayList<>();

        List<Integer> friendsByUserId = friendshipRepository.getFriendsByUserId(userId);

        for (Integer user : friendsByUserId) {
            if (chatService.isFriendHasInChat(user) <= 0) {
                getFriends.add(userService.getUserByUserId(user));
            }
        }

        return getFriends;
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