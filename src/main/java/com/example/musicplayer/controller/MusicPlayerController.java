package com.example.musicplayer.controller;

import com.example.musicplayer.object.Track;
import com.example.musicplayer.service.MusicPlayerService;
import javazoom.jl.decoder.BitstreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
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
    public String setPathToFolder(@RequestParam String pathToFolder, HttpServletResponse response) {
        track.setPathToFolder(pathToFolder);
        musicPlayerService.setPathToCookie(pathToFolder, response);
        return "redirect:/";
    }

    @PostMapping("/favourite")
    public String setFavourite(Model model, @RequestParam String trackTitle, HttpServletResponse response) throws IOException, BitstreamException, InterruptedException {
        musicPlayerService.setPathToCookie(track.getPathToFolder(), response);
        musicPlayerService.setFavouriteTracksToCookie(trackTitle, response);
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        Thread.sleep(1000);
        return "redirect:/";
    }

    @GetMapping("/deleteFavourite/{trackTitle}")
    public String deleteFromFavourite(@PathVariable String trackTitle, HttpServletRequest request, HttpServletResponse response) {
        musicPlayerService.removeTrackFromFavourite(trackTitle, request, response);
        return "redirect:/favourite";
    }

    @GetMapping("/")
    public String common(Model model, HttpServletRequest request) throws IOException, BitstreamException {
        musicPlayerService.setPathFromCookie(request);
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/favourite")
    public String favourite(Model model, HttpServletRequest request) throws IOException, BitstreamException {
        musicPlayerService.setPathFromCookie(request);
        model.addAttribute("favouriteTrackList", musicPlayerService.getFavouriteTracks(request));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/delete/{trackTitle}")
    public String delete(@PathVariable String trackTitle) {
        System.out.println(musicPlayerService.deleteTrack(track.getPathToFolder(), trackTitle));
        return "redirect:/";
    }

    @GetMapping("/shuffle")
    public String shuffle(Model model, HttpServletRequest request) throws IOException, BitstreamException {
        musicPlayerService.setPathFromCookie(request);
        model.addAttribute("trackList",
                musicPlayerService.getShuffleMusic());
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping(value = "/play/{pathName}")
    public ResponseEntity play(@PathVariable String pathName,
                               HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        musicPlayerService.setPathFromCookie(request);
        System.out.println("Selected: " + track.getPathToFolder());
        response.setHeader("Accept-Ranges", "bytes");
        track.setFullTitle(pathName);
        String process = "Play";
        return musicPlayerService.mediaResourceProcessing(process);
    }

    @GetMapping(value = "/download/{pathName}", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity download(@PathVariable String pathName, HttpServletRequest request)
            throws FileNotFoundException {
        musicPlayerService.setPathFromCookie(request);
        System.out.println("Selected: " + track.getPathToFolder());
        track.setFullTitle(pathName);
        String process = "Download";
        return musicPlayerService.mediaResourceProcessing(process);
    }

    @GetMapping("/{sortName}={directory}")
    public String sort(Model model, @PathVariable String sortName, @PathVariable String directory,
                       HttpServletRequest request) throws IOException, BitstreamException {
        musicPlayerService.setPathFromCookie(request);
        model.addAttribute("trackList",
                musicPlayerService.sort(sortName, directory));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam String trackTitle, HttpServletRequest request)
            throws IOException, BitstreamException {
        musicPlayerService.setPathFromCookie(request);
        model.addAttribute("trackList", musicPlayerService.search(trackTitle));
        model.addAttribute("currentPath", track.getPathToFolder());
        return "index.html";
    }

    @PostMapping("/upload")
    public String upload(Model model, @RequestParam MultipartFile uploadTrack, HttpServletRequest request)
            throws IOException, BitstreamException, InterruptedException {
        musicPlayerService.setPathFromCookie(request);
        model.addAttribute("resultUpload",
                musicPlayerService.uploadTrack(track.getPathToFolder(), uploadTrack));
        model.addAttribute("currentPath", track.getPathToFolder());
        model.addAttribute("trackList", musicPlayerService.getMusic(track.getPathToFolder()));
        Thread.sleep(1000);
        return "redirect:/";
    }
}