package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserServiceImpl implements IUserService{
    @Override
    public void follow(Integer userId, Integer userIdToFollow) {//TODO ❤️

    }

    @Override
    public FollowerCountDto getFollowersCount(Integer userId) {//TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowersList(Integer userId) {//TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowedList(Integer userId) {//TODO ❤️
        return null;
    }

    @Override
    public void unFollow(Integer userId, Integer userIdToUnFollow) {//TODO ❤️

    }

}

