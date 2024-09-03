package me.lawrenceli.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

public record PageDTO(
        @RequestParam(value = "page", defaultValue = "0") @NotNull int page,
        @RequestParam(value = "size", defaultValue = "5") int size
) {
}
