package com.github.wenqiglantz.service.customer.persistence.repository;

import com.github.wenqiglantz.service.customer.persistence.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    Optional<Customer> findByCustomerId(String customerId);
}
