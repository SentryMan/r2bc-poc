package com.jojo.r2bc.poc;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import com.jojo.r2bc.poc.domain.Customer;
import com.jojo.r2bc.poc.repo.CustomerRepository;

@SpringBootApplication
public class R2bcPocApplication {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(R2bcPocApplication.class);

  public static void main(String[] args) {

    new SpringApplicationBuilder(R2bcPocApplication.class)
        .web(WebApplicationType.REACTIVE)
        .run(args);
  }

  @Bean
  public CommandLineRunner demo(CustomerRepository repository) {

    return args -> {
      // save a few customers
      repository
          .saveAll(
              List.of(
                  new Customer("Jack", "Bauer"),
                  new Customer("Chloe", "O'Brian"),
                  new Customer("Kim", "Bauer"),
                  new Customer("David", "Palmer"),
                  new Customer("Michelle", "Dessler")))
          .blockLast();

      // fetch all customers
      log.info(
          """
          		Customers found with findAll():
          		-------------------------------""");

      repository.findAll().map(Object::toString).doOnNext(log::info).blockLast();

      log.info("");

      // fetch an individual customer by ID
      repository
          .findById(1L)
          .doOnNext(
              customer ->
                  log.info(
                      """
                		"Customer found with findById(1L):"
                		--------------------------------
                		{}

                		""",
                      customer.toString()))
          .block(Duration.ofSeconds(10));

      // fetch customers by last name
      log.info("Customer found with findByLastName('Bauer'):");
      log.info("--------------------------------------------");
      repository.findByLastName("Bauer").map(Object::toString).blockLast(Duration.ofSeconds(10));
      log.info("");
    };
  }
}
