package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.News;
import com.dreamtech.tldental.models.Tags;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository_V2 extends JpaRepository<News, String> {
    List<News> findByTitle(String title);
    News findBySlug(String name);
    List<News> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    List<News> findByTitleContainingIgnoreCaseAndTagsSlugIn(String keyword, List<String> tagSlugs, Pageable pageable);
    Long countByTitleContainingIgnoreCaseAndTagsSlugIn(String title, List<String> tagSlugs);
    Long countByTitleContainingIgnoreCase(String title);
    @Query("SELECT n FROM News n WHERE n.highlight <> 0")
    List<Object[]> findHighlightNews();

    @Query("SELECT n FROM News n WHERE EXTRACT(MONTH FROM n.createAt) = :month")
    List<Object[]> findNewsByMonth(@Param("month") int month);
}
