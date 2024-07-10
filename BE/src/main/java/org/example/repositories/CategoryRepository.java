package org.example.repositories;

import org.example.models.Category;
import org.example.models.Post;
import org.example.objects.PostDTO;
import org.example.objects.PostWithMedia;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends Neo4jRepository<Category, Long> {
    Category findByName(String name);
    @Query("MATCH (c:Category)<-[:HAS_CATEGORY]-(p:Post)\n" +
            "WHERE c.name = $categoryName\n" +
            "OPTIONAL MATCH (p)-[:HAS_IMAGE]->(image:Image)\n" +
            "OPTIONAL MATCH (p)-[:HAS_VIDEO]->(video:Video)\n" +
            "RETURN ID(p)")
    List<Long> findPostsByCategoryNames(@Param("categoryName") String categoryName);
}
