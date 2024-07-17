package org.example.services;


import ch.qos.logback.core.joran.sanity.Pair;
import org.example.models.Post;
import org.example.models.User;
import org.example.objects.SearchResultDTO;
import org.example.repositories.CategoryRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private final SearchRepository searchRepository;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final CategoryRepository categoryRepository;

    public SearchService(SearchRepository searchRepository, PostRepository postRepository,CategoryRepository categoryRepository) {
        this.searchRepository = searchRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }
    public List<Post> searchResult(String condition) {
        List<Post> posts= searchRepository.search(condition);
        for(Post post: posts) {
            post.setImgUrl(postRepository.getImage(post.getId()));
            post.setCategory(categoryRepository.getCategory(post.getId()));
        }
        return posts;
    }

}
