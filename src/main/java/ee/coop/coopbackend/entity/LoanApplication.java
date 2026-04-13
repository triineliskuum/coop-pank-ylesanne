package ee.coop.coopbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 32)
    @NotBlank
    private String firstName;

    @Size(max = 32)
    @NotBlank
    private String lastName;

    @NotBlank
    private String personalCode;

    @Min(6)
    @Max(360)
    private int loanPeriodMonths;

    @DecimalMin("0.0")
    private double interestMargin;

    private double baseInterestRate;

    @Min(5000)
    private double loanAmount;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private RejectionReason rejectionReason;

    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<PaymentSchedule> paymentSchedules = new java.util.ArrayList<>();
}
