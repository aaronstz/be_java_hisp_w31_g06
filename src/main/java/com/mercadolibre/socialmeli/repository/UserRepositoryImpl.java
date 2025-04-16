package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.User;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserRepositoryImpl implements IUserRepository{

    private  final Map<Integer, User> users =new HashMap<>();
    @Override
    public void saveFollow(Integer userId, Integer userIdToFollow) { //TODO ❤️

    }

    @Override
    public Integer findFollowersCount(Integer userId) { //TODO ❤️
        return null;
    }

    @Override
    public Set<User> findFollowersList(Integer userId) { //TODO ❤️
        return null;
    }

    @Override
    public Set<User> findFollowingList(Integer userId) { //TODO ❤️
        return null;
    }

    @Override
    public void removeFollow(Integer userId, Integer userIdToUnFollow) { //TODO ❤️
        User user = users.get(userId);
        User toUnfollow = users.get(userIdToUnFollow);

        user.getFollowing().remove(toUnfollow);
        toUnfollow.getFollowing().remove(user);
    }

    @Override
    public User findUserById(Integer userId) {//TODO ❤️
        return users.get(userId);
    }

    @Override
    public boolean existsById(Integer userId) {
        return users.containsKey(userId);
    }


    }






