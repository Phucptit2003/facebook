package org.example.repositories;

import org.example.models.Category;
import org.example.models.Post;
import org.example.models.User;
import org.example.objects.FriendDTO;
import org.example.queryresults.PostQueryResult;
import org.example.services.UserService;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {


    Optional<User> findUserByUsername(String username);
    boolean existsByUsername(String username);

    @Query("MATCH (u:User {name:$username1}), (f:User {name:$username2})\n" +
            "OPTIONAL MATCH (u)-[fr:Friend]->(f), (f)-[fr1:Friend]->(u)\n" +
            "WITH u, f, collect(fr) as friendships1, collect(fr1) as friendships2\n" +
            "FOREACH (_ IN CASE WHEN size(friendships1) = 0 AND size(friendships2) = 0 THEN [1] ELSE [] END | \n" +
            "    CREATE (u)-[:Friend]->(f) \n" +
            "    CREATE (f)-[:Friend]->(u))\n" +
            "FOREACH (_ IN CASE WHEN size(friendships1) > 0 AND size(friendships2) > 0 THEN [1] ELSE [] END | \n" +
            "    DELETE friendships1[0], friendships2[0])\n")
    void addFriend(@Param("username1") String username1, @Param("username2") String username2);



    @Query("MATCH (u:User {username:$username})-[:Friend]->(f:User)\n" +
            "match (f)-[:createPost]->(p:Post)RETURN ID(p)")
    List<Long> findFriendPosts(@Param("username") String username);

    @Query("MATCH (u:User {username: $username})-[r:LIKED|COMMENTED|SHARED]->(p:Post)\n" +
            "OPTIONAL MATCH (p)<-[like:LIKED]-(:User)\n" +
            "OPTIONAL MATCH (p)<-[comment:COMMENTED]-(:User)\n" +
            "OPTIONAL MATCH (p)<-[share:SHARED]-(:User)\n" +
            "OPTIONAL MATCH (p)-[:HAS_CATEGORY]->(c:Category)\n" +
            "WITH p, COUNT(DISTINCT like) AS likeCount, COUNT(DISTINCT comment) AS commentCount, COUNT(DISTINCT share) AS shareCount, c.name AS categories \n" +
            "WHERE likeCount > 0 OR commentCount > 0 OR shareCount > 0\n" +
            "RETURN categories \n" +
            "ORDER BY (likeCount + commentCount + shareCount) DESC")
    List<String> findInteractedCategories(@Param("username") String username);


}
