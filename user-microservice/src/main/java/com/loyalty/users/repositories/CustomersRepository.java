package com.loyalty.users.repositories;

import com.loyalty.users.domain.Customer;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {

}
