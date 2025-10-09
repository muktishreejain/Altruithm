package com.altruithm.backend.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "charity_basic")
@Data
public class CharityBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double ascore;
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String ein;

    @Column(name = "tot_exp")
    private Double totalExpenses;

    @Column(name = "fund_eff")
    private Double fundEfficiency;

    private Double fscore;

    @Column(name = "NAME", length = 500)
    private String name;

    @Column(name = "tot_rev")
    private Double totalRevenue;

    private Double score;

    @Column(length = 10)
    private String state;

    @Column(length = 50)
    private String size;

    @Column(name = "EIN")
    private Long einNumber;

    @Column(name = "STATE", length = 10)
    private String stateCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ZIP", length = 20)
    private String zip;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "impact_score", length = 50)
    private String impactScore;

    @Column(name = "impact_efficiency")
    private Double impactEfficiency;

    @Column(name = "INCOME_AMT")
    private Double incomeAmount;

    @Column(name = "REVENUE_AMT")
    private Double revenueAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}