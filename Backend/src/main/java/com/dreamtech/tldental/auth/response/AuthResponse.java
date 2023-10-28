package com.dreamtech.tldental.auth.response;

import com.dreamtech.tldental.users.model.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    Users user;
    String jwt;
}
