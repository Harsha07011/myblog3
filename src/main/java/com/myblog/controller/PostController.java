package com.myblog.controller;

import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    //http://localhost:8080/api/posts
        @PreAuthorize("hasRole('ADMIN')")
       @PostMapping
        public ResponseEntity<?>CreatePost(@Valid @RequestBody PostDto postDto, BindingResult result){
        if (result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        PostDto dto = postService.createPost(postDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    //http://localhost:8080/api/posts
    //http://localhost:8080/api/posts?pageNo=0&pageSize=10&sortBy=title&sortDir=asc
    @GetMapping
        public PostResponse getAllPosts(
                @RequestParam(value = "pageNo", defaultValue = "0",required = false)int pageNo,
                @RequestParam(value = "pageSize", defaultValue = "10",required = false)int pageSize,
                @RequestParam(value = "sortBy", defaultValue = "id",required = false)String sortBy,
                @RequestParam(value = "sortDir", defaultValue = "0",required = false)String sortDir
        ){
           return postService.getAllPost(pageNo,pageSize,sortBy,sortDir);
         }
    //http://localhost:8080/api/posts/2
    @GetMapping("/{id}")
    public ResponseEntity<PostDto>getPostById(
            @PathVariable("id")long id){
        return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
    }

    //http://localhost:8080/api/posts/2
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("id")long id,
                                              @RequestBody PostDto postDto){
        PostDto postResponse = postService.updatePost(postDto, id);
        return ResponseEntity.ok(postResponse);
    }
    //http://localhost:8080/api/posts/2
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String>deletePostById(@PathVariable("id")long id){
        postService.deletePostById(id);
        return new ResponseEntity<String>("Post is deleted!!",HttpStatus.OK);
    }
    }
