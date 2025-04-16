package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.User;

import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private List<User> users;

    public UserRepositoryImpl() {
        // Inicializar lista de usuarios.
    }

    @Override
    public void saveFollow(Integer userId, Integer userIdToFollow) { // TODO ❤️

    }

    @Override
    public Integer findFollowersCount(Integer userId) { // TODO ❤️
        return null;
    }

    @Override
    public Set<User> findFollowersList(Integer userId) { // TODO ❤️
        return null;
    }

    @Override
    public Set<User> findFollowingList(Integer userId) { // TODO ❤️
        return null;
    }

    @Override
    public void removeFollow(Integer userId, Integer userIdToUnFollow) { // TODO ❤️

    }

    @Override
    public User findUserById(Integer userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
