package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.Product;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ProductRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.h2.util.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private IStorageService storageService;

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

    @PatchMapping("/highlight/{slug}")
    ResponseEntity<ResponseObject> updateHighlight(@PathVariable String slug) {
        Optional<Product> foundProduct = Optional.ofNullable(repository.findBySlug(slug));

        if (foundProduct.isPresent()) {
            Product existingProduct = foundProduct.get();
            existingProduct.setHighlight(!existingProduct.isHighlight());

            Product savedProduct = repository.save(existingProduct);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Updated product successfully", savedProduct)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Cannot find product", "")
            );
        }
    }

    @PatchMapping("/{id}")
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
    ResponseEntity<ResponseObject> createProduct(@RequestParam("imgs") List<MultipartFile> imgs,
                                                 @RequestPart("mainImg") MultipartFile mainImg,
                                                 @RequestParam ("data") String data) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(data, Product.class);

        // Check existed item
        List<Product> foundProducts = repository.findByName(product.getName().trim());

        System.out.println(imgs.get(0).getOriginalFilename());
        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", "")
            );
        }

        String mainImgFileName = storageService.storeFile(mainImg);
        product.setMainImg(mainImgFileName);

        List<String> imgList = new ArrayList<>();
        for (int i = 0; i < imgs.size(); i++) {
            String fileName = storageService.storeFile(mainImg);
            imgList.add(fileName);
        }
        imgList.add(mainImgFileName);
        product.setImgs(imgList.toString());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert product successfully", repository.save(product))
        );
    }
}
