package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByName(String name);
    Product findBySlug(String name);
}

