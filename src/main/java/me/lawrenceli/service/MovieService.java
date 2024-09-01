package me.lawrenceli.service;

import jakarta.validation.constraints.NotNull;
import me.lawrenceli.model.entity.Movie;
import me.lawrenceli.repository.MovieRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
}
