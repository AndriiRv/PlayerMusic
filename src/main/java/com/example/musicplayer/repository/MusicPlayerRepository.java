package com.example.musicplayer.repository;

import com.example.musicplayer.model.Folder;
import com.example.musicplayer.model.Track;
import com.example.musicplayer.service.MusicPlayerService;
import javazoom.jl.decoder.BitstreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static com.example.musicplayer.service.MusicPlayerService.getDuration;

@Repository
public class MusicPlayerRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    public static Logger log = Logger.getLogger(MusicPlayerService.class.getName());
    private final FolderRowMapper folderRowMapper = new FolderRowMapper();
    private final TrackRowMapper trackRowMapper = new TrackRowMapper();

    @Autowired
    public MusicPlayerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setPathToFolder(int userId, String pathToFolder) {
        String sql = "INSERT INTO path_to_folder (user_id, path) VALUES(:user_id, :path)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("path", pathToFolder);
        jdbcTemplate.update(sql, parameterSource);
    }

    public List<Folder> getPathsToFolder(int userId) {
        String sql = "SELECT id, path FROM path_to_folder WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.query(sql, parameterSource, folderRowMapper);
    }

    public Integer countOfPathsSetForUser(int userId, String pathToFolder) {
        String sql = "SELECT COUNT(*) FROM path_to_folder WHERE user_id = :user_id AND path = :path";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("path", pathToFolder);
        return jdbcTemplate.queryForObject(sql, parameterSource, Integer.class);
    }

    public void removeAllPathsByUserId(int userId) {
        String sql = "DELETE FROM path_to_folder WHERE user_id = :user_id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("user_id", userId));
    }

    public Track getTrackFromFullTitle(String fullTitle) {
        try {
            String sql = "SELECT * FROM music WHERE full_title = :full_title";
            return jdbcTemplate.queryForObject(sql,
                    new MapSqlParameterSource("full_title", fullTitle), trackRowMapper);
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
        String sql = "SELECT m.id, m.full_title, m.size, m.title, m.singer FROM favourite AS f INNER JOIN \"user\" AS u ON f.user_id = u.id " +
                "INNER JOIN music AS m ON f.music_id = m.id WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.query(sql, parameterSource, trackRowMapper);
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

    public List<Track> getMusicFromTable() {
        String sql = "SELECT id, full_title, size, title, singer FROM music";
        return jdbcTemplate.query(sql, new MapSqlParameterSource(), trackRowMapper);
    }

    public int countOfTrackInFolder(String pathToFolder) {
        System.out.println(Objects.requireNonNull(new File(pathToFolder).list()).length);
        return Objects.requireNonNull(new File(pathToFolder).list()).length;
    }

    public Integer countOfTrackInTable() {
        String sql = "SELECT COUNT(*) FROM music";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    public List<Track> getMusic(Track track, String pathToFolder) {
        int id = 0;
        double size;
        File file = new File(pathToFolder);
        List<Track> listOfTracks = new ArrayList<>();
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
                        e.printStackTrace();
                    }

                    Path dateCreatedOfTrack = Paths.get(String.valueOf(trackElement));
                    BasicFileAttributes attr = null;
                    try {
                        attr = Files.readAttributes(dateCreatedOfTrack, BasicFileAttributes.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String creationDateTime = String.valueOf(Objects.requireNonNull(attr).creationTime());
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(creationDateTime);

                    int indexDash = track.getFullTitle().indexOf("-");
                    int indexAfterTitle = track.getFullTitle().indexOf(".mp3");

                    String singer;
                    String title;
                    if (indexDash != -1) {
                        title = track.getFullTitle().substring(0, indexDash - 1);
                        singer = track.getFullTitle().substring(indexDash + 2, indexAfterTitle);
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

                    if (checkIfTrackHasInTable(track.getTitle(), track.getSinger()) != 1) {
                        String sqlInsertIntoMusic = "INSERT INTO music (title, singer, length, size, date, full_title) " +
                                "VALUES(:title, :singer, :length, :size, :date, :full_title)";
                        SqlParameterSource parameterSourceInsert = new MapSqlParameterSource()
                                .addValue("title", track.getTitle())
                                .addValue("singer", track.getSinger())
                                .addValue("length", track.getLength())
                                .addValue("size", track.getSize())
                                .addValue("date", track.getDate())
                                .addValue("full_title", track.getFullTitle());
                        jdbcTemplate.update(sqlInsertIntoMusic, parameterSourceInsert);

                        log.info("Save in bd: " + trackElement.getName());
                    }
                }
            }

            return listOfTracks;
        } else {
            return null;
        }
    }

    public boolean deleteTrack(String pathToFolder, String title) {
        File file = new File(pathToFolder + "/" + title);
        return file.delete();
    }
}