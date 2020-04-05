package com.example.musicplayer.friendship.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.friendship.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/people")
    @ResponseBody
    public List<User> allPeople(@AuthenticationPrincipal User user) {
        return friendshipService.getAllUsers(user);
    }

    @GetMapping("/friends")
    @ResponseBody
    public List<User> myFriends(@AuthenticationPrincipal User user) {
        return friendshipService.getFriendsByUserIdForFriend(user.getId());
    }

    @PostMapping("/friends")
    @ResponseStatus(HttpStatus.OK)
    public void createFriendship(@AuthenticationPrincipal User user, @RequestParam Integer possibleFriendId) {
        friendshipService.createFriendship(user.getId(), possibleFriendId);
    }
}