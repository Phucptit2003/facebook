package org.example.repositories;

import org.example.controllers.UserController;
import org.example.models.Image;
import org.example.models.Post;
import org.example.queryresults.PostQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

//    @Query("MATCH (user:User {username: $username}), (post:Post ) " +" WHERE ID(post)=$postId "+
//            "CREATE (user)-[:createPost]->(post) Return user,post")
//    void createPostRelationship(@Param("username") String username,@Param("postId") Long postId);
        @Query("MATCH (user:User {username: $username}),(post:Post )"+"where ID(post)=$postId"+ " CREATE (user)-[:createPost ]->(post)"+
                "RETURN user, post")
        PostQueryResult createPostRelationship(@Param("username") String username,@Param("postId") Long postId);
}
