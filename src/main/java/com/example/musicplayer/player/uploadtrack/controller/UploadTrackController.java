package com.example.musicplayer.player.uploadtrack.controller;

import com.example.musicplayer.player.uploadtrack.service.UploadTrackService;
import com.example.musicplayer.sign.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadTrackController {
    private final UploadTrackService uploadTrackService;

    @Autowired
    public UploadTrackController(UploadTrackService uploadTrackService) {
        this.uploadTrackService = uploadTrackService;
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Object> upload(@AuthenticationPrincipal User user, MultipartFile musicTrackAsFile) {
        boolean ifTrackWasUploaded = uploadTrackService.uploadTrack(user, musicTrackAsFile);
        if (ifTrackWasUploaded) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}