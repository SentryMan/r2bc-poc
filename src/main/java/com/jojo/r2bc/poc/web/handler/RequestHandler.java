package com.jojo.r2bc.poc.web.handler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jojo.r2bc.poc.domain.Customer;
import com.jojo.r2bc.poc.repo.CustomerRepository;

import reactor.core.publisher.Mono;

@Component
@Transactional
public class RequestHandler {

  private final CustomerRepository repo;

  public RequestHandler(CustomerRepository repo) {

    this.repo = repo;
  }

  public Mono<ServerResponse> getByID(ServerRequest request) {

    return Mono.just(request.pathVariable("id"))
        .map(Long::valueOf)
        .flatMap(repo::findById)
        .flatMap(ServerResponse.ok()::bodyValue);
  }

  public Mono<ServerResponse> getByLastName(ServerRequest request) {

    return Mono.just(request.pathVariable("name"))
        .flatMapMany(repo::findByLastName)
        .collectList()
        .flatMap(ServerResponse.ok()::bodyValue);
  }

  public Mono<ServerResponse> saveCustomer(ServerRequest request) {

    return request
        .bodyToMono(Customer.class)
        .flatMapMany(repo::save)
        .collectList()
        .flatMap(ServerResponse.ok()::bodyValue);
  }

  public Mono<ServerResponse> deleteByID(ServerRequest request) {

    return Mono.just(request.pathVariable("id"))
        .map(Long::valueOf)
        .flatMapMany(repo::deleteById)
        .collectList()
        .flatMap(ServerResponse.ok()::bodyValue);
  }
}
