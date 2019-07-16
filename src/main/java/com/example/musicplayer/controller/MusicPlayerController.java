package com.example.musicplayer.controller;

import com.example.musicplayer.object.Track;
import com.example.musicplayer.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MusicPlayerController {
    private final MusicPlayerService musicPlayerService;
    private final Track track;

    @Autowired
    public MusicPlayerController(MusicPlayerService musicPlayerService,
                                 Track track) {
        this.musicPlayerService = musicPlayerService;
        this.track = track;
    }

    @GetMapping("/")
    public String getMusic(Model model) {
        if (track.getPathToFolder() != null) {
            model.addAttribute("trackList", musicPlayerService.getMusic(
                    track.getPathToFolder()));
        } else {
            track.setPathToFolder("D:/Music/musicvk");
            model.addAttribute("trackList", musicPlayerService.getMusic(
                    track.getPathToFolder()));
        }
        model.addAttribute("pathToFolder", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/{pathName}")
    public String play(Model model, @PathVariable String pathName) {
        track.setTitle(pathName);
        if (!pathName.isEmpty()) {
            musicPlayerService.playMp3();
        }
        return "redirect:/";
    }

    @GetMapping("/stop")
    public String stop() {
        musicPlayerService.close();
        return "redirect:/";
    }

    @GetMapping("/sortBy/{sort}={directory}")
    public String sort(Model model, @PathVariable String sort, @PathVariable String directory) {
        model.addAttribute("trackList", musicPlayerService.sort(
                musicPlayerService.getMusic(track.getPathToFolder()), sort, directory));
        model.addAttribute("pathToFolder", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String trackTitle) {
        model.addAttribute("trackList", musicPlayerService.search(
                musicPlayerService.getMusic(track.getPathToFolder()), trackTitle));
        model.addAttribute("pathToFolder", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/folder")
    public String getPathToFolder(Model model, @RequestParam String pathToFolder) {
        track.setPathToFolder(pathToFolder);
        model.addAttribute("pathToFolder", track.getPathToFolder());
        return "redirect:/";
    }
}