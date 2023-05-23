package com.myblog.controller;

import com.myblog.payload.CommentDto;
import com.myblog.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable(value = "postId") long postId,
            @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(postId,
                commentDto), HttpStatus.CREATED);
    }
    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto>getCommentByPostId(
            @PathVariable(value = "postId")long postId){
    return commentService.getCommentsByPostId(postId);
    }
    //http://localhost:8080/api/posts/1/comments
    @GetMapping("/posts/{postId}/comments/{Id}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable(value = "postId")long postId,
            @PathVariable(value = "Id")long commentId
    ){
        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto,HttpStatus.OK);
    }
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> UpdateComment(
            @PathVariable(value = "postId")long postId,
            @PathVariable(value = "id")long commentId,
            @RequestBody CommentDto commentDto){
        CommentDto updateComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updateComment,HttpStatus.OK);
    }
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId")long postId,
                                                @PathVariable(value = "id")long commentId
                                                ){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment Deleted Success",HttpStatus.OK);
    }
}