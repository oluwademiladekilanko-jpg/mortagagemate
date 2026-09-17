package com.kilanko.mortgagemate.controller;

import com.kilanko.mortgagemate.dto.ApplicationRequest;
import com.kilanko.mortgagemate.dto.CalculationResponse;
import com.kilanko.mortgagemate.model.MortgageApplication;
import com.kilanko.mortgagemate.repository.MortgageApplicationRepository;
import com.kilanko.mortgagemate.service.MortgageCalculator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // fine for a local learning project; lock this down for anything real
public class MortgageController {

    private final MortgageCalculator calculator;
    private final MortgageApplicationRepository repository;

    public MortgageController(MortgageCalculator calculator, MortgageApplicationRepository repository) {
        this.calculator = calculator;
        this.repository = repository;
    }

    /** Calculate only — nothing is saved. Used for the "live preview" on the frontend. */
    @PostMapping("/calculate")
    public CalculationResponse calculate(@Valid @RequestBody ApplicationRequest req) {
        double payment = calculator.monthlyPayment(
                req.getHomePrice(), req.getDownPayment(), req.getAnnualInterestRate(), req.getLoanTermYears());
        boolean affordable = calculator.isAffordable(req.getAnnualIncome(), payment);
        return new CalculationResponse(payment, affordable);
    }

    /** Calculate AND persist as a tracked application. */
    @PostMapping("/applications")
    public ResponseEntity<MortgageApplication> submitApplication(@Valid @RequestBody ApplicationRequest req) {
        double payment = calculator.monthlyPayment(
                req.getHomePrice(), req.getDownPayment(), req.getAnnualInterestRate(), req.getLoanTermYears());

        MortgageApplication application = new MortgageApplication(
                req.getApplicantName(), req.getAnnualIncome(), req.getHomePrice(),
                req.getDownPayment(), req.getAnnualInterestRate(), req.getLoanTermYears(), payment);

        MortgageApplication saved = repository.save(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/applications")
    public List<MortgageApplication> listApplications() {
        return repository.findAll();
    }

    @GetMapping("/applications/{id}")
    public ResponseEntity<MortgageApplication> getApplication(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/applications/{id}/status")
    public ResponseEntity<MortgageApplication> updateStatus(
            @PathVariable Long id, @RequestParam MortgageApplication.Status status) {
        return repository.findById(id)
                .map(app -> {
                    app.setStatus(status);
                    return ResponseEntity.ok(repository.save(app));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
