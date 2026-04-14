package ee.coop.coopbackend.service;

import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.entity.PaymentSchedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentScheduleServiceTest {

    private final PaymentScheduleService paymentScheduleService = new PaymentScheduleService();

    @Test
    void generateSchedule_shouldCreateCorrectNumberOfPayments() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(5000);
        loanApplication.setLoanPeriodMonths(24);
        loanApplication.setInterestMargin(1.5);
        loanApplication.setBaseInterestRate(2.3);

        List<PaymentSchedule> schedule = paymentScheduleService.generateSchedule(loanApplication);

        assertEquals(24, schedule.size());
    }

    @Test
    void generateSchedule_shouldSetFirstPaymentDateToToday() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(5000);
        loanApplication.setLoanPeriodMonths(12);
        loanApplication.setInterestMargin(1.5);
        loanApplication.setBaseInterestRate(2.3);

        List<PaymentSchedule> schedule = paymentScheduleService.generateSchedule(loanApplication);

        assertEquals(LocalDate.now(), schedule.get(0).getPaymentDate());
    }

    @Test
    void generateSchedule_shouldSetLastRemainingBalanceToZero() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(5000);
        loanApplication.setLoanPeriodMonths(12);
        loanApplication.setInterestMargin(1.5);
        loanApplication.setBaseInterestRate(2.3);

        List<PaymentSchedule> schedule = paymentScheduleService.generateSchedule(loanApplication);

        PaymentSchedule lastPayment = schedule.get(schedule.size() - 1);

        assertEquals(0.0, lastPayment.getRemainingBalance());
    }

    @Test
    void generateSchedule_shouldCreatePositiveMonthlyPayments() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(5000);
        loanApplication.setLoanPeriodMonths(12);
        loanApplication.setInterestMargin(1.5);
        loanApplication.setBaseInterestRate(2.3);

        List<PaymentSchedule> schedule = paymentScheduleService.generateSchedule(loanApplication);

        assertTrue(schedule.stream().allMatch(payment -> payment.getMonthlyPayment() > 0));
    }

    @Test
    void generateSchedule_shouldWorkWithZeroInterestRate() {
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanAmount(1200);
        loanApplication.setLoanPeriodMonths(12);
        loanApplication.setInterestMargin(0.0);
        loanApplication.setBaseInterestRate(0.0);

        List<PaymentSchedule> schedule = paymentScheduleService.generateSchedule(loanApplication);

        assertEquals(12, schedule.size());
        assertEquals(100.0, schedule.get(0).getMonthlyPayment());
        assertEquals(0.0, schedule.get(schedule.size() - 1).getRemainingBalance());
    }
}