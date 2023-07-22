package com.dreamtech.tldental.repositories;

import com.dreamtech.tldental.models.InformationCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationCompanyRepository extends JpaRepository<InformationCompany, String>  {
    InformationCompany findByType(String type);
    List<InformationCompany> findAllByType(String type);
}
