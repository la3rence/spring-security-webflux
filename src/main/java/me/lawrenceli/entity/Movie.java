package me.lawrenceli.entity;

import org.springframework.data.annotation.Id;

public record Movie(
        @Id Long id,
        String title,
        String description) {
}
