package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.News;
import com.dreamtech.tldental.models.Product;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.NewsRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    @Autowired
    private NewsRepository repository;
    @Autowired
    private IStorageService storageService;


    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String id) {
        Optional<News> foundNews = repository.findById(id);
//        repository.deleteById(id);
        System.out.println(foundNews.isPresent());

        if (foundNews.isPresent()) {


            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted news successfully", foundNews)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find news with id = "+id, "")
        );
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> createProduct(@RequestPart("img") MultipartFile img,
                                                 @RequestParam ("data") String data) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            News news = objectMapper.readValue(data, News.class);

            // Check existed item
            List<News> foundNews = repository.findByTitle(news.getTitle().trim());
            if (foundNews.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "News's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (news.getTitle().contains("/") || news.getTitle().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "News's name should not have /", "")
                );
            }

            String mainImgFileName = storageService.storeFile(img);
            news.setImg(mainImgFileName);
            news.setTitle(news.getTitle().trim());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert news successfully", repository.save(news))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }
}
