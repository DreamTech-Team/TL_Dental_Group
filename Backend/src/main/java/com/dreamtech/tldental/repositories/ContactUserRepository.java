package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.ContactUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactUserRepository extends JpaRepository<ContactUser, String>  {
    List<ContactUser> findByFullnameContainingIgnoreCase(String keyword, Pageable pageable);
}
