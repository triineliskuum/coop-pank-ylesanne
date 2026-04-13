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

    @PostMapping
    public LoanApplicationResponse createLoanApplication(@Valid @RequestBody LoanApplicationRequest request) {
        LoanApplication saved = loanApplicationService.createApplication(request);
        return loanApplicationMapper.toResponse(saved);
    }

    @GetMapping
    public List<LoanApplicationResponse> getAll() {
        return loanApplicationService.getAll()
                .stream()
                .map(loanApplicationMapper::toResponse)
                .toList();
    }

    @PostMapping("/{id}/approve")
    public LoanApplicationResponse approveLoanApplication(@PathVariable Long id) {
        LoanApplication approved = loanApplicationService.approveApplication(id);
        return loanApplicationMapper.toResponse(approved);
    }

    @PostMapping("/{id}/reject")
    public LoanApplicationResponse rejectLoanApplication(@PathVariable Long id,
                                                         @Valid @RequestBody RejectLoanRequest request) {
        LoanApplication rejected = loanApplicationService.rejectApplication(id, request.getRejectionReason());
        return loanApplicationMapper.toResponse(rejected);
    }
}