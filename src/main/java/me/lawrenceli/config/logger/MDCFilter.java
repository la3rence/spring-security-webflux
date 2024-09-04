package me.lawrenceli.config.logger;

import io.micrometer.context.ContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import static me.lawrenceli.config.Constants.REQUEST_ID;

@Component
public class MDCFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(MDCFilter.class);

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = request.getId();

        // https://gist.github.com/chemicL/0e0d8e95e28414f0ecb769a5b8ca326e
        ContextRegistry.getInstance().registerThreadLocalAccessor(REQUEST_ID, () -> MDC.get(REQUEST_ID),
                value -> MDC.put(REQUEST_ID, requestId), () -> MDC.remove(REQUEST_ID));
        MDC.put(REQUEST_ID, requestId);

        exchange.getResponse().getHeaders().set(REQUEST_ID, requestId);
        logger.info("Request {} {}", request.getMethod(), request.getPath());

        return chain.filter(exchange)
                .contextWrite(Context.of(REQUEST_ID, requestId))
                .doFinally(s -> MDC.remove(REQUEST_ID));
    }

}
