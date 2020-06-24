package com.example.musicplayer.player.music.service;

import com.example.musicplayer.dashboard.service.DashboardService;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackList;
import com.example.musicplayer.player.search.searchplaceholder.SearchPlaceholderService;
import com.example.musicplayer.sign.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MusicPlayerService {
    private final MusicService musicService;
    private final MusicTrackSaverService musicTrackSaverService;
    private final SearchPlaceholderService searchPlaceholderService;
    private final DashboardService dashboardService;
    private final TrackList trackList;
    private final String pathToFolder;
    private final Logger log = LoggerFactory.getLogger(MusicPlayerService.class.getName());

    public MusicPlayerService(MusicService musicService,
                              MusicTrackSaverService musicTrackSaverService,
                              SearchPlaceholderService searchPlaceholderService,
                              DashboardService dashboardService,
                              TrackList trackList,
                              @Value("${music-player.directory}") String pathToFolder) {
        this.musicService = musicService;
        this.musicTrackSaverService = musicTrackSaverService;
        this.searchPlaceholderService = searchPlaceholderService;
        this.dashboardService = dashboardService;
        this.trackList = trackList;
        this.pathToFolder = pathToFolder;
    }

    @PostConstruct
    public void postConstruct() {
        trackList.setMusicTracks(getMusicTracksFromDb());
        dashboardService.getMusicForDashboard();
    }

    private Set<Track> getMusicTracksFromDb() {
        musicTrackSaverService.addTrackToDb();
        musicService.checkIfTrackExistInSystem(pathToFolder, musicService.getMusicFromTable());
        return musicService.getMusicFromTable();
    }

    public String getRandomTrackToPutInSearchPlaceholder() {
        return searchPlaceholderService.getRandomTrackToPutInSearchPlaceholder(trackList.getMusicTracks());
    }

    public Set<Track> getShuffleMusic(int page) {
        List<Track> listOfMusicTracks = new ArrayList<>(trackList.getMusicTracks());
        Collections.shuffle(listOfMusicTracks);
        log.info("Shuffled list");
        return listOfMusicTracks.stream()
                .limit(page)
                .collect(Collectors.toSet());
    }

    public Set<Track> filteredMusic(String genreTitle, int page) {
        return trackList.getMusicTracks().stream()
                .filter(e -> e.getGenre() != null)
                .filter(e -> e.getGenre().equals(genreTitle))
                .limit(page)
                .collect(Collectors.toSet());
    }

    public Set<Track> getSortedMusic(String sort, String directory, int page) {
        Set<Track> allTracks = new LinkedHashSet<>(trackList.getMusicTracks());
        log.info("Load from RAM");

        Set<Track> sortedTracks;
        switch (sort) {
            case "size":
                sortedTracks = sortInSwitch(directory, page, allTracks, Comparator.comparing(Track::getSize));
                break;
            case "length":
                sortedTracks = sortInSwitch(directory, page, allTracks, Comparator.comparing(Track::getLength));
                break;
            case "date":
                sortedTracks = sortInSwitch(directory, page, allTracks, Comparator.comparing(Track::getDateTime));
                break;
            default:
                sortedTracks = sortInSwitch(directory, page, allTracks, Comparator.comparing(Track::getFullTitle));
        }
        log.info("Selected sort: {} with directory {}", sort.toUpperCase(), directory.toUpperCase());
        return sortedTracks;
    }

    private Set<Track> sortInSwitch(String directory, int page, Set<Track> allTracks, Comparator<Track> trackComparator) {
        if (directory.equals("DESC")) {
            return allTracks.stream().sorted(trackComparator.reversed()).limit(page).collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            return allTracks.stream().sorted(trackComparator).limit(page).collect(Collectors.toCollection(LinkedHashSet::new));
        }
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
            log.info("'{}' - {}: {}", user.getUsername(), process, pathName);
        } else {
            log.info("'{}' - {}: {}", process, "guest", pathName);
        }
        return new ResponseEntity<>(byteArrayResource, httpHeaders, HttpStatus.OK);
    }
}