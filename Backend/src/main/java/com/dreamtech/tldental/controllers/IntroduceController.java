package com.dreamtech.tldental.controllers;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamtech.tldental.models.ContentPage;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;

@RestController
@RequestMapping("/api/v1/introduce")
public class IntroduceController {
    @Autowired
    private ContentPageRepository contentPageRepository;

    @GetMapping("/letter")
    public ResponseEntity<ResponseObject> getLetter() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get letter successfully", contentPageRepository.findHomePageByTypeName("introduce::letter"))
        );
    }

    @PostMapping("/letter")
    public ResponseEntity<ResponseObject> addLetter(@RequestBody ContentPage entity) {
        Optional<ContentPage> foundLetter = contentPageRepository.findHomePageByTypeName("introduce::letter");
    
        if (foundLetter.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Type name already taken", "")
            );
        }

        entity.setType("introduce::letter");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add letter successfully", contentPageRepository.save(entity))
        );
    }

    @PatchMapping("/letter")
    public ResponseEntity<ResponseObject> updateLetter(@RequestBody ContentPage entity) {
        Optional<ContentPage> foundLetter = contentPageRepository.findById(entity.getId());
    
        if (foundLetter.isPresent()) {
            ContentPage letter = foundLetter.get();

            if (letter.getType().equals("introduce::letter")) {
                BeanUtils.copyProperties(entity, letter);

                letter.setType("introduce::letter");

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update letter successfully", contentPageRepository.save(letter))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }
}
