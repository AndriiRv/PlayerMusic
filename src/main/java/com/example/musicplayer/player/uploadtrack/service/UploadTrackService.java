package com.example.musicplayer.player.uploadtrack.service;

import com.example.musicplayer.player.music.service.MusicPlayerService;
import com.example.musicplayer.sign.authentication.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadTrackService {
    private final String pathToFolder;
    private final Logger log = LoggerFactory.getLogger(UploadTrackService.class.getName());
    private final MusicPlayerService musicPlayerService;

    @Autowired
    public UploadTrackService(@Value("${music-player.directory}") String pathToFolder,
                              MusicPlayerService musicPlayerService) {
        this.pathToFolder = pathToFolder;
        this.musicPlayerService = musicPlayerService;
    }

    public boolean uploadTrack(User user, MultipartFile file) {
        String titleOfTrack = file.getOriginalFilename();
        if (titleOfTrack != null && titleOfTrack.endsWith("mp3")) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(pathToFolder + titleOfTrack);
                log.info("{}{}", pathToFolder, titleOfTrack);
                log.info(titleOfTrack + " - was upload in db by: '" + user.getUsername() + "'");
                Files.write(path, bytes);
            } catch (IOException e) {
                log.warn(e.toString());
            }
            musicPlayerService.checkIfTrackExistInSystem();
            return true;
        }
        return false;
    }
}