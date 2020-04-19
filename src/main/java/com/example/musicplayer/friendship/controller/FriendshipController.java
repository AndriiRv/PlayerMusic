package com.example.musicplayer.friendship.controller;

import com.example.musicplayer.friendship.service.FriendshipService;
import com.example.musicplayer.sign.authentication.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public List<User> myFriends(@AuthenticationPrincipal User user) {
        return friendshipService.getFriendsByUserIdForFriend(user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createFriendship(@AuthenticationPrincipal User user, Integer possibleFriendId) {
        friendshipService.createFriendship(user, possibleFriendId);
    }

    @GetMapping("/isAlready")
    public Integer isFriendHasByUserId(int friendId, @AuthenticationPrincipal User user) {
        return friendshipService.isFriendHasByUserId(friendId, user.getId());
    }

    @DeleteMapping
    public void deleteFriendship(@AuthenticationPrincipal User user, int secondUserId) {
        friendshipService.deleteFriendship(user.getId(), secondUserId);
    }
}