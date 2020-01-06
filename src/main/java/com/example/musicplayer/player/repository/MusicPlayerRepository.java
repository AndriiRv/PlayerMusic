package com.example.musicplayer.player.repository;

import com.example.musicplayer.player.model.Track;
import javazoom.jl.decoder.BitstreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Logger;

import static com.example.musicplayer.player.service.MusicPlayerService.getDuration;

@Repository
public class MusicPlayerRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public static final Logger log = Logger.getLogger(MusicPlayerRepository.class.getName());
    private int counterLoadTracks = 0;
    private List<Track> listOfTracks;

    @Autowired
    public MusicPlayerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        listOfTracks = new ArrayList<>();
    }

    public Track getTrackFromFullTitle(String fullTitle) {
        try {
            String sql = "SELECT * FROM music WHERE full_title = :full_title";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("full_title", fullTitle), new BeanPropertyRowMapper<>(Track.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Integer isTrackAlreadyInFavourite(int userId, int musicId) {
        String sql = "SELECT COUNT(*) FROM favourite WHERE user_id = :user_id AND music_id = :music_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("music_id", musicId);
        return jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
    }

    public void setMusicToFavourite(int userId, int musicId) {
        String sql = "INSERT INTO favourite (user_id, music_id) VALUES(:user_id, :music_id)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("music_id", musicId);
        jdbcTemplate.update(sql, parameterSource);
    }

    public List<Track> getFavouriteTracks(int userId) {
        String sql = "SELECT m.id, m.full_title, m.size, m.title, m.singer " +
                "FROM favourite AS f INNER JOIN \"user\" AS u ON f.user_id = u.id " +
                "INNER JOIN music AS m ON f.music_id = m.id WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.query(sql, parameterSource, new BeanPropertyRowMapper<>(Track.class));
    }

    public void deleteTrackFromFavourite(int userId, int musicId) {
        String sql = "DELETE FROM favourite WHERE user_id = :user_id AND music_id = :music_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("music_id", musicId);
        jdbcTemplate.update(sql, parameterSource);
    }

    private Integer checkIfTrackHasInTable(String title, String singer) {
        String sql = "SELECT COUNT(*) FROM music WHERE title = :title AND singer = :singer";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("title", title)
                .addValue("singer", singer);
        return jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
    }

    public List<Track> getMusic(Track track, String pathToFolder) {
        listOfTracks.clear();

        counterLoadTracks++;

        if (counterLoadTracks == 1) {
            int id = 0;
            double size;
            File file = new File(pathToFolder);

            File[] tracks = file.listFiles();
            if (file.exists()) {
                for (File trackElement : Objects.requireNonNull(tracks)) {
                    String trackWithExtension = String.valueOf(trackElement);
                    if (trackWithExtension.endsWith("mp3")
                            || trackWithExtension.endsWith("wav")
                            || trackWithExtension.endsWith("wma")) {
                        id = id + 1;

                        track.setId(id);
                        track.setFullTitle(trackElement.getName());
                        track.setPathToFolder(pathToFolder);

                        int convertFromByteToMb = 1048576;
                        size = trackElement.length();

                        track.setSize(Math.round((size / convertFromByteToMb) * 100.0) / 100.0);
                        try {
                            track.setLength(getDuration(trackElement));
                        } catch (IOException | BitstreamException e) {
                            log.warning(e.toString());
                        }

                        Path dateCreatedOfTrack = Paths.get(String.valueOf(trackElement));
                        BasicFileAttributes attr = null;
                        try {
                            attr = Files.readAttributes(dateCreatedOfTrack, BasicFileAttributes.class);
                        } catch (IOException e) {
                            log.warning(e.toString());
                        }
                        String creationDateTime = String.valueOf(Objects.requireNonNull(attr).creationTime());
                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(creationDateTime);

                        int indexDash = track.getFullTitle().indexOf(" - ");
                        int indexAfterTitle = track.getFullTitle().indexOf(".mp3");

                        String singer;
                        String title;
                        if (indexDash != -1) {
                            singer = track.getFullTitle().substring(0, indexDash);
                            title = track.getFullTitle().substring(indexDash + 3, indexAfterTitle);
                            track.setTitle(title);
                            track.setSinger(singer);
                        }

                        track.setDateTime(zonedDateTime.toLocalDateTime());
                        track.setDate(zonedDateTime.toLocalDate());
                        track.setTime(LocalTime.of(
                                zonedDateTime.getHour(),
                                zonedDateTime.getMinute(),
                                zonedDateTime.getSecond()));

                        listOfTracks.add(new Track(
                                track.getId(),
                                track.getFullTitle(),
                                track.getTitle(),
                                track.getSinger(),
                                track.getSize(),
                                track.getLength(),
                                track.getPathToFolder(),
                                track.getDateTime(),
                                track.getDate(),
                                track.getTime()));

                        log.info("Load: " + trackElement.getName());

                        if (checkIfTrackHasInTable(track.getTitle(), track.getSinger()) != 1) {
                            String sqlInsertIntoMusic = "INSERT INTO music (title, singer, length, size, date, time, date_time, full_title) " +
                                    "VALUES(:title, :singer, :length, :size, :date, :time, :date_time, :full_title)";
                            SqlParameterSource parameterSourceInsert = new MapSqlParameterSource()
                                    .addValue("title", track.getTitle())
                                    .addValue("singer", track.getSinger())
                                    .addValue("length", track.getLength())
                                    .addValue("size", track.getSize())
                                    .addValue("date", track.getDate())
                                    .addValue("time", track.getTime())
                                    .addValue("date_time", track.getDateTime())
                                    .addValue("full_title", track.getFullTitle());
                            jdbcTemplate.update(sqlInsertIntoMusic, parameterSourceInsert);

                            log.info("Save in bd: " + trackElement.getName());
                        }
                    }
                }
            }
        } else {
            listOfTracks = getMusicFromTable();
            log.info("Load from bd");
        }
        return listOfTracks;
    }

    private List<Track> getMusicFromTable() {
        String sql = "SELECT id, full_title, size, length, title, singer, date, time, date_time FROM music";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), new BeanPropertyRowMapper<>(Track.class));
    }
}