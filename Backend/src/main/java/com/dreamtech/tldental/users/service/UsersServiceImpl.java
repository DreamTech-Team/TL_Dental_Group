package com.dreamtech.tldental.users.service;

import com.dreamtech.tldental.users.Requests.UsersRequest;
import com.dreamtech.tldental.users.model.Users;
import com.dreamtech.tldental.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    public List<Users> GetAllUsers() {
        return usersRepository.findAll();
    }

    public List<Users> searchUser(String searchText, Pageable pageable) {
        return usersRepository.searchUser(searchText, pageable);
    }

    public Users changePassword(String id, String newPassword) {
        Users user = usersRepository.findById(id).get();

        if (user.isChanged() == true) return null;

        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        user.setChanged(true);
        return usersRepository.save(user);
    }

    public Users AddUser(UsersRequest user) {
        Users newUser = new Users();
        newUser.setFullname(user.getFullname());
        newUser.setAddress(user.getAddress());
        newUser.setPhonenumber(user.getPhonenumber());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        newUser.setRoles(user.getRoles());
        return usersRepository.save(newUser);
    }
}
