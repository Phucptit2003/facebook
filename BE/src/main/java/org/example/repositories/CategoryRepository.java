package org.example.repositories;

import org.example.models.Category;
import org.example.models.Post;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends Neo4jRepository<Category, Long> {
    @Query("MATCH (c:Category)-[:HAS_POST]->(p:Post) WHERE c.name IN $categoryNames RETURN p")
    List<Post> findPostsByCategoryNames(@Param("categoryNames") List<String> categoryNames);
}
