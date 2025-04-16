package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IProductRepository;
import com.mercadolibre.socialmeli.repository.IUserRepository;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserServiceImpl implements IUserService {

    IUserRepository userRepository;

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
    public String unFollow(Integer userId, Integer userIdToUnfollow) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Usuario con ID " + userId + " no existe.");
        }

        if (!userRepository.isFollowing(userId, userIdToUnfollow)) {
            throw new BadRequestException("El usuario " + userId + " no sigue al usuario " + userIdToUnfollow);
        }

        userRepository.removeFollow(userId, userIdToUnfollow);
        return "El usuario " + userId + " dejó de seguir a " + userIdToUnfollow + " correctamente.";

    }
}

