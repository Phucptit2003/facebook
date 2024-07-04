package org.example.controllers;

//import org.example.models.Author;
import org.example.models.Image;
import org.example.models.LikeRequest;
import org.example.models.Post;
import org.example.models.User;
import org.example.objects.PostDTO;
import org.example.repositories.PostRepository;
import org.example.services.PostService;
import org.example.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final RecommendationService recommendationService;

    @Autowired
    public PostController(PostService postService, PostRepository postRepository, RecommendationService recommendationService) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPost(){
        return postService.getAllPost();
    }

    @GetMapping("/edit/{id}")
    public Post getPostById(@PathVariable Long id){
        return postService.findPostById(id);
    }

    @PostMapping("/edit/{id}")
    public Post updatePost(@PathVariable Long id,@RequestBody PostDTO postDTO) throws IOException {
        return postService.editPost(id, postDTO.getTitle(), postDTO.getContent(), postDTO.getImageFile(), postDTO.getVideoFile());
    }
    @PostMapping
    public Post createPost(

                            @RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           @RequestParam(value = "videoFile", required = false) MultipartFile videoFile) throws IOException {

        return postService.createPost(title, content, imageFile, videoFile);
    }

    @GetMapping("/recommend/{username}")
    public List<Post> getRecommendationPostByUsername(@PathVariable String username){
        List<Post> recommend= recommendationService.getRecommendationPosts(username);
        return recommend;
    }
}
