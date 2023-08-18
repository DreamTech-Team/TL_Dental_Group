package com.dreamtech.tldental.auth.service;

import com.dreamtech.tldental.users.Requests.UsersRequest;
import com.dreamtech.tldental.users.model.Users;

import java.util.Optional;

public interface AuthService {
    public Optional<Users> AddUser(UsersRequest user);
}
