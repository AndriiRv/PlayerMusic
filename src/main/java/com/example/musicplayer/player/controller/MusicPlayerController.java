package com.example.musicplayer.player.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class MusicPlayerController {
    private final MusicService musicService;

    @Autowired
    public MusicPlayerController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping("/")
    public String common(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("name", user.getName());
        } else {
            model.addAttribute("name", "guest");
        }
        return "index.html";
    }

    @PostMapping("/lyric")
    @ResponseBody
    public String getLyric(String url, String nameOfTrack, String artistOfTrack, String apiKey) {
        return url + artistOfTrack + "/" + nameOfTrack + "?apikey=" + apiKey;
    }

    @GetMapping("/shuffle")
    @ResponseBody
    public List<Track> shuffle() {
        return musicService.getShuffleMusic();
    }

    @GetMapping(value = "/play/{pathName}")
    public ResponseEntity play(@PathVariable String pathName, HttpServletResponse response) {
        response.setHeader("Accept-Ranges", "bytes");
        String process = "Play";

        return musicService.mediaResourceProcessing(pathName, process);
    }

    @GetMapping(value = "/download/{pathName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity download(@PathVariable String pathName) {
        String process = "Download";

        return musicService.mediaResourceProcessing(pathName, process);
    }

    @GetMapping("/sort/{sortName}={directory}")
    @ResponseBody
    public List<Track> sort(Model model, @PathVariable String sortName, @PathVariable String directory, @AuthenticationPrincipal User user) {
        return musicService.sort(sortName, directory);
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam MultipartFile musicTrackAsFile) {
        musicService.uploadTrack(musicTrackAsFile);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/searchPlaceholder")
    @ResponseBody
    public String getRandomTrackToPutInSearchPlaceholder() {
        return musicService.getRandomTrackToPutInSearchPlaceholder();
    }
}
