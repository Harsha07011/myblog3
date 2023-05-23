package com.myblog.service.impl;

import com.myblog.entity.Post;
import com.myblog.exception.ResourceNotFoundException;
import com.myblog.payload.PostDto;
import com.myblog.payload.PostResponse;
import com.myblog.repository.PostRepository;
import com.myblog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post newpost = postRepository.save(post);
        PostDto dto = new PostDto();
        dto.setId(newpost.getId());
        dto.setTitle(newpost.getTitle());
        dto.setContent(newpost.getContent());
        dto.setDescription(newpost.getDescription());
        return dto;
    }

    @Override
    public PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> content = postRepository.findAll(pageable);
        List<Post> posts = content.getContent();

        List<PostDto> postDtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
      PostResponse postResponse=new PostResponse();
      postResponse.setContent(postDtos);
      postResponse.setPageNo(content.getNumber());
      postResponse.setPageSize(content.getSize());
      postResponse.setTotalPages(content.getTotalPages());
      postResponse.setLast(content.isLast());
      return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post", "Id", id));
        postRepository.deleteById(id);
    }



//    private PostDto mapToDto(Post post) {
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setContent(post.getContent());
//        postDto.setDescription(post.getDescription());
//        return postDto;
//    }
//    private Post mapToEntity(PostDto postDto){
//        Post post=new Post();
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
//        post.setDescription(postDto.getDescription());
//        return post;
//    }
    private PostDto mapToDto(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
        return postDto;

    }
    private Post mapToEntity(PostDto dto) {
        Post post = mapper.map(dto, Post.class);
        return post;
    }
}