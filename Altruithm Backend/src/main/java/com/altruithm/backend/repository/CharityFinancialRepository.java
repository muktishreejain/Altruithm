package com.altruithm.backend.repository;

import com.altruithm.entity.CharityFinancial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CharityFinancialRepository extends JpaRepository<CharityFinancial, Long> {

    Optional<CharityFinancial> findByNameIgnoreCase(String name);

    Optional<CharityFinancial> findByEin(Long ein);

    @Query("SELECT c FROM CharityFinancial c WHERE c.adminExpensePercent > :threshold")
    List<CharityFinancial> findHighAdminExpenseCharities(@Param("threshold") Double threshold);

    @Query("SELECT c FROM CharityFinancial c WHERE c.programExpensePercent < :threshold")
    List<CharityFinancial> findLowProgramExpenseCharities(@Param("threshold") Double threshold);

    @Query("SELECT c FROM CharityFinancial c WHERE c.score >= :minScore AND c.fundEfficiency >= :minEfficiency")
    List<CharityFinancial> findQualityCharities(
            @Param("minScore") Double minScore,
            @Param("minEfficiency") Double minEfficiency
    );
}