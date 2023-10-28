package com.dreamtech.tldental.users.Requests;

import lombok.Getter;

@Getter
public class PasswordRequest {
    String password;
    String confirmPassword;
}
