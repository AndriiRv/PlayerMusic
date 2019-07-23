package com.example.musicplayer.service;

import com.example.musicplayer.object.Track;
import com.example.musicplayer.repositories.MusicPlayerRepository;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
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
    private FileInputStream FIS;
    private BufferedInputStream BIS;
    private boolean canResume = false;
    private int total;
    private int stopped;
    private boolean valid;

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

    public void pause() {
        try {
            stopped = FIS.available();
            player.close();
            FIS = null;
            BIS = null;
            player = null;
            if (valid) canResume = true;
        } catch (Exception ignored) {

        }
    }

    public void resume() {
        counter = counter + 1;
        if (!canResume) {
            return;
        }
        if (play(total - stopped)) {
            canResume = false;
        }
    }

    public boolean play(int pos) {
        valid = true;
        canResume = false;
        counter++;
        try {
            if (counter >= 1 && counter % 2 != 0) {
                FIS = new FileInputStream(track.getPathToFolder() + "/" + track.getTitle());
                total = FIS.available();
                if (pos > -1) {
                    FIS.skip(pos);
                }
                BIS = new BufferedInputStream(FIS);
                player = new Player(BIS);
                thread = new Thread(this);
                thread.start();
            } else {
                close();
                play(-1);
            }
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    @Override
    public void run() {
        try {
            player.play();
        } catch (Exception e) {
            valid = false;
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
        player.close();
        thread.stop();
    }
}