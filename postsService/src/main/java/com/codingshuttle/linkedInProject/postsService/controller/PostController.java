package com.codingshuttle.linkedInProject.postsService.controller;

import com.codingshuttle.linkedInProject.postsService.auth.AuthContextHolder;
import com.codingshuttle.linkedInProject.postsService.dto.PostCreateRequestDto;
import com.codingshuttle.linkedInProject.postsService.dto.PostDto;
import com.codingshuttle.linkedInProject.postsService.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(@RequestPart("post") PostCreateRequestDto postCreateRequestDto,
                                              @RequestPart("file") MultipartFile file) {

        PostDto postDto = postService.createPost(postCreateRequestDto, AuthContextHolder.getCurrentUserId(),file);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

//    @GetMapping("/{postId}")
//    public ResponseEntity<PostDto> getPost(@PathVariable Long postId, @RequestHeader("X-User-Id") Long userId) {
//        log.info("User Id "+ userId);
//        PostDto postDto = postService.getPostById(postId);
//        return ResponseEntity.ok(postDto);
//    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        Long userId = AuthContextHolder.getCurrentUserId();
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }


}
