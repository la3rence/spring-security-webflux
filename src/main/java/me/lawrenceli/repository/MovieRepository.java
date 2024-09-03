package me.lawrenceli.repository;

import me.lawrenceli.model.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovieRepository extends R2dbcRepository<Movie, Long> {

    Flux<Movie> findByTitle(String title);

    Flux<Movie> findByTitleContaining(String title, Pageable pageable);

    Flux<Movie> findBy(Pageable pageable);

    Mono<Long> countByTitleContaining(String title);

}
