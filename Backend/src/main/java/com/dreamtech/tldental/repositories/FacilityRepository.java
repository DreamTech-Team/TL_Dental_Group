package com.dreamtech.tldental.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamtech.tldental.models.Facility;

public interface FacilityRepository extends JpaRepository<Facility, String> {
    
}
