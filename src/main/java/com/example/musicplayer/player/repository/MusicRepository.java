package com.example.musicplayer.player.repository;

import com.example.musicplayer.player.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class MusicRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public MusicRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Track getTrackByFullTitle(String trackFullTitle) {
        try {
            String sql =
                    "SELECT * " +
                            "FROM music " +
                            "WHERE full_title = :trackFullTitle";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("trackFullTitle", trackFullTitle), new BeanPropertyRowMapper<>(Track.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Integer checkIfTrackExistInTable(String fullTitle) {
        try {
            String sql = "SELECT COUNT(*) FROM music WHERE full_title = :fullTitle";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("fullTitle", fullTitle), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public boolean insertMusicToDb(Track track) {
        String sqlInsertIntoMusic =
                "INSERT INTO music (title, singer, full_title, length, size, date, time, date_time) " +
                        "VALUES(:title, :singer, :fullTitle, :length, :size, :date, :time, :date_time)";
        SqlParameterSource parameterSourceInsert = new MapSqlParameterSource()
                .addValue("title", track.getTitle())
                .addValue("singer", track.getSinger())
                .addValue("fullTitle", track.getFullTitle())
                .addValue("length", track.getLength())
                .addValue("size", track.getSize())
                .addValue("date", track.getDate())
                .addValue("time", track.getTime())
                .addValue("date_time", track.getDateTime());
        return jdbcTemplate.update(sqlInsertIntoMusic, parameterSourceInsert) != 0;
    }

    public List<Track> getMusicFromTable() {
        String sql =
                "SELECT id, full_title, size, length, title, singer, date, time, date_time " +
                        "FROM music";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new BeanPropertyRowMapper<>(Track.class));
    }

    public List<String> getMusicTitleFromTable() {
        String sql = "SELECT full_title FROM music";
        return jdbcTemplate.queryForList(sql, new MapSqlParameterSource(), String.class);
    }

    public void removeMusicTrackFromDbByFullTitle(String fullTitle) {
        String sql = "DELETE FROM music WHERE full_title = :fullTitle";
        jdbcTemplate.update(sql, new MapSqlParameterSource("fullTitle", fullTitle));
    }

    public String getRandomTrackToPutInSearchPlaceholder() {
        String sql = "SELECT full_title FROM music";
        List<String> allFullTitleTracks = jdbcTemplate.queryForList(sql, new MapSqlParameterSource(), String.class);
        return allFullTitleTracks.get(new Random().nextInt(allFullTitleTracks.size()));
    }
}
