package ee.coop.coopbackend.controller;

import ee.coop.coopbackend.dto.LoanApplicationRequest;
import ee.coop.coopbackend.dto.LoanApplicationResponse;
import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.mapper.LoanApplicationMapper;
import ee.coop.coopbackend.service.LoanApplicationService;
import ee.coop.coopbackend.dto.RejectLoanRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling loan application endpoints.
 */
@RestController
@RequestMapping("/loans")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;
    private final LoanApplicationMapper loanApplicationMapper;

    public LoanApplicationController(LoanApplicationService loanApplicationService,
                                     LoanApplicationMapper loanApplicationMapper) {
        this.loanApplicationService = loanApplicationService;
        this.loanApplicationMapper = loanApplicationMapper;
    }

    /**
     * Creates a new loan application.
     */
    @PostMapping
    public LoanApplicationResponse createLoanApplication(@Valid @RequestBody LoanApplicationRequest request) {
        LoanApplication saved = loanApplicationService.createApplication(request);
        return loanApplicationMapper.toResponse(saved);
    }

    /**
     * Returns all loan applications.
     */
    @GetMapping
    public List<LoanApplicationResponse> getAll() {
        return loanApplicationService.getAll()
                .stream()
                .map(loanApplicationMapper::toResponse)
                .toList();
    }

    /**
     * Approves a loan application if it is in review state.
     */
    @PostMapping("/{id}/approve")
    public LoanApplicationResponse approveLoanApplication(@PathVariable Long id) {
        LoanApplication approved = loanApplicationService.approveApplication(id);
        return loanApplicationMapper.toResponse(approved);
    }

    /**
     * Rejects a loan application with a given reason.
     */
    @PostMapping("/{id}/reject")
    public LoanApplicationResponse rejectLoanApplication(@PathVariable Long id,
                                                         @Valid @RequestBody RejectLoanRequest request) {
        LoanApplication rejected = loanApplicationService.rejectApplication(id, request.getRejectionReason());
        return loanApplicationMapper.toResponse(rejected);
    }
}