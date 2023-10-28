package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.*;
import com.dreamtech.tldental.repositories.*;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/news")
public class NewsController_V2 {
    @Autowired
    private NewsRepository_V2 repository;
    @Autowired
    private TagsRepository tagsRepository;
    @Autowired
    private ContentPageRepository contentPageRepository;
    @Autowired
    private IStorageService storageService;

    private final Executor executor;

    @Autowired
    public NewsController_V2() {
        this.executor = Executors.newFixedThreadPool(5);
    }

    // GET ALL NEWS WITH FILTER
    @GetMapping("")
    ResponseEntity<ResponseObject> getFilter(
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @RequestParam(value = "outstanding", required = false) boolean outstanding,
            @RequestParam(value = "popular", required = false) boolean popular,
            @RequestParam(required = false, defaultValue = "12") String pageSize,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false) List<String> filterTags,
            @RequestParam(required = false, defaultValue = "desc") String sort) {
        Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortByCreateAt = Sort.by(sortDirection, "createAt");

        List<News> newsList = null;
        if (filterTags == null) {
            newsList = repository.findByTitleContainingIgnoreCase(key, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));
        } else {
            newsList = repository.findByTitleContainingIgnoreCaseAndTagsSlugIn(key, filterTags, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));
        }

        if (outstanding == true) {
            newsList = newsList.stream()
                    .filter(news -> news.getHighlight() == 1)
                    .collect(Collectors.toList());
        }
        if (popular == true) {
            newsList = newsList.stream()
                    .filter(news -> news.getTags().size() > 1)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query news successfully",
                        new DataPageObject(newsList.size(), page, pageSize, newsList)));
    }

    @GetMapping("/total")
    ResponseEntity<ResponseObject> getTotal(
            @RequestParam(value = "key", required = false, defaultValue = "") String key,
            @RequestParam(required = false) List<String> filterTags) {
        Long total = 0L;
        if (filterTags == null) {
            total = repository.countByTitleContainingIgnoreCase(key);
        } else {
            total = repository.countByTitleContainingIgnoreCaseAndTagsSlugIn(key, filterTags);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query total successfully", total));
    }

    // GET NEWS WITH FILTER BY MONTH
    @GetMapping("/month")
    ResponseEntity<ResponseObject> getNewsByMonth(@RequestParam int month) {
        List<Object[]> foundNews = repository.findNewsByMonth(month);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get news by month (" + month + ") successfully", foundNews));
    }

    // GET DETAIL NEWS
    @GetMapping("/{slug}")
    ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        Optional<News> foundNews = Optional.ofNullable(repository.findBySlug(slug));
        return foundNews.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query news successfully", foundNews))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = " + slug, ""));
    }

    // CREATE NEWS
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PostMapping("")
    ResponseEntity<ResponseObject> createNews(@RequestPart("img") MultipartFile img,
            @RequestParam("data") String data,
            @RequestParam(value = "tags", required = false) List<String> tags) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            News newsData = objectMapper.readValue(data, News.class);

            // Check existed item
            List<News> foundNews = repository.findByTitle(newsData.getTitle().trim());
            if (foundNews.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "News's name already taken", ""));
            }

            // Check name has "/" or "\"
            if (newsData.getTitle().contains("/") || newsData.getTitle().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "News's name should not have /", ""));
            }

            // Upload image to cloudinary
            CompletableFuture<Void> futureMainImg = CompletableFuture.runAsync(() -> {
                String mainImgFileName = storageService.storeFile(img);
                newsData.setImg(mainImgFileName);
            }, executor);
            newsData.setTitle(newsData.getTitle().trim());

            // Create FK_TAGS_NEWS
            CompletableFuture<Void>[] futures = new CompletableFuture[tags.size()];
            for (int i = 0; i < tags.size(); i++) {
                int index = i;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    Tags tag = tagsRepository.findById(tags.get(index)).orElse(null);
                    newsData.getTags().add(tag);
                }, executor);
                futures[i] = future;
            }
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);

            allFutures.join();
            futureMainImg.join();

            // Create News
            News resNews = repository.save(newsData);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert news successfully", resNews));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // UPDATE NEWS
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateNews(@PathVariable String id,
            @RequestPart(value = "img", required = false) MultipartFile img,
            @RequestParam("data") String data,
            @RequestParam(value = "tags", required = false) List<String> tags) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        News newsData = objectMapper.readValue(data, News.class);

        Optional<News> foundNews = repository.findById(id);
        if (foundNews.isPresent()) {
            String oldUrlImg = foundNews.get().getImg();
            newsData.setCreateAt(foundNews.get().getCreateAt());
            // Copy new data
            BeanUtils.copyProperties(newsData, foundNews.get());

            // Update FK_NEWS_TAGS if it was changed
            CompletableFuture<Void>[] futures = new CompletableFuture[tags.size()];
            if (tags.size() > 0) {
                newsData.getTags().clear();
                // Create FK_TAGS_NEWS
                for (int i = 0; i < tags.size(); i++) {
                    int index = i;
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        Tags tag = tagsRepository.findById(tags.get(index)).orElse(null);
                        newsData.getTags().add(tag);
                    }, executor);
                    futures[i] = future;
                }
            }
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);

            // Update img
            if (img != null && img.getSize() != 0) {
                CompletableFuture<Void> futureDeleteMainImg = CompletableFuture.runAsync(() -> {
                    storageService.deleteFile(oldUrlImg);
                }, executor);

                // Upload image to cloudinary
                CompletableFuture<Void> futureMainImg = CompletableFuture.runAsync(() -> {
                    String mainImgFileName = storageService.storeFile(img);
                    foundNews.get().setImg(mainImgFileName);
                }, executor);

                futureMainImg.join();
                futureDeleteMainImg.join();
            }

            allFutures.join();

            News resNews = repository.save(foundNews.get());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update news successfully", resNews));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find news with id = " + newsData.getId(), ""));
        }
    }

    // DELETE NEWS
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteNews(@PathVariable String id) {
        Optional<News> foundNews = repository.findById(id);

        if (foundNews.isPresent()) {
            storageService.deleteFile(foundNews.get().getImg());

            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Deleted news successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find news with id = " + id, ""));
    }

    // HIGHLIGHT //
    // GET ALL HIGHLIGHT NEWS
    @GetMapping("/highlight")
    ResponseEntity<ResponseObject> getHighlightNews() {
        List<News> foundNews = repository.findHighlightNews();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get all highlight news successfully", foundNews));
    }

    // UPDATE HIGHLIGHT NEWS
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PatchMapping("/highlight/{slug}")
    ResponseEntity<ResponseObject> updateHighlightNews(@PathVariable String slug,
            @RequestParam int highlight) {
        Optional<News> foundNews = Optional.ofNullable(repository.findBySlug(slug));
        foundNews.get().setHighlight(highlight);
        return foundNews.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update highlight news successfully", repository.save(foundNews.get())))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product with id = " + slug, ""));
    }

    // BANNER HEADER
    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get header successfully",
                        contentPageRepository.findHomePageByTypeName("news_list::header")));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PostMapping("/header")
    public ResponseEntity<ResponseObject> addHeader(@RequestParam("image") MultipartFile image) {
        Optional<ContentPage> foundHeader = contentPageRepository.findHomePageByTypeName("news_list::header");

        if (foundHeader.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", ""));
        }

        String imageFile = storageService.storeFile(image);

        ContentPage entity = new ContentPage(null, "Danh sách tin tức", null, imageFile, null, "news_list::header");

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add header successfully", contentPageRepository.save(entity)));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PatchMapping("/header")
    public ResponseEntity<ResponseObject> updateHeader(@RequestParam("data") String data,
            @RequestParam(name = "image", required = false) MultipartFile image)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        Optional<ContentPage> foundHeader = contentPageRepository.findById(entity.getId());

        if (foundHeader.isPresent()) {
            ContentPage header = foundHeader.get();

            if (header.getType().equals("news_list::header")) {
                BeanUtils.copyProperties(entity, header);

                if (image != null) {
                    storageService.deleteFile(header.getImage());
                    header.setImage(storageService.storeFile(image));
                }

                header.setType("news_list::header");

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Update header successfully", contentPageRepository.save(header)));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Cannot found your data", ""));
    }
}
