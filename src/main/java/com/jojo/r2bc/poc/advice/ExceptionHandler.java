package com.jojo.r2bc.poc.advice;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.jojo.r2bc.poc.domain.ErrorResponse;

import reactor.core.publisher.Mono;

/**
 * This class handles exceptions and returns a corresponding ServerResponse
 *
 * @authors nhn485, bzp194
 */
@Component
public class ExceptionHandler {

  private static final org.slf4j.Logger log =
      org.slf4j.LoggerFactory.getLogger(ExceptionHandler.class);

  public Mono<ServerResponse> handle(Throwable exception) {
    log.error("Unhandled Exception : ", exception);
    final var text = exception.getMessage();
    final var errorResponse = new ErrorResponse("50003", text);

    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(errorResponse);
  }
}
