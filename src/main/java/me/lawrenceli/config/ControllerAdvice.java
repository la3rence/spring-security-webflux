package me.lawrenceli.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ControllerAdvice /* extends ResponseEntityExceptionHandler */ {

    @ExceptionHandler(AuthenticationException.class)
    public Mono<ProblemDetail> handle401(AuthenticationException e) {
        return Mono.just(ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED));
    }

}
