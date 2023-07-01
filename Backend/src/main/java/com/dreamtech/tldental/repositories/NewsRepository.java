package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, String> {
    List<News> findByTitle(String title);
    News findBySlug(String name);
}
