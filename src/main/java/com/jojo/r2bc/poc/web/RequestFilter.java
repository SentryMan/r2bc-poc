package com.jojo.r2bc.poc.web;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jojo.r2bc.poc.advice.ExceptionHandler;

import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * Builds Response Mono for Routed Requests. <br>
 * <br>
 * Runtime errors in the stream are passed to advice through this class
 *
 * @author nhn485
 */
@Component
public class RequestFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(RequestFilter.class);

  private final ExceptionHandler advice;

  public RequestFilter(ExceptionHandler advice) {
    this.advice = advice;
  }

  @Override
  public Mono<ServerResponse> filter(
      ServerRequest serverRequest, HandlerFunction<ServerResponse> handlerFunction) {

    final var requestStart = Instant.now();
    final Map<String, Object> requestDetails = new HashMap<>();

    // create map of request details
    requestDetails.put("HTTP Method", serverRequest.method());
    requestDetails.put("URI", serverRequest);
    requestDetails.putAll(serverRequest.pathVariables());
    requestDetails.putAll(serverRequest.queryParams());

    return handlerFunction
        // call handler Method
        .handle(serverRequest)
        // handle exceptions
        .onErrorResume(advice::handle)
        // log response
        .doOnNext(
            serverResponse -> {
              final var end = Instant.now();
              final var responseDetails =
                  Map.of(
                      "Response Time",
                      Duration.between(requestStart, end).toMillis(),
                      "HTTP Status",
                      serverResponse.rawStatusCode());

              log.info("API_Response:\n{}", responseDetails);
            })
        // add request details to reactor context for logging
        .contextWrite(Context.of(requestDetails));
  }
}
