package com.kilanko.mortgagemate.repository;

import com.kilanko.mortgagemate.model.MortgageApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MortgageApplicationRepository extends JpaRepository<MortgageApplication, Long> {
}
