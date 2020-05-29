package com.example.musicplayer.conversation.message.repository;

import com.example.musicplayer.conversation.message.model.Message;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MessageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MessageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveMessage(int chatId, String message, int userId) {
        String sql = "INSERT INTO message(chat_id, user_id, message_text, date_time) "
                + "VALUES (:chatId, :userId, :messageText, :dateTime)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("chatId", chatId)
                .addValue("messageText", message)
                .addValue("userId", userId)
                .addValue("dateTime", LocalDateTime.now()));
    }

    public List<Message> getMessageByChatId(int chatId) {
        String sql = "SELECT u.name, u.surname, m.message_text, m.date_time AS dateTime " +
                "FROM message AS m INNER JOIN \"user\" u ON m.user_id = u.id WHERE m.chat_id = :chat_id";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("chat_id", chatId), new BeanPropertyRowMapper<>(Message.class));
    }
}