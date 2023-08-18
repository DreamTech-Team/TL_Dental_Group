package com.dreamtech.tldental.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtech.tldental.models.ContentPage;
import com.dreamtech.tldental.models.Facility;
import com.dreamtech.tldental.models.RecruitSection1;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.dreamtech.tldental.repositories.FacilityRepository;
import com.dreamtech.tldental.repositories.RecruitmentRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/facility")
public class FacilityController {
    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private IStorageService storageService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<ResponseObject> getFacility() {
        List<Facility> facilities = facilityRepository.findAll();
        if (facilities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", "Facility not found", null));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get facility successfully", facilities.get(0)));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ResponseObject> addFacility(@RequestParam("data") String data,
            @RequestParam("image") MultipartFile image) throws JsonMappingException, JsonProcessingException {
        List<Facility> facilities = facilityRepository.findAll();
        if (!facilities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", "Facility has already existed", null));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Facility entity = objectMapper.readValue(data, Facility.class);

        String imageF = storageService.storeFile(image);
        entity.setImage(imageF);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Add facility successfully", facilityRepository.save(entity)));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping("/")
    public ResponseEntity<ResponseObject> updateFacility(@RequestParam("data") String data,
            @RequestParam(name = "image", required = false) MultipartFile image)
            throws JsonMappingException, JsonProcessingException {
        List<Facility> facilities = facilityRepository.findAll();
        if (facilities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", "Facility not found", null));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Facility entity = objectMapper.readValue(data, Facility.class);

        if (image != null) {
            storageService.deleteFile(facilities.get(0).getImage());
            String imageF = storageService.storeFile(image);
            entity.setImage(imageF);
        }

        Facility facility = facilities.get(0);
        BeanUtils.copyProperties(entity, facility, "id");

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update facility successfully", facilityRepository.save(facility)));
    }
}
