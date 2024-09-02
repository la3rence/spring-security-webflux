package me.lawrenceli.config;

import me.lawrenceli.config.exception.CheckException;
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

    @ExceptionHandler(CheckException.class)
    public Mono<ProblemDetail> handle400(CheckException e) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setDetail(e.getDetail());
        return Mono.just(detail);
    }

}
