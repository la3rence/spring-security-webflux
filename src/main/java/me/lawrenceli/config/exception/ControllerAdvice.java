package me.lawrenceli.config.exception;

import me.lawrenceli.utils.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ControllerAdvice /* extends ResponseEntityExceptionHandler */ {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<R<ProblemDetail>> handle401(AuthenticationException e) {
        return Mono.just(R.fail(e.getMessage()));
    }

    @ExceptionHandler(CheckException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<R<ProblemDetail>> handle400(CheckException e) {
        return Mono.just(R.fail(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<R<ProblemDetail>> handle400(IllegalArgumentException e) {
        return Mono.just(R.fail(e.getMessage()));
    }

}
