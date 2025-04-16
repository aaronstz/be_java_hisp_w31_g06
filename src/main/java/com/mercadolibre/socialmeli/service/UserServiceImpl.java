package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.ConflictException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IUserRepository;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    IUserRepository userRepository;


    @Override
    public void follow(Integer userId, Integer userIdToFollow) {// TODO ❤️

    }

    @Override
    public FollowerCountDto getFollowersCount(Integer userId) {// TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowersList(Integer userId) {// TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowedList(Integer userId) {// TODO ❤️
        return null;
    }
private UserDto mapToDto(User user){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(user, UserDto.class);
}
    @Override
    public void unFollow(Integer userId, Integer userIdToUnFollow) {
        if (userId.equals(userIdToUnFollow)) {
            throw new ConflictException("Un usuario no puede dejar de seguirse.");
        }

        User user = userRepository.findUserById(userId);
        User userToUnFollow = userRepository.findUserById(userIdToUnFollow);

        if (user == null || userToUnFollow == null) {
            throw new NotFoundException("Uno de los usuarios no fue encontrado");
        }

        if (!userRepository.isFollowing(userId, userIdToUnFollow)) {
            throw new ConflictException("Ni se puede dejar de seguir a un usuario que no estás siguiendo");
        }

        userRepository.removeFollow(user, userToUnFollow);
    }
}
