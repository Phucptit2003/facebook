package org.example.services;


import org.example.models.Category;
import org.example.models.Post;
import org.example.objects.PostDTO;
import org.example.objects.PostWithMedia;
import org.example.repositories.CategoryRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RecommendationService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Post> getRecommendationPosts(String username) {
        List<Post> recommendationPosts = new ArrayList<>();
        //tim kiem bai viết phổ biến theo số lượng interact
        List<Post> postsInteracts  = new ArrayList<>();
        postsInteracts=postRepository.findTop10ByInteractions();
        for (Post post : postsInteracts) {
            post.setImgUrl(postRepository.getImage(post.getId()));
            recommendationPosts.add(post);
        }                                                   
        //tìm kiếm các bài post có chung chủ đề
       List<String> categoriesNames = userRepository.findInteractedCategories(username);
        for (String categoryName : categoriesNames) {
            System.out.println(categoryName);
            List<Long> IDposts = categoryRepository.findPostsByCategoryNames(categoryName);
            for(Long IDpost : IDposts) {
                System.out.println(IDpost);
                Post post= postRepository.findById(IDpost).get();
                if(!recommendationPosts.contains(post)) {
                    recommendationPosts.add(post);
                    post.setImgUrl(postRepository.getImage(IDpost));
                }
            }
        }
        List<Long> postsFriend;
        postsFriend=userRepository.findFriendPosts(username);
        for (Long postID : postsFriend) {
            Post post = postRepository.findById(postID).get();
            if(!recommendationPosts.contains(post)) {
                post.setImgUrl(postRepository.getImage(post.getId()));
                recommendationPosts.add(post);
            }
        }
        return recommendationPosts.stream().distinct().collect(Collectors.toList());
    }

}
