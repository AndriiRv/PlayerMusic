package com.example.musicplayer.conversation.message.service;

import com.example.musicplayer.conversation.message.model.Message;
import com.example.musicplayer.conversation.message.model.MessageDto;
import com.example.musicplayer.conversation.message.repository.MessageRepository;
import com.example.musicplayer.sign.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

    }

    private MessageDto toDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setName(message.getName());
        messageDto.setSurname(message.getSurname());
        messageDto.setMessage(message.getMessage());
        messageDto.setDatetime(message.getDatetime().format(DateTimeFormatter.ofPattern("yyyy/dd/MM h:mm a")));
        return messageDto;
    }

    public void saveMessage(int chatId, String message, User user) {
        messageRepository.saveMessage(chatId, message, user.getId());
    }

    public List<MessageDto> getMessageByChatId(int chatId) {
        List<Message> messages = messageRepository.getMessageByChatId(chatId);
        messages = messages.stream()
                .sorted(Comparator.comparing(Message::getDatetime))
                .collect(Collectors.toList());

        List<MessageDto> list = new ArrayList<>();
        for (Message element : messages) {
            list.add(toDto(element));
        }
        return list;
    }
}