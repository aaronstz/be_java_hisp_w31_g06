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
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
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

        User user = userRepository.findUserById(userId);

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
    public FollowerCountDto getFollowersCount(Integer userId) {

        User user = userRepository.findFollowersCount(userId);

        if(user == null) {
            throw new NotFoundException("No se encontró al usuario " + userId);
        }

        FollowerCountDto followerCountDto = new FollowerCountDto();
        followerCountDto.setUserId(user.getUserId());
        followerCountDto.setUserName(user.getUserName());
        followerCountDto.setFollowersCount(user.getFollowersCount());

        return followerCountDto;
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
    private List<User> getListOfUsers() {
        List<User> userList = userRepository.findAll();

        if(userList.isEmpty()) {
            throw new NotFoundException("No se encontraron productos.");
        }

        return userList;
    }
}
