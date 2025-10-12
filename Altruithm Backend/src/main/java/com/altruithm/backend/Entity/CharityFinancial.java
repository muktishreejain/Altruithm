package com.altruithm.backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "charity_financial")
@Data
public class CharityFinancial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ← CHANGED FROM Long

    @Column(name = "ascore")
    private Double ascore;

    @Column(name = "category")
    private String category;

    @Column(name = "tot_exp")
    private Double totalExpenses;

    @Column(name = "admin_exp_p")
    private Double adminExpensePercent;

    @Column(name = "fund_eff")
    private Double fundEfficiency;

    @Column(name = "fund_exp_p")
    private Double fundExpensePercent;

    @Column(name = "program_exp_p")
    private Double programExpensePercent;

    @Column(name = "leader_comp_p")
    private Double leaderCompPercent;

    @Column(name = "program_exp")
    private Double programExpense;

    @Column(name = "fund_exp")
    private Double fundExpense;

    @Column(name = "fscore")
    private Double fscore;

    @Column(name = "NAME", length = 500)
    private String name;

    @Column(name = "tot_rev")
    private Double totalRevenue;

    @Column(name = "subcategory")
    private String subcategory;

    @Column(name = "score")
    private Double score;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "EIN")
    private Long ein;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "impact_score", length = 50)
    private String impactScore;

    @Column(name = "impact_efficiency")
    private Double impactEfficiency;

    @Column(name = "INCOME_AMT")
    private Double incomeAmount;

    @Column(name = "REVENUE_AMT")
    private Long revenueAmount;

    @Column(name = "NTEE_CD", length = 20)
    private String nteeCode;

    @Column(name = "ASSET_AMT")
    private Double assetAmount;

    @Column(name = "ACCT_PD")
    private Long accountingPeriod;

    @Column(name = "PF_FILING_REQ_CD")
    private Integer pfFilingReqCode;

    @Column(name = "FILING_REQ_CD")
    private Integer filingReqCode;

    @Column(name = "INCOME_CD")
    private Integer incomeCode;

    @Column(name = "ASSET_CD")
    private Integer assetCode;

    @Column(name = "FOUNDATION")
    private Integer foundation;

    @Column(name = "DEDUCTIBILITY")
    private Double deductibility;

    @Column(name = "RULING")
    private Double ruling;

    @Column(name = "CLASSIFICATION")
    private Double classification;
}