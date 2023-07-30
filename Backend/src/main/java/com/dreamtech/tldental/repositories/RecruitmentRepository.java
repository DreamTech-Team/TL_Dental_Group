package com.dreamtech.tldental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtech.tldental.models.Recruitment;

public interface RecruitmentRepository extends JpaRepository<Recruitment, String> {
    
}
