package me.lawrenceli.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import me.lawrenceli.model.dto.UserDTO;
import me.lawrenceli.model.entity.Role;
import me.lawrenceli.model.entity.User;
import me.lawrenceli.repository.RoleRepository;
import me.lawrenceli.repository.UserRepository;
import me.lawrenceli.repository.UserRoleRepository;
import me.lawrenceli.model.vo.UserVO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lawrence.li
 * @since 2024/09/01
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    public Mono<User> getUserByName(@NotNull String name) {
        return userRepository.findByName(name);
    }

    public Flux<Role> getRolesByUserName(@NotNull String name) {
        return userRepository.findByName(name)
                .flatMapMany(user -> userRoleRepository.findByUserId(user.userId())
                        .flatMap(userRole -> roleRepository.findById(userRole.roleId())));
    }

    @Cacheable(key = "#name", value = "user")
    public Mono<UserVO> getUserDetails(@NotNull String name) {
        return userRepository.findByName(name)
                .flatMap(user -> userRoleRepository.findByUserId(user.userId())
                        .flatMap(userRole -> roleRepository.findById(userRole.roleId()))
                        .collectList()
                        .map(roles -> new UserVO(user.userId(), user.name(), user.mail(), user.password(),
                                user.active(), roles)));
    }

    public Mono<User> addUser(@Valid UserDTO user) {
        var newUser = new User(null, user.name(), user.mail(), user.password(), user.active());
        return userRepository.save(newUser);
    }
}
