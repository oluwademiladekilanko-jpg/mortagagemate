package com.kilanko.mortgagemate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ApplicationRequest {

    @NotBlank
    private String applicantName;

    @Positive
    private double annualIncome;

    @Positive
    private double homePrice;

    @Positive
    private double downPayment;

    @Positive
    private double annualInterestRate;

    @Positive
    private int loanTermYears;

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public double getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(double annualIncome) { this.annualIncome = annualIncome; }

    public double getHomePrice() { return homePrice; }
    public void setHomePrice(double homePrice) { this.homePrice = homePrice; }

    public double getDownPayment() { return downPayment; }
    public void setDownPayment(double downPayment) { this.downPayment = downPayment; }

    public double getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(double annualInterestRate) { this.annualInterestRate = annualInterestRate; }

    public int getLoanTermYears() { return loanTermYears; }
    public void setLoanTermYears(int loanTermYears) { this.loanTermYears = loanTermYears; }
}
