package org.example.controllers;


import ch.qos.logback.core.joran.sanity.Pair;
import org.example.models.Post;
import org.example.models.User;
import org.example.objects.SearchResultDTO;
import org.example.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/get")
    public List<Post> search(@RequestParam("condition") String condition) {
        System.out.println(condition);
        return searchService.searchResult(condition);
    }
}
