package com.example.musicplayer.player.uploadtrack.controller;

import com.example.musicplayer.player.uploadtrack.dto.UploadedTrackDto;
import com.example.musicplayer.player.uploadtrack.service.UploadTrackConverter;
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

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/upload")
public class UploadTrackController {
    private final UploadTrackService uploadTrackService;
    private final UploadTrackConverter converter;

    public UploadTrackController(UploadTrackService uploadTrackService,
                                 UploadTrackConverter converter) {
        this.uploadTrackService = uploadTrackService;
        this.converter = converter;
    }

    @PostMapping
    public ResponseEntity<Object> upload(@AuthenticationPrincipal User user, MultipartFile musicTrackAsFile) {
        boolean ifTrackWasUploaded = uploadTrackService.uploadTrack(user, musicTrackAsFile);
        if (ifTrackWasUploaded) {
            return new ResponseEntity<>("Music track successful upload to system", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Music track not uploaded because already exist in platform", HttpStatus.CONFLICT);
    }

    @GetMapping
    public Set<UploadedTrackDto> getUploadTracksByUserId(@AuthenticationPrincipal User user) {
        return uploadTrackService.getUploadTracksByUserId(user.getId()).stream()
                .map(converter::convertToUploadedTrack)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}