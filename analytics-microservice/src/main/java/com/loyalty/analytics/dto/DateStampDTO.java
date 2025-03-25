package com.loyalty.analytics.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record DateStampDTO(LocalDate date, String day, int stampCount) {}
