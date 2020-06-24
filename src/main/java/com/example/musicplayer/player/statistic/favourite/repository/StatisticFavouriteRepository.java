package com.example.musicplayer.player.statistic.favourite.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StatisticFavouriteRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(StatisticFavouriteRepository.class.getName());

    public StatisticFavouriteRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getCountOfFavouriteByMusicId(int musicId) {
        try {
            String sql =
                    "SELECT counter_favourite " +
                            "FROM statistic " +
                            "WHERE music_id = :musicId";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("musicId", musicId), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public void addCountOfFavouriteByMusicId(int musicId) {
        Map<Integer, Integer> musicIdCounterFavouriteMap = null;
        try {
            String sql = "SELECT music_id, counter_favourite FROM statistic WHERE music_id = :musicId";
            musicIdCounterFavouriteMap = jdbcTemplate.query(sql, new MapSqlParameterSource("musicId", musicId), (ResultSet rs) -> {
                Map<Integer, Integer> map = new HashMap<>();
                while (rs.next()) {
                    map.put(rs.getInt("music_id"), rs.getInt("counter_favourite"));
                }
                return map;
            });
        } catch (EmptyResultDataAccessException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }

        if (musicIdCounterFavouriteMap == null || musicIdCounterFavouriteMap.get(musicId) == null) {
            String sql = "INSERT INTO statistic(music_id, counter_favourite) VALUES (:musicId, :counterFavourite)";
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("musicId", musicId)
                    .addValue("counterFavourite", 1));
        } else {
            Integer integer = musicIdCounterFavouriteMap.get(musicId);
            integer++;
            String sql = "UPDATE statistic SET counter_favourite = :counterFavourite WHERE music_id = :musicId";
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("musicId", musicId)
                    .addValue("counterFavourite", integer));
        }
    }
}