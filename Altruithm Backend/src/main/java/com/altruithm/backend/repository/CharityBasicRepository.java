package com.altruithm.backend.repository;

import com.altruithm.entity.CharityBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharityBasicRepository extends JpaRepository<CharityBasic, Long> {

    Optional<CharityBasic> findByNameIgnoreCase(String name);

    Optional<CharityBasic> findByEin(String ein);

    List<CharityBasic> findByScoreGreaterThanEqual(Double minScore);

    List<CharityBasic> findByCategory(String category);

    @Query("SELECT c FROM CharityBasic c WHERE c.score >= :minScore ORDER BY c.score DESC")
    List<CharityBasic> findTopRatedCharities(@Param("minScore") Double minScore);

    @Query("SELECT c FROM CharityBasic c WHERE c.fundEfficiency >= :minEfficiency")
    List<CharityBasic> findEfficientCharities(@Param("minEfficiency") Double minEfficiency);
}