package org.example.services;

import org.example.models.*;
import org.example.realtionship.CommentRelationship;
import org.example.realtionship.ShareRelationship;
import org.example.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class InteractService  {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public InteractService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void toggleLike(String username , Long postId) {
        boolean removed = postRepository.toggleLike(username, postId);
        if (!removed) {
            postRepository.likePost(username, postId);
            System.out.println("Liked");
        }
    }

    @Transactional
    public void commentPost(String username, Long postId, String content) {
        postRepository.commentPost(username, postId, content);
    }

    @Transactional
    public void sharePost(String username, Long postId) {
        postRepository.sharePost(username, postId);
    }
}