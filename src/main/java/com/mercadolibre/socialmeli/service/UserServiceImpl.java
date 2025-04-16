package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.ConflictException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IUserRepository;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();

        if(userList.isEmpty()) {
            throw new NotFoundException("No se encontraron productos.");
        }
        ObjectMapper mapper = new ObjectMapper();


        return userList.stream().map(u -> mapper.convertValue(u, UserDto.class)).toList();
    }

    @Override
    public String follow(Integer userId, Integer userIdToFollow) {// TODO ❤️
        if (userId.equals(userIdToFollow)) {
            throw new ConflictException("Un usuario no puede seguirse a sí mismo");
        }

        List<User> userList = getListOfUsers();

        User user = userList.stream().filter(ul -> ul.getUserId().equals(userId)).findFirst().orElse(null);

        if (user == null) {
            throw new NotFoundException("No se encontró al seguidor");
        }

        if (userRepository.isUserAlreadyFollowing(userId, userIdToFollow)) {
            throw new ConflictException("El usuario ya sigue al otro");
        }

        userRepository.saveFollow(userId, userIdToFollow);

        return "El usuario " + userId + " siguio a " + userIdToFollow;
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

    @Override
    public void unFollow(Integer userId, Integer userIdToUnFollow) {// TODO ❤️

    }

    private List<User> getListOfUsers() {
        List<User> userList = userRepository.findAll();

        if(userList.isEmpty()) {
            throw new NotFoundException("No se encontraron productos.");
        }

        return userList;
    }

}
