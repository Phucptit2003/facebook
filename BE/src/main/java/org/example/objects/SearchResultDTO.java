package org.example.objects;

import org.example.models.Post;
import org.example.models.User;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.web.bind.annotation.GetMapping;

public class SearchResultDTO {
    @Id
    @GeneratedValue
    private  Long id;
    private User user;
    private Post post;

    public SearchResultDTO(Post post, User user) {
        this.user = user;
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
