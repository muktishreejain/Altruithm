package com.altruithm.backend.repository;

import com.altruithm.backend.model.Charity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharityRepository extends JpaRepository<Charity, Long> {
    boolean existsByName(String name);  // Add this line
}