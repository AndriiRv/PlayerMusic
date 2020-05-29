package com.example.musicplayer.player.uploadtrack.repository;

import com.example.musicplayer.player.music.model.Track;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UploadTrackRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UploadTrackRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void uploadTrackByUserId(int userId, int musicId) {
        String sql = "INSERT INTO uploaded_track (user_id, music_id, date_time) VALUES(:userId, :musicId, :dateTime)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("musicId", musicId)
                .addValue("dateTime", LocalDateTime.now()));
    }

    public List<Track> getUploadTracksByUserId(int userId) {
        String sql = ""
                + "SELECT ut.music_id AS id, m.full_title, tc.picture AS byteOfPicture "
                + "FROM uploaded_track AS ut "
                + "INNER JOIN music AS m ON m.id = ut.music_id "
                + "INNER JOIN track_cover AS tc ON tc.music_id = m.id "
                + "INNER JOIN \"user\" AS u ON u.id = ut.user_id "
                + "WHERE u.id = :userId";
        return jdbcTemplate.query(sql, new MapSqlParameterSource("userId", userId), new BeanPropertyRowMapper<>(Track.class));
    }
}