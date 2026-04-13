package ee.coop.coopbackend.repository;

import ee.coop.coopbackend.entity.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
}