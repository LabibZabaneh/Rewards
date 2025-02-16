package com.loyalty.analytics.repositories;

import com.loyalty.analytics.domain.Customer;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
}
