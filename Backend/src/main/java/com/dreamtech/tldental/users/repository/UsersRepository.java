package com.dreamtech.tldental.users.repository;

import com.dreamtech.tldental.users.model.Users;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    @Query( "SELECT u FROM Users u WHERE (:search_text IS NULL) OR (u.fullname LIKE %:search_text% OR u.email LIKE %:search_text%)" )
    List<Users> searchUser(@Param("search_text") String searchText, Pageable pageable);

    Optional<Users> findByEmail(String email);
    Optional<Users> findById(String id);
}
