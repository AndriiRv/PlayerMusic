package com.example.musicplayer.player.history.repository;

import com.example.musicplayer.player.music.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class HistoryRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public HistoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer isTrackAlreadyInHistoryByUserId(int userId, int musicId) {
        String sql = "SELECT COUNT(*) FROM history WHERE user_id = :userId AND music_id = :musicId";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId), Integer.class);
    }

    public void setTrackToHistoryByUserId(int userId, int musicId) {
        String sql = "INSERT INTO history (user_id, music_id, date) VALUES (:userId, :musicId, :date)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId)
                .addValue("date", LocalDateTime.now()));
    }

    public List<Track> getHistoryByUserId(int userId) {
        String sql = "SELECT m.*, picture AS byteOfPicture " +
                "FROM music AS m " +
                "INNER JOIN history AS h ON h.music_id = m.id " +
                "LEFT JOIN track_cover AS tc ON tc.music_id = m.id " +
                "WHERE h.user_id = :userId " +
                "ORDER BY h.date DESC";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("userId", userId), new BeanPropertyRowMapper<>(Track.class));
    }

    public void removeFromHistory(int userId, int musicId) {
        String sql = "DELETE FROM history WHERE user_id = :userId AND music_id = :musicId";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId));

    }
}