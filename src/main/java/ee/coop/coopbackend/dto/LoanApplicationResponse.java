package ee.coop.coopbackend.dto;

import ee.coop.coopbackend.entity.RejectionReason;
import ee.coop.coopbackend.entity.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanApplicationResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String personalCode;
    private int loanPeriodMonths;
    private double interestMargin;
    private double baseInterestRate;
    private double loanAmount;
    private Status status;
    private RejectionReason rejectionReason;
    private java.util.List<PaymentScheduleResponse> paymentSchedules;
}