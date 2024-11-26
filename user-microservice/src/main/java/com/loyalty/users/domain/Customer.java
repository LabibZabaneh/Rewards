package com.loyalty.users.domain;

import com.loyalty.users.domain.enums.SchemeStatus;
import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.*;


@Entity
@Serdeable
public class Customer {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private SchemeStatus schemeStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
