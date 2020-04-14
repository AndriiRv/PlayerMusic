package com.example.musicplayer.player.countofplayed.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayedRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PlayedRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getCountOfPlayedByMusicId(int musicId) {
        try {
            String sql =
                    "SELECT counter " +
                            "FROM count_played " +
                            "WHERE music_id = :musicId";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("musicId", musicId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public void addCountOfPlayedByMusicId(int musicId) {
        Integer counter;
        try {
            String sql = "SELECT counter FROM count_played WHERE music_id = :musicId";
            counter = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("musicId", musicId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            counter = null;
        }

        if (counter == null) {
            String sql = "INSERT INTO count_played(music_id, counter) VALUES (:musicId, :counter)";
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("musicId", musicId)
                    .addValue("counter", 1));
        } else {
            counter++;
            String sql = "UPDATE count_played SET counter = :counter WHERE music_id = :musicId";
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("musicId", musicId)
                    .addValue("counter", counter));
        }
    }
}