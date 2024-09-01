package me.lawrenceli.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import me.lawrenceli.model.dto.UserDTO;
import me.lawrenceli.model.entity.User;
import me.lawrenceli.service.UserService;
import me.lawrenceli.model.vo.UserVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@Tag(name = "用户")
@SecurityRequirement(name = "jwt")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Current User")
    public Mono<UserVO> me(@Schema(hidden = true) @AuthenticationPrincipal Principal principal) {
        return userService.getUserDetails(principal.getName());
    }

    @PostMapping
    @Operation(summary = "Add User")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<User> addUser(@RequestBody @Valid UserDTO user) {
        return userService.addUser(user); // .map(ResponseEntity::ok);
    }
}
