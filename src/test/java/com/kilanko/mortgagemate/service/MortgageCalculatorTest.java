package com.kilanko.mortgagemate.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MortgageCalculatorTest {

    private final MortgageCalculator calculator = new MortgageCalculator();

    @Test
    void calculatesMonthlyPaymentForStandardLoan() {
        // $400,000 home, $80,000 down -> $320,000 principal, 6% APR, 30-year term
        double payment = calculator.monthlyPayment(400_000, 80_000, 6.0, 30);

        // Known correct answer for these inputs is ~$1918.56/month
        assertEquals(1918.56, payment, 1.0);
    }

    @Test
    void zeroInterestSplitsPrincipalEvenlyAcrossPayments() {
        double payment = calculator.monthlyPayment(120_000, 20_000, 0.0, 10);
        assertEquals(100_000.0 / 120, payment, 0.01);
    }

    @Test
    void fullyPaidDownPaymentMeansNoLoanNeeded() {
        double payment = calculator.monthlyPayment(200_000, 200_000, 5.0, 30);
        assertEquals(0.0, payment);
    }

    @Test
    void flagsPaymentAboveTwentyEightPercentRuleAsUnaffordable() {
        // $60,000/year -> $1,400/month guideline ceiling
        assertFalse(calculator.isAffordable(60_000, 1_500));
        assertTrue(calculator.isAffordable(60_000, 1_200));
    }
}
