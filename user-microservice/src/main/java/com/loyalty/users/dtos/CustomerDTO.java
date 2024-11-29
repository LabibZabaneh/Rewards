package com.loyalty.users.dtos;

import com.loyalty.users.domain.enums.SchemeStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CustomerDTO {

    private String name;
    private String email;
    private SchemeStatus schemeStatus;
    private String schemeDescription;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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
