package me.lawrenceli.model;

import java.util.List;

public record Page<T>(List<T> data, Long count) {
}
