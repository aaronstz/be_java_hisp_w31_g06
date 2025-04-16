package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.FollowingListDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Following;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.ConflictException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        ObjectMapper mapper = new ObjectMapper();
        return userRepository.findAll().stream().map(u -> mapper.convertValue(u, UserDto.class)).toList();
    }

    @Override
    public String follow(Integer userId, Integer userIdToFollow) {
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
    public FollowerCountDto getFollowersCount(Integer userId) {// TODO ❤️
        return null;
    }

    @Override
    public UserListDto getFollowersList(Integer userId) {// TODO ❤️
        return null;
    }

    @Override
    public FollowingListDto getFollowedList(Integer userId) {
        Set<Follow> foundFollowing = userRepository.findFollowingList(userId);
        if(foundFollowing == null) {
            throw new NotFoundException("No se encontraron seguidos");
        }
        Set<Following> foundFollowed = foundFollowing.stream().map(f -> {
            Following foundFollow = new Following();
            foundFollow.setUserId(f.getUserId());
            foundFollow.setUserName(f.getUserName());
            return foundFollow;
        }).collect(Collectors.toSet());

        FollowingListDto foundList = new FollowingListDto();
        foundList.setUserId(userId);
        foundList.setUserName(userRepository.findUserById(userId).getUserName());
        foundList.setFollowed(foundFollowed);

        return foundList;
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
