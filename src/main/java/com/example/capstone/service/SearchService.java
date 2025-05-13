package com.example.capstone.service;

import com.example.capstone.domain.Search;
import com.example.capstone.dto.SearchDto;
import com.example.capstone.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository repository;

    public void saveSearch(SearchDto dto) {
        Search entity = Search.builder()
                .userId(dto.getUser_id())
                .keyword(dto.getKeyword())
                .location(dto.getLocation())
                .minPrice(Integer.parseInt(dto.getMin_price()))
                .maxPrice(Integer.parseInt(dto.getMax_price()))
                .build();

        repository.save(entity);
    }

    public List<Search> getSearchesByUser(String userId) {
        return repository.findByUserId(userId);
    }
}
