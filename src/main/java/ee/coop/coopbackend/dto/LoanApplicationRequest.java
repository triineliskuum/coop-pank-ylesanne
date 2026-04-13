package ee.coop.coopbackend.dto;

import jakarta.validation.constraints.*;

public class LoanApplicationRequest {

    @NotBlank
    @Size(max = 32)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    private String lastName;

    @NotBlank
    private String personalCode;

    @Min(6)
    @Max(360)
    private int loanPeriodMonths;

    @DecimalMin("0.0")
    private double interestMargin;

    @DecimalMin("0.0")
    private double baseInterestRate;

    @Min(5000)
    private double loanAmount;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public int getLoanPeriodMonths() {
        return loanPeriodMonths;
    }

    public void setLoanPeriodMonths(int loanPeriodMonths) {
        this.loanPeriodMonths = loanPeriodMonths;
    }

    public double getInterestMargin() {
        return interestMargin;
    }

    public void setInterestMargin(double interestMargin) {
        this.interestMargin = interestMargin;
    }

    public double getBaseInterestRate() {
        return baseInterestRate;
    }

    public void setBaseInterestRate(double baseInterestRate) {
        this.baseInterestRate = baseInterestRate;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }
}
