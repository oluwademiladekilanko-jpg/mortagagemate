package com.kilanko.mortgagemate.dto;

public class CalculationResponse {
    private final double monthlyPayment;
    private final boolean affordable;

    public CalculationResponse(double monthlyPayment, boolean affordable) {
        this.monthlyPayment = monthlyPayment;
        this.affordable = affordable;
    }

    public double getMonthlyPayment() { return monthlyPayment; }
    public boolean isAffordable() { return affordable; }
}
