package com.loyalty.rewards.repositories;

import com.loyalty.rewards.domain.Customer;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
}
