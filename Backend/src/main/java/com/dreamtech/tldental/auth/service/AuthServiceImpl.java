package com.dreamtech.tldental.auth.service;

import com.dreamtech.tldental.users.Requests.UsersRequest;
import com.dreamtech.tldental.users.model.Users;
import com.dreamtech.tldental.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;

    public Optional<Users> AddUser(UsersRequest user) {
        Users newUser = new Users();
        newUser.setEmail(user.getEmail());
        newUser.setFullname(user.getFullname());
        newUser.setAddress(user.getAddress());
        newUser.setPhonenumber(user.getPhonenumber());
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        return Optional.of(usersRepository.save(newUser));
    }
}
