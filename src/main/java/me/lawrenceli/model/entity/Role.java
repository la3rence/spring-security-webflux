package me.lawrenceli.model.entity;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

public record Role(@Id Long roleId, @NotEmpty String roleName) {
}
