package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.Product;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/Products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query product successfully", repository.findAll())
        );
    }

    @GetMapping("/{slug}")
    ResponseEntity<ResponseObject> findById(@PathVariable String slug) {
        Optional<Product> foundProduct = Optional.ofNullable(repository.findBySlug(slug));
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = "+slug, "")
                );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        Optional<Product> foundProduct = repository.findById(id);

        if (foundProduct.isPresent()) {
            Product existingProduct = foundProduct.get();

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setHighlight(updatedProduct.isHighlight());
            existingProduct.setImgs(updatedProduct.getImgs());
            existingProduct.setMainImg(updatedProduct.getMainImg());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setPriceSale(updatedProduct.getPriceSale());
            existingProduct.setSummary(updatedProduct.getSummary());

            try {
                Product savedProduct = repository.save(existingProduct);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Updated product successfully", savedProduct)
                );
            } catch (Exception exception) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Product's name existed", "")
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Cannot find product", "")
            );
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String id) {
        Optional<Product> foundProduct = repository.findById(id);
        repository.deleteById(id);
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Deleted product successfully", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = "+id, "")
                );
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        List<Product> foundProducts = repository.findByName(newProduct.getName().trim());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert product successfully", repository.save(newProduct))
        );
    }
}
