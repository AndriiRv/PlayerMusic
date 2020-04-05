package com.example.musicplayer.player.search.controller;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public Set<Track> searchTracks(String searchString) {
        return searchService.searchTracks(searchString);
    }
}