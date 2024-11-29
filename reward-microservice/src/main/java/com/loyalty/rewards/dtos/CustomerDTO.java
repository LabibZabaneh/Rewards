package com.loyalty.rewards.dtos;

import com.loyalty.rewards.domain.enums.SchemeStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CustomerDTO {
    private SchemeStatus status;
    private String schemeDescription;

    public SchemeStatus getStatus() {
        return status;
    }

    public void setStatus(SchemeStatus status) {
        this.status = status;
    }

    public String getSchemeDescription() {
        return schemeDescription;
    }

    public void setSchemeDescription(String schemeDescription) {
        this.schemeDescription = schemeDescription;
    }
}
