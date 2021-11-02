package com.jojo.r2bc.poc.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.jojo.r2bc.poc.domain.Customer;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

  @Query("SELECT * FROM customer WHERE last_name = :lastname")
  Flux<Customer> findByLastName(String lastName);
}
