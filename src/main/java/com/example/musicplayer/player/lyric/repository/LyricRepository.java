package com.example.musicplayer.player.lyric.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class LyricRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LyricRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveLyricToDb(Integer userId, int musicId, String text) {
        String sql = "INSERT INTO lyric(user_id, music_id, text, date_time) VALUES (:userId, :musicId, :text, :dateTime)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId)
                .addValue("text", text)
                .addValue("dateTime", LocalDateTime.now())
        );
    }

    public String getLyricByMusicId(int musicId) {
        try {
            String sql = "SELECT text FROM lyric WHERE music_id = :musicId";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("musicId", musicId), String.class);
        } catch (EmptyResultDataAccessException e) {
            return "";
        }
    }
}