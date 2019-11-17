package com.example.musicplayer.chat.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.chat.service.ChatService;
import com.example.musicplayer.chat.service.MessageService;
import com.example.musicplayer.friendship.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private int chatId;

    @Autowired
    public ChatController(ChatService chatService,
                          MessageService messageService,
                          UserService userService,
                          FriendshipService friendshipService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/chat")
    public String getMessage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", friendshipService.getFriendsByUserIdForChat(user.getId()));
        model.addAttribute("chats", chatService.getAllChatsByUserId(user.getId()));
        return "/chat";
    }

    @PostMapping("/chat")
    public String createChat(Model model, @AuthenticationPrincipal User user, @RequestParam int companionId) {
        model.addAttribute("users", userService.getAllUsers());

        String nameOfCompanion = userService.getUserByUserId(companionId).getName();
        chatService.createPrivateChat(user, companionId, user.getName() + " - " + nameOfCompanion);
        return "redirect:/chat";
    }

    @GetMapping("/message")
    public String getMessage(Model model, @RequestParam String chatTitle, @AuthenticationPrincipal User user) {
        model.addAttribute("users", friendshipService.getFriendsByUserIdForChat(user.getId()));
        model.addAttribute("chats", chatService.getAllChatsByUserId(user.getId()));
        model.addAttribute("messages", messageService.getMessageByChatId(chatTitle));

        chatId = chatService.getChatByChatTitle(chatTitle).getId();

        return "chat.html";
    }

    @PostMapping("/message")
    public String createMessage(@AuthenticationPrincipal User user, @RequestParam String message) {
        if (chatId != 0) {
            messageService.saveMessage(chatId, message, user);
        }
        return "redirect:/chat";
    }
}