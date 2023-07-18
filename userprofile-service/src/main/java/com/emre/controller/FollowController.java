package com.emre.controller;

import com.emre.dto.request.CreateFollowDto;
import com.emre.repository.entity.Follow;
import com.emre.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.emre.constant.ApiUrls.*;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Follow>> findAll() {
        return ResponseEntity.ok(followService.findAll());
    }

    @PostMapping(CREATE)
    public ResponseEntity<?> createFollow(@RequestBody CreateFollowDto dto) {
        return ResponseEntity.ok(followService.createFollow(dto));
    }
}
