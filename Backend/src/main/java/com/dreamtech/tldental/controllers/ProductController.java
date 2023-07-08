package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.Product;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ProductRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.dreamtech.tldental.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private IStorageService storageService;

    // GET ALL WITH FILTER
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllProducts(@RequestParam("cate1") String cate1,
                                                  @RequestParam("cate2") String cate2,
                                                  @RequestParam(required = false, defaultValue = "12") String pageSize,
                                                  @RequestParam(required = false, defaultValue = "0") String page,
                                                  @RequestParam(required = false, defaultValue = "desc") String sort) {
        System.out.println(cate1);
        System.out.println(cate2);
        // HANDLE FILTER

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query product successfully", repository.findAll())
        );
    }

    // GET DETAIL
    @GetMapping("/{slug}")
    ResponseEntity<ResponseObject> findBySlug(@PathVariable String slug) {
        Optional<Product> foundProduct = Optional.ofNullable(repository.findBySlug(slug));
        return foundProduct.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query product successfully", foundProduct)
                ):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = "+slug, "")
                );
    }

    // UPDATE PRODUCT
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@PathVariable String id,
                                                 @RequestParam("imgs") List<MultipartFile> imgs,
                                                 @RequestParam(value = "mainImg", required = false) MultipartFile mainImg,
                                                 @RequestParam("data") String data,
                                                 @RequestParam("removeImgs") String removeImgs) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Product updatedProduct = objectMapper.readValue(data, Product.class);

            // Check existed item
            Optional<Product> foundProduct = repository.findById(id);
            if (foundProduct.isPresent()) {
                Product existingProduct = foundProduct.get();
                List<String> oldImgs = Utils.convertStringToImages(existingProduct.getImgs());

                if (updatedProduct.getName() != null) {
                    // Check name has "/" or "\"
                    if (updatedProduct.getName().contains("/") || updatedProduct.getName().contains("/")) {
                        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                                new ResponseObject("failed", "Product name should not have /", "")
                        );
                    }
                    existingProduct.setName(updatedProduct.getName().trim());
                }

                if (updatedProduct.getDescription() != null)
                    existingProduct.setDescription(updatedProduct.getDescription());
                if (updatedProduct.getSummary() != null)
                    existingProduct.setSummary(updatedProduct.getSummary());

                existingProduct.setHighlight(updatedProduct.getHighlight());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setPriceSale(updatedProduct.getPriceSale());

                // Check main image was changed
                if (mainImg != null && mainImg.getSize() != 0) {
                    storageService.deleteFile(existingProduct.getMainImg());
                    String mainImgFileName = storageService.storeFile(mainImg);
                    existingProduct.setMainImg(mainImgFileName);
                }

                // Remove images at old imgs
                List<String> imgsRemove = Utils.convertStringToImages(removeImgs);
                for (int i = 0; i < imgsRemove.size(); i++) {
                    int index = oldImgs.indexOf(imgsRemove.get(i));
                    if (index != -1) {
                        storageService.deleteFile(imgsRemove.get(i));
                        oldImgs.remove(index);
                    }
                }

                // Upload new imgs
                for (int i = 0; i < imgs.size(); i++) {
                    if (imgs.get(i).getSize() != 0) {
                        String fileName = storageService.storeFile(imgs.get(i));
                        oldImgs.add(fileName);
                    }
                }
                existingProduct.setImgs(oldImgs.toString());

                Product savedProduct = repository.save(existingProduct);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Updated product successfully", savedProduct)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find product", "")
                );
            }

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String id) {
        Optional<Product> foundProduct = repository.findById(id);

        if (foundProduct.isPresent()) {
            // Delete images on cloudinary
            if (foundProduct.get().getImgs().length() > 2) {
                List<String> imgs = Utils.convertStringToImages(foundProduct.get().getImgs());
                for (int i = 0; i < imgs.size(); i++) {
                    storageService.deleteFile(imgs.get(i));
                }
            }
            if (foundProduct.get().getMainImg().length() > 0) {
                storageService.deleteFile(foundProduct.get().getMainImg());
            }

            // Delete product on mySQL
            repository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted product successfully", foundProduct)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find product with id = "+id, "")
        );
    }

    // CREATE PRODUCT
    @PostMapping("")
    ResponseEntity<ResponseObject> createProduct(@RequestParam("imgs") List<MultipartFile> imgs,
                                                 @RequestParam("mainImg") MultipartFile mainImg,
                                                 @RequestParam ("data") String data){
        try {
            System.out.println(mainImg);
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(data, Product.class);

            // Check existed item
            List<Product> foundProducts = repository.findByName(product.getName().trim());
            if (foundProducts.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Product name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (product.getName().contains("/") || product.getName().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Product name should not have /", "")
                );
            }

            if (mainImg.getSize() == 0) {
                throw new RuntimeException("Main image is empty!");
            }
            String mainImgFileName = storageService.storeFile(mainImg);
            product.setMainImg(mainImgFileName);
            product.setName(product.getName().trim());

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
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }


    // GET ALL HIGHLIGHT PRODUCTS
    @GetMapping("/highlight")
    ResponseEntity<ResponseObject> getAllHighlight() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Updated product successfully", repository.getAllHighlight())
        );
    }

    // UPDATE HIGHLIGHT NEWS
    @PatchMapping("/highlight")
    ResponseEntity<ResponseObject> updateHighlight(@RequestBody ArrayList<Map<String, String>> data) {
        ArrayList<Object> res = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            System.out.println();
            Optional<Product> foundProduct = Optional.ofNullable(
                    repository.findBySlug(data.get(i).getOrDefault("slug", "none")));

            if (foundProduct.isPresent()) {
                Product existingProduct = foundProduct.get();
                existingProduct.setHighlight(Integer.parseInt(data.get(i).getOrDefault("highlight", "0")));

                Product savedProduct = repository.save(existingProduct);
                res.add(savedProduct);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Updated product successfully", res)
        );
    }
}
