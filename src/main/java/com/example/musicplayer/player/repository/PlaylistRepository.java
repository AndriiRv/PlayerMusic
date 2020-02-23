package com.example.musicplayer.player.repository;

import com.example.musicplayer.player.model.Playlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaylistRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PlaylistRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Playlist getPlaylistByUserIdTitle(int userId, String playlistTitle) {
        try {
            String sql = "" +
                    "SELECT id, title " +
                    "FROM playlist " +
                    "WHERE title = :playlistTitle " +
                    "AND user_id = :userId";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource()
                            .addValue("playlistTitle", playlistTitle)
                            .addValue("userId", userId),
                    new BeanPropertyRowMapper<>(Playlist.class)
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void createPlaylist(int userId, String titleOfPlaylist) {
        String sql = "INSERT INTO playlist (user_id, title) VALUES (:userId, :title)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("title", titleOfPlaylist)
        );
    }

    public List<String> getAllPlaylistsByUser(int userId) {
        String sql = "SELECT title FROM playlist WHERE user_id = :userId";
        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource("userId", userId), String.class);
    }

    public void insertMusicTrackToPlaylistByUser(int userPlaylistId, int musicId) {
        String sql = "INSERT INTO user_playlist (user_playlist_id, music_id) VALUES (:userPlaylistId, :musicId)";
        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userPlaylistId", userPlaylistId)
                .addValue("musicId", musicId)
        );
    }

    public List<String> getMusicFromPlaylistByUser(int userId, int playlistId) {
        String sql = "" +
                "SELECT full_title " +
                "FROM music AS m " +
                "INNER JOIN user_playlist AS up ON up.music_id = m.id " +
                "INNER JOIN playlist AS p ON p.id = up.user_playlist_id " +
                "INNER JOIN \"user\" u on u.id = p.user_id " +
                "WHERE u.id = :userId " +
                "AND up.user_playlist_id = :playlistId";
        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("playlistId", playlistId), String.class);
    }
}
