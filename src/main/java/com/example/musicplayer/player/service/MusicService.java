package com.example.musicplayer.player.service;

import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.repository.MusicRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final Logger log = LoggerFactory.getLogger(MusicService.class.getName());
    private final String pathToFolder;
    private final Track track;
    private List<Track> listOfTracks;

    @Autowired
    public MusicService(MusicRepository musicRepository,
                        @Value("${music-player.directory}")
                                String pathToFolder, Track track) {
        this.musicRepository = musicRepository;
        this.pathToFolder = pathToFolder;
        this.track = track;
        listOfTracks = new ArrayList<>();
    }

    private List<Track> getMusic() {
        File file = new File(pathToFolder);
        File[] tracks = file.listFiles();

        if (tracks != null && file.exists()) {
            int id = 0;

            for (File trackElement : tracks) {
                String trackWithExtension = trackElement.getName();

                if (trackWithExtension.endsWith("mp3") && musicRepository.checkIfTrackExistInTable(trackElement.getName()) != 1) {
                    id++;

                    track.setId(id);
                    setFullTitleSingerAndTitle(track, trackElement);
                    setSizeAndLength(trackElement, track);
                    setDateTime(trackElement);

                    listOfTracks.add(new Track(track.getId(), track.getFullTitle(),
                            track.getTitle(), track.getSinger(), track.getSize(),
                            track.getLength(), track.getDateTime(), track.getDate(),
                            track.getTime()));

                    if (musicRepository.insertMusicToDb(track)) {
                        log.info("{}. Save in bd: {}", id, trackElement.getName());
                    } else {
                        log.info("Something went wrong with save music to db");
                    }
                }
            }
            listOfTracks = musicRepository.getMusicFromTable();
            log.info("Load from bd");
        }
        return listOfTracks.stream()
                .sorted(Comparator.comparing(Track::getFullTitle))
                .collect(Collectors.toList());
    }

    private void setDateTime(File trackElement) {
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

    private void setSizeAndLength(File trackElement, Track track) {
        double size;
        int convertFromByteToMb = 1048576;
        size = trackElement.length();

        track.setSize(Math.round((size / convertFromByteToMb) * 100.0) / 100.0);
        track.setLength(getDuration(trackElement));
    }

    Track getTrackByFullTitle(String trackFullTitle) {
        return musicRepository.getTrackByFullTitle(trackFullTitle);
    }

    public boolean uploadTrack(MultipartFile file) {
        String titleOfTrack = file.getOriginalFilename();
        if (titleOfTrack != null && titleOfTrack.endsWith("mp3")) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(pathToFolder + titleOfTrack);
                log.info("{}{}", pathToFolder, titleOfTrack);
                Files.write(path, bytes);
            } catch (IOException e) {
                log.warn(e.toString());
            }
            checkIfTrackExistInSystem();
            return true;
        }
        return false;
    }

    private void checkIfTrackExistInSystem() {
        File file = new File(pathToFolder);
        File[] tracks = file.listFiles();

        if (tracks != null && file.exists()) {

            List<String> musicTitle = new ArrayList<>();
            for (File element : tracks) {
                if (element.getName().endsWith(".mp3")) {
                    musicTitle.add(element.getName());
                }
            }

            List<String> musicTitleFromTable = musicRepository.getMusicTitleFromTable();

            musicTitleFromTable.removeAll(musicTitle);

            for (String element : musicTitleFromTable) {
                musicRepository.removeMusicTrackFromDbByFullTitle(element);
            }
        }
    }

    public List<Track> getShuffleMusic() {
        List<Track> shuffledMusic = getMusic();
        Collections.shuffle(shuffledMusic);
        return shuffledMusic;
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

    public List<Track> sort(String sort, String directory) {
        List<Track> allTracks = getMusic();
        String variable;

        switch (sort) {
            case "size":
                variable = "size";
                break;
            case "length":
                variable = "length";
                break;
            case "date":
                variable = "date";
                break;
            default:
                variable = "title";
        }
        int lengthList = allTracks.size();
        Track[] tracks = allTracks.toArray(new Track[lengthList]);
        if (directory.equals("ASC")) {
            allTracks = insertSort(tracks, '>', variable);
        } else if (directory.equals("DESC")) {
            allTracks = insertSort(tracks, '<', variable);
        }
        log.info("Selected sort: {} with directory {}",
                sort.toUpperCase(), directory.toUpperCase());
        return allTracks;
    }

    private List<Track> insertSort(Track[] tracks, char character, String variable) {
        int in;
        int out;

        for (out = 1; out < tracks.length; out++) {
            Track track = tracks[out];
            in = out;

            if (character == '>') {
                switch (variable) {
                    case "title":
                        while (in > 0 && tracks[in - 1].getFullTitle().compareTo(track.getFullTitle()) > 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                    case "length":
                        while (in > 0 && tracks[in - 1].getLength().compareTo(track.getLength()) > 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                    case "size":
                        while (in > 0 && tracks[in - 1].getSize().compareTo(track.getSize()) > 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                    case "date":
                        while (in > 0 && tracks[in - 1].getDateTime().compareTo(track.getDateTime()) > 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                }
            } else {
                switch (variable) {
                    case "title":
                        while (in > 0 && tracks[in - 1].getFullTitle().compareTo(track.getFullTitle()) < 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                    case "length":
                        while (in > 0 && tracks[in - 1].getLength().compareTo(track.getLength()) < 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                    case "size":
                        while (in > 0 && tracks[in - 1].getSize().compareTo(track.getSize()) < 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                    case "date":
                        while (in > 0 && tracks[in - 1].getDateTime().compareTo(track.getDateTime()) < 0) {
                            tracks[in] = tracks[in - 1];
                            --in;
                        }
                        break;
                }
            }
            tracks[in] = track;
        }
        return Arrays.asList(tracks);
    }

    public ResponseEntity<ByteArrayResource> mediaResourceProcessing(String fullTitle, String process) {
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
        log.info("{}: {}", process, pathName);
        return new ResponseEntity<>(byteArrayResource, httpHeaders, HttpStatus.OK);
    }

    public String getRandomTrackToPutInSearchPlaceholder() {
        return musicRepository.getRandomTrackToPutInSearchPlaceholder();
    }
}
