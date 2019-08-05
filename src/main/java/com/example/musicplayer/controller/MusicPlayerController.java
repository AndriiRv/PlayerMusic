package com.example.musicplayer.controller;

import com.example.musicplayer.object.Track;
import com.example.musicplayer.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@Controller
public class MusicPlayerController {
    private final MusicPlayerService musicPlayerService;
    private final Track track;

    @Autowired
    public MusicPlayerController(MusicPlayerService musicPlayerService,
                                 Track track) {
        this.musicPlayerService = musicPlayerService;
        this.track = track;
        track.setPathToFolder("D:/Music/musicvk");
    }

    @GetMapping("/")
    public String getMusic(Model model) {
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/shuffle")
    public String getShuffleMusic(Model model) {
        model.addAttribute("trackList",
                musicPlayerService.getShuffleMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping(value = "/playInBrowser/{pathName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity playInBrowser(@PathVariable String pathName) throws FileNotFoundException {
        track.setTitle(pathName);
        return musicPlayerService.playInBrowser();
    }

    @GetMapping("/play/{trackTitle}")
    public String play(Model model, @PathVariable String trackTitle) {
        track.setTitle(trackTitle);
        if (!trackTitle.isEmpty()) {
            musicPlayerService.play();
        }
        model.addAttribute("trackList", musicPlayerService.getMusic(
                track.getPathToFolder()));
        return "redirect:/";
    }

    @GetMapping("/stop")
    public String stop() {
        musicPlayerService.close();
        return "redirect:/";
    }

    @GetMapping("/{sort}={directory}")
    public String sort(Model model, @PathVariable String sort, @PathVariable String directory) {
        model.addAttribute("trackList", musicPlayerService.sort(
                musicPlayerService.getMusic(track.getPathToFolder()), sort, directory));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String trackTitle) {
        model.addAttribute("trackList", musicPlayerService.search(
                musicPlayerService.getMusic(track.getPathToFolder()), trackTitle));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/folder")
    public String getPathToFolder(Model model, @RequestParam String pathToFolder) {
        track.setPathToFolder(pathToFolder);
        return "redirect:/";
    }
}