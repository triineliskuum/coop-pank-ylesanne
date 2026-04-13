package ee.coop.coopbackend.controller;

import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.repository.LoanApplicationRepository;
import ee.coop.coopbackend.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final LoanApplicationRepository loanApplicationRepository;

    public LoanApplicationController(LoanApplicationService loanApplicationService, LoanApplicationRepository loanApplicationRepository) {
        this.loanApplicationService = loanApplicationService;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    @PostMapping
    public LoanApplication createLoanApplication(@Valid @RequestBody LoanApplication loanApplication) {
        return loanApplicationService.createApplication(loanApplication);
    }

    @GetMapping
    public List<LoanApplication> getAll() {
        return loanApplicationRepository.findAll();
    }
}