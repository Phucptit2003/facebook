package org.example.services;


import org.example.models.Category;
import org.example.models.Post;
import org.example.repositories.CategoryRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        recommendationPosts.addAll(postRepository.findTop10ByInteractions());
        List<Category> categories = userRepository.findInteractedCategories(username);
        List<String> categoriesNames = categories.stream().map(Category::getName).collect(Collectors.toList());
        recommendationPosts.addAll(categoryRepository.findPostsByCategoryNames(categoriesNames));
        recommendationPosts.addAll(userRepository.findFriendPosts(username));
        return recommendationPosts.stream().distinct().collect(Collectors.toList());
    }

}
