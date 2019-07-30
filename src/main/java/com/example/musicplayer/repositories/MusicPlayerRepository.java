package com.example.musicplayer.repositories;

import com.example.musicplayer.object.Track;
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

import static com.example.musicplayer.service.MusicPlayerService.getDuration;
import static com.example.musicplayer.service.MusicPlayerService.getExtension;

@Repository
public class MusicPlayerRepository {
    public List<Track> getMusic(Track track, String pathToFolder) {
        int id = 1;
        double size;
        File file = new File(pathToFolder);
        List<Track> listOfTracks = new ArrayList<>();
        File[] tracks = file.listFiles();
        assert tracks != null;
        if (file.exists()) {
            for (int i = 0; i < tracks.length; i++) {
                String trackWithExtension = getExtension(String.valueOf(tracks[i]));
                if (trackWithExtension.equals("mp3")
                        || trackWithExtension.equals("wav")
                        || trackWithExtension.equals("wma")) {
                    track.setId(id++);
                    track.setTitle(tracks[i].getName());
                    track.setPathToFolder(pathToFolder);
                    int convertToMb = 1048576;
                    size = tracks[i].length();
                    track.setSize(Math.round((size / convertToMb) * 100.0) / 100.0);
                    track.setLength(getDuration(tracks[i]));
                    Path dateCreatedOfTrack = Paths.get(String.valueOf(tracks[i]));
                    BasicFileAttributes attr = null;
                    try {
                        attr = Files.readAttributes(dateCreatedOfTrack, BasicFileAttributes.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert attr != null;
                    String creationDateTime = String.valueOf(attr.creationTime());
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(creationDateTime);
                    track.setDateTime(zonedDateTime.toLocalDateTime());
                    track.setDate(zonedDateTime.toLocalDate());
                    track.setTime(LocalTime.of(
                            zonedDateTime.getHour() + 3,
                            zonedDateTime.getMinute(),
                            zonedDateTime.getSecond()));
                    listOfTracks.add(new Track(track.getId(), track.getTitle(), track.getSize(), track.getLength(), track.getPathToFolder(), track.getDateTime(), track.getDate(), track.getTime()));
                }
            }
            return listOfTracks;
        } else {
            return null;
        }
    }
}