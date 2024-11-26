package com.loyalty.users.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CustomerDTO {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
