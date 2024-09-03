package me.lawrenceli.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import me.lawrenceli.config.exception.CheckException;
import me.lawrenceli.model.dto.UserDTO;
import me.lawrenceli.model.entity.Role;
import me.lawrenceli.model.entity.User;
import me.lawrenceli.model.entity.UserRole;
import me.lawrenceli.model.vo.UserVO;
import me.lawrenceli.repository.RoleRepository;
import me.lawrenceli.repository.UserRepository;
import me.lawrenceli.repository.UserRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       RoleRepository roleRepository, R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    public Mono<User> getUserByName(@NotNull String name) {
        return userRepository.findByName(name);
    }

    public Flux<Role> getRolesByUserName(@NotNull String name) {
        return userRepository.findByName(name)
                .flatMapMany(user -> userRoleRepository.findByUserId(user.userId())
                        .flatMap(userRole -> roleRepository.findById(userRole.roleId())));
    }

    /**
     * For spring security user details service
     */
    @Cacheable(key = "#name", value = "user")
    public Mono<UserVO> getUserDetails(@NotNull String name) {
        return userRepository.findByName(name)
                .flatMap(user -> userRoleRepository.findByUserId(user.userId())
                        .flatMap(userRole -> roleRepository.findById(userRole.roleId()))
                        .collectList()
                        .map(roles -> new UserVO(user.userId(), user.name(), user.mail(), user.password(),
                                user.active(), roles)));
    }

    public Mono<UserVO> getUserDetailsFluent(@NotNull String name) {
        // Fluent API example
        return r2dbcEntityTemplate.selectOne(query(where("name").is(name)), User.class)
                .flatMap(user -> r2dbcEntityTemplate.select(query(where("user_id").is(user.userId())), UserRole.class)
                        .flatMap(userRole -> r2dbcEntityTemplate.select(query(where("role_id").is(userRole.roleId())), Role.class))
                        .collectList().map(roles -> new UserVO(user.userId(), user.name(), user.mail(),
                                user.password(), user.active(), roles)));
    }

    @Transactional
    public Mono<User> addUser(@Valid UserDTO user) {
        logger.info("adding user {}", user);
        return userRepository.findByName(user.name())
                .flatMap(existing -> Mono.<User>error(new CheckException("Existing user name")))
                .switchIfEmpty(
                        Mono.defer(() -> {
                            var newUser = new User(null, user.name(), user.mail(), user.password(), user.active());
                            return userRepository.save(newUser);
                        })
                );
    }

    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public Mono<Boolean> deleteUser(Long userId) {
        return userRepository.findById(userId)
                .flatMap(user -> userRepository.deleteById(user.userId())
                        .then(Mono.just(true)))
                .switchIfEmpty(Mono.just(true));
    }

    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public Mono<User> userSetActive(Long userId, boolean active) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    var update = new User(user.userId(), user.name(), user.mail(), user.password(), active);
                    return userRepository.save(update);
                })
                .switchIfEmpty(Mono.error(new CheckException("User doesn't exist")));
    }
}
