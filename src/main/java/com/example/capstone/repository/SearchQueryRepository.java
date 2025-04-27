package com.example.capstone.repository;

import com.example.capstone.domain.SearchQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchQueryRepository extends MongoRepository<SearchQuery, String> {
}