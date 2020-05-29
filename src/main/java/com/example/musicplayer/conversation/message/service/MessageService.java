package com.example.musicplayer.conversation.message.service;

import com.example.musicplayer.conversation.message.model.Message;
import com.example.musicplayer.conversation.message.model.MessageDto;
import com.example.musicplayer.conversation.message.repository.MessageRepository;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(int chatId, String message, User user) {
        messageRepository.saveMessage(chatId, message, user.getId());
    }

    public List<MessageDto> getMessageByChatId(int chatId) {
        return messageRepository.getMessageByChatId(chatId).stream()
                .sorted(Comparator.comparing(Message::getDatetime))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private MessageDto toDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setName(message.getName());
        messageDto.setSurname(message.getSurname());
        messageDto.setMessageText(message.getMessageText());
        messageDto.setDatetime(message.getDatetime().format(DateTimeFormatter.ofPattern("yyyy/dd/MM h:mm a")));
        return messageDto;
    }
}