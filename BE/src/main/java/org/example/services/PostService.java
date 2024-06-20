package org.example.services;

//import org.example.models.Author;
import org.example.controllers.UserController;
import org.example.models.Image;
import org.example.models.Post;
import org.example.models.User;
import org.example.models.Video;
import org.example.queryresults.PostQueryResult;
import org.example.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserController userController;
    private String username;


    @Autowired
    public PostService(PostRepository postRepository, UserController userController) {
        this.postRepository = postRepository;
        this.userController = userController;
    }

    public List<Post> getAllPost(){
        return postRepository.getAllPost();
    }

    public Post createPost(String title, String content, MultipartFile imageFile, MultipartFile videoFile) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        if (imageFile != null) {
            Image image = saveImageFile(imageFile, "images");
            post.getImages().add(image);
        }

        if (videoFile != null) {
            Video video = saveVideoFile(videoFile, "videos");
            post.getVideos().add(video);
        }

        username=userController.username;

        Post post1= postRepository.save(post);
        Long postId=post1.getId();
        System.out.println(username+" "+post.getContent()+" "+postId);
         postRepository.createPostRelationship(username,postId);
        return post1;
    }

    private Image saveImageFile(MultipartFile file, String folder) throws IOException {
        // Tạo đường dẫn thư mục nếu nó chưa tồn tại
        Path directoryPath = Paths.get(folder);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Tạo tên tệp an toàn
        String originalFilename = file.getOriginalFilename();
        String safeFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // Đường dẫn đầy đủ đến tệp
        Path filePath = Paths.get(folder, safeFilename);

        // Sao chép tệp từ MultipartFile vào đường dẫn
        Files.copy(file.getInputStream(), filePath);

        // Tạo đối tượng Video và lưu thông tin
        Image image = new Image();
        image.setFilename(safeFilename);
        image.setContentType(file.getContentType());

        // Lưu video vào hệ thống hoặc cơ sở dữ liệu

        return image;
    }

    private Video saveVideoFile(MultipartFile file, String folder) throws IOException {
        // Tạo đường dẫn thư mục nếu nó chưa tồn tại
        Path directoryPath = Paths.get(folder);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Tạo tên tệp an toàn
        String originalFilename = file.getOriginalFilename();
        String safeFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // Đường dẫn đầy đủ đến tệp
        Path filePath = Paths.get(folder, safeFilename);

        // Sao chép tệp từ MultipartFile vào đường dẫn
        Files.copy(file.getInputStream(), filePath);

        // Tạo đối tượng Video và lưu thông tin
        Video video = new Video();
        video.setFilename(safeFilename);
        video.setContentType(file.getContentType());

        // Lưu video vào hệ thống hoặc cơ sở dữ liệu

        return video;
    }

}
