package com.example.musicplayer.service;

import com.example.musicplayer.object.Folder;
import com.example.musicplayer.object.Track;
import com.example.musicplayer.repositories.MusicPlayerRepository;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

@Service
public class MusicPlayerService {
    private final MusicPlayerRepository musicPlayerRepository;
    private final Track track;
    private final Folder folder;
    private static Logger log = Logger.getLogger(MusicPlayerService.class.getName());

    @Autowired
    public MusicPlayerService(MusicPlayerRepository musicPlayerRepository,
                              Track track,
                              Folder folder) {
        this.musicPlayerRepository = musicPlayerRepository;
        this.track = track;
        this.folder = folder;
    }

    public List<Track> getMusic(String pathToFolder) throws IOException, BitstreamException {
        if (pathToFolder == null) {
            return null;
        } else {
            return musicPlayerRepository.getMusic(track, pathToFolder);
        }
    }

    public List<Folder> getFolders(String pathName) {
        return musicPlayerRepository.getFolders(folder, pathName);
    }

    public void setPathFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("pathToFolder")) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
            track.setPathToFolder(cookieValue);
        }
    }

    public boolean deleteTrack(String pathToFolder, String title) {
        return musicPlayerRepository.deleteTrack(pathToFolder, title);
    }

    public String uploadTrack(String pathToFolder, MultipartFile file) {
        String titleOfTrack = file.getOriginalFilename();
        assert titleOfTrack != null;
        if (titleOfTrack.endsWith("mp3")) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(pathToFolder + "/" + titleOfTrack);
                System.out.println(pathToFolder + "/" + titleOfTrack);
                Files.write(path, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "OK";
        }
        return "Error - track hasn't mp3 extension";
    }

    public List<Track> getShuffleMusic() throws IOException, BitstreamException {
        List<Track> shuffledMusic = getMusic(track.getPathToFolder());
        Collections.shuffle(shuffledMusic);
        return shuffledMusic;
    }

    public static String getDuration(File file) throws IOException, BitstreamException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Bitstream bitstream = new Bitstream(Objects.requireNonNull(fileInputStream));
        Header header = bitstream.readFrame();
        long tn;
        tn = fileInputStream.getChannel().size();
        long ms = (long) (Objects.requireNonNull(header).total_ms((int) tn));
        long second = (ms / 1000) % 60;
        long minute = (ms / (1000 * 60)) % 60;
        long hour = (ms / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public List<Track> sort(List<Track> tracks, String sort, String directory) {
        Comparator<Track> variable = null;
        switch (sort) {
            case "size":
                variable = Comparator.comparing(Track::getSize);
                break;
            case "length":
                variable = Comparator.comparing(Track::getLength);
                break;
            case "title":
                variable = Comparator.comparing(Track::getTitle);
                break;
            case "date":
                variable = Comparator.comparing(Track::getDateTime).thenComparing(Track::getTitle);
                break;
        }
        if (directory.equals("ASC")) {
            tracks.sort(variable);
        } else if (directory.equals("DESC")) {
            assert variable != null;
            tracks.sort(variable.reversed());
        }
        log.info("Selected sort: " + sort.toUpperCase() + " with directory " + directory.toUpperCase()
                + ", " + track.getPathToFolder());
        return tracks;
    }

    public List<Track> search(List<Track> tracks, String trackTitle) {
        String lowerCaseTrackTitle = trackTitle.toLowerCase();
        List<Track> listOfTracks = new ArrayList<>();
        for (Track track : tracks) {
            if (lowerCaseTrackTitle.length() != 1) {
                if (track.getTitle().toLowerCase().contains(lowerCaseTrackTitle)) {
                    listOfTracks.add(track);
                }
            } else {
                if (track.getTitle().toLowerCase().startsWith(lowerCaseTrackTitle)) {
                    listOfTracks.add(track);
                }
            }
        }
        return listOfTracks;
    }

    public ResponseEntity<InputStreamResource> mediaResourceProcessing(String process) throws FileNotFoundException {
        String file = track.getPathToFolder() + "/" + track.getTitle();
        long length = new File(file).length();
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        log.info(currentDate() + " | " + currentTime() + " - " + process + ": " + track.getPathToFolder()
                + "/" + track.getTitle());
        return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    private String currentTime() {
        String hour = String.valueOf(LocalTime.now().getHour());
        String minutes = String.valueOf(LocalTime.now().getMinute());
        String second = String.valueOf(LocalTime.now().getSecond());
        return formatNumberForDateAndTime(hour) + ":" + formatNumberForDateAndTime(minutes) + ":"
                + formatNumberForDateAndTime(second);
    }

    private String currentDate() {
        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.valueOf(LocalDate.now().getMonthValue());
        String day = String.valueOf(LocalDate.now().getDayOfMonth());
        return formatNumberForDateAndTime(day) + "." + formatNumberForDateAndTime(month) + "." + year;
    }

    private String formatNumberForDateAndTime(String variable) {
        if (variable.length() == 1) {
            variable = "0" + variable;
        }
        return variable;
    }
}