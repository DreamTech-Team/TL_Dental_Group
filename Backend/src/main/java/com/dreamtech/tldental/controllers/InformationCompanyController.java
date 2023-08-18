package com.dreamtech.tldental.controllers;

import com.dreamtech.tldental.models.InformationCompany;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.repositories.InformationCompanyRepository;
import com.dreamtech.tldental.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/information")
public class InformationCompanyController {
        @Autowired
        private InformationCompanyRepository repository;
        @Autowired
        private IStorageService storageService;

        private final Executor executor;

        @Autowired
        public InformationCompanyController() {
                this.executor = Executors.newFixedThreadPool(5);
        }

        @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
        @GetMapping("")
        public ResponseEntity<ResponseObject> getGeneral(@RequestParam(defaultValue = "GENERAL") String type) {
                List<InformationCompany> data = repository.findAllByType(type);
                Map<String, Object> results = new HashMap<>();
                for (int i = 0; i < data.size(); i++) {
                        results.put(data.get(i).getTitle(), data.get(i));
                }
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("ok", "Get " + type + " information successfully!", results));
        }

        @Async
        @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
        @PatchMapping("/general")
        public ResponseEntity<ResponseObject> updateGeneral(@RequestBody GeneralData data) {
                Map<String, Object> results = new HashMap<>();

                CompletableFuture<InformationCompany> nameFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("name", data.name, "GENERAL")), executor);
                nameFuture.thenAcceptAsync(resInstance -> results.put("name", resInstance));

                CompletableFuture<InformationCompany> sloganFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("slogan", data.slogan, "GENERAL")),
                                executor);
                sloganFuture.thenAcceptAsync(resInstance -> results.put("slogan", resInstance));

                CompletableFuture<InformationCompany> duringTimeFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("duringTime", data.duringTime, "GENERAL")),
                                executor);
                duringTimeFuture.thenAcceptAsync(resInstance -> results.put("duringTime", resInstance));

                return CompletableFuture.allOf(nameFuture, sloganFuture, duringTimeFuture)
                                .thenApply(ignore -> ResponseEntity.status(HttpStatus.OK).body(
                                                new ResponseObject("ok", "Update general information successfully!",
                                                                results)))
                                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseObject("failed", ex.getMessage(), "")))
                                .join();
        }

        @Async
        @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
        @PatchMapping("/contact")
        public ResponseEntity<ResponseObject> addContact(@RequestBody ContactData data) {
                Map<String, Object> results = new HashMap<>();

                CompletableFuture<InformationCompany> phoneFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("phone", data.phone, "CONTACT")),
                                executor);
                phoneFuture.thenAcceptAsync(resInstance -> results.put("phone", resInstance));

                CompletableFuture<InformationCompany> hotlineFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("hotline", data.hotline, "CONTACT")),
                                executor);
                hotlineFuture.thenAcceptAsync(resInstance -> results.put("hotline", resInstance));

                CompletableFuture<InformationCompany> emailFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("email", data.email, "CONTACT")),
                                executor);
                emailFuture.thenAcceptAsync(resInstance -> results.put("email", resInstance));

                CompletableFuture<InformationCompany> facebookFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("facebook", data.facebook, "CONTACT")),
                                executor);
                facebookFuture.thenAcceptAsync(resInstance -> results.put("facebook", resInstance));

                CompletableFuture<InformationCompany> zaloFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("zalo", data.zalo, "CONTACT")), executor);
                zaloFuture.thenAcceptAsync(resInstance -> results.put("zalo", resInstance));

                return CompletableFuture.allOf(phoneFuture, hotlineFuture, emailFuture, facebookFuture, zaloFuture)
                                .thenApply(ignore -> ResponseEntity.status(HttpStatus.OK).body(
                                                new ResponseObject("ok", "Update contact successfully!", results)))
                                .exceptionally(ex -> {
                                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                        new ResponseObject("failed", ex.getMessage(), ""));
                                })
                                .join();
        }

        @Async
        @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
        @PatchMapping("/ceo")
        public ResponseEntity<ResponseObject> updateCEO(@RequestBody CEOData data) {
                Map<String, Object> results = new HashMap<>();

                CompletableFuture<InformationCompany> nameFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("fullname_ceo", data.fullnameCEO, "CEO")),
                                executor);
                nameFuture.thenAcceptAsync(resInstance -> results.put("fullnameCEO", resInstance));

                CompletableFuture<InformationCompany> addressFuture = CompletableFuture.supplyAsync(
                                () -> repository.save(new InformationCompany("address_ceo", data.addressCEO, "CEO")),
                                executor);
                addressFuture.thenAcceptAsync(resInstance -> results.put("addressCEO", resInstance));

                CompletableFuture<InformationCompany> inaugurationFuture = CompletableFuture
                                .supplyAsync(() -> repository.save(new InformationCompany("inauguration_ceo",
                                                data.inaugurationCEO, "CEO")), executor);
                inaugurationFuture.thenAcceptAsync(resInstance -> results.put("inaugurationCEO", resInstance));

                return CompletableFuture.allOf(nameFuture, addressFuture, inaugurationFuture)
                                .thenApply(ignore -> ResponseEntity.status(HttpStatus.OK).body(
                                                new ResponseObject("ok", "Update CEO information successfully!",
                                                                results)))
                                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseObject("failed", ex.getMessage(), "")))
                                .join();
        }

        @Async
        @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
        @PatchMapping("/logo")
        public ResponseEntity<ResponseObject> updateLogo(@RequestParam("logo") MultipartFile logo) {
                InformationCompany logoExisted = repository.findByType("LOGO");
                if (logoExisted != null) {
                        CompletableFuture.supplyAsync(() -> storageService.deleteFile(logoExisted.getContent()));
                }
                String pathLogo = storageService.storeFile(logo);
                return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("ok", "Update logo successfully",
                                                repository.save(new InformationCompany("logo", pathLogo, "LOGO"))));
        }

        private static class GeneralData {
                private String name;
                private String slogan;
                private String duringTime;

                public GeneralData(String name, String slogan, String duringTime) {
                        this.name = name;
                        this.slogan = slogan;
                        this.duringTime = duringTime;
                }
        }

        private static class ContactData {
                private String phone;
                private String hotline;
                private String email;
                private String facebook;
                private String zalo;

                public ContactData(String phone, String hotline, String email, String facebook, String zalo) {
                        this.phone = phone;
                        this.hotline = hotline;
                        this.email = email;
                        this.facebook = facebook;
                        this.zalo = zalo;
                }
        }

        private static class CEOData {
                private String fullnameCEO;
                private String addressCEO;
                private String inaugurationCEO;

                public CEOData(String fullnameCEO, String addressCEO, String inaugurationCEO) {
                        this.fullnameCEO = fullnameCEO;
                        this.addressCEO = addressCEO;
                        this.inaugurationCEO = inaugurationCEO;
                }
        }
}
