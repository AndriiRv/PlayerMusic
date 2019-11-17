package com.example.musicplayer.player.service;

import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.repository.MusicPlayerRepository;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    public MusicPlayerService(MusicPlayerRepository musicPlayerRepository,
                              Track track) {
        this.musicPlayerRepository = musicPlayerRepository;
        this.track = track;
    }

//    public void setPathToFolder(int userId, String pathToFolder) {
//        if (musicPlayerRepository.countOfPathsSetForUser(userId, pathToFolder) <= 0) {
//            musicPlayerRepository.setPathToFolder(userId, pathToFolder);
//        }
//    }

//    public List<Folder> getPathsToFolder(int userId) {
//        return musicPlayerRepository.getPathsToFolder(userId);
//    }

//    public String getPathToFolder(int userId, String pathToFolder) {
//        List<Folder> pathsToFolder = getPathsToFolder(userId);
//        String selectedPath = null;
//
//        for (Folder folder : pathsToFolder) {
//            if (folder.getPath().equals(pathToFolder)) {
//                selectedPath = folder.getPath();
//            }
//        }
//        MusicPlayerRepository.log.info("Selected path: " + selectedPath);
//        return selectedPath;
//    }

//    public String getLastSelectedPathToFolder(int userId) {
//        List<Folder> pathsToFolder = getPathsToFolder(userId);
//        String lastSelectedPath = null;
//
//        for (Folder folder : pathsToFolder) {
//            folder = pathsToFolder.get(pathsToFolder.size() - 1);
//            lastSelectedPath = folder.getPath();
//        }
//        MusicPlayerRepository.log.info("Last selected path: " + lastSelectedPath);
//        return lastSelectedPath;
//    }

    public List<Track> getMusic(String pathToFolder) {
        if (pathToFolder == null) {
            return null;
//        } else if (musicPlayerRepository.countOfTrackInFolder(pathToFolder)
//                != musicPlayerRepository.countOfTrackInTable()) {
//            return musicPlayerRepository.getMusic(track, pathToFolder);
        } else {
            return musicPlayerRepository.getMusic(track, pathToFolder);
        }
    }

    public void setMusicToFavourite(int userId, String fullTitle) {
        Track track = musicPlayerRepository.getTrackFromFullTitle(fullTitle);
        if (musicPlayerRepository.isTrackAlreadyInFavourite(userId, track.getId()) != 1) {
            musicPlayerRepository.setMusicToFavourite(userId, track.getId());
        }
    }

    public List<Track> getFavouriteTracks(int userId) {
        return musicPlayerRepository.getFavouriteTracks(userId);
    }

    public void deleteTrackFromFavourite(int userId, String fullTitle) {
        Track track = musicPlayerRepository.getTrackFromFullTitle(fullTitle);
        musicPlayerRepository.deleteTrackFromFavourite(userId, track.getId());
    }

//    public void removeAllPathsByUserId(int userId) {
//        musicPlayerRepository.removeAllPathsByUserId(userId);
//    }

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