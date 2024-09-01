package me.lawrenceli.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.lawrenceli.dto.LoginDTO;
import me.lawrenceli.security.JWTProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/login")
@Tag(name = "Login")
public class LoginController {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JWTProvider tokenProvider;

    public LoginController(ReactiveAuthenticationManager reactiveAuthenticationManager, JWTProvider tokenProvider) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping
    @Operation
    public Mono<ResponseEntity<?>> login(@Valid @RequestBody Mono<LoginDTO> dto) {
        return dto.flatMap(login -> reactiveAuthenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(login.username(), login.password()))
                        .map(tokenProvider::createToken))
                .map(jwt -> {
                    var tokenBody = Map.of("access_token", jwt);
                    return ResponseEntity.ok(tokenBody);
                });
    }

}
