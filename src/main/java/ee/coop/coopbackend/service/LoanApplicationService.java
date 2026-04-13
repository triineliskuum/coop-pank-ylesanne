package ee.coop.coopbackend.service;

import ee.coop.coopbackend.dto.LoanApplicationRequest;
import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.entity.RejectionReason;
import ee.coop.coopbackend.entity.Status;
import ee.coop.coopbackend.mapper.LoanApplicationMapper;
import ee.coop.coopbackend.repository.LoanApplicationRepository;
import ee.coop.coopbackend.util.PersonalCodeUtils;
import ee.coop.coopbackend.entity.PaymentSchedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final PaymentScheduleService paymentScheduleService;
    private final LoanApplicationMapper loanApplicationMapper;

    @Value("${loan.max-age}")
    private int maxAge;

    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository,
                                  PaymentScheduleService paymentScheduleService,
                                  LoanApplicationMapper loanApplicationMapper) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.paymentScheduleService = paymentScheduleService;
        this.loanApplicationMapper = loanApplicationMapper;
    }

    public LoanApplication createApplication(LoanApplicationRequest request) {
        LoanApplication loanApplication = loanApplicationMapper.toEntity(request);

        int age = calculateAge(loanApplication.getPersonalCode());

        if (age < 18) {
            loanApplication.setStatus(Status.REJECTED);
            loanApplication.setRejectionReason(RejectionReason.UNDERAGE);
        } else if (age > maxAge) {
            loanApplication.setStatus(Status.REJECTED);
            loanApplication.setRejectionReason(RejectionReason.CUSTOMER_TOO_OLD);
        } else if (loanApplication.getLoanAmount() > 5000 && age < 21) {
            loanApplication.setStatus(Status.REJECTED);
            loanApplication.setRejectionReason(RejectionReason.RISK_TOO_HIGH);
        } else {
            loanApplication.setStatus(Status.IN_REVIEW);
            loanApplication.setRejectionReason(null);

            List<PaymentSchedule> schedule = paymentScheduleService.generateSchedule(loanApplication);
            loanApplication.setPaymentSchedules(schedule);
        }

        return loanApplicationRepository.save(loanApplication);
    }

    public List<LoanApplication> getAll() {
        return loanApplicationRepository.findAll();
    }

    public LoanApplication approveApplication(Long id) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan application not found"));

        if (loanApplication.getStatus() != Status.IN_REVIEW) {
            throw new IllegalArgumentException("Only IN_REVIEW applications can be approved");
        }

        loanApplication.setStatus(Status.APPROVED);
        loanApplication.setRejectionReason(null);

        return loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication rejectApplication(Long id, RejectionReason rejectionReason) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Loan application not found"));

        if (loanApplication.getStatus() != Status.IN_REVIEW) {
            throw new IllegalArgumentException("Only IN_REVIEW applications can be rejected");
        }

        loanApplication.setStatus(Status.REJECTED);
        loanApplication.setRejectionReason(rejectionReason);

        return loanApplicationRepository.save(loanApplication);
    }

    private int calculateAge(String personalCode) {
        try {
            LocalDate birthDate = PersonalCodeUtils.extractBirthDate(personalCode);
            return Period.between(birthDate, LocalDate.now()).getYears();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid personal code");
        }
    }
}