package com.kilanko.mortgagemate.service;

import org.springframework.stereotype.Service;

/**
 * Pure calculation logic, deliberately kept free of Spring/HTTP/DB concerns
 * so it's trivial to unit test on its own.
 */
@Service
public class MortgageCalculator {

    /**
     * Standard fixed-rate amortization formula:
     * M = P * [r(1+r)^n] / [(1+r)^n - 1]
     *
     * @param homePrice          total price of the home
     * @param downPayment        cash paid up front
     * @param annualInterestRate annual rate as a percentage, e.g. 5.25
     * @param loanTermYears      loan term in years
     * @return the fixed monthly payment (principal + interest only)
     */
    public double monthlyPayment(double homePrice, double downPayment,
                                  double annualInterestRate, int loanTermYears) {
        double principal = homePrice - downPayment;
        if (principal <= 0) {
            return 0.0;
        }

        double monthlyRate = (annualInterestRate / 100) / 12;
        int numPayments = loanTermYears * 12;

        if (monthlyRate == 0) {
            return principal / numPayments;
        }

        double factor = Math.pow(1 + monthlyRate, numPayments);
        return principal * (monthlyRate * factor) / (factor - 1);
    }

    /**
     * Rough affordability check using the common "28% of gross monthly
     * income" guideline lenders use for the housing payment alone.
     */
    public boolean isAffordable(double annualIncome, double monthlyPayment) {
        double monthlyIncome = annualIncome / 12;
        return monthlyPayment <= monthlyIncome * 0.28;
    }
}
