package ee.coop.coopbackend.dto;

import ee.coop.coopbackend.entity.RejectionReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectLoanRequest {

    @NotNull
    private RejectionReason rejectionReason;
}
