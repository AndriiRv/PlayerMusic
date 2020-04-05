package com.example.musicplayer.player.music.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.service.MusicPlayerService;
import com.example.musicplayer.player.music.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class MusicPlayerController {
    private final MusicPlayerService musicPlayerService;
    private final MusicService musicService;

    @Autowired
    public MusicPlayerController(MusicPlayerService musicPlayerService,
                                 MusicService musicService) {
        this.musicPlayerService = musicPlayerService;
        this.musicService = musicService;
    }

    @GetMapping("/")
    public String common(Model model, @AuthenticationPrincipal User user, HttpServletRequest request) {
        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("surname", user.getSurname());
            model.addAttribute("email", user.getEmail());
        } else {
            model.addAttribute("name", "guest");
        }
        return "index.html";
    }

    @PostMapping("/lyric")
    @ResponseBody
    public String getLyric(@AuthenticationPrincipal User user, String url, String nameOfTrack,
                           String artistOfTrack, String apiKey) {
        return musicPlayerService.getLyric(user, url, nameOfTrack, artistOfTrack, apiKey);
    }

    @GetMapping("/shuffle/{isShuffle}")
    @ResponseBody
    public List<Track> shuffle(@RequestParam int page, @PathVariable boolean isShuffle) {
        return musicPlayerService.getShuffleMusic(page, isShuffle);
    }

    @GetMapping(value = "/play/{pathName}")
    public ResponseEntity<ByteArrayResource> play(@AuthenticationPrincipal User user,
                                                  @PathVariable String pathName,
                                                  HttpServletResponse response) {
        response.setHeader("Accept-Ranges", "bytes");
        String process = "play";
        return musicPlayerService.mediaResourceProcessing(user, pathName, process);
    }

    @GetMapping(value = "/download/{pathName}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<ByteArrayResource> download(@AuthenticationPrincipal User user,
                                                      @PathVariable String pathName) {
        String process = "download";
        return musicPlayerService.mediaResourceProcessing(user, pathName, process);
    }

    @GetMapping("/sort/{sortName}={directory}")
    @ResponseBody
    public List<Track> sort(@PathVariable String sortName, @PathVariable String directory,
                            Integer page) {
        return musicPlayerService.sort(sortName, directory, page);
    }

    @GetMapping("/searchPlaceholder")
    @ResponseBody
    public String getRandomTrackToPutInSearchPlaceholder() {
        return musicService.getRandomTrackToPutInSearchPlaceholder();
    }
}