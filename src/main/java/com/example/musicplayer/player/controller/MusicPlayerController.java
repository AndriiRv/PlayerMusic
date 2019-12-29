package com.example.musicplayer.player.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

//    @PostMapping("/welcome")
//    public String setPathToFolder(@AuthenticationPrincipal User user, @RequestParam String pathToFolder) {
////        musicPlayerService.setPathToFolder(user.getId(), pathToFolder);
////        track.setPathToFolder(musicPlayerService.getPathToFolder(user.getId(), pathToFolder));
//        return "redirect:/";
//    }

    @PostMapping("/favourite")
    public String setFavourite(Model model, @RequestParam String trackTitle, @AuthenticationPrincipal User user)
            throws InterruptedException {
        model.addAttribute("name", user.getName());
//        if (track.getPathToFolder() == null) {
////            track.setPathToFolder(musicPlayerService.getLastSelectedPathToFolder(user.getId()));
//        }

        musicPlayerService.setMusicToFavourite(user.getId(), trackTitle);
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        Thread.sleep(1000);
        return "redirect:/";
    }

    @PostMapping("/deleteFavourite")
    public String deleteFromFavourite(@AuthenticationPrincipal User user, @RequestParam String trackTitle) {
        musicPlayerService.deleteTrackFromFavourite(user.getId(), trackTitle);
        return "redirect:/favourite";
    }

//    @PostMapping("/deletePath")
//    public String deletePathFromCookie(@AuthenticationPrincipal User user) {
//        musicPlayerService.removeAllPathsByUserId(user.getId());
//        return "redirect:/";
//    }

    @GetMapping("/")
    public String common(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("name", user.getName());
        } else {
            model.addAttribute("name", "guest");
        }

//        if (track.getPathToFolder() == null) {
//            track.setPathToFolder(musicPlayerService.getLastSelectedPathToFolder(user.getId()));
//        }

//        model.addAttribute("allWrotePath", musicPlayerService.getPathsToFolder(user.getId()));

        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/favourite")
    public String favourite(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("name", user.getName());
        model.addAttribute("favouriteTrackList", musicPlayerService.getFavouriteTracks(user.getId()));
//        model.addAttribute("allWrotePath", musicPlayerService.getPathsToFolder(user.getId()));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/shuffle")
    public String shuffle(Model model, HttpServletRequest request) {
        model.addAttribute("trackList",
                musicPlayerService.getShuffleMusic());
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping(value = "/play/{pathName}")
    public ResponseEntity play(@PathVariable String pathName, HttpServletResponse response) throws IOException {
        System.out.println("Selected: " + track.getPathToFolder());
        response.setHeader("Accept-Ranges", "bytes");
        track.setFullTitle(pathName);
        String process = "Play";
        return musicPlayerService.mediaResourceProcessing(process);
    }

    @GetMapping(value = "/download/{pathName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity download(@PathVariable String pathName)
            throws IOException {
        System.out.println("Selected: " + track.getPathToFolder());
        track.setFullTitle(pathName);
        String process = "Download";
        return musicPlayerService.mediaResourceProcessing(process);
    }

    @GetMapping("/{sortName}={directory}")
    public String sort(Model model, @PathVariable String sortName, @PathVariable String directory,
                       @AuthenticationPrincipal User user) {
        model.addAttribute("name", user.getName());

//        if (track.getPathToFolder() == null) {
//            track.setPathToFolder(musicPlayerService.getLastSelectedPathToFolder(user.getId()));
//        }
//        model.addAttribute("allWrotePath", musicPlayerService.getPathsToFolder(user.getId()));

        model.addAttribute("trackList",
                musicPlayerService.sort(sortName, directory));
        model.addAttribute("currentPath", track.getPathToFolder());

        return "index.html";
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam MultipartFile uploadTrack, HttpServletRequest request)
            throws InterruptedException {
        model.addAttribute("resultUpload",
                musicPlayerService.uploadTrack(track.getPathToFolder(), uploadTrack));
        model.addAttribute("currentPath", track.getPathToFolder());
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        Thread.sleep(1000);
        return "redirect:/";
    }
}