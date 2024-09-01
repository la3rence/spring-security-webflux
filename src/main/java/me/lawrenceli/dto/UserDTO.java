package me.lawrenceli.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotBlank String name,
        @Email String mail,
        @NotEmpty String password,
        @NotNull Boolean active) {
}
