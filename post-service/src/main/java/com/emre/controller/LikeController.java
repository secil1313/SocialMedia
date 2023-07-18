package com.emre.controller;

import com.emre.repository.entity.Like;
import com.emre.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.emre.constant.ApiUrls.*;

@RestController
@RequestMapping(LIKE)
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Like>> findAll() {
        return ResponseEntity.ok(likeService.findAll());
    }

}
