package com.emre.controller;

import com.emre.repository.entity.Dislike;
import com.emre.service.DislikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.emre.constant.ApiUrls.*;

@RestController
@RequestMapping(DISLIKE)
@RequiredArgsConstructor
public class DislikeController {
    private final DislikeService dislikeService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Dislike>> findAll() {
        return ResponseEntity.ok(dislikeService.findAll());
    }
}
