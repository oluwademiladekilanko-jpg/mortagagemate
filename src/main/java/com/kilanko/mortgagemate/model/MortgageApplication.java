package com.kilanko.mortgagemate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "mortgage_applications")
public class MortgageApplication {

    public enum Status { SUBMITTED, UNDER_REVIEW, APPROVED, DENIED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String applicantName;

    @Positive
    private double annualIncome;

    @Positive
    private double homePrice;

    @Positive
    private double downPayment;

    @Positive
    private double annualInterestRate; // e.g. 5.25 for 5.25%

    @Positive
    private int loanTermYears;

    private double monthlyPayment; // computed and stored at submit time

    @Enumerated(EnumType.STRING)
    private Status status = Status.SUBMITTED;

    protected MortgageApplication() {
        // required by JPA
    }

    public MortgageApplication(String applicantName, double annualIncome, double homePrice,
                                double downPayment, double annualInterestRate, int loanTermYears,
                                double monthlyPayment) {
        this.applicantName = applicantName;
        this.annualIncome = annualIncome;
        this.homePrice = homePrice;
        this.downPayment = downPayment;
        this.annualInterestRate = annualInterestRate;
        this.loanTermYears = loanTermYears;
        this.monthlyPayment = monthlyPayment;
    }

    public Long getId() { return id; }
    public String getApplicantName() { return applicantName; }
    public double getAnnualIncome() { return annualIncome; }
    public double getHomePrice() { return homePrice; }
    public double getDownPayment() { return downPayment; }
    public double getAnnualInterestRate() { return annualInterestRate; }
    public int getLoanTermYears() { return loanTermYears; }
    public double getMonthlyPayment() { return monthlyPayment; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
