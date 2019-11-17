package com.example.musicplayer.friendship.repository;

import com.example.musicplayer.friendship.model.Friendship;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipRowMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setId(rs.getInt("id"));
        friendship.setName(rs.getString("name"));
        friendship.setSurname(rs.getString("surname"));
        return friendship;
    }
}