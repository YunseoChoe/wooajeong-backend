package com.example.capstone.controller;

import com.example.capstone.domain.SearchQuery;
import com.example.capstone.repository.SearchQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/searches/save")
public class SearchQueryController {

    private final SearchQueryRepository searchQueryRepository;

    @Autowired
    public SearchQueryController(SearchQueryRepository searchQueryRepository) {
        this.searchQueryRepository = searchQueryRepository;
    }

    @PostMapping
    public ResponseEntity<SearchQuery> saveQuery(@RequestBody SearchQuery searchQuery) {
        SearchQuery savedQuery = searchQueryRepository.save(searchQuery);
        return ResponseEntity.ok(savedQuery);
    }
}
