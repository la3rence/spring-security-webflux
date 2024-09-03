package me.lawrenceli.service;

import jakarta.validation.constraints.NotNull;
import me.lawrenceli.model.Page;
import me.lawrenceli.model.PageDTO;
import me.lawrenceli.model.entity.Movie;
import me.lawrenceli.repository.MovieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Flux<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Flux<Movie> findByTitle(@NotNull String title) {
        return movieRepository.findByTitle(title);
    }

    public Mono<Page<Movie>> page(@Nullable String title, PageDTO page) {
        return StringUtils.hasText(title) ?
                movieRepository.findByTitleContaining(title, PageRequest.of(page.page(), page.size()))
                        .collectList().zipWith(movieRepository.countByTitleContaining(title), Page::new) :
                movieRepository.findBy(PageRequest.of(page.page(), page.size())).collectList()
                        .zipWith(movieRepository.count(), Page::new);
    }
}
