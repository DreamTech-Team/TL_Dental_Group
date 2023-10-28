package com.dreamtech.tldental.users.service;

import com.dreamtech.tldental.users.Requests.UsersRequest;
import com.dreamtech.tldental.users.model.Users;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface UsersService {
    public List<Users> GetAllUsers();

    public List<Users> searchUser(String searchText, Pageable pageable);

    public Users changePassword(String id, String newPassword);

    public Users AddUser(UsersRequest user);
}
