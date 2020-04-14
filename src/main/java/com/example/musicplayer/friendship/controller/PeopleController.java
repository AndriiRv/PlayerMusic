package com.example.musicplayer.friendship.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.friendship.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final FriendshipService friendshipService;

    @Autowired
    public PeopleController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public List<User> allPeople(@AuthenticationPrincipal User user) {
        return friendshipService.getAllUsers(user);
    }
}