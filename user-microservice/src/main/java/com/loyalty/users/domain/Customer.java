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
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private SchemeStatus schemeStatus;
    @Column(nullable = false)
    private String schemeDescription;

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
