package org.example.repositories;

import ch.qos.logback.core.joran.sanity.Pair;
import org.example.models.Post;
import org.example.models.User;
import org.example.objects.SearchResultDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface SearchRepository extends Neo4jRepository<Post,Long> {
    @Query("MATCH (p:Post)<-[:createPost]-(u:User)\n" +
            "WHERE p.title CONTAINS $condition OR p.content CONTAINS $condition OR u.username CONTAINS $condition \n" +
            "RETURN p \n")
    List<Post> search(@Param("condition") String condition);
}
