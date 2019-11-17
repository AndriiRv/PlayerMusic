package com.example.musicplayer.chat.repository;

import com.example.musicplayer.chat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MessageRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final MessageRowMapper messageRowMapper = new MessageRowMapper();

    @Autowired
    public MessageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveMessage(int chatId, String message, int userId) {
        String sql = "INSERT INTO message (chat_id, message, user_id, time, date) " +
                "VALUES (:chat_id, :message, :user_id, :time, :date)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("message", message)
                .addValue("user_id", userId)
                .addValue("time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .addValue("date", LocalDate.now());
        jdbcTemplate.update(sql, parameterSource);
    }

    public List<Message> getMessageByChatId(int chatId) {
        String sql = "SELECT u.name, u.surname, m.message, m.time, m.date " +
                "FROM message AS m INNER JOIN \"user\" u on m.user_id = u.id WHERE m.chat_id = :chat_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("chat_id", chatId);
        List<Message> messages = jdbcTemplate.query(sql, parameterSource, messageRowMapper);
        messages = messages.stream()
                .sorted(Comparator.comparing(Message::getTime).thenComparing(Message::getDate))
                .collect(Collectors.toList());
        return messages;
    }

//    public List<Message> getMessagesByUserIdAndChatId(int userId, int chatId) {
//        String sql = "SELECT u.name, u.surname, m.message, m.time, m.date " +
//                "FROM message AS m INNER JOIN \"user\" u on m.user_id = u.id " +
//                "WHERE m.user_id = :user_id AND m.chat_id = :chat_id";
//
//        SqlParameterSource parameterSource = new MapSqlParameterSource()
//                .addValue("user_id", userId)
//                .addValue("chatId", chatId);
//        return jdbcTemplate.query(sql, new MapSqlParameterSource(), messageRowMapper);
//    }
}