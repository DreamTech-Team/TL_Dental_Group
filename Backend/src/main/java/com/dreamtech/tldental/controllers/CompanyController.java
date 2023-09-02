package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.*;
import com.dreamtech.tldental.repositories.CompanyRepository;
import com.dreamtech.tldental.repositories.ProductRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private IStorageService storageService;

    // GET ALL WITH FILTER
    @GetMapping("")
    public ResponseEntity<ResponseObject> getAll(@RequestParam(required = false) boolean highlight) {
        try {

            if (highlight) {
                List<Object[]> res = companyRepository.findHighlightCompany();
                List<Object> data = handleDataCompany(res);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query company successfully", data));
            } else {
                List<Company> data = companyRepository.findAll();
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query company successfully", data));
            }

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // GET DETAIL
    @GetMapping("/{slug}")
    public ResponseEntity<ResponseObject> getDetail(@PathVariable String slug) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query company successfully", companyRepository.findBySlug(slug)));
    }

    // CREATE COMPANY
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<ResponseObject> createCompany(@RequestParam("data") String data,
            @RequestPart("logo") MultipartFile logo) {
        try {
            // Convert String to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Company companyData = objectMapper.readValue(data, Company.class);

            // Check existed item
            List<Company> foundTags = companyRepository.findByName(companyData.getName().trim());
            if (foundTags.size() > 0) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name already taken", ""));
            }

            // Check name has "/" or "\"
            if (companyData.getName().contains("/") || companyData.getName().contains("/")) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Company's name should not have /", ""));
            }

            // Upload image to cloudinary
            String mainImgFileName = storageService.storeFile(logo);
            companyData.setLogo(mainImgFileName);
            companyData.setHighlight(0);
            companyData.setName(companyData.getName().trim());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert company successfully", companyRepository.save(companyData)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // DELETE
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCompany(@PathVariable String id) {
        try {
            Optional<Company> foundCompany = companyRepository.findById(id);

            if (foundCompany.isPresent()) {
                storageService.deleteFile(foundCompany.get().getLogo());

                companyRepository.deleteById(id);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Deleted company successfully", foundCompany));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find company with id = " + id, ""));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // UPDATE
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateCompany(@PathVariable String id,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestParam("data") String data) throws JsonProcessingException {
        // Convert String to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        Company companyData = objectMapper.readValue(data, Company.class);

        Optional<Company> foundCompany = companyRepository.findById(id);
        if (foundCompany.isPresent()) {
            String oldUrlLogo = foundCompany.get().getLogo();
            companyData.setCreateAt(foundCompany.get().getCreateAt());
            companyData.setLogo(oldUrlLogo);
            // Copy new data
            BeanUtils.copyProperties(companyData, foundCompany.get());

            // Update img
            if (logo != null && logo.getSize() != 0) {
                storageService.deleteFile(oldUrlLogo);
                // Upload image to cloudinary
                String mainImgFileName = storageService.storeFile(logo);
                foundCompany.get().setLogo(mainImgFileName);
            }

            Company resCompany = companyRepository.save(foundCompany.get());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update company successfully", resCompany));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find company with id = " + companyData.getId(), ""));
        }
    }

    // HIGHLIGHT
    // UPDATE HIGHLIGHT COMPANY
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/highlight/{slug}")
    ResponseEntity<ResponseObject> updateHighlightCompany(@PathVariable String slug,
            @RequestParam int highlight) {
        try {
            Company foundCompany = companyRepository.findBySlug(slug);
            if (foundCompany == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find company with slug = " + slug, ""));
            }
            if (highlight != 0 && foundCompany.getOutstandingProductId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Please update outstanding product before update highlight!", ""));
            } else {
                foundCompany.setHighlight(highlight);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Update highlight company successfully",
                                companyRepository.save(foundCompany)));
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    // UPDATE OUTSTANDING COMPANY
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/outstanding/{id}")
    ResponseEntity<ResponseObject> updateOutstandingCompany(@PathVariable String id,
            @RequestParam(value = "idProduct", required = false) String idProduct) {
        try {
            Optional<Company> foundCompany = companyRepository.findById(id);

            if (foundCompany == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find company with id = " + id, ""));
            }

            if (idProduct == null) {
                foundCompany.get().setHighlight(0);
                foundCompany.get().setOutstandingProductId(null);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Update outstanding company successfully",
                                companyRepository.save(foundCompany.get())));
            }

            Optional<Product> foundProduct = productRepository.findById(idProduct);
            if (!foundProduct.isPresent() || !foundCompany.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can not find product or company!", ""));
            } else if (foundProduct.get().getFkCategory().getCompanyId().getId() != foundCompany.get().getId()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "This product is not belong to " + foundCompany.get().getName(),
                                ""));
            }

            foundCompany.get().setHighlight(1);
            foundCompany.get().setOutstandingProductId(idProduct);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update outstanding company successfully",
                            companyRepository.save(foundCompany.get())));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    private List<Object> handleDataCompany(List<Object[]> srcList) {
        List<Object> combinedList = new ArrayList<>(); // Result
        Map<String, Object> tempObj = new HashMap<String, Object>();

        for (Object[] result : srcList) {
            tempObj.put("company", (Company) result[0]);
            tempObj.put("outstandingProduct", (Product) result[1]);
            combinedList.add(tempObj);
            tempObj = new HashMap<>();
        }
        return combinedList;
    }
}
