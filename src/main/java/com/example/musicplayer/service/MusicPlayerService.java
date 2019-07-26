package com.example.musicplayer.service;

import com.example.musicplayer.object.Track;
import com.example.musicplayer.repositories.MusicPlayerRepository;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MusicPlayerService implements Runnable {
    private final MusicPlayerRepository musicPlayerRepository;
    private Track track;
    private Player player;
    private Thread thread;
    private int counter;

    @Autowired
    public MusicPlayerService(MusicPlayerRepository musicPlayerRepository,
                              Track track) {
        this.musicPlayerRepository = musicPlayerRepository;
        this.track = track;
    }

    public List<Track> getMusic(String pathToFolder) {
        if (musicPlayerRepository.getMusic(track, pathToFolder) != null) {
            return musicPlayerRepository.getMusic(track, pathToFolder);
        } else {
            return null;
        }
    }

    public static String getExtension(String pathname) {
        int index = pathname.lastIndexOf('.');
        if (index > 0 && index < pathname.length() - 1) {
            return pathname.substring(index + 1).toLowerCase();
        }
        return "";
    }

    public static String getDuration(File file) {
        Header header = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MusicPlayerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        Bitstream bitstream = new Bitstream(Objects.requireNonNull(fileInputStream));
        try {
            header = bitstream.readFrame();
        } catch (BitstreamException ex) {
            Logger.getLogger(MusicPlayerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        long tn = 0;
        try {
            tn = fileInputStream.getChannel().size();
        } catch (IOException ex) {
            Logger.getLogger(MusicPlayerService.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                variable = Comparator.comparing(Track::getDate);
                break;
        }
        if (directory.equals("ASC")) {
            tracks.sort(variable);
        } else if (directory.equals("DESC")) {
            assert variable != null;
            tracks.sort(variable.reversed());
        } else {
            tracks.sort(Comparator.comparing(Track::getId));
        }
        return tracks;
    }

    public List<Track> search(List<Track> tracks, String trackTitle) {
        String lowerCaseTrackTitle = trackTitle.toLowerCase();
        List<Track> list = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getTitle().toLowerCase().contains(lowerCaseTrackTitle)) {
                list.add(track);
            }
        }
        return list;
    }

    public void play() {
        counter++;
        if (counter >= 1 && counter % 2 != 0) {
            thread = new Thread(this);
            thread.start();
        } else {
            close();
            play();
        }
    }

    @Override
    public void run() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(track.getPathToFolder() + "/" + track.getTitle());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert stream != null;
            player = new Player(stream);
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        try {
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<InputStreamResource> playInBrowser() throws FileNotFoundException {
        String file = track.getPathToFolder() + "/" + track.getTitle();
        long length = new File(file).length();
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    public void close() {
        if (player != null || thread != null) {
            Objects.requireNonNull(player).close();
            thread.stop();
        }
    }
}