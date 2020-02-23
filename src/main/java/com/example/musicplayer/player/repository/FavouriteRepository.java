package com.example.musicplayer.player.repository;

import com.example.musicplayer.player.model.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FavouriteRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicRepository.class.getName());
    private final Track track;

    @Autowired
    public FavouriteRepository(NamedParameterJdbcTemplate jdbcTemplate,
                               Track track) {
        this.jdbcTemplate = jdbcTemplate;
        this.track = track;
    }

    public Integer isTrackAlreadyInFavouriteByUserId(int userId, int musicId) {
        String sql =
                "SELECT COUNT(*) " +
                        "FROM favourite " +
                        "WHERE user_id = :userId " +
                        "AND music_id = :musicId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId);
        return jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
    }

    public void setMusicToFavouriteByUserId(int userId, int musicId) {
        String sql = "" +
                "INSERT INTO favourite (user_id, music_id, date) " +
                "VALUES(:userId, :musicId, :date)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId)
                .addValue("date", LocalDateTime.now());
        jdbcTemplate.update(sql, parameterSource);
    }

    public List<String> getFavouriteTracksByUserId(int userId) {
        String sql =
                "SELECT m.full_title " +
                        "FROM favourite AS f " +
                        "INNER JOIN music AS m ON f.music_id = m.id " +
                        "WHERE f.user_id = :userId " +
                        "ORDER BY f.date";
        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource("userId", userId), String.class);
    }

    public void deleteTrackFromFavourite(int userId, int musicId) {
        String sql =
                "DELETE " +
                        "FROM favourite " +
                        "WHERE user_id = :userId " +
                        "AND music_id = :musicId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId);
        jdbcTemplate.update(sql, parameterSource);
    }
}
