package org.example.models;


import lombok.Getter;
import org.example.realtionship.CommentRelationship;
import org.example.realtionship.ShareRelationship;
import org.example.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Node
public class Post {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String author;
    private String imgUrl;
    private int interactions;
    @Relationship(type = "HAS_LIKE", direction = Relationship.Direction.INCOMING)
    private List<User> likedByUsers;

    @Relationship(type = "HAS_COMMENT", direction = Relationship.Direction.INCOMING)
    private List<CommentRelationship> comments;

    @Relationship(type = "HAS_SHARE", direction = Relationship.Direction.INCOMING)
    private List<ShareRelationship> sharedByUsers;
    @Relationship(type = "HAS_POST", direction = Relationship.Direction.INCOMING)
    private Category category;


    public List<User> getLikedByUsers() {
        return likedByUsers;
    }

    public void setLikedByUsers(List<User> likedByUsers) {
        this.likedByUsers = likedByUsers;
    }

    public void setComments(List<CommentRelationship> comments) {
        this.comments = comments;
    }

    public List<ShareRelationship> getSharedByUsers() {
        return sharedByUsers;
    }

    public void setSharedByUsers(List<ShareRelationship> sharedByUsers) {
        this.sharedByUsers = sharedByUsers;
    }


    public int getInteractions() {
        return interactions;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }



    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Relationship(type = "HAS_IMAGE", direction = Relationship.Direction.OUTGOING)
    private Image images = new Image();

    @Relationship(type = "HAS_VIDEO", direction = Relationship.Direction.OUTGOING)
    private List<Video> videos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Relationship(type = "createPost", direction = Relationship.Direction.INCOMING)
    private User creator;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

}

