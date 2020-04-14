package com.example.musicplayer.player.music.repository;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public String getFullTitleByMusicId(int musicId) {
        String getFullTitleSql = "SELECT full_title FROM music WHERE id = :musicId";
        return jdbcTemplate.queryForObject(getFullTitleSql, new MapSqlParameterSource("musicId", musicId), String.class);
    }

    public Integer checkIfTrackExistInTable(String fullTitle) {
        try {
            String sql = "SELECT COUNT(*) FROM music WHERE full_title = :fullTitle";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("fullTitle", fullTitle), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public Integer isMusicTableEmpty() {
        try {
            String sql = "SELECT COUNT(*) FROM music";
            return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public int insertMusicToDb(Track track) {
        String sqlInsertIntoMusic =
                "INSERT INTO music (title, singer, full_title, length, size, date, time, date_time, genre, year, album) " +
                        "VALUES(:title, :singer, :fullTitle, :length, :size, :date, :time, :date_time, :genre, :year, :album)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSourceInsert = new MapSqlParameterSource()
                .addValue("title", track.getTitle())
                .addValue("singer", track.getSinger())
                .addValue("fullTitle", track.getFullTitle())
                .addValue("length", track.getLength())
                .addValue("size", track.getSize())
                .addValue("date", track.getDate())
                .addValue("time", track.getTime())
                .addValue("date_time", track.getDateTime())
                .addValue("genre", track.getGenre())
                .addValue("year", track.getYear())
                .addValue("album", track.getAlbumTitle());
        jdbcTemplate.update(sqlInsertIntoMusic, parameterSourceInsert, keyHolder, new String[] {"id"});
        return (int) Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<TrackDto> getMusicFromTable() {
        String sql = ""
                + "SELECT m.*, tc.picture AS byteOfPicture "
                + "FROM music AS m "
                + "LEFT JOIN track_cover AS tc ON tc.music_id = m.id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TrackDto.class));
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