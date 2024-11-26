package com.loyalty.users.dto;

import com.loyalty.users.domain.enums.SchemeStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CustomerDTO {
    private String name;
    private SchemeStatus schemeStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SchemeStatus getSchemeStatus() {
        return schemeStatus;
    }

    public void setSchemeStatus(SchemeStatus schemeStatus) {
        this.schemeStatus = schemeStatus;
    }
}
