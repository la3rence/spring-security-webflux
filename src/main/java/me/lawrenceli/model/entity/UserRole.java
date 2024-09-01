package me.lawrenceli.model.entity;

import jakarta.validation.constraints.NotNull;

public record UserRole(@NotNull Long userId,
                       @NotNull Long roleId) {
}
