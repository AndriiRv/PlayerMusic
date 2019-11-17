package com.example.musicplayer.chat.repository;

import com.example.musicplayer.chat.model.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatRowMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        chat.setId(rs.getInt("id"));
        chat.setTitle(rs.getString("title"));
        return chat;
    }
}