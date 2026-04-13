package ee.coop.coopbackend.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentScheduleResponse {

    private int paymentNumber;
    private LocalDate paymentDate;
    private double monthlyPayment;
    private double principalPart;
    private double interestPart;
    private double remainingBalance;
}
