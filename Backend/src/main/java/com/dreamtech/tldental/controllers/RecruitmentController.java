package com.dreamtech.tldental.controllers;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dreamtech.tldental.models.ContentPage;
import com.dreamtech.tldental.models.DataPageObject;
import com.dreamtech.tldental.models.RecruitSection1;
import com.dreamtech.tldental.models.RecruitSection2;
import com.dreamtech.tldental.models.Recruitment;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.ContentPageRepository;
import com.dreamtech.tldental.repositories.RecruitmentRepository;
import com.dreamtech.tldental.services.IStorageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/recruitment")
public class RecruitmentController {
    @Autowired
    private ContentPageRepository contentPageRepository;
    
    @Autowired
    private RecruitmentRepository recruitmentRepository;

    @Autowired
    private IStorageService storageService;

    @GetMapping("/header")
    public ResponseEntity<ResponseObject> getHeader() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Get header successfully", contentPageRepository.findAllByType("recruitment::header").get())
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

    @PatchMapping(value="/header")
    public ResponseEntity<ResponseObject> updateHeader (@RequestBody ContentPage[] entity) throws JsonMappingException, JsonProcessingException {

        if (entity.length > 3) 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("error", "", null)
            );

        for (ContentPage item : entity) {
            Optional<ContentPage> foundedItem = contentPageRepository.findById(item.getId());

            if (!foundedItem.isPresent() || !foundedItem.get().getType().equals("recruitment::header"))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("error", "Header not found", null)
                );
            item.setType("recruitment::header");
        }

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Update header successfully", contentPageRepository.saveAll(Arrays.asList(entity)))
        );
    }

    @GetMapping(value="/section1")
    public ResponseEntity<ResponseObject> getSection1() {
        Optional<ContentPage> section1_1 = contentPageRepository.findHomePageByTypeName("recruitment::section1_1");
        Optional<ContentPage> section1_2 = contentPageRepository.findHomePageByTypeName("recruitment::section1_2");
        Optional<ContentPage> section1_image1 = contentPageRepository.findHomePageByTypeName("recruitment::section1_image1");
        Optional<ContentPage> section1_image2 = contentPageRepository.findHomePageByTypeName("recruitment::section1_image2");

        if (section1_1.isPresent() && section1_2.isPresent() && section1_image1.isPresent() && section1_image2.isPresent()) {
            RecruitSection1 recruitSection1 = new RecruitSection1(section1_1.get(), section1_2.get(), section1_image1.get(), section1_image2.get());
            
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get Section 1 Successfully", recruitSection1)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @PostMapping(value="/section1")
    public ResponseEntity<ResponseObject> addSection1(@RequestParam("data") String data, @RequestParam("image1") MultipartFile image1, @RequestParam("image2") MultipartFile image2) throws JsonMappingException, JsonProcessingException {

        Optional<ContentPage> section1_1 = contentPageRepository.findHomePageByTypeName("recruitment::section1_1");
        Optional<ContentPage> section1_2 = contentPageRepository.findHomePageByTypeName("recruitment::section1_2");
        Optional<ContentPage> section1_image1 = contentPageRepository.findHomePageByTypeName("recruitment::section1_image1");
        Optional<ContentPage> section1_image2 = contentPageRepository.findHomePageByTypeName("recruitment::section1_image2");

        if (section1_1.isPresent() || section1_2.isPresent() || section1_image1.isPresent() || section1_image2.isPresent()) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("error", "Section1 has already existed", null)
            );
        }

        ObjectMapper objectMapper = new ObjectMapper();
        RecruitSection1 entity = objectMapper.readValue(data, RecruitSection1.class);

        entity.getSubItem1().setType("recruitment::section1_1");
        entity.getSubItem2().setType("recruitment::section1_2");
        entity.getImage1().setType("recruitment::section1_image1");
        entity.getImage2().setType("recruitment::section1_image2");

        String image1File = storageService.storeFile(image1);
        entity.getImage1().setImage(image1File);

        String image2File = storageService.storeFile(image2);
        entity.getImage2().setImage(image2File);

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add section1 successfully", contentPageRepository.saveAll(Arrays.asList(entity.getSubItem1(), entity.getSubItem2(), entity.getImage1(), entity.getImage2())))
        );
    }

    @PatchMapping(value="/section1")
    public ResponseEntity<ResponseObject> updateSection1(@RequestParam("data") String data, @RequestParam(name = "image1", required = false) MultipartFile image1, @RequestParam(name = "image2", required = false) MultipartFile image2) throws JsonMappingException, JsonProcessingException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        RecruitSection1 entity = objectMapper.readValue(data, RecruitSection1.class);
        
        Optional<ContentPage> foundSection1_1 = contentPageRepository.findById(entity.getSubItem1().getId());
        Optional<ContentPage> foundSection1_2 = contentPageRepository.findById(entity.getSubItem2().getId());
        Optional<ContentPage> foundSection1Image1 = contentPageRepository.findById(entity.getImage1().getId());
        Optional<ContentPage> foundSection1Image2 = contentPageRepository.findById(entity.getImage2().getId());

        if (foundSection1_1.isPresent() && foundSection1_2.isPresent() && foundSection1Image1.isPresent() && foundSection1Image2.isPresent()) {
            ContentPage section1_1 = foundSection1_1.get();
            ContentPage section1_2 = foundSection1_2.get();
            ContentPage section1Image1 = foundSection1Image1.get();
            ContentPage section1Image2 = foundSection1Image2.get();
            
            if (section1_1.getType().equals("recruitment::section1_1") 
                && section1_2.getType().equals("recruitment::section1_2") 
                && section1Image1.getType().equals("recruitment::section1_image1") 
                && section1Image2.getType().equals("recruitment::section1_image2")) {

                BeanUtils.copyProperties(entity.getSubItem1(), section1_1);
                BeanUtils.copyProperties(entity.getSubItem2(), section1_2);
                BeanUtils.copyProperties(entity.getImage1(), section1Image1);
                BeanUtils.copyProperties(entity.getImage2(), section1Image2);
                
                section1_1.setType("recruitment::section1_1");
                section1_2.setType("recruitment::section1_2");
                section1Image1.setType("recruitment::section1_image1");
                section1Image2.setType("recruitment::section1_image2");

                if (image1 != null) {
                    storageService.deleteFile(section1Image1.getImage());
                    
                    String image1File = storageService.storeFile(image1);   
                    section1Image1.setImage(image1File);
                }

                if (image2 != null) {
                    storageService.deleteFile(section1Image2.getImage());
                    
                    String image2File = storageService.storeFile(image2);   
                    section1Image2.setImage(image2File);
                }

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update Section 1 Successfully", contentPageRepository.saveAll(Arrays.asList(section1_1, section1_2, section1Image1, section1Image2)))
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping(value="/section2")
    public ResponseEntity<ResponseObject> getSection2() {
        Optional<ContentPage> section2 = contentPageRepository.findHomePageByTypeName("recruitment::section2");
        Optional<ContentPage[]> section2_subitem = contentPageRepository.findAllByType("recruitment::section2_subitem");

        if (section2.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get section2 successfully", new RecruitSection2(section2.get(), section2_subitem, null))
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @PostMapping(value="/section2")
    public ResponseEntity<ResponseObject> addSection2(@RequestBody RecruitSection2 entity) {
        Optional<ContentPage> section2 = contentPageRepository.findHomePageByTypeName("recruitment::section2");

        if (section2.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("error", "Section2 has already existed", null)
            );
        }

        ContentPage[] subItem = entity.getSubItem().get();

        entity.getMainItem().setType("recruitment::section2");

        for (int i = 0; i < entity.getSubItem().get().length; i++) {
            subItem[i].setType("recruitment::section2_subitem");   
        }

        contentPageRepository.save(entity.getMainItem());
        contentPageRepository.saveAll(Arrays.asList(subItem));

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add section1 successfully", "")
        );
    }

    @PatchMapping(value="/section2")
    public ResponseEntity<ResponseObject> updateSection2(@RequestBody RecruitSection2 entity) {

        Optional<ContentPage> section2 = contentPageRepository.findById(entity.getMainItem().getId());

        if (section2.isPresent()) {
            ContentPage mainItem = section2.get();

            if (mainItem.getType().equals("recruitment::section2")) {

                BeanUtils.copyProperties(entity.getMainItem(), mainItem);
                mainItem.setType("recruitment::section2");

                Optional<String[]> foundedDeletedId = entity.getDeletedSubItem();
                
                if (foundedDeletedId.isPresent()) {
                    Optional<ContentPage[]> foundSubItem = contentPageRepository.findAllByType("recruitment::section2_subitem");
                    
                    if (foundSubItem.isPresent()) {
                        List<String> deletedId = Arrays.asList(foundedDeletedId.get());

                        ContentPage[] availableItem = foundSubItem.get();

                        for (int i = 0; i < availableItem.length; i++) {
                            if (deletedId.contains(availableItem[i].getId())) {
                                contentPageRepository.deleteById(availableItem[i].getId());
                            }
                        }
                    }
                }

                Optional<ContentPage[]> foundEntitySubItem = entity.getSubItem();
                contentPageRepository.save(mainItem);

                if (foundEntitySubItem.isPresent()) {

                    ContentPage[] subItem = foundEntitySubItem.get();

                    for (int i = 0; i < subItem.length; i++) {
                        subItem[i].setType("recruitment::section2_subitem");
                    }

                    contentPageRepository.saveAll(Arrays.asList(subItem));
                }

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update Section 2 Successfully", "")
                );
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @GetMapping("/")
    ResponseEntity<ResponseObject> getRecruitment(@RequestParam(value = "q", required = false) String query,
                                                  @RequestParam(required = false, defaultValue = "12") String pageSize,
                                                  @RequestParam(required = false, defaultValue = "0") String page,
                                                  @RequestParam(required = false, defaultValue = "desc") String sort) {
        try {
            // HANDLE FILTER
            Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sortByCreateAt = Sort.by(sortDirection, "createAt");

            List<Recruitment> newsList = recruitmentRepository.searchRecruitment(query, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));

            int total = newsList.size();
            
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get recruitment successfully", new DataPageObject(total, page, pageSize, newsList))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getById(@PathVariable("id") String id) {
        try {
            Optional<Recruitment> recruitment = recruitmentRepository.findById(id);

            if (recruitment.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get recruitment successfully", recruitment.get())
                );
            }

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Cannot found your data", "")
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
        }
    }

    @PostMapping(value="/")
    public ResponseEntity<ResponseObject> addRecruitment(@RequestBody Recruitment entity) {

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Add recruitment successfully", recruitmentRepository.save(entity))
        );
    }

    @PatchMapping(value="/")
    public ResponseEntity<ResponseObject> updateRecruitment(@RequestBody Recruitment entity) {
        Optional<Recruitment> foundRecruitment = recruitmentRepository.findById(entity.getId());

        if (foundRecruitment.isPresent()) {
            Recruitment recruitment = foundRecruitment.get();
            ZonedDateTime created = recruitment.getCreateAt();

            BeanUtils.copyProperties(entity, recruitment);
            recruitment.setCreateAt(created);

            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update recruitment successfully", recruitmentRepository.save(recruitment))
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<ResponseObject> deleteRecruitment(@PathVariable String id) {
        Optional<Recruitment> foundRecruitment = recruitmentRepository.findById(id);

        if (foundRecruitment.isPresent()) {
            recruitmentRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete recruitment successfully", "")
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
            new ResponseObject("failed", "Cannot found your data", "")
        );
    }
}
