package ee.coop.coopbackend.service;

import ee.coop.coopbackend.entity.LoanApplication;
import ee.coop.coopbackend.entity.RejectionReason;
import ee.coop.coopbackend.entity.Status;
import ee.coop.coopbackend.repository.LoanApplicationRepository;
import ee.coop.coopbackend.util.PersonalCodeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

    @Value("${loan.max-age}")
    private int maxAge;

    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
    }

    public LoanApplication createApplication(LoanApplication loanApplication) {
        int age = calculateAge(loanApplication.getPersonalCode());

        if (age > maxAge) {
            loanApplication.setStatus(Status.REJECTED);
            loanApplication.setRejectionReason(RejectionReason.CUSTOMER_TOO_OLD);
            return loanApplicationRepository.save(loanApplication);
        }

        loanApplication.setStatus(Status.NEW);
        loanApplication.setRejectionReason(null);
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