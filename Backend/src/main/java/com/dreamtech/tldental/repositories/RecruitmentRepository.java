package com.dreamtech.tldental.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dreamtech.tldental.models.Recruitment;

public interface RecruitmentRepository extends JpaRepository<Recruitment, String> {
    // search a recruitment by title or position or location or department or working_form with pagination
    @Query( "SELECT rc FROM Recruitment rc WHERE (:search_text IS NULL) OR (rc.title LIKE %:search_text% OR rc.position LIKE %:search_text% OR rc.location LIKE %:search_text% OR rc.department LIKE %:search_text% OR rc.working_form LIKE %:search_text%)" )
    List<Recruitment> searchRecruitment(@Param("search_text") String searchText, Pageable pageable);
}
