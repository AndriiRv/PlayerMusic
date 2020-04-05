package com.example.musicplayer.player.music.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.picture.service.PictureService;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.musicplayer.player.music.model.Track.dateTrackComparator;
import static com.example.musicplayer.player.music.model.Track.fullTitleTrackComparator;
import static com.example.musicplayer.player.music.model.Track.lengthTrackComparator;
import static com.example.musicplayer.player.music.model.Track.sizeTrackComparator;

@Service
public class MusicPlayerService {
    private final MusicService musicService;
    private final PictureService pictureService;
    private final Logger log = LoggerFactory.getLogger(MusicPlayerService.class.getName());
    private final String pathToFolder;
    private final Track track;
    private List<Track> listOfTracks;

    @Autowired
    public MusicPlayerService(MusicService musicService,
                              PictureService pictureService,
                              @Value("${music-player.directory}") String pathToFolder,
                              Track track) {
        this.musicService = musicService;
        this.pictureService = pictureService;
        this.pathToFolder = pathToFolder;
        this.track = track;
        listOfTracks = new ArrayList<>();
    }

    public List<Track> getListOfTracks() {
        return listOfTracks;
    }

    private List<Track> getMusic() {
        File file = new File(pathToFolder);
        File[] tracks = file.listFiles();

        if (listOfTracks.isEmpty()) {
            if (tracks != null && file.exists()) {
                if (musicService.isMusicTableEmpty() == 0) {
                    int id = 0;

                    for (File trackElement : tracks) {
                        String trackWithExtension = trackElement.getName();
                        if (trackWithExtension.endsWith("mp3") && musicService.checkIfTrackExistInTable(trackElement.getName()) != 1) {
                            Track track = new Track();
                            id++;

                            track.setId(id);
                            setFullTitleSingerAndTitle(track, trackElement);
                            setSizeAndLength(track, trackElement);
                            setDateTime(track, trackElement);
                            setAlbumYearAndGenreToMusicTrack(track, trackElement);

                            int musicTrackId = musicService.insertMusicToDb(track);
                            setCoverToMusicTrack(trackElement, musicTrackId);
                            listOfTracks.add(track);
                            log.info("{}. save in db: {}", id, trackElement.getName());
                        }
                    }
                } else {
                    listOfTracks = musicService.getMusicFromTable();
                    log.info("Load from bd");
                }
            } else {
                log.warn("Music tracks ain't found");
            }
        } else {
            log.info("Load from RAM");
        }
        checkIfTrackExistInSystem();
        return listOfTracks.stream()
                .sorted(Comparator.comparing(Track::getFullTitle))
                .collect(Collectors.toList());
    }

    private void setFullTitleSingerAndTitle(Track track, File trackElement) {
        track.setFullTitle(trackElement.getName());
        track.setSinger(getManuallyTitleAndSinger(track).getSinger());
        track.setTitle(getManuallyTitleAndSinger(track).getTitle());
    }

    private Track getManuallyTitleAndSinger(Track track) {
        int indexDash = track.getFullTitle().indexOf(" - ");
        if (indexDash != -1) {
            int indexAfterTitle = track.getFullTitle().indexOf(".mp3");

            String singer;
            String title;

            singer = track.getFullTitle().substring(0, indexDash);
            title = track.getFullTitle().substring(indexDash + 3, indexAfterTitle);
            track.setTitle(title);
            track.setSinger(singer);
        } else {
            track.setTitle("");
            track.setSinger("");
        }
        return track;
    }

    private void setSizeAndLength(Track track, File trackElement) {
        double size;
        int convertFromByteToMb = 1048576;
        size = trackElement.length();

        track.setSize(Math.round((size / convertFromByteToMb) * 100.0) / 100.0);
        track.setLength(getDuration(trackElement));
    }

    private void setDateTime(Track track, File trackElement) {
        Path dateCreatedOfTrack = Paths.get(String.valueOf(trackElement));
        BasicFileAttributes attr = null;
        try {
            attr = Files.readAttributes(dateCreatedOfTrack, BasicFileAttributes.class);
        } catch (IOException e) {
            log.warn(e.toString());
        }
        String creationDateTime = String.valueOf(Objects.requireNonNull(attr).creationTime());
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(creationDateTime);

        track.setDateTime(zonedDateTime.toLocalDateTime());
        track.setDate(zonedDateTime.toLocalDate());
        track.setTime(LocalTime.of(
                zonedDateTime.getHour(),
                zonedDateTime.getMinute(),
                zonedDateTime.getSecond()));
    }

    private void setCoverToMusicTrack(File trackFile, int musicTrackId) {
        byte[] coverFromMusicTrack = getCoverFromMusicTrack(trackFile.getName());

        track.setByteOfPicture(coverFromMusicTrack);
        if (coverFromMusicTrack.length != 0) {
            pictureService.addPictureToMusic(musicTrackId, coverFromMusicTrack);
        } else {
            FileInputStream fileInputStream;
            byte[] imageInBytes = new byte[0];
            try {
                fileInputStream = new FileInputStream(Objects.requireNonNull(ClassLoader
                        .getSystemClassLoader()
                        .getResource("static/images/disk.jpg"))
                        .getFile());
                imageInBytes = Objects.requireNonNull(fileInputStream).readAllBytes();
            } catch (IOException e) {
                log.warn(e.toString());
            }
            pictureService.addPictureToMusic(musicTrackId, imageInBytes);
        }
    }

    private byte[] getCoverFromMusicTrack(String trackTitle) {
        try {
            Mp3File mp3file = new Mp3File(pathToFolder + trackTitle);
            if (mp3file.hasId3v2Tag()) {

                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                byte[] imageData = id3v2Tag.getAlbumImage();
                if (imageData != null) {
                    return imageData;
                }
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            log.warn(e.toString());
        }
        return new byte[0];
    }

    private void setAlbumYearAndGenreToMusicTrack(Track track, File trackFile) {
        try {
            Mp3File mp3file = new Mp3File(pathToFolder + trackFile.getName());
            if (mp3file.hasId3v1Tag()) {
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                track.setAlbumTitle(id3v1Tag.getAlbum());
                track.setYear(id3v1Tag.getYear());
                track.setGenre(id3v1Tag.getGenreDescription());
            }
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                if (track.getAlbumTitle() == null || track.getAlbumTitle().isBlank()) {
                    track.setAlbumTitle(id3v2Tag.getAlbum());
                }
                if (track.getYear() == null || track.getYear().isBlank()) {
                    track.setYear(id3v2Tag.getYear());
                }
                if (track.getGenre() == null || track.getGenre().isBlank()) {
                    track.setGenre(id3v2Tag.getGenreDescription());
                }
            }

            if (track.getAlbumTitle() == null || !isStringIsUTF8(track.getAlbumTitle())) {
                track.setAlbumTitle(null);
            }
            if (track.getGenre() == null || !isStringIsUTF8(track.getGenre())) {
                track.setGenre(null);
            }
            if (track.getYear() == null || !isStringIsUTF8(track.getYear())) {
                track.setYear(null);
            }

        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            log.warn(e.toString());
        }
    }

    private boolean isStringIsUTF8(String string) {
        try {
            return StandardCharsets.US_ASCII.newEncoder().canEncode(string);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public String getLyric(User user, String url, String nameOfTrack, String artistOfTrack, String apiKey) {
        log.info(user.getUsername() + " get lyric in " + artistOfTrack + " - " + nameOfTrack);
        return url + artistOfTrack + "/" + nameOfTrack + "?apikey=" + apiKey;
    }

    public void checkIfTrackExistInSystem() {
        File file = new File(pathToFolder);
        File[] tracks = file.listFiles();

        if (tracks != null && file.exists()) {
            List<String> musicTitle = new ArrayList<>();
            for (File element : tracks) {
                if (element.getName().endsWith(".mp3")) {
                    musicTitle.add(element.getName());
                }
            }

            List<String> musicTitleFromTable = musicService.getMusicTitleFromTable();
            musicTitleFromTable.removeAll(musicTitle);

            for (String element : musicTitleFromTable) {
                musicService.removeMusicTrackFromDbByFullTitle(element);
                log.info("'" + element + "' - removed from db");
            }
        }
    }

    public List<Track> getShuffleMusic(int page, boolean isShuffle) {
        List<Track> shuffledMusic;

        if (listOfTracks == null || listOfTracks.isEmpty()) {
            shuffledMusic = getMusic();
        } else {
            shuffledMusic = listOfTracks;
            log.info("Load from RAM");
        }

        if (isShuffle) {
            Collections.shuffle(shuffledMusic);
        }
        return shuffledMusic.stream()
                .limit(page)
                .collect(Collectors.toList());
    }

    private String getDuration(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            Bitstream bitstream = new Bitstream(Objects.requireNonNull(fileInputStream));
            Header header = bitstream.readFrame();
            long tn = fileInputStream.getChannel().size();
            long ms = (long) (Objects.requireNonNull(header).total_ms((int) tn));
            long second = (ms / 1000) % 60;
            long minute = (ms / (1000 * 60)) % 60;
            long hour = (ms / (1000 * 60 * 60)) % 24;

            return String.format("%02d:%02d:%02d", hour, minute, second);
        } catch (IOException | BitstreamException exception) {
            log.warn(exception.toString());
        }
        return "";
    }

    public List<Track> sort(String sort, String directory, int page) {
        List<Track> allTracks;

        if (listOfTracks == null || listOfTracks.isEmpty()) {
            allTracks = getMusic();
        } else {
            allTracks = listOfTracks;
            log.info("Load from RAM");
        }

        switch (sort) {
            case "size":
                if (directory.equals("DESC")) {
                    allTracks.sort(sizeTrackComparator.reversed());
                } else {
                    allTracks.sort(sizeTrackComparator);
                }
                break;
            case "length":
                if (directory.equals("DESC")) {
                    allTracks.sort(lengthTrackComparator.reversed());
                } else {
                    allTracks.sort(lengthTrackComparator);
                }
                break;
            case "date":
                if (directory.equals("DESC")) {
                    allTracks.sort(dateTrackComparator.reversed());
                } else {
                    allTracks.sort(dateTrackComparator);
                }
                break;
            default:
                if (directory.equals("DESC")) {
                    allTracks.sort(fullTitleTrackComparator.reversed());
                } else {
                    allTracks.sort(fullTitleTrackComparator);
                }
        }
        log.info("Selected sort: {} with directory {}",
                sort.toUpperCase(), directory.toUpperCase());

        return allTracks.stream()
                .limit(page)
                .collect(Collectors.toList());
    }

    public ResponseEntity<ByteArrayResource> mediaResourceProcessing(User user, String fullTitle, String process) {
        String pathName = fullTitle;
        if (pathName.contains("%5B")) {
            pathName = pathName.replace("%5B", "[").replace("%5D", "]");
        }
        String file = pathToFolder + "/" + pathName;
        long length = new File(file).length();

        ByteArrayResource byteArrayResource = null;
        try (InputStream inputStream = new FileInputStream(file)) {
            byteArrayResource = new ByteArrayResource(inputStream.readAllBytes());
        } catch (IOException exception) {
            log.warn(exception.toString());
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());

        if (user != null) {
            log.info("{} {}: {}", user.getUsername(), process, pathName);
        } else {
            log.info("{}: {}", process, pathName);
        }
        return new ResponseEntity<>(byteArrayResource, httpHeaders, HttpStatus.OK);
    }
}