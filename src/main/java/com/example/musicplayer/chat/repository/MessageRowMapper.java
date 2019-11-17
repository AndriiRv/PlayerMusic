package com.example.musicplayer.chat.repository;

import com.example.musicplayer.chat.model.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setName(rs.getString("name"));
        message.setSurname(rs.getString("surname"));
        message.setMessage(rs.getString("message"));
        message.setTime(rs.getString("time"));
        message.setDate(rs.getString("date"));
        return message;
    }
}