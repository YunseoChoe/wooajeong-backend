package com.example.capstone.controller;

import com.example.capstone.domain.Search;
import com.example.capstone.dto.SearchDto;
import com.example.capstone.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/searches")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;

    // 관심 매물 저장
    // [POST] /searches/save
    @PostMapping("/save")
    public void saveSearch(@RequestBody SearchDto dto) {
        service.saveSearch(dto);
    }

    // 관심 매물 불러오기 (user별로)
    // [GET] /searches/list?user_id=1
    @GetMapping("/list")
    public List<Search> getSearches(@RequestParam("user_id") String userId) {
        return service.getSearchesByUser(userId);
    }
}
