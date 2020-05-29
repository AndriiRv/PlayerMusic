package com.example.musicplayer.player.search.globalsearch.controller;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.search.globalsearch.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public Set<TrackDto> searchTracks(String searchString) {
        return searchService.searchTracks(searchString);
    }
}