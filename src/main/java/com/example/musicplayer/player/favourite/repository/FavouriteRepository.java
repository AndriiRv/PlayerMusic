package com.example.musicplayer.player.favourite.repository;

import com.example.musicplayer.player.music.model.Track;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class FavouriteRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FavouriteRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getCountOfFavouriteMusicByUserId(int userId) {
        String sql = ""
                + "SELECT COUNT(*) FROM favourite WHERE user_id = :userId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("userId", userId), Integer.class);
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

    public void renameTrackByUser(int userId, int musicId, String musicTitle) {
        String sql = ""
                + "UPDATE favourite SET own_user_music_title = :musicTitle "
                + "WHERE user_id = :userId AND music_id = :musicId";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("musicTitle", musicTitle)
                .addValue("userId", userId)
                .addValue("musicId", musicId));
    }

    public void setMusicToFavouriteByUserId(int userId, int musicId) {
        String sql = "" +
                "INSERT INTO favourite (user_id, music_id, date_time) " +
                "VALUES(:userId, :musicId, :dateTime)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId)
                .addValue("dateTime", LocalDateTime.now());
        jdbcTemplate.update(sql, parameterSource);
    }

    public List<Track> getFavouriteTracksByUserId(int userId) {
        String sql = ""
                + "SELECT m.*, picture AS byteOfPicture, s.counter_played AS countOfPlayed, s.counter_favourite AS countOfFavourite "
                + "FROM music AS m "
                + "INNER JOIN favourite AS f ON f.music_id = m.id "
                + "LEFT JOIN track_cover AS tc ON tc.music_id = m.id "
                + "LEFT JOIN statistic AS s ON s.music_id = m.id "
                + "WHERE f.user_id = :userId "
                + "ORDER BY f.date_time DESC";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("userId", userId), new BeanPropertyRowMapper<>(Track.class));
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