package com.example.musicplayer.conversation.chat.repository;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.conversation.chat.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ChatRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ChatRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createPrivateChat(int userId, int companionId, String title) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlCreateChat = "INSERT INTO chat (title, type) VALUES (:title, :type)";
        jdbcTemplate.update(sqlCreateChat, new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("type", "Private"), keyHolder, new String[] {"id"});
        int chatId = (int) Objects.requireNonNull(keyHolder.getKey()).longValue();

        String sqlInsertCurrentUserToThisChat = "INSERT INTO chat_user (chat_id, user_id) VALUES (:chat_id, :user_id)";
        jdbcTemplate.update(sqlInsertCurrentUserToThisChat, new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("user_id", userId));
        jdbcTemplate.update(sqlInsertCurrentUserToThisChat, new MapSqlParameterSource()
                .addValue("chat_id", chatId)
                .addValue("user_id", companionId));
    }

    public List<Chat> getAllChatsByUserId(int userId) {
        String sql = "SELECT c.id, c.title FROM chat AS c INNER JOIN chat_user cu ON c.id = cu.chat_id " +
                "INNER JOIN \"user\" u ON cu.user_id = u.id WHERE cu.user_id = :user_id";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("user_id", userId), new BeanPropertyRowMapper<>(Chat.class));
    }

    public void removeChatById(int chatId) {
        String sql = "DELETE FROM chat WHERE id = :chatId";
        jdbcTemplate.update(sql, new MapSqlParameterSource("chatId", chatId));
    }

    public User getFriendByCurrentUserIdAndChatId(int chatId, int userId) {
        String sql = "SELECT u.* FROM chat_user AS cu INNER JOIN \"user\" AS u ON u.id = cu.user_id WHERE user_id != :userId AND chat_id = :chatId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("chatId", chatId), new BeanPropertyRowMapper<>(User.class));
    }

    public void removeChatByRemovedFriendship(int userId, int friendId) {
        String sql = "SELECT chat_id FROM chat_user WHERE user_id = :userId";
        List<Integer> chatIds = jdbcTemplate.queryForList(sql, new MapSqlParameterSource("userId", userId), Integer.class);
        for (Integer chatId : chatIds) {
            List<Integer> chatIdsByFriendId = jdbcTemplate.queryForList(sql, new MapSqlParameterSource("userId", friendId), Integer.class);
            for (Integer chatIdByFriendId : chatIdsByFriendId) {
                if (chatIdByFriendId.equals(chatId)) {
                    String sql3 = "DELETE FROM chat WHERE id = :id";
                    jdbcTemplate.update(sql3, new MapSqlParameterSource("id", chatId));
                    return;
                }
            }
        }
    }
}