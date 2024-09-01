package me.lawrenceli.config.security;

import me.lawrenceli.service.UserService;
import me.lawrenceli.utils.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity // reactive security context
@EnableReactiveMethodSecurity // reactive pre authorize check
@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final JWTProvider tokenProvider;

    public SecurityConfig(UserService userService, JWTProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // HttpBasicServerAuthenticationEntryPoint
                .httpBasic(spec -> spec.authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it ->
                        it.pathMatchers("/api/user/**").authenticated()
                                .anyExchange().permitAll()
                )
                .exceptionHandling(spec ->
                        spec.accessDeniedHandler((exchange, denied) -> {
                                    ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
                                    detail.setInstance(exchange.getRequest().getURI());
                                    return ResponseUtil.writeProblem(exchange, detail);
                                })
                                .authenticationEntryPoint((exchange, ignore) ->
                                        ResponseUtil.writeProblem(exchange,
                                                ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED)))
                )
                .authenticationManager(reactiveAuthenticationManager())
                .addFilterAt(new JWTFilter(tokenProvider), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        return name -> userService.getUserDetails(name)
                .map(user -> User.withUsername(name)
                        .password("{noop}" + user.password())
                        .accountLocked(!user.active())
                        .disabled(!user.active())
                        .authorities(user.roles().parallelStream()
                                .map(role -> String.format("ROLE_%s", role.roleName()))
                                .toList().toArray(new String[0]))
                        .build()
                );
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
    }

}
