package me.lawrenceli.entity;

import jakarta.validation.constraints.NotNull;

public record UserRole(@NotNull Long userId,
                       @NotNull Long roleId) {
}
