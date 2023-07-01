package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByName(String name);
    Product findBySlug(String name);

    @Query("SELECT p FROM Product p WHERE p.highlight <> 0")
    List<Product> getAllHighlight();
}

