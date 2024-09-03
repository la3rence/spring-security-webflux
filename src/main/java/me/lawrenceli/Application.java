package me.lawrenceli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@EnableWebFlux
@EnableCaching
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(Application.class, args);
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    // }
}

