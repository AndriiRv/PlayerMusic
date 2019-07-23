package com.example.musicplayer.controller;

import com.example.musicplayer.object.Track;
import com.example.musicplayer.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        model.addAttribute("trackTitle", track.getTitle());
        return "index.html";
    }

    @GetMapping(value = "/playInBrowser/{pathName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity playInBrowser(HttpServletRequest request, @PathVariable String pathName,
                                        HttpServletResponse response) throws FileNotFoundException {
        track.setTitle(pathName);
        return musicPlayerService.playInBrowser();
    }

    @GetMapping("/play/{trackTitle}")
    public String play(Model model, @PathVariable String trackTitle) {
        track.setTitle(trackTitle);
        if (!trackTitle.isEmpty()) {
            musicPlayerService.play(-1);
        }
        model.addAttribute("trackList", musicPlayerService.getMusic(
                track.getPathToFolder()));
        return "redirect:/";
    }

    @GetMapping("/pause")
    public String pause() {
        musicPlayerService.pause();
        return "redirect:/";
    }

    @GetMapping("/resume/{pathName}")
    public String resume(Model model, @PathVariable String pathName) {
        track.setTitle(pathName);
        if (!pathName.isEmpty()) {
            musicPlayerService.resume();
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