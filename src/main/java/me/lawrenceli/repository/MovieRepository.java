package me.lawrenceli.repository;

import me.lawrenceli.model.entity.Movie;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovieRepository extends R2dbcRepository<Movie, Long> {

    Flux<Movie> findByTitle(String title);

}
