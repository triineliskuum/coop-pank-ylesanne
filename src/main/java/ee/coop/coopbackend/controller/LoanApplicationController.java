package ee.coop.coopbackend.controller;

import ee.coop.coopbackend.dto.LoanApplicationRequest;
import ee.coop.coopbackend.dto.LoanApplicationResponse;
import ee.coop.coopbackend.dto.PaymentScheduleResponse;
import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.service.LoanApplicationService;
import ee.coop.coopbackend.dto.RejectLoanRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    public LoanApplicationResponse createLoanApplication(@Valid @RequestBody LoanApplicationRequest request) {
        LoanApplication saved = loanApplicationService.createApplication(request);
        return mapToResponse(saved);
    }

    @GetMapping
    public List<LoanApplicationResponse> getAll() {
        return loanApplicationService.getAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PostMapping("/{id}/approve")
    public LoanApplicationResponse approveLoanApplication(@PathVariable Long id) {
        LoanApplication approved = loanApplicationService.approveApplication(id);
        return mapToResponse(approved);
    }

    @PostMapping("/{id}/reject")
    public LoanApplicationResponse rejectLoanApplication(@PathVariable Long id,
                                                         @Valid @RequestBody RejectLoanRequest request) {
        LoanApplication rejected = loanApplicationService.rejectApplication(id, request.getRejectionReason());
        return mapToResponse(rejected);
    }

    private LoanApplicationResponse mapToResponse(LoanApplication loanApplication) {
        LoanApplicationResponse response = new LoanApplicationResponse();
        response.setId(loanApplication.getId());
        response.setFirstName(loanApplication.getFirstName());
        response.setLastName(loanApplication.getLastName());
        response.setPersonalCode(loanApplication.getPersonalCode());
        response.setLoanPeriodMonths(loanApplication.getLoanPeriodMonths());
        response.setInterestMargin(loanApplication.getInterestMargin());
        response.setBaseInterestRate(loanApplication.getBaseInterestRate());
        response.setLoanAmount(loanApplication.getLoanAmount());
        response.setStatus(loanApplication.getStatus());
        response.setRejectionReason(loanApplication.getRejectionReason());

        response.setPaymentSchedules(
                loanApplication.getPaymentSchedules()
                        .stream()
                        .map(this::mapPaymentScheduleToResponse)
                        .toList()
        );

        return response;
    }

    private PaymentScheduleResponse mapPaymentScheduleToResponse(ee.coop.coopbackend.entity.PaymentSchedule payment) {
        PaymentScheduleResponse response = new PaymentScheduleResponse();
        response.setPaymentNumber(payment.getPaymentNumber());
        response.setPaymentDate(payment.getPaymentDate());
        response.setMonthlyPayment(payment.getMonthlyPayment());
        response.setPrincipalPart(payment.getPrincipalPart());
        response.setInterestPart(payment.getInterestPart());
        response.setRemainingBalance(payment.getRemainingBalance());
        return response;
    }
}