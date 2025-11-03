package com.altruithm.backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "charity_basic")
@Data
public class CharityBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ← CHANGED FROM Long

    @Column(name = "ascore")
    private Double ascore;

    @Column(name = "category")
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ein", insertable = false, updatable = false)  // ← ADDED
    private String einLowercase;

    @Column(name = "tot_exp")
    private Double totalExpenses;

    @Column(name = "fund_eff")
    private Double fundEfficiency;

    @Column(name = "fscore")
    private Double fscore;

    @Column(name = "NAME", length = 500)
    private String name;

    @Column(name = "tot_rev")
    private Double totalRevenue;

    @Column(name = "score")
    private Double score;

    @Column(name = "state", length = 10, insertable = false, updatable = false)  // ← ADDED
    private String stateLowercase;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "EIN")
    private Long einUppercase;

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
}