package com.example.musicplayer.player.uploadtrack.controller;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.uploadtrack.service.UploadTrackService;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/upload")
public class UploadTrackController {
    private final UploadTrackService uploadTrackService;

    public UploadTrackController(UploadTrackService uploadTrackService) {
        this.uploadTrackService = uploadTrackService;
    }

    @PostMapping
    public ResponseEntity<Object> upload(@AuthenticationPrincipal User user, MultipartFile musicTrackAsFile) {
        boolean ifTrackWasUploaded = uploadTrackService.uploadTrack(user, musicTrackAsFile);
        if (ifTrackWasUploaded) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public Set<TrackDto> getUploadTracksByUserId(@AuthenticationPrincipal User user) {
        return uploadTrackService.getUploadTracksByUserId(user.getId());
    }
}