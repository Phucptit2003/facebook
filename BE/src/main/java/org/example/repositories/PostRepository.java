package org.example.repositories;

import org.example.controllers.UserController;
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
        @Query("MATCH (post:Post) return post")
        List<Post> getAllPost();
//    @Query("MATCH (user:User {username: $username}), (post:Post ) " +" WHERE ID(post)=$postId "+
//            "CREATE (user)-[:createPost]->(post) Return user,post")
//    void createPostRelationship(@Param("username") String username,@Param("postId") Long postId);
        @Query("MATCH (user:User {username: $username}),(post:Post )"+"where ID(post)=$postId"+ " CREATE (user)-[:createPost ]->(post)"+
                "RETURN user, post")
        PostQueryResult createPostRelationship(@Param("username") String username,@Param("postId") Long postId);
}
