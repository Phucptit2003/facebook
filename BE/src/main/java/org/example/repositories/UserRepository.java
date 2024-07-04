package org.example.repositories;

import org.example.models.Category;
import org.example.models.Post;
import org.example.models.User;
import org.example.queryresults.PostQueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);


    @Query("MATCH (u:User)-[:FRIEND]->(f:User)-[:LIKED|COMMENTED|SHARED]->(p:Post) WHERE u.username=$username RETURN p")
    List<Post> findFriendPosts(@Param("username") String username);

    @Query("MATCH (u:User)-[:LIKED|COMMENTED|SHARED]->(p:Post)-[:HAS_POST]->(c:Category) WHERE u.username=$username RETURN DISTINCT c")
    List<Category> findInteractedCategories(@Param("username") String username);


}
