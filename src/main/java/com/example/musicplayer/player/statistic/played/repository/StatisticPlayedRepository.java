package com.example.musicplayer.player.statistic.played.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StatisticPlayedRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(StatisticPlayedRepository.class.getName());

    public StatisticPlayedRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getCountOfPlayedByMusicId(int musicId) {
        try {
            String sql =
                    "SELECT counter_played " +
                            "FROM statistic " +
                            "WHERE music_id = :musicId";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("musicId", musicId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public void addCountOfPlayedByMusicId(int musicId) {
        Map<Integer, Integer> musicIdCounterPlayedMap = null;
        try {
            String sql = "SELECT music_id, counter_played FROM statistic WHERE music_id = :musicId";
            musicIdCounterPlayedMap = jdbcTemplate.query(sql, new MapSqlParameterSource("musicId", musicId), (ResultSet rs) -> {
                Map<Integer, Integer> map = new HashMap<>();
                while (rs.next()) {
                    map.put(rs.getInt("music_id"), rs.getInt("counter_played"));
                }
                return map;
            });
        } catch (EmptyResultDataAccessException e) {
            log.error(e.toString());
        }

        if (musicIdCounterPlayedMap == null || musicIdCounterPlayedMap.get(musicId) == null) {
            String sql = "INSERT INTO statistic(music_id, counter_played) VALUES (:musicId, :counterPlayed)";
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("musicId", musicId)
                    .addValue("counterPlayed", 1));
        } else {
            Integer integer = musicIdCounterPlayedMap.get(musicId);
            integer++;
            String sql = "UPDATE statistic SET counter_played = :counterPlayed WHERE music_id = :musicId";
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("musicId", musicId)
                    .addValue("counterPlayed", integer));
        }
    }
}