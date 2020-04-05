package com.example.musicplayer.player.picture.repository;

import com.example.musicplayer.player.picture.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PictureRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PictureRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addPictureToMusic(int musicId, byte[] picture) {
        String sql = "INSERT INTO track_cover (music_id, picture) VALUES (:musicId, :picture)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("musicId", musicId)
                .addValue("picture", picture));
    }

    public byte[] getPictureByMusicId(int musicId) {
        try {
            String sql = "SELECT picture FROM track_cover WHERE music_id = :musicId";

            return jdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource("musicId", musicId),
                    (rs, rowNum) -> rs.getBytes(1));
        } catch (EmptyResultDataAccessException e) {
            return new byte[0];
        }
    }

    public List<Picture> getAllPicture() {
        String sql = "SELECT music_id, picture FROM track_cover";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new BeanPropertyRowMapper<>(Picture.class));
    }
}
