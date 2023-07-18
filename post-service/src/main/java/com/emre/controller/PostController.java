package com.emre.controller;

import com.emre.dto.request.CreateCommentDto;
import com.emre.dto.request.CreateNewPostRequestDto;
import com.emre.dto.request.UpdatePostRequestDto;
import com.emre.repository.entity.Post;
import com.emre.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.emre.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(POST)
public class PostController {
    private final PostService postService;

    @PostMapping(CREATE + "/{token}")
    public ResponseEntity<Post> createPost(@PathVariable String token, @RequestBody CreateNewPostRequestDto dto) {
        return ResponseEntity.ok(postService.createPost(token, dto));
    }

    @PostMapping(CREATE + "/with-rabbitmq" + "/{token}")
    public ResponseEntity<Post> createPostWithRabbitMq(@PathVariable String token, @RequestBody CreateNewPostRequestDto dto) {
        return ResponseEntity.ok(postService.createPostWithRabbitMq(token, dto));
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @PutMapping(UPDATE + "/{token}")
    public ResponseEntity<Post> update(@PathVariable String token, @RequestBody UpdatePostRequestDto dto) {
        return ResponseEntity.ok(postService.update(token, dto));
    }

    @PostMapping(LIKE_POST)
    public ResponseEntity<Boolean> likePost(String token, String postId) {
        return ResponseEntity.ok(postService.likePost(token, postId));
    }
    @PostMapping(DISLIKE)
    public ResponseEntity<Boolean> dislikePost(String token, String postId) {
        return ResponseEntity.ok(postService.dislikePost(token, postId));
    }

    @DeleteMapping(DELETE_BY_ID + "/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable String postId, String token) {
        return ResponseEntity.ok(postService.deletePost(postId, token));
    }

    @PostMapping(CREATE + "/comment/{token}")
    public ResponseEntity<String> doCommentToPost(@PathVariable String token,@RequestBody CreateCommentDto dto) {
        return ResponseEntity.ok(postService.doCommentToPost(token, dto));
    }
}
