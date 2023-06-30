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
import com.dreamtech.tldental.models.HomeSection1;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;


@RestController 
@RequestMapping("/api/v1/home")
public class HomePageController {
    @Autowired
    private ContentPageRepository repository;

    @GetMapping("/header")
    ResponseEntity<ResponseObject> getHomeHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Header Successfully", repository.findHomePageByTypeName("home::header"))
        );
    }

    @PostMapping(value="/header")
    public ResponseEntity<ResponseObject> addHomeHeader(@RequestBody ContentPage entity) {
        
        Optional<ContentPage> foundContentPage = repository.findHomePageByTypeName("home::header");

        if (foundContentPage.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", "")
            );
        }

        entity.setType("home::header");

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add Header Successfully", repository.save(entity))
        );
    }

    @PatchMapping(value="/content-page")
    public ResponseEntity<ResponseObject> updateContentPage(@RequestBody ContentPage entity) { 
        Optional<ContentPage> foundContentPage = repository.findById(entity.getId());

        if (foundContentPage.isPresent()) {
            ContentPage currentContentPage = foundContentPage.get();
            BeanUtils.copyProperties(entity, currentContentPage);

            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Content Page Successfully", repository.save(currentContentPage))
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    // SECTION 1

    @GetMapping("/section1")
    ResponseEntity<ResponseObject> getSection1() {
        Optional<ContentPage> Section1Heading = repository.findHomePageByTypeName("home::section1_heading");
        Optional<ContentPage> Section1SubItem1 = repository.findHomePageByTypeName("home::section1_subitem1");
        Optional<ContentPage> Section1SubItem2 = repository.findHomePageByTypeName("home::section1_subitem2");
        Optional<ContentPage> Section1SubItem3 = repository.findHomePageByTypeName("home::section1_subitem3");

        if (Section1Heading.isPresent() && Section1SubItem1.isPresent() && Section1SubItem2.isPresent() && Section1SubItem3.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Section 1 Successfully", new ContentPage[] {
                    Section1Heading.get(),
                    Section1SubItem1.get(),
                    Section1SubItem2.get(),
                    Section1SubItem3.get()
                })
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }    

    @PostMapping(value="/section1")
    public ResponseEntity<ResponseObject> addSection1(@RequestBody HomeSection1 entity) {

        Optional<ContentPage> Section1Heading = repository.findHomePageByTypeName("home::section1_heading");
        Optional<ContentPage> Section1SubItem1 = repository.findHomePageByTypeName("home::section1_subitem1");
        Optional<ContentPage> Section1SubItem2 = repository.findHomePageByTypeName("home::section1_subitem2");
        Optional<ContentPage> Section1SubItem3 = repository.findHomePageByTypeName("home::section1_subitem3");
        
        if (Section1Heading.isPresent() && Section1SubItem1.isPresent() && Section1SubItem2.isPresent() && Section1SubItem3.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Type name already taken", "")
            );
        }

        entity.getHeading().setType("home::section1_heading");
        entity.getSubItem1().setType("home::section1_subitem1");
        entity.getSubItem2().setType("home::section1_subitem2");
        entity.getSubItem3().setType("home::section1_subitem3");

        repository.save(entity.getHeading());
        repository.save(entity.getSubItem1());
        repository.save(entity.getSubItem2());
        repository.save(entity.getSubItem3());

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add Header Successfully", entity)
        );
    }
}
