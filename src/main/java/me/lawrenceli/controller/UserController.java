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
import me.lawrenceli.utils.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
@SecurityRequirement(name = "jwt")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Current User")
    public Mono<UserVO> me(@Schema(hidden = true) @AuthenticationPrincipal Principal principal) {
        return userService.getUserDetails(principal.getName());
    }

    @GetMapping("/{name}")
    @Operation(summary = "Query User By Name")
    public Mono<R<UserVO>> queryUserByName(@PathVariable String name) {
        return R.success(userService.getUserDetailsFluent(name));
    }

    @PostMapping
    @Operation(summary = "Add User")
    //    @PreAuthorize("hasRole('ADMIN')")
    public Mono<R<User>> addUser(@RequestBody @Valid UserDTO user) {
        return R.success(userService.addUser(user));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete User")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<R<Boolean>> deleteUser(@PathVariable Long userId) {
        return R.success(userService.deleteUser(userId));
    }

    @PatchMapping("/{userId}")
    @Operation(summary = "Disable User")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<R<User>> setUserInactive(@PathVariable Long userId) {
        return R.success(userService.userSetActive(userId, false), "disabled");
    }
}
