package com.example.musicplayer.friendship.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.friendship.service.FriendshipService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final UserService userService;

    public FriendshipController(FriendshipService friendshipService,
                                UserService userService) {
        this.friendshipService = friendshipService;
        this.userService = userService;
    }

    @GetMapping("/friendship")
    public String myFriends(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("friends", friendshipService.getFriendsByUserIdForFriend(user.getId()));
        return "friendship.html";
    }

    @PostMapping("/friendship")
    public String createFriendship(@AuthenticationPrincipal User user, @RequestParam String possibleFriendUsername) {
        friendshipService.createFriendship(user.getId(), possibleFriendUsername);
        return "redirect:/friendship";
    }
}