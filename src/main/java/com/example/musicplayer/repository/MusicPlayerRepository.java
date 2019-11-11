package com.example.musicplayer.repository;

import com.example.musicplayer.model.Track;
import javazoom.jl.decoder.BitstreamException;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.example.musicplayer.service.MusicPlayerService.getDuration;

@Repository
public class MusicPlayerRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

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

    public String getPathToFolder(int userId) {
        String sql = "SELECT path FROM path_to_folder WHERE user_id = :user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", userId);
        return jdbcTemplate.queryForObject(sql, parameterSource, String.class);
    }

    public List<Track> getMusic(Track track, String pathToFolder) {
        int id = 0;
        double size;
        File file = new File(pathToFolder);
        List<Track> listOfTracks = new ArrayList<>();
        File[] tracks = file.listFiles();
        assert tracks != null;
        if (file.exists()) {
            for (File trackElement : tracks) {
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

//                    int indexDash = track.getFullTitle().indexOf("-");
//                    int indexAfterTitle = track.getFullTitle().indexOf(".mp3");
//
//                    String singer;
//                    String title;
//                    if (indexDash != -1) {
//                        singer = track.getFullTitle().substring(0, indexDash);
//                        title = track.getFullTitle().substring(indexDash + 1, indexAfterTitle);
//                        track.setTitle(title);
//                        track.setSinger(singer);
//                    }

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