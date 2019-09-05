package com.example.musicplayer.service;

import com.example.musicplayer.object.Folder;
import com.example.musicplayer.object.Track;
import com.example.musicplayer.repositories.MusicPlayerRepository;
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
import java.util.logging.Logger;

@Service
public class MusicPlayerService {
    private final MusicPlayerRepository musicPlayerRepository;
    private final Track track;
    private final Folder folder;
    private static Logger log = Logger.getLogger(MusicPlayerService.class.getName());
    private final Map<String, String> letters = new HashMap<>();

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

    public Map<String, String> putLetters() {
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

    public String toTransliteration(String text) {
        StringBuilder stringBuilder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            String letter = text.substring(i, i + 1);
            stringBuilder.append(putLetters().getOrDefault(letter, letter));
        }
        return stringBuilder.toString();
    }

    public void setPathToCookie(String pathToFolder, HttpServletResponse response) {
        String changedPath = pathToFolder;
        if (!pathToFolder.matches("\\W")) {
            changedPath = changedPath.toLowerCase().replaceAll("\\W", "");
        }
        Cookie selectPathCookie = new Cookie("path" + changedPath, pathToFolder);
        selectPathCookie.setMaxAge(31536000);
        response.addCookie(selectPathCookie);
        Cookie commonPathCookie = new Cookie("mainFolder", pathToFolder);
        commonPathCookie.setMaxAge(31536000);
        response.addCookie(commonPathCookie);
    }

    public void getPathFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieValue;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("mainFolder")) {
                    cookieValue = cookie.getValue();
                    track.setPathToFolder(cookieValue);
                }
            }
        }
    }

    public List<Folder> getAllWroteManuallyPathFromCookie(HttpServletRequest request) {
        List<Folder> folders = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        String cookieValue;
        int id = 1;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("path")) {
                    folder.setId(id++);
                    cookieValue = cookie.getValue();
                    folder.setPath(cookieValue);
                    folders.add(new Folder(folder.getId(), folder.getPath()));
                }
            }
            return folders;
        }
        return null;
    }

    public void removeAllPathFromCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("path")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public void setFavouriteTracksToCookie(String trackTitle, HttpServletResponse response) {
        String lowerCaseTitle = trackTitle;
        lowerCaseTitle = lowerCaseTitle.toLowerCase();
        lowerCaseTitle = toTransliteration(lowerCaseTitle);
        if (!trackTitle.matches("\\W")) {
            lowerCaseTitle = lowerCaseTitle.toLowerCase().replaceAll("\\W", "");
        }
        Base64.Encoder enc = Base64.getEncoder();
        String encoded = enc.encodeToString(trackTitle.getBytes());
        Cookie cookie = new Cookie("fav" + lowerCaseTitle, encoded);
        cookie.setMaxAge(31536000);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public List<Track> getFavouriteTracks(HttpServletRequest request) throws IOException, BitstreamException {
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

    public List<Track> getShuffleMusic() throws IOException, BitstreamException {
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

    public List<Track> sort(String sort, String directory) throws IOException, BitstreamException {
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
        Integer lengthList = allTracks.size();
        Track[] tracks = allTracks.toArray(new Track[lengthList]);
        if (directory.equals("ASC")) {
            allTracks = insertSort(tracks, '>', variable);
        } else if (directory.equals("DESC")) {
            allTracks = insertSort(tracks, '<', variable);
        }
        log.info("Selected sort: " + sort.toUpperCase() + " with directory " + directory.toUpperCase()
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

    public List<Track> search(String trackTitle) throws IOException, BitstreamException {
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

    public ResponseEntity<ByteArrayResource> mediaResourceProcessing(String process) throws IOException {
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
        log.info(currentDate() + " | " + currentTime() + " - " + process + ": " + track.getPathToFolder()
                + "/" + pathName);
        return new ResponseEntity<>(byteArrayResource, httpHeaders, HttpStatus.OK);
    }

    public String currentTime() {
        String hour = String.valueOf(LocalTime.now().getHour());
        String minutes = String.valueOf(LocalTime.now().getMinute());
        String second = String.valueOf(LocalTime.now().getSecond());
        return formatNumberForDateAndTime(hour) + ":" + formatNumberForDateAndTime(minutes) + ":"
                + formatNumberForDateAndTime(second);
    }

    public String currentDate() {
        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.valueOf(LocalDate.now().getMonthValue());
        String day = String.valueOf(LocalDate.now().getDayOfMonth());
        return formatNumberForDateAndTime(day) + "." + formatNumberForDateAndTime(month) + "." + year;
    }

    public String formatNumberForDateAndTime(String variable) {
        if (variable.length() == 1) {
            variable = "0" + variable;
        }
        return variable;
    }
}