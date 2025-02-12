package com.loyalty.rewards.dtos;

import com.loyalty.rewards.domain.enums.SchemeStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CustomerDTO {
    private SchemeStatus schemeStatus;
    private String schemeDescription;

    public SchemeStatus getSchemeStatus() {
        return schemeStatus;
    }

    public void setSchemeStatus(SchemeStatus schemeStatus) {
        this.schemeStatus = schemeStatus;
    }

    public String getSchemeDescription() {
        return schemeDescription;
    }

    public void setSchemeDescription(String schemeDescription) {
        this.schemeDescription = schemeDescription;
    }
}
