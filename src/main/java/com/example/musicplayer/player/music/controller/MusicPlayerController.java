package com.example.musicplayer.player.music.controller;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.music.service.MusicPlayerService;
import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.model.UserDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Controller
public class MusicPlayerController {
    private final MusicPlayerService musicPlayerService;

    public MusicPlayerController(MusicPlayerService musicPlayerService) {
        this.musicPlayerService = musicPlayerService;
    }

    @GetMapping("/")
    public String common(Model model, @AuthenticationPrincipal User user) {
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

    @GetMapping("/getAuthorizedUser")
    @ResponseBody
    public UserDto getUser(@AuthenticationPrincipal User user) {
        return new UserDto(user.getUsername(), user.getName(), user.getSurname(), user.getEmail());
    }

    @GetMapping("/shuffle")
    @ResponseBody
    public Set<TrackDto> shuffle(@RequestParam int page) {
        return musicPlayerService.getShuffleMusic(page);
    }

    @GetMapping(value = "/play/{musicTitle}")
    public ResponseEntity<ByteArrayResource> play(@AuthenticationPrincipal User user,
                                                  @PathVariable String musicTitle,
                                                  HttpServletResponse response) {
        response.setHeader("Accept-Ranges", "bytes");
        String process = "play";
        return musicPlayerService.mediaResourceProcessing(user, musicTitle, process);
    }

    @GetMapping(value = "/download/{musicTitle}",
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<ByteArrayResource> download(@AuthenticationPrincipal User user, @PathVariable String musicTitle) {
        String process = "download";
        return musicPlayerService.mediaResourceProcessing(user, musicTitle, process);
    }

    @GetMapping("/filter/{genreTitle}")
    @ResponseBody
    public Set<TrackDto> filter(@PathVariable String genreTitle, Integer page) {
        return musicPlayerService.filteredMusic(genreTitle, page);
    }

    @GetMapping("/sort/{sortName}={directory}")
    @ResponseBody
    public Set<TrackDto> sort(@PathVariable String sortName, @PathVariable String directory, Integer page) {
        return musicPlayerService.getSortedMusic(sortName, directory, page);
    }

    @GetMapping("/searchPlaceholder")
    @ResponseBody
    public String getRandomTrackToPutInSearchPlaceholder() {
        return musicPlayerService.getRandomTrackToPutInSearchPlaceholder();
    }
}