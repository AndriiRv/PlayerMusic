package com.example.musicplayer.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.model.Track;
import com.example.musicplayer.service.MusicPlayerService;
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
    }

    @PostMapping("/welcome")
    public String setPathToFolder(@AuthenticationPrincipal User user, @RequestParam String pathToFolder) {
        musicPlayerService.setPathToFolder(user.getId(), pathToFolder);
        track.setPathToFolder(musicPlayerService.getPathToFolder(user.getId(), pathToFolder));
        return "redirect:/";
    }

    @PostMapping("/favourite")
    public String setFavourite(Model model, @RequestParam String trackTitle,
                               HttpServletRequest request, HttpServletResponse response)
            throws InterruptedException {
//        musicPlayerService.setPathToCookie(track.getPathToFolder(), response);
//        musicPlayerService.setFavouriteTracksToCookie(trackTitle, response);
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        Thread.sleep(1000);
        return "redirect:/";
    }

    @PostMapping("/deleteFavourite")
    public String deleteFromFavourite(@RequestParam String trackTitle, HttpServletRequest request,
                                      HttpServletResponse response) {
        musicPlayerService.removeTrackFromFavourite(trackTitle, request, response);
        return "redirect:/favourite";
    }

    @PostMapping("/deletePath")
    public String deletePathFromCookie(@AuthenticationPrincipal User user) {
        musicPlayerService.removeAllPathsByUserId(user.getId());
        return "redirect:/";
    }

    @GetMapping("/")
    public String common(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("name", user.getName());
        if (track.getPathToFolder() == null) {
            track.setPathToFolder(musicPlayerService.getLastSelectedPathToFolder(user.getId()));
        }
        model.addAttribute("allWrotePath", musicPlayerService.getPathsToFolder(user.getId()));

        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/favourite")
    public String favourite(Model model, HttpServletRequest request) {
//        musicPlayerService.getPathFromCookie(request);
//        model.addAttribute("allWrotePath", musicPlayerService.getAllWroteManuallyPathFromCookie(request));
        model.addAttribute("favouriteTrackList", musicPlayerService.getFavouriteTracks(request));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/shuffle")
    public String shuffle(Model model, HttpServletRequest request) {
//        musicPlayerService.getPathFromCookie(request);
//        model.addAttribute("allWrotePath", musicPlayerService.getAllWroteManuallyPathFromCookie(request));
        model.addAttribute("trackList",
                musicPlayerService.getShuffleMusic());
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping(value = "/play/{pathName}")
    public ResponseEntity play(@PathVariable String pathName,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
//        musicPlayerService.getPathFromCookie(request);
        System.out.println("Selected: " + track.getPathToFolder());
        response.setHeader("Accept-Ranges", "bytes");
        track.setFullTitle(pathName);
        String process = "Play";
        return musicPlayerService.mediaResourceProcessing(process, request);
    }

    @GetMapping(value = "/download/{pathName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity download(@PathVariable String pathName, HttpServletRequest request)
            throws IOException {
//        musicPlayerService.getPathFromCookie(request);
        System.out.println("Selected: " + track.getPathToFolder());
        track.setFullTitle(pathName);
        String process = "Download";
        return musicPlayerService.mediaResourceProcessing(process, request);
    }

    @GetMapping("/{sortName}={directory}")
    public String sort(Model model, @PathVariable String sortName, @PathVariable String directory,
                       HttpServletRequest request) {
//        musicPlayerService.getPathFromCookie(request);
        model.addAttribute("trackList",
                musicPlayerService.sort(sortName, directory));
        model.addAttribute("currentPath", track.getPathToFolder());
//        model.addAttribute("allWrotePath", musicPlayerService.getAllWroteManuallyPathFromCookie(request));
        return "index.html";
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam MultipartFile uploadTrack, HttpServletRequest request)
            throws InterruptedException {
//        musicPlayerService.getPathFromCookie(request);
        model.addAttribute("resultUpload",
                musicPlayerService.uploadTrack(track.getPathToFolder(), uploadTrack));
        model.addAttribute("currentPath", track.getPathToFolder());
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        Thread.sleep(1000);
//        model.addAttribute("allWrotePath", musicPlayerService.getAllWroteManuallyPathFromCookie(request));
        return "redirect:/";
    }
}