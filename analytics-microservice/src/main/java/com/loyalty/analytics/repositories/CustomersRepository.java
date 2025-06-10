package com.loyalty.analytics.repositories;

import com.loyalty.analytics.domain.Customer;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {

    @Join(value = "dailyStampCounts", type = Join.Type.LEFT_FETCH)
    Optional<Customer> findById(long id);

}
