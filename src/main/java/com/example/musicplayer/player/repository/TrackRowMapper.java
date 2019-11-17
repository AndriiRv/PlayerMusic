package com.example.musicplayer.player.repository;

import com.example.musicplayer.player.model.Track;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackRowMapper implements RowMapper<Track> {

    @Override
    public Track mapRow(ResultSet rs, int rowNum) throws SQLException {
        Track track = new Track();
        track.setId(rs.getInt("id"));
        track.setFullTitle(rs.getString("full_title"));
        track.setSize(rs.getDouble("size"));
        track.setTitle(rs.getString("title"));
        track.setSinger(rs.getString("singer"));
        return track;
    }
}