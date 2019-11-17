package com.example.musicplayer.chat.repository;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.chat.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ChatRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ChatRowMapper chatRowMapper = new ChatRowMapper();

    @Autowired
    public ChatRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createPrivateChat(int userId, int companionId, String title) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlCreateChat = "INSERT INTO chat (title, type) VALUES (:title, :type)";
        SqlParameterSource parameterSourceChat = new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("type", "Private");
        jdbcTemplate.update(sqlCreateChat, parameterSourceChat, keyHolder, new String[]{"id"});
        int chatId = (int) Objects.requireNonNull(keyHolder.getKey()).longValue();

        String sqlInsertCurrentUserToThisChat = "INSERT INTO chat_user (chat_id, user_id) VALUES (:chat_id, :user_id)";
        SqlParameterSource parameterSourceCurrentUser = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("user_id", userId);
        jdbcTemplate.update(sqlInsertCurrentUserToThisChat, parameterSourceCurrentUser);

        String sqlInsertCompanionToThisChat = "INSERT INTO chat_user (chat_id, user_id) VALUES (:chat_id, :user_id)";
        SqlParameterSource parameterSourceCompanion = new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("user_id", companionId);
        jdbcTemplate.update(sqlInsertCompanionToThisChat, parameterSourceCompanion);
    }

    public List<Chat> getAllChatsByUserId(int userId) {
        String sql = "SELECT c.id, c.title FROM chat AS c INNER JOIN chat_user cu on c.id = cu.chat_id " +
                "INNER JOIN \"user\" u on cu.user_id = u.id WHERE cu.user_id = :user_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.query(sql, parameterSource, chatRowMapper);
    }

    public Integer isFriendHasInChat(int userId) {
        String sql = "SELECT COUNT(*) FROM chat_user WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
    }

    public Chat getChatByChatTitle(String title) {
        try {
            String sql = "SELECT * FROM chat WHERE title = :title";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("title", title), chatRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}