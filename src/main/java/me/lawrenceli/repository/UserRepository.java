package me.lawrenceli.repository;

import me.lawrenceli.model.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findByName(String name);

    //    @Query("""
    //           select user.*, role.* from user
    //           left join user_role on user.user_id = user_role.user_id
    //           left join role on user_role.role_id = role.role_id
    //           where name = :name
    //           """)
    //    Mono<?> test(String name);


}
