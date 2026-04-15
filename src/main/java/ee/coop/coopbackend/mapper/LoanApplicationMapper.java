package ee.coop.coopbackend.mapper;

import ee.coop.coopbackend.dto.LoanApplicationRequest;
import ee.coop.coopbackend.dto.LoanApplicationResponse;
import ee.coop.coopbackend.dto.PaymentScheduleResponse;
import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.entity.PaymentSchedule;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between DTOs and entities.
 */
@Component
public class LoanApplicationMapper {

    // Converts request DTO to entity
    public LoanApplication toEntity(LoanApplicationRequest request) {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setFirstName(request.getFirstName());
        loanApplication.setLastName(request.getLastName());
        loanApplication.setPersonalCode(request.getPersonalCode());
        loanApplication.setLoanPeriodMonths(request.getLoanPeriodMonths());
        loanApplication.setInterestMargin(request.getInterestMargin());
        loanApplication.setBaseInterestRate(request.getBaseInterestRate());
        loanApplication.setLoanAmount(request.getLoanAmount());
        return loanApplication;
    }

    // Converts entity to response DTO
    public LoanApplicationResponse toResponse(LoanApplication loanApplication) {
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
                        .map(this::toPaymentScheduleResponse)
                        .toList()
        );

        return response;
    }

    public PaymentScheduleResponse toPaymentScheduleResponse(PaymentSchedule payment) {
        PaymentScheduleResponse paymentResponse = new PaymentScheduleResponse();
        paymentResponse.setPaymentNumber(payment.getPaymentNumber());
        paymentResponse.setPaymentDate(payment.getPaymentDate());
        paymentResponse.setMonthlyPayment(payment.getMonthlyPayment());
        paymentResponse.setPrincipalPart(payment.getPrincipalPart());
        paymentResponse.setInterestPart(payment.getInterestPart());
        paymentResponse.setRemainingBalance(payment.getRemainingBalance());
        return paymentResponse;
    }
}