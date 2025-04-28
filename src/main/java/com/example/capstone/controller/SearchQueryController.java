package com.example.capstone.controller;

import com.example.capstone.domain.SearchQuery;
import com.example.capstone.repository.SearchQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/searches")
public class SearchQueryController {

    private final SearchQueryRepository searchQueryRepository;

    @Autowired
    public SearchQueryController(SearchQueryRepository searchQueryRepository) {
        this.searchQueryRepository = searchQueryRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<SearchQuery> saveQuery(@RequestBody SearchQuery searchQuery) {
        SearchQuery savedQuery = searchQueryRepository.save(searchQuery);
        return ResponseEntity.ok(savedQuery);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SearchQuery>> getAllQueries() {
        List<SearchQuery> queries = searchQueryRepository.findAll();
        return ResponseEntity.ok(queries);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuery(@PathVariable String id) {
        Optional<SearchQuery> query = searchQueryRepository.findById(id);
        if (query.isPresent()) {
            searchQueryRepository.deleteById(id);
            return ResponseEntity.ok("삭제 성공: id = " + id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
