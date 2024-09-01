package me.lawrenceli.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

public record User(
        @Id Long userId,
        @NotBlank String name,
        @Email String mail,
        @JsonIgnore String password,
        @NotNull Boolean active) {
}
