package org.example.services;

//import org.example.models.Author;
import jakarta.transaction.Transactional;
import org.example.controllers.UserController;
import org.example.models.Image;
import org.example.models.Post;
import org.example.models.User;
import org.example.models.Video;
import org.example.queryresults.PostQueryResult;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
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
    private final UserService userService;
    private final UserRepository userRepository;
    private String username;


    @Autowired
    public PostService(PostRepository postRepository, UserController userController, UserService userService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userController = userController;
        this.userService=userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<Post> getAllPost(){
        List<Post> getAllPost=postRepository.getAllPost();


        for (Post post : getAllPost) {
            if(postRepository.getImage(post.getId())!=null) {
                String imageUrl = postRepository.getImage(post.getId());
                post.setImgUrl(imageUrl);
                System.out.println("Images: " + post.getImgUrl());
            };

             // Check if images are populated
        }
        return getAllPost;
    }
    @Transactional
    public Post createPost(String title, String content, MultipartFile imageFile, MultipartFile videoFile) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        if (imageFile != null) {
            Image image = saveImageFile(imageFile, "C:\\Users\\P R O B O O K\\Documents\\Phuc\\Facebook\\FE/FE/public/picture");
//            post.getImages().add(image);
            post.setImages(image);
        }

        if (videoFile != null) {
            Video video = saveVideoFile(videoFile, "videos");
            post.getVideos().add(video);
        }

        username=userController.username;
        post.setAuthor(username);
        Post post1= postRepository.save(post);
        Long postId=post1.getId();
         postRepository.createPostRelationship(username,postId);

        return post1;
    }

    private Image saveImageFile(MultipartFile file, String folder) throws IOException {
        // Ensure directory exists; create if not
        Path directoryPath = Paths.get(folder);
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Generate a safe and unique filename
        String originalFilename = file.getOriginalFilename();
        String safeFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // Construct full file path
        Path filePath = Paths.get(folder, safeFilename);

        // Copy file content to the target path
        Files.copy(file.getInputStream(), filePath);

        // Create Image object and set metadata
        Image image = new Image();
        image.setFilename(safeFilename);
        image.setContentType(file.getContentType());

        // Return saved Image object
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
