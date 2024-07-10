package org.example.objects;

import org.example.models.Image;
import org.example.models.Post;
import org.example.models.Video;
import org.springframework.data.neo4j.core.schema.Node;
import java.util.List;

public class PostWithMedia {
    private Post post;
    private List<Image> images;
    private List<Video> videos;

    // Getters and setters
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}

