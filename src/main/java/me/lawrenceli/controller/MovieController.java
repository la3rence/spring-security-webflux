package me.lawrenceli.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.lawrenceli.model.entity.Movie;
import me.lawrenceli.service.MovieService;
import me.lawrenceli.utils.R;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lawrence.li
 * @since 2024/08/31
 */
@RestController
@RequestMapping("/test")
@Tag(name = "Resource")
public class MovieController {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final MovieService movieService;

    public MovieController(ReactiveRedisTemplate<String, String> reactiveRedisTemplate, MovieService movieService) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.movieService = movieService;
    }

    @GetMapping("/movie")
    @Operation(summary = "movie")
    @ResponseStatus(HttpStatus.OK)
    public Mono<R<List<Movie>>> movies(@RequestParam(required = false) String title) {
        if (StringUtils.hasText(title)) {
            return movieService.findByTitle(title).collectList().map(R::success);
        }
        return movieService.findAll().collectList().map(R::success);
    }

    @GetMapping("/hello")
    public Mono<R<String>> hello() {
        // reactive redis query
        return reactiveRedisTemplate.<String, String>opsForHash().get("test", "hi").map(R::success);
    }
}
