package ee.coop.coopbackend.service;

import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.entity.PaymentSchedule;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for generating annuity-based payment schedules.
 */
@Service
public class PaymentScheduleService {

    /**
     * Generates a monthly annuity payment schedule.
     * Calculates principal, interest and remaining balance for each month.
     */
    public List<PaymentSchedule> generateSchedule(LoanApplication loanApplication) {
        List<PaymentSchedule> schedule = new ArrayList<>();

        double principal = loanApplication.getLoanAmount();
        int months = loanApplication.getLoanPeriodMonths();
        double annualRate = loanApplication.getInterestMargin() + loanApplication.getBaseInterestRate();
        // Calculate monthly annuity payment using standard formula
        double monthlyRate = annualRate / 100.0 / 12.0;

        LocalDate firstPaymentDate = LocalDate.now();
        double remainingBalance = principal;

        double monthlyPayment;
        if (monthlyRate == 0) {
            monthlyPayment = principal / months;
        } else {
            monthlyPayment = principal * monthlyRate / (1 - Math.pow(1 + monthlyRate, -months));
        }

        monthlyPayment = round(monthlyPayment);

        for (int i = 1; i <= months; i++) {
            // Calculate interest and principal portions for each payment
            double interestPart = round(remainingBalance * monthlyRate);
            double principalPart = round(monthlyPayment - interestPart);

            if (i == months) {
                principalPart = round(remainingBalance);
                monthlyPayment = round(principalPart + interestPart);
            }

            remainingBalance = round(remainingBalance - principalPart);
            if (remainingBalance < 0) {
                remainingBalance = 0;
            }

            PaymentSchedule payment =  new PaymentSchedule();
            payment.setPaymentNumber(i);
            payment.setPaymentDate(firstPaymentDate.plusMonths(i - 1));
            payment.setMonthlyPayment(monthlyPayment);
            payment.setPrincipalPart(principalPart);
            payment.setInterestPart(interestPart);
            payment.setRemainingBalance(remainingBalance);
            payment.setLoanApplication(loanApplication);

            schedule.add(payment);
        }

        return schedule;
    }

    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}