# Coop Loan Approval Process Backend

This project is a backend application for handling loan applications and approval processes.  
It was built as part of a Coop Pank internship assignment.

---

##

The system allows:
- Submitting loan applications
- Validating applicant data (including Estonian personal code)
- Automatic decision logic (age, risk rules)
- Generating annuity-based payment schedules
- Reviewing applications (approve / reject)

---

##

- Java 21
- Spring Boot 3
- PostgreSQL
- Docker & Docker Compose
- Spring Data JPA
- SpringDoc OpenAPI (Swagger)
- JUnit 5 + Mockito

---

## How to Run

### 1. Start database

```bash
docker compose up -d
```

### 2. Run application

From Intellij or terminal:

```bash
./mvnw spring-boot:run
```

### 3. Open Swagger

```bash
http://localhost:8080/swagger-ui/index.html
```

---

## API Endpoints

Loan application
- POST /loans – create loan application
- GET /loans – get all applications

Review process
- POST /loans/{id}/approve – approve application
- POST /loans/{id}/reject – reject application

---

## Business Logic

Validation rules
- Personal code must follow Estonian format
- Loan amount must be at least 5000 €
- Loan period: 6–360 months

Decision logic

Application is automatically rejected if:
- Applicant is under 18 → UNDERAGE
- Applicant is over max age (configurable) → CUSTOMER_TOO_OLD
- High risk (young + large loan) → RISK_TOO_HIGH

Otherwise:
- Status is set to IN_REVIEW

---

## Payment Schedule
- Annuity-based monthly payments
- First payment date = current date
- Stored in database
- Returned in API response

---

## Review Flow

Applications in IN_REVIEW can be:
- Approved → APPROVED
- Rejected → REJECTED (manual reason required)

---

## Testing

Unit tests are implemented using:
- JUnit 5
- Mockito

Tested components:
- LoanApplicationService (business logic)
- PaymentScheduleService (annuity calculation)
- PersonalCodeUtils (validation and parsing)

Run tests:
```bash
./mvnw test
```

---

## Configuration

Example:
```bash
loan.max-age=70
```

---

## Docker

Application uses Docker for database:
```bash
postgres:
  image: postgres:16
  ...
```

---

## Notes
- DTO pattern is used to separate API and database models
- Mapping logic is handled via a dedicated mapper class
- Global exception handling ensures clean API responses
