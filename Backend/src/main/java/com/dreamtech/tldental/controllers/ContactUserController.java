package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.ContactUser;
import com.dreamtech.tldental.models.DataPageObject;
import com.dreamtech.tldental.models.News;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContactUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/contact")
public class ContactUserController {
    @Autowired
    ContactUserRepository contactUserRepository;

    @GetMapping("")
    ResponseEntity<ResponseObject> getFilter(@RequestParam(value = "key", required = false, defaultValue = "") String key,
                                             @RequestParam(required = false, defaultValue = "12") String pageSize,
                                             @RequestParam(required = false, defaultValue = "0") String page,
                                             @RequestParam(required = false, defaultValue = "desc") String sort) {
        Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortByCreateAt = Sort.by(sortDirection, "createAt");

        List<ContactUser> contactList = contactUserRepository.findByFullnameContainingIgnoreCase(key, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query contact user list successfully", new DataPageObject(contactList.size(), page, pageSize, contactList))
        );
    }

    @PostMapping("")
    ResponseEntity<ResponseObject> createContact(@RequestBody ContactUser data) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Post contact user successfully", contactUserRepository.save(data))
        );
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PatchMapping("/{id}")
    ResponseEntity<ResponseObject> updateContacted(@PathVariable String id,
                                                       @RequestParam boolean contacted) {
        ContactUser foundContact = contactUserRepository.findById(id).orElse(null);
        if (foundContact != null) {
            foundContact.setContacted(contacted);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update highlight news successfully", contactUserRepository.save(foundContact))
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can not find product with id = "+id, "")
        );
    }
}
