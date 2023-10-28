package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.services.IStorageService;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(path = "/api/v1/fileUpload")
public class FileUploadController {

    @Autowired
    private IStorageService storageService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String generatedFileName = storageService.storeFile(file);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Upload file successfully", generatedFileName));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", exception.getMessage(), ""));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteFile(@RequestParam String url) {
        try {
            boolean isDeleted = storageService.deleteFile(url);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete file successfully", isDeleted));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", exception.getMessage(), ""));
        }
    }
}
