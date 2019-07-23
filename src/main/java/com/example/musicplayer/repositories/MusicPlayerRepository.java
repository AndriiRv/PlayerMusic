package com.example.musicplayer.repositories;

import com.example.musicplayer.object.Track;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayer.service.MusicPlayerService.getDuration;
import static com.example.musicplayer.service.MusicPlayerService.getExtension;

@Repository
public class MusicPlayerRepository {
    public List<Track> getMusic(Track track, String pathToFolder) {
        File file = new File(pathToFolder);
        List<Track> listOfTracks = new ArrayList<>();
        File[] tracks = file.listFiles();
        assert tracks != null;
        int id = 1;
        String title;
        double size;
        String length;
        if (file.exists()) {
            for (int i = 0; i < tracks.length; i++) {
                if (getExtension(String.valueOf(tracks[i])).equals("mp3")
                        || getExtension(String.valueOf(tracks[i])).equals("wav")
                        || getExtension(String.valueOf(tracks[i])).equals("wma")) {
                    track.setId(id++);
                    title = tracks[i].getName();
                    track.setTitle(title);
                    track.setPathToFolder(pathToFolder);
                    size = tracks[i].length();
                    size = size / 1024 / 1024;
                    track.setSize(Math.round(size * 100.0) / 100.0);
                    length = getDuration(tracks[i]);
                    track.setLength(length);
                    Path createDateTrack = Paths.get(String.valueOf(tracks[i]));
                    BasicFileAttributes attr = null;
                    try {
                        attr = Files.readAttributes(createDateTrack, BasicFileAttributes.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert attr != null;
                    String creationDate = String.valueOf(attr.creationTime());
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(creationDate);
                    track.setDate(zonedDateTime.toLocalDate());
                    listOfTracks.add(new Track(track.getId(), track.getTitle(), track.getSize(), track.getLength(), track.getPathToFolder(), track.getDate()));
                }
            }
            return listOfTracks;
        } else {
            return null;
        }
    }
}