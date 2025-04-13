package com.example.capstone.controller;

import com.example.capstone.domain.ChattingContent;
import com.example.capstone.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class TestController {

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/save")
    public ChattingContent test(@RequestParam("name") String name, @RequestParam("age") Long age) {
        ChattingContent content = new ChattingContent(name,age);

        return testRepository.save(content);
    }

    @GetMapping("/find")
    public ChattingContent test(@RequestParam("name") String name) {
        ChattingContent content = testRepository.findChattingContentByName(name);

        return content;
    }
}
