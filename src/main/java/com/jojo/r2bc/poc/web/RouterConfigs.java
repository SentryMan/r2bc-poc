package com.jojo.r2bc.poc.web;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jojo.r2bc.poc.web.handler.RequestHandler;

@Configuration
public class RouterConfigs {

  private static final String BASEPATH = "/r2bc-test";

  @Bean
  public RouterFunction<ServerResponse> router(RequestHandler handler, RequestFilter filter) {

    return RouterFunctions.route(GET(BASEPATH + "/get/{id}"), handler::getByID)
        .andRoute(GET(BASEPATH + "/lastname/{name}"), handler::getByLastName)
        .andRoute(POST(BASEPATH + "/save"), handler::saveCustomer)
        .andRoute(DELETE(BASEPATH + "/delete/{id}"), handler::deleteByID)
        .andRoute(GET(BASEPATH + "/health"), r -> ServerResponse.ok().bodyValue("{status:UP}"))
        .filter(filter);
  }
}
