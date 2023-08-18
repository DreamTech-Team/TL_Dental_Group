package com.dreamtech.tldental.users.controller;

import com.dreamtech.tldental.models.DataPageObject;
import com.dreamtech.tldental.models.EmailDetails;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.services.EmailService;
import com.dreamtech.tldental.users.Requests.PasswordRequest;
import com.dreamtech.tldental.users.Requests.UsersRequest;
import com.dreamtech.tldental.users.model.Users;
import com.dreamtech.tldental.users.repository.UsersRepository;
import com.dreamtech.tldental.users.service.UsersService;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class UsersController {
    @Autowired 
    private EmailService emailService;

    @Autowired
    private final UsersService usersService;

    @Autowired
    private final UsersRepository usersRepository;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("")
    ResponseEntity<ResponseObject> getUsers(@RequestParam(value = "q", required = false) String query,
            @RequestParam(required = false, defaultValue = "12") String pageSize,
            @RequestParam(required = false, defaultValue = "0") String page,
            @RequestParam(required = false, defaultValue = "desc") String sort) {
        try {
            Sort.Direction sortDirection = sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sortByCreateAt = Sort.by(sortDirection, "createAt");

            List<Users> newsList = usersService.searchUser(query, PageRequest
                    .of(Integer.parseInt(page), Integer.parseInt(pageSize), sortByCreateAt));

            int total = newsList.size();

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Get users successfully",
                            new DataPageObject(total, page, pageSize, newsList)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> addUser(@RequestBody UsersRequest entity) {
        try {
            String generatedString = RandomStringUtils.randomAlphanumeric(8);
            entity.setPassword(generatedString);

            Users user = usersService.AddUser(entity);

            EmailDetails details = new EmailDetails(entity.getEmail(), "Mật khẩu đăng nhập của bạn: " + generatedString, "TL Dental Password");
            
            emailService.sendSimpleMail(details);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Add user successfully", user));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseObject> addUser(@PathVariable("id") String id) {
        try {
            usersRepository.deleteById(id);
            
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete user successfully", ""));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable("id") String id, @RequestBody UsersRequest entity) {
        try {
            Optional<Users> foundUser = usersRepository.findById(id);

            if (foundUser.isPresent()) {
                Users user = foundUser.get();
                String pass = user.getPassword();
                
                BeanUtils.copyProperties(entity, user);
                user.setPassword(pass);
                
                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update user successfully", usersRepository.save(user)));
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Update user successfully", ""));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') || hasRole('ROLE_STAFF')")
    @PatchMapping(value = "password/{id}")
    public ResponseEntity<ResponseObject> changePassword(@PathVariable("id") String id, @RequestBody PasswordRequest entity) {
        try {
            if (!Objects.equals(entity.getPassword(), entity.getConfirmPassword())) 
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Password do not match", ""));

            Users user = usersService.changePassword(id, entity.getPassword());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "User already changed password", ""));
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "User change password successfully", user));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), ""));
        }
    }
}
