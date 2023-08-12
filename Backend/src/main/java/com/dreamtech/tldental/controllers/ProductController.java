package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.*;
import com.dreamtech.tldental.repositories.CategoryFKRepository;
import com.dreamtech.tldental.repositories.CompanyRepository;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.dreamtech.tldental.repositories.ProductRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.dreamtech.tldental.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CategoryFKRepository categoryFKRepository;
    @Autowired
    private ContentPageRepository contentPageRepository;
    @Autowired
    private IStorageService storageService;

    private final Executor executor;

    @Autowired
    public ProductController() {
        this.executor = Executors.newFixedThreadPool(5);
    }

    // GET ALL WITH FILTER
    @GetMapping("")
    ResponseEntity<ResponseObject> getAllProducts(@RequestParam(value = "company", required = false) String company,
                                                  @RequestParam(value = "cate1", required = false) String cate1,
                                                  @RequestParam(value = "cate2", required = false) String cate2,
                                                  @RequestParam(value = "key", required = false) String key,
                                                  @RequestParam(required = false, defaultValue = "12") String pageSize,
                                                  @RequestParam(required = false, defaultValue = "0") String page,
                                                  @RequestParam(required = false) String sortPrice,
                                                  @RequestParam(required = false, defaultValue = "desc") String sort) {
        try {
            // HANDLE FILTER
            Sort sortBy = null;
            Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.Order orderByCreateAt = new Sort.Order(sortDirection, "createAt");

            if (sortPrice != null) {
                Sort.Direction sortDirectionPrice = sortPrice.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                Sort.Order orderByPrice = new Sort.Order(sortDirectionPrice, "price");
                sortBy = Sort.by(orderByPrice, orderByCreateAt);
            } else {
                sortBy = Sort.by(orderByCreateAt);
            }

            List<Object> newsList = repository.findFilteredProducts(key, company, cate1, cate2, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortBy));

            int total = newsList.size();

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query product successfully", new DataPageObject(total, page, pageSize, newsList))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    @GetMapping("/total")
    ResponseEntity<ResponseObject> getTotal(@RequestParam(value = "company", required = false) String company,
                                            @RequestParam(value = "cate1", required = false) String cate1,
                                            @RequestParam(value = "cate2", required = false) String cate2,
                                            @RequestParam(value = "key", required = false) String key) {
        List<Object> newsList = repository.findFilteredProducts(key, company, cate1, cate2, null);
        int total = newsList.size();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query total successfully", total)
        );
    }

    // SEARCH BY NAME OR COMPANY
    @GetMapping("/search")
    ResponseEntity<ResponseObject> searchProducts(@RequestParam(value = "key", required = false) String key,
                                                  @RequestParam(required = false, defaultValue = "12") String pageSize,
                                                  @RequestParam(required = false, defaultValue = "0") String page,
                                                  @RequestParam(required = false, defaultValue = "desc") String sort) {
        try {
            // HANDLE FILTER
            Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sortByCreateAt = Sort.by(sortDirection, "createAt");

            List<Object> newsList = repository.searchProductsByNameOrCompany(key, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));

            int total = newsList.size();

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query product successfully", new DataPageObject(total, page, pageSize, newsList))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
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
                                                 @RequestParam(value = "imgs", required = false) List<MultipartFile> imgs,
                                                 @RequestParam(value = "mainImg", required = false) MultipartFile mainImg,
                                                 @RequestParam("data") String data,
                                                 @RequestParam(value = "removeImgs", required = false) List<String> removeImgs) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            ProductData updatedProduct = objectMapper.readValue(data, ProductData.class);

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

                if (updatedProduct.getFkCategory() != null) {
                    CategoryFK categoryFK = categoryFKRepository.findById(updatedProduct.getFkCategory()).orElseGet(null);
                    existingProduct.setFkCategory(categoryFK);
                }

                if (updatedProduct.getDescription() != null)
                    existingProduct.setDescription(updatedProduct.getDescription());
                if (updatedProduct.getSummary() != null)
                    existingProduct.setSummary(updatedProduct.getSummary());

                existingProduct.setHighlight(updatedProduct.getHighlight());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setPriceSale(updatedProduct.getPriceSale());

                // Thực hiện các công việc trong đa luồng
                CompletableFuture<Void> mainImageFuture = CompletableFuture.runAsync(() -> {
                    // Check main image was changed
                    if (mainImg != null && mainImg.getSize() != 0) {
                        String oldMainImgUrl = existingProduct.getMainImg();
                        int index = oldImgs.indexOf(oldMainImgUrl);
                        if (index != -1) {
                            oldImgs.remove(index);
                        }

                        storageService.deleteFile(oldMainImgUrl);
                        String mainImgFileName = storageService.storeFile(mainImg);
                        existingProduct.setMainImg(mainImgFileName);
                        oldImgs.add(0, mainImgFileName);
                    }
                }, executor);

                CompletableFuture<Void> removeImagesFuture = CompletableFuture.runAsync(() -> {
                    if (removeImgs != null) {
                        // Remove images at old imgs
                        for (int i = 0; i < removeImgs.size(); i++) {
                            int index = oldImgs.indexOf(removeImgs.get(i));
                            if (index != -1) {
                                storageService.deleteFile(removeImgs.get(i));
                                oldImgs.remove(index);
                            }
                        }
                    }
                }, executor);

                List<CompletableFuture<Void>> uploadImageFutures = new ArrayList<>();
                if (imgs != null) {
                    // Upload new imgs
                    for (int i = 0; i < imgs.size(); i++) {
                        final int index = i;
                        CompletableFuture<Void> uploadImageFuture = CompletableFuture.runAsync(() -> {
                            if (imgs.get(index).getSize() != 0) {
                                String fileName = storageService.storeFile(imgs.get(index));
                                oldImgs.add(fileName);
                            }
                        }, executor);
                        uploadImageFutures.add(uploadImageFuture);
                    }
                }

                // Đợi tất cả các luồng đa nhiệm hoàn thành
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                        mainImageFuture,
                        removeImagesFuture,
                        CompletableFuture.allOf(uploadImageFutures.toArray(new CompletableFuture[0]))
                );
                allFutures.join();

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
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable String id) {
        try {
            Optional<Product> foundProduct = repository.findById(id);

            if (foundProduct.isPresent()) {
                Company company = companyRepository.findByOutstandingProductId(foundProduct.get().getId());
                if (company != null) {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("failed", "Failed to delete product. Exist a company has this product!", "")
                    );
                }

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
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // CREATE PRODUCT
    @PostMapping("")
    ResponseEntity<ResponseObject> createProduct(@RequestParam(value = "imgs", required = true) List<MultipartFile> imgs,
                                                 @RequestParam(value = "mainImg", required = true) MultipartFile mainImg,
                                                 @RequestParam (value = "data", required = true) String data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductData obj = objectMapper.readValue(data, ProductData.class);

            CategoryFK categoryFK = categoryFKRepository.findById(obj.getFkCategory()).orElseGet(null);
            Product product = new Product(obj.getName(), obj.getPrice(), obj.getPriceSale(), obj.getSummary(), obj.getDescription(), categoryFK);

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

            List<CompletableFuture<String>> futures = new ArrayList<>();
            // Thực hiện đa luồng cho việc tải lên ảnh mới
            for (int i = 0; i < imgs.size(); i++) {
                int index = i;
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                    String fileName = storageService.storeFile(imgs.get(index));
                    return fileName;
                }, executor);
                futures.add(future);
            }
            // Đợi tất cả các luồng đa nhiệm hoàn thành và lấy kết quả
            List<String> imgList = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());

            imgList.add(0, mainImgFileName);
            product.setImgs(imgList.toString());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert product successfully", repository.save(product))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
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


    //  BANNER HEADER
    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get header successfully", contentPageRepository.findHomePageByTypeName("product_list::header"))
        );
    }

    @PostMapping("/header")
    public ResponseEntity<ResponseObject> addHeader(@RequestParam("image") MultipartFile image) {
        Optional<ContentPage> foundHeader = contentPageRepository.findHomePageByTypeName("product_list::header");

        if (foundHeader.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", "")
            );
        }

        String imageFile = storageService.storeFile(image);

        ContentPage entity = new ContentPage(null, "Chính sách", null, imageFile, null, "product_list::header");

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add header successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping("/header")
    public ResponseEntity<ResponseObject> updateHeader(@RequestParam("data") String data, @RequestParam(name = "image", required = false) MultipartFile image) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        Optional<ContentPage> foundHeader = contentPageRepository.findById(entity.getId());

        if (foundHeader.isPresent()) {
            ContentPage header = foundHeader.get();

            if (header.getType().equals("product_list::header")) {
                BeanUtils.copyProperties(entity, header);

                if (image != null) {
                    storageService.deleteFile(header.getImage());
                    header.setImage(storageService.storeFile(image));
                }

                header.setType("product_list::header");

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Update header successfully", contentPageRepository.save(header))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Cannot found your data", "")
        );
    }
    

    private static class ProductData {
        private String id;
        private String name;
        private String slug;
        private int price;
        private int priceSale;
        private String summary;
        private String description;
        private String mainImg;
        private String imgs;
        private int highlight;
        private String createAt;
        private String fkCategory;

        public ProductData() {
        }

        public ProductData(String id, String name, String slug, int price, int priceSale, String summary, String description, String mainImg, String imgs, int highlight, String createAt, String fkCategory) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.price = price;
            this.priceSale = priceSale;
            this.summary = summary;
            this.description = description;
            this.mainImg = mainImg;
            this.imgs = imgs;
            this.highlight = highlight;
            this.createAt = createAt;
            this.fkCategory = fkCategory;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getMainImg() {
            return mainImg;
        }

        public void setMainImg(String mainImg) {
            this.mainImg = mainImg;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPriceSale() {
            return priceSale;
        }

        public void setPriceSale(int priceSale) {
            this.priceSale = priceSale;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getHighlight() {
            return highlight;
        }

        public void setHighlight(int highlight) {
            this.highlight = highlight;
        }

        public String getFkCategory() {
            return fkCategory;
        }

        public void setFkCategory(String fkCategory) {
            this.fkCategory = fkCategory;
        }
    }
}
