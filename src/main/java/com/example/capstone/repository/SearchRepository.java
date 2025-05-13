package com.example.capstone.repository;

import com.example.capstone.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Search, Long> {
    List<Search> findByUserId(String userId);
}
