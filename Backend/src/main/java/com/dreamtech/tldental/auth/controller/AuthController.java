package com.dreamtech.tldental.auth.controller;

import com.dreamtech.tldental.UserSecurity.dao.JpaUserDetailsService;
import com.dreamtech.tldental.auth.request.AuthenticationRequest;
import com.dreamtech.tldental.auth.response.AuthResponse;
import com.dreamtech.tldental.auth.service.AuthService;
import com.dreamtech.tldental.config.JwtUtils;
import com.dreamtech.tldental.models.ResponseObject;
import com.dreamtech.tldental.users.model.Users;
import com.dreamtech.tldental.users.repository.UsersRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JpaUserDetailsService jpaUserDetailsService;

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    private final UsersRepository usersRepository;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> authenticate(@RequestBody AuthenticationRequest request,
            HttpServletResponse response) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword(),
                            new ArrayList<>()));
            final UserDetails user = jpaUserDetailsService.loadUserByUsername(request.getEmail());
            if (user != null) {
                String jwt = jwtUtils.generateToken(user);
                // Cookie cookie = new Cookie("jwt", jwt);
                // cookie.setMaxAge(30 * 24 * 60 * 60); // expires in 7 days
                // // cookie.setSecure(true);
                // cookie.setHttpOnly(true);
                // cookie.setPath("/"); // Global
                // response.addCookie(cookie);
                Users userInfo = usersRepository.findByEmail(request.getEmail()).get();

                AuthResponse data = new AuthResponse(userInfo, jwt);

                return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Login successfully", data));
            }

            return ResponseEntity.status(400).body(new ResponseObject("failed", "Error authenticating", ""));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ResponseObject("failed", e.getMessage(), ""));
        }
    }
}
