package ee.coop.coopbackend.service;

import ee.coop.coopbackend.dto.LoanApplicationRequest;
import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.entity.PaymentSchedule;
import ee.coop.coopbackend.entity.RejectionReason;
import ee.coop.coopbackend.entity.Status;
import ee.coop.coopbackend.mapper.LoanApplicationMapper;
import ee.coop.coopbackend.repository.LoanApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private PaymentScheduleService paymentScheduleService;

    @Mock
    private LoanApplicationMapper loanApplicationMapper;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(loanApplicationService, "maxAge", 70);
    }

    @Test
    void createApplication_shouldRejectUnderageApplicant() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setPersonalCode("61001010000");

        LoanApplication mapped = new LoanApplication();
        mapped.setPersonalCode(request.getPersonalCode());
        mapped.setLoanAmount(5000);
        mapped.setLoanPeriodMonths(12);

        when(loanApplicationMapper.toEntity(request)).thenReturn(mapped);
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication result = loanApplicationService.createApplication(request);

        assertEquals(Status.REJECTED, result.getStatus());
        assertEquals(RejectionReason.UNDERAGE, result.getRejectionReason());
        verify(paymentScheduleService, never()).generateSchedule(any());
    }

    @Test
    void createApplication_shouldRejectTooOldApplicant() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setPersonalCode("35001010000");

        LoanApplication mapped = new LoanApplication();
        mapped.setPersonalCode(request.getPersonalCode());
        mapped.setLoanAmount(5000);
        mapped.setLoanPeriodMonths(12);

        when(loanApplicationMapper.toEntity(request)).thenReturn(mapped);
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication result = loanApplicationService.createApplication(request);

        assertEquals(Status.REJECTED, result.getStatus());
        assertEquals(RejectionReason.CUSTOMER_TOO_OLD, result.getRejectionReason());
        verify(paymentScheduleService, never()).generateSchedule(any());
    }

    @Test
    void createApplication_shouldRejectRiskTooHighApplicant() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setPersonalCode("50601010000");
        request.setLoanAmount(10000);

        LoanApplication mapped = new LoanApplication();
        mapped.setPersonalCode(request.getPersonalCode());
        mapped.setLoanAmount(10000);
        mapped.setLoanPeriodMonths(24);

        when(loanApplicationMapper.toEntity(request)).thenReturn(mapped);
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication result = loanApplicationService.createApplication(request);

        assertEquals(Status.REJECTED, result.getStatus());
        assertEquals(RejectionReason.RISK_TOO_HIGH, result.getRejectionReason());
        verify(paymentScheduleService, never()).generateSchedule(any());
    }

    @Test
    void createApplication_shouldSetInReviewAndGenerateSchedule() {
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setPersonalCode("49403136526");
        request.setLoanAmount(5000);

        LoanApplication mapped = new LoanApplication();
        mapped.setPersonalCode(request.getPersonalCode());
        mapped.setLoanAmount(5000);
        mapped.setLoanPeriodMonths(24);

        PaymentSchedule payment1 = new PaymentSchedule();
        payment1.setPaymentNumber(1);

        List<PaymentSchedule> schedule = List.of(payment1);

        when(loanApplicationMapper.toEntity(request)).thenReturn(mapped);
        when(paymentScheduleService.generateSchedule(any(LoanApplication.class))).thenReturn(schedule);
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication result = loanApplicationService.createApplication(request);

        assertEquals(Status.IN_REVIEW, result.getStatus());
        assertNull(result.getRejectionReason());
        assertEquals(1, result.getPaymentSchedules().size());
        verify(paymentScheduleService).generateSchedule(mapped);
    }

    @Test
    void approveApplication_shouldSetStatusApproved() {
        LoanApplication application = new LoanApplication();
        application.setId(1L);
        application.setStatus(Status.IN_REVIEW);

        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication result = loanApplicationService.approveApplication(1L);

        assertEquals(Status.APPROVED, result.getStatus());
        assertNull(result.getRejectionReason());
    }

    @Test
    void approveApplication_shouldThrowIfNotInReview() {
        LoanApplication application = new LoanApplication();
        application.setId(1L);
        application.setStatus(Status.REJECTED);

        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.approveApplication(1L)
        );

        assertEquals("Only IN_REVIEW applications can be approved", ex.getMessage());
        verify(loanApplicationRepository, never()).save(any());
    }

    @Test
    void rejectApplication_shouldSetstatusRejected() {
        LoanApplication application = new LoanApplication();
        application.setId(1L);
        application.setStatus(Status.IN_REVIEW);

        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication result = loanApplicationService.rejectApplication(1L, RejectionReason.MANUAL_REJECT);

        assertEquals(Status.REJECTED, result.getStatus());
        assertEquals(RejectionReason.MANUAL_REJECT, result.getRejectionReason());
    }

    @Test
    void rejectApplication_shouldThrowIfNotInReview() {
        LoanApplication application = new LoanApplication();
        application.setId(1L);
        application.setStatus(Status.APPROVED);

        when(loanApplicationRepository.findById(1L)).thenReturn(Optional.of(application));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> loanApplicationService.rejectApplication(1L, RejectionReason.MANUAL_REJECT)
        );

        assertEquals("Only IN_REVIEW applications can be rejected", ex.getMessage());
        verify(loanApplicationRepository, never()).save(any());
    }
}