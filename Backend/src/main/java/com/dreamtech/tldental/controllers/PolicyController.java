package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.Company;
import com.dreamtech.tldental.models.ContentPage;
import com.dreamtech.tldental.models.Policy;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.dreamtech.tldental.repositories.PolicyRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("/api/v1/policy")
public class PolicyController {
    @Autowired
    PolicyRepository repository;
    @Autowired
    private ContentPageRepository contentPageRepository;
    @Autowired
    private IStorageService storageService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll() {
        try {
            List<Policy> data = repository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query company successfully", data)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", exception.getMessage(), "")
            );
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query company successfully", repository.findBySlug(slug))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", exception.getMessage(), "")
            );
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createPolicy(@RequestParam ("data") String data,
                                                        @RequestPart("symbol") MultipartFile symbol) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Policy policyData = objectMapper.readValue(data, Policy.class);

            // Check existed item
            List<Policy> foundTags = repository.findByName(policyData.getName().trim());
            if (foundTags.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Policy's name already taken", "")
                );
            }

            // Check name has "/" or "\"
            if (policyData.getName().contains("/") || policyData.getName().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name should not have /", "")
                );
            }

            // Upload image to cloudinary
            String mainImgFileName = storageService.storeFile(symbol);
            policyData.setSymbol(mainImgFileName);
            policyData.setName(policyData.getName().trim());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert company successfully", repository.save(policyData))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deletePolicy(@PathVariable String id) {
        try {
            Optional<Policy> foundpolicy = repository.findById(id);

            if (foundpolicy.isPresent()) {
                storageService.deleteFile(foundpolicy.get().getSymbol());

                repository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Deleted policy successfully", foundpolicy)
                );
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find policy with id = " + id, "")
            );
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    // UPDATE
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateCompany(@PathVariable String id,
                                                 @RequestPart(value = "symbol", required = false) MultipartFile symbol,
                                                 @RequestParam ("data") String data) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Policy policyData = objectMapper.readValue(data, Policy.class);

        Optional<Policy> foundPolicy = repository.findById(id);
        if (foundPolicy.isPresent()) {
            String oldUrlLogo = foundPolicy.get().getSymbol();
            policyData.setCreateAt(foundPolicy.get().getCreateAt());
            policyData.setSymbol(oldUrlLogo);
            // Copy new data
            BeanUtils.copyProperties(policyData, foundPolicy.get());

            // Update img
            if (symbol != null && symbol.getSize() !=0) {
                storageService.deleteFile(oldUrlLogo);
                // Upload image to cloudinary
                String mainImgFileName = storageService.storeFile(symbol);
                foundPolicy.get().setSymbol(mainImgFileName);
            }


            Policy resNews = repository.save(foundPolicy.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update policy successfully", resNews)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find policy with id = " + policyData.getId(), "")
            );
        }
    }


    //  BANNER HEADER
    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get header successfully", contentPageRepository.findHomePageByTypeName("policy::header"))
        );
    }

    @PostMapping("/header")
    public ResponseEntity<ResponseObject> addHeader(@RequestParam("image") MultipartFile image) {
        Optional<ContentPage> foundHeader = contentPageRepository.findHomePageByTypeName("policy::header");

        if (foundHeader.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", "")
            );
        }

        String imageFile = storageService.storeFile(image);

        ContentPage entity = new ContentPage(null, "Chính sách", null, imageFile, null, "policy::header");

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

            if (header.getType().equals("policy::header")) {
                BeanUtils.copyProperties(entity, header);

                if (image != null) {
                    storageService.deleteFile(header.getImage());
                    header.setImage(storageService.storeFile(image));
                }

                header.setType("policy::header");

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Update header successfully", contentPageRepository.save(header))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Cannot found your data", "")
        );
    }
}
