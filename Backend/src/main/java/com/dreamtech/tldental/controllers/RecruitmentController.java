package com.dreamtech.tldental.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamtech.tldental.models.ContentPage;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {
    @Autowired
    private ContentPageRepository contentPageRepository;

    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get header successfully", contentPageRepository.findHomePageByTypeName("recruitment::header"))
        );
    }

    @PostMapping(value="/header")
    public ResponseEntity<ResponseObject> addHeader (@RequestParam("data") String data) throws JsonMappingException, JsonProcessingException {

        Optional<ContentPage[]> headers = contentPageRepository.findAllByType("recruitment::header");

        if (headers.isPresent()) {

            ContentPage[] listHeader = headers.get();

            if (listHeader.length >= 3) 
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", "Header is full", null)
                );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ContentPage entity = objectMapper.readValue(data, ContentPage.class);

        entity.setType("recruitment::header");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add header successfully", contentPageRepository.save(entity))
        );
    }
}
