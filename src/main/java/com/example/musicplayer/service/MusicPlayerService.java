package com.example.musicplayer.service;

import com.example.musicplayer.model.Folder;
import com.example.musicplayer.model.Track;
import com.example.musicplayer.repository.MusicPlayerRepository;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class MusicPlayerService {
    private final MusicPlayerRepository musicPlayerRepository;
    private final Track track;
    private final Map<String, String> letters = new HashMap<>();

    @Autowired
    public MusicPlayerService(MusicPlayerRepository musicPlayerRepository,
                              Track track) {
        this.musicPlayerRepository = musicPlayerRepository;
        this.track = track;
    }

    public void setPathToFolder(int userId, String pathToFolder) {
        if (musicPlayerRepository.isPathSetForUser(userId, pathToFolder) <= 0) {
            musicPlayerRepository.setPathToFolder(userId, pathToFolder);
        }
    }

    public List<Folder> getPathsToFolder(int userId) {
        return musicPlayerRepository.getPathsToFolder(userId);
    }

    public String getPathToFolder(int userId, String pathToFolder) {
        List<Folder> pathsToFolder = getPathsToFolder(userId);
        String selectedPath = null;

        for (Folder folder : pathsToFolder) {
            if (folder.getPath().equals(pathToFolder)) {
                selectedPath = folder.getPath();
            }
        }
        System.out.println(selectedPath);
        return selectedPath;
    }

    public String getLastSelectedPathToFolder(int userId) {
        List<Folder> pathsToFolder = getPathsToFolder(userId);
        String lastSelectedPath = null;

        for (Folder folder : pathsToFolder) {
            folder = pathsToFolder.get(pathsToFolder.size() - 1);
            lastSelectedPath = folder.getPath();
        }
        System.out.println(lastSelectedPath);
        return lastSelectedPath;
    }

    public List<Track> getMusic(String pathToFolder) {
        if (pathToFolder == null) {
            return null;
        } else {
            return musicPlayerRepository.getMusic(track, pathToFolder);
        }
    }

    private Map<String, String> putLetters() {
        letters.put("а", "a");
        letters.put("б", "b");
        letters.put("в", "v");
        letters.put("г", "h");
        letters.put("д", "d");
        letters.put("е", "e");
        letters.put("ё", "e");
        letters.put("ж", "zh");
        letters.put("з", "z");
        letters.put("і", "i");
        letters.put("и", "y");
        letters.put("й", "i");
        letters.put("к", "k");
        letters.put("л", "l");
        letters.put("м", "m");
        letters.put("н", "n");
        letters.put("о", "o");
        letters.put("п", "p");
        letters.put("р", "r");
        letters.put("с", "s");
        letters.put("т", "t");
        letters.put("у", "u");
        letters.put("ф", "f");
        letters.put("х", "kh");
        letters.put("ц", "ts");
        letters.put("ч", "ch");
        letters.put("ш", "sh");
        letters.put("щ", "sch");
        letters.put("ь", "");
        letters.put("ъ", "'");
        letters.put("ы", "y");
        letters.put("э", "e");
        letters.put("ю", "yu");
        letters.put("я", "ya");
        return letters;
    }

    private String toTransliteration(String text) {
        StringBuilder stringBuilder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            String letter = text.substring(i, i + 1);
            stringBuilder.append(putLetters().getOrDefault(letter, letter));
        }
        return stringBuilder.toString();
    }

    public void removeAllPathsByUserId(int userId) {
        musicPlayerRepository.removeAllPathsByUserId(userId);
    }

//    public void setFavouriteTracksToCookie(String trackTitle, HttpServletResponse response) {
//        String lowerCaseTitle = trackTitle;
//        lowerCaseTitle = lowerCaseTitle.toLowerCase();
//        lowerCaseTitle = toTransliteration(lowerCaseTitle);
//        if (!trackTitle.matches("\\W")) {
//            lowerCaseTitle = lowerCaseTitle.toLowerCase().replaceAll("\\W", "");
//        }
//        Base64.Encoder enc = Base64.getEncoder();
//        String encoded = enc.encodeToString(trackTitle.getBytes());
//        Cookie cookie = new Cookie("fav" + lowerCaseTitle, encoded);
//        cookie.setMaxAge(31536000);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//    }

    public List<Track> getFavouriteTracks(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieValue;

        List<Track> allTracks = musicPlayerRepository.getMusic(track, track.getPathToFolder());
        List<Track> favouriteTracks = new ArrayList<>();

        if (cookies != null && allTracks != null) {
            for (Cookie cookie : cookies) {
                for (Track track : allTracks) {
                    if (cookie.getName().contains("fav")) {
                        cookieValue = cookie.getValue();
                        Base64.Decoder dec = Base64.getDecoder();
                        String decoded = new String(dec.decode(cookieValue));
                        if (track.getFullTitle().equals(decoded)) {
                            favouriteTracks.add(track);
                        }
                    }
                }
            }
            return favouriteTracks;
        } else {
            return null;
        }
    }

    public void removeTrackFromFavourite(String trackTitle, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String cookieValue;

        if (trackTitle.contains("#")) {
            trackTitle = trackTitle.toLowerCase().replaceAll("\\W", "");
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("fav")) {
                    cookieValue = cookie.getValue();
                    Base64.Decoder dec = Base64.getDecoder();
                    String decoded = new String(dec.decode(cookieValue));
                    if (decoded.equals(trackTitle)) {
                        cookie.setValue("");
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
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

    public List<Track> getShuffleMusic() {
        List<Track> shuffledMusic = musicPlayerRepository.getMusic(track, track.getPathToFolder());
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

    public List<Track> sort(String sort, String directory) {
        List<Track> allTracks = musicPlayerRepository.getMusic(track, track.getPathToFolder());
        String variable = null;

        switch (sort) {
            case "size":
                variable = "size";
                break;
            case "length":
                variable = "length";
                break;
            case "title":
                variable = "title";
                break;
            case "date":
                variable = "date";
                break;
        }
        int lengthList = allTracks.size();
        Track[] tracks = allTracks.toArray(new Track[lengthList]);
        if (directory.equals("ASC")) {
            allTracks = insertSort(tracks, '>', variable);
        } else if (directory.equals("DESC")) {
            allTracks = insertSort(tracks, '<', variable);
        }
        MusicPlayerRepository.log.info("Selected sort: " + sort.toUpperCase() + " with directory " + directory.toUpperCase()
                + ", " + track.getPathToFolder());
        return allTracks;
    }

    private List<Track> insertSort(Track[] tracks, char character, String variable) {
        int in, out;

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

    public List<Track> search(String trackTitle) {
        String lowerCaseTrackTitle = trackTitle.toLowerCase();
        List<Track> allTracks = musicPlayerRepository.getMusic(track, track.getPathToFolder());
        List<Track> searchedTracks = new ArrayList<>();
        for (Track track : allTracks) {
            if (lowerCaseTrackTitle.length() != 1) {
                if (track.getFullTitle().toLowerCase().contains(lowerCaseTrackTitle)) {
                    searchedTracks.add(track);
                }
            } else {
                if (track.getFullTitle().toLowerCase().startsWith(lowerCaseTrackTitle)) {
                    searchedTracks.add(track);
                }
            }
        }
        return searchedTracks;
    }

    public ResponseEntity<ByteArrayResource> mediaResourceProcessing(String process, HttpServletRequest request) throws IOException {
        String pathName = track.getFullTitle();
        if (pathName.contains("%5B")) {
            pathName = pathName.replace("%5B", "[").replace("%5D", "]");
        }
        String file = track.getPathToFolder() + "/" + pathName;
        long length = new File(file).length();
        InputStream inputStream = new FileInputStream(file);
        ByteArrayResource byteArrayResource = new ByteArrayResource(inputStream.readAllBytes());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        MusicPlayerRepository.log.info(currentDate() + " | " + currentTime() + " - " + process + ": " + track.getPathToFolder()
                + "/" + pathName);
        return new ResponseEntity<>(byteArrayResource, httpHeaders, HttpStatus.OK);
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