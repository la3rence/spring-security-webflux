package me.lawrenceli.config.exception;

import me.lawrenceli.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ControllerAdvice /* extends ResponseEntityExceptionHandler */ {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Mono<R<String>> handle401(AuthenticationException e) {
        logger.warn(e.getMessage());
        return R.monoFail(e.getMessage());
    }

    @ExceptionHandler(CheckException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<R<String>> handle400(CheckException e) {
        return R.monoFail(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<R<String>> handle400(IllegalArgumentException e) {
        return R.monoFail(e.getMessage());
    }

}
