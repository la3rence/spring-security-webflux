package me.lawrenceli.model.vo;

import me.lawrenceli.model.entity.Role;

import java.util.List;

public record UserVO(
        Long userId,
        String name,
        String mail,
        String password,
        Boolean active,
        List<Role> roles) {
}
