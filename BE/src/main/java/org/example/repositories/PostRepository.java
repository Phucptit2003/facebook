package org.example.repositories;

import org.example.controllers.UserController;
import org.example.models.Image;
import org.example.models.Post;
import org.example.queryresults.PostQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends Neo4jRepository<Post,Long> {
        @Query("MATCH (post:Post)\n" +
                "OPTIONAL MATCH (post)-[:HAS_IMAGE]->(image:Image)\n" +
                "OPTIONAL MATCH (post)-[:HAS_VIDEO]->(video:Video)\n" +
                "RETURN post, COLLECT(DISTINCT image) AS images, COLLECT(DISTINCT video) AS videos")
        List<Post> getAllPost();

        @Query("MATCH (post:Post)-[:HAS_IMAGE]-> (image:Image)\n" +
                "where ID(post)=$postId \n" +
                "return image.filename")
        String getImage(@Param("postId") Long postId);

        @Query("MATCH (p:Post ) where ID(p)=$postId \n" +
                "SET p.title = $title,\n" +
                "    p.content = $content\n" +
                "WITH p\n" +
                "OPTIONAL MATCH (p)-[r:HAS_IMAGE]->(oldImage:Image)\n" +
                "DELETE r, oldImage\n" +
                "WITH p\n" +
                "OPTIONAL MATCH (p)-[r:HAS_VIDEO]->(oldVideo:Video)\n" +
                "DELETE r, oldVideo\n" +
                "WITH p\n" +
                "FOREACH (ignoreMe IN CASE WHEN $imageFile IS NOT NULL THEN [1] ELSE [] END |\n" +
                "    CREATE (newImage:Image {url: $imageFile})\n" +
                "    CREATE (p)-[:HAS_IMAGE]->(newImage)\n" +
                ")\n" +
                "FOREACH (ignoreMe IN CASE WHEN $videoFile IS NOT NULL THEN [1] ELSE [] END |\n" +
                "    CREATE (newVideo:Video {url: $videoFile})\n" +
                "    CREATE (p)-[:HAS_VIDEO]->(newVideo)\n" +
                ")\n" +
                "RETURN p")
        Post updatePost(@Param("postId") Long postId, @Param("title") String title, @Param("content") String content, @Param("imageFile")MultipartFile imageFile, @Param("videoFile") MultipartFile videoFile);

        @Query("MATCH (u:User), (p:Post) WHERE u.username = $username AND ID(p) = $postId " +
                "MERGE (u)-[:LIKED {timestamp: datetime()}]->(p) " +
                "RETURN p")
        Post likePost(@Param("username") String username, @Param("postId") Long postId);

        @Query("MATCH (u:User)-[l:LIKED]->(p:Post) " +
                "WHERE u.username=$username AND ID(p) = $postId " +
                "DELETE l " +
                "RETURN CASE WHEN COUNT(l) > 0 THEN true ELSE false END AS removed")
        boolean toggleLike(@Param("username") String username, @Param("postId") Long postId);


        @Query("MATCH (u:User), (p:Post) WHERE u.username=$username AND ID(p) = $postId " +
                "MERGE (u)-[:COMMENTED {content: $content, timestamp: datetime()}]->(p) " +
                "RETURN p")
        Post commentPost(@Param("username") String username, @Param("postId") Long postId, @Param("content") String content);
        @Query("MATCH (u:User), (p:Post) WHERE u.username=$username AND ID(p) = $postId " +
                "MERGE (u)-[:SHARED {timestamp: datetime()}]->(p) " +
                "RETURN p")
        Post sharePost(@Param("username") String username, @Param("postId") Long postId);

        @Query("MATCH (p:Post),(u:User)\n" +
                "OPTIONAL MATCH (p)<-[l:LIKED]-(u)\n" +
                "OPTIONAL MATCH (p)<-[c:COMMENTED]-(u)\n" +
                "OPTIONAL MATCH (p)<-[s:SHARED]-(u)\n" +
                "OPTIONAL MATCH (p)-[:HAS_IMAGE]->(image:Image)\n" +
                "OPTIONAL MATCH (p)-[:HAS_VIDEO]->(video:Video)\n" +
                "WITH p, \n" +
                "     COUNT(DISTINCT l) AS likeCount, \n" +
                "     COUNT(DISTINCT c) AS commentCount, \n" +
                "     COUNT(DISTINCT s) AS shareCount, \n" +
                "     COLLECT(DISTINCT image) AS images, \n" +
                "     COLLECT(DISTINCT video) AS videos\n" +
                "RETURN p, images, videos\n" +
                "ORDER BY (likeCount + commentCount + shareCount) DESC\n" +
                "LIMIT 10\n")
        List<Post> findTop10ByInteractions();

        @Query("MATCH (user:User {username: $username}),(post:Post )"+"where ID(post)=$postId"+ " CREATE (user)-[:createPost ]->(post)"+
                "RETURN user, post")
        PostQueryResult createPostRelationship(@Param("username") String username,@Param("postId") Long postId);

//        @Query("match (p:Post)where id(p)=$postID\n" +
//                "optional match (p)-[:HAS_IMAGE]->(image:Image)\n" +
//                "optional match (p)-[:HAS_VIDEO]->(video:Video)\n" +
//                "return p,image")
//        List<Post> fi
}
