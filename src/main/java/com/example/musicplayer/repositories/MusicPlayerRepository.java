package com.example.musicplayer.repositories;

import com.example.musicplayer.object.Folder;
import com.example.musicplayer.object.Track;
import javazoom.jl.decoder.BitstreamException;
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

@Repository
public class MusicPlayerRepository {
    public List<Track> getMusic(Track track, String pathToFolder) throws IOException, BitstreamException {
        int id = 1;
        double size;
        File file = new File(pathToFolder);
        List<Track> listOfTracks = new ArrayList<>();
        File[] tracks = file.listFiles();
        assert tracks != null;
        if (file.exists()) {
            for (int i = 0; i < tracks.length; i++) {
                String trackWithExtension = String.valueOf(tracks[i]);
                if (trackWithExtension.endsWith("mp3")
                        || trackWithExtension.endsWith("wav")
                        || trackWithExtension.endsWith("wma")) {
                    track.setId(id++);
                    track.setTitle(tracks[i].getName());
                    track.setPathToFolder(pathToFolder);
                    int convertFromByteToMb = 1048576;
                    size = tracks[i].length();
                    track.setSize(Math.round((size / convertFromByteToMb) * 100.0) / 100.0);
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
                    listOfTracks.add(new Track(
                            track.getId(),
                            track.getTitle(),
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

    public List<Folder> getFolders(Folder folder, String pathName) {
        File file = new File(pathName);
        File[] folders = file.listFiles();
        List<Folder> listOfFolders = new ArrayList<>();
        int id = 1;
        if (file.exists()) {
            assert folders != null;
            for (int i = 0; i < folders.length; i++) {
                if (folders[i].isDirectory()) {
                    getFolders(folder, folders[i].getAbsolutePath());
                    folder.setId(id++);
                    String fromReverseToCommonSlash = folders[i].getAbsolutePath();
                    if (fromReverseToCommonSlash.contains("\\")) {
                        fromReverseToCommonSlash = folders[i]
                                .getAbsolutePath().replaceAll("\\\\", "/");
                    }
                    folder.setPath(fromReverseToCommonSlash);
                    listOfFolders.add(new Folder(folder.getId(), folder.getPath()));
                }
            }
            return listOfFolders;
        } else {
            return null;
        }
    }

    public boolean deleteTrack(String pathToFolder, String title) {
        File file = new File(pathToFolder + "/" + title);
        return file.delete();
    }
}