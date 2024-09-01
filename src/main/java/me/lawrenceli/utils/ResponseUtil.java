package me.lawrenceli.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ResponseUtil {
    private ResponseUtil() {
    }

    private final static ObjectMapper mapper = new ObjectMapper();

    public static Mono<Void> writeProblem(ServerWebExchange exchange, ProblemDetail detail) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setRawStatusCode(detail.getStatus());
        try {
            byte[] bytes = mapper.writeValueAsBytes(detail);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
