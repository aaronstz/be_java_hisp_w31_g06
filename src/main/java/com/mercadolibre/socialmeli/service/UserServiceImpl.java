package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.*;

import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.User;

import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.ConflictException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public MensajeDto follow(Integer userId, Integer userIdToFollow) {
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

        return new MensajeDto("El usuario " + userId + " siguió al usuario " + userIdToFollow);
    }

    @Override
    public FollowerCountDto getFollowersCount(Integer userId) {

        User user = userRepository.findFollowersCount(userId);

        if (user == null) {
            throw new NotFoundException("No se encontró al usuario " + userId);
        }

        FollowerCountDto followerCountDto = new FollowerCountDto();
        followerCountDto.setUserId(user.getUserId());
        followerCountDto.setUserName(user.getUserName());
        followerCountDto.setFollowersCount(user.getFollowersCount());

        return followerCountDto;
    }

    @Override
    public UserListDto getFollowersList(Integer userId, String order) {
        Set<Follow> foundFollowers = Optional.ofNullable(userRepository.findFollowersList(userId))
                .orElseThrow(() -> new NotFoundException("No se encontraron seguidores para el usuario con ID: " + userId));

        List<Follow> filteredFollowers = createFollowList(foundFollowers);
        sortFollowListByName(filteredFollowers, order);

        return buildUserListDto(userId, filteredFollowers);
    }

    @Override
    public FollowingListDto getFollowedList(Integer userId, String order) {
        Set<Follow> foundFollowing = Optional.ofNullable(userRepository.findFollowingList(userId))
                .orElseThrow(() -> new NotFoundException("No se encontraron seguidos para el usuario con ID: " + userId));

        List<Follow> followedList = createFollowList(foundFollowing);
        sortFollowListByName(followedList, order);

        return buildFollowingListDto(userId, followedList);
    }

    private List<Follow> createFollowList(Set<Follow> foundFollowers) {
        return foundFollowers.stream()
                .map(f -> {
                    Follow follow = new Follow();
                    follow.setUserId(f.getUserId());
                    follow.setUserName(f.getUserName());
                    return follow;
                })
                .collect(Collectors.toList());
    }

    private UserListDto buildUserListDto(int userId, List<Follow> followers) {
        UserListDto userListDto = new UserListDto();
        userListDto.setUserId(userId);
        userListDto.setUserName(userRepository.findUserById(userId).getUserName());
        userListDto.setFollower(new LinkedHashSet<>(followers));
        return userListDto;
    }

    private FollowingListDto buildFollowingListDto(int userId, List<Follow> followed) {
        FollowingListDto followingListDto = new FollowingListDto();
        followingListDto.setUserId(userId);
        followingListDto.setUserName(userRepository.findUserById(userId).getUserName());
        followingListDto.setFollowed(new LinkedHashSet<>(followed));
        return followingListDto;
    }

    private void sortFollowListByName(List<Follow> list, String order) {
        switch (order.toLowerCase()) {
            case "name_desc":
                list.sort(Comparator.comparing(Follow::getUserName).reversed());
                break;
            case "name_asc":
                list.sort(Comparator.comparing(Follow::getUserName));
                break;
            default:
                throw new BadRequestException("Parámetro 'order' inválido. Valores permitidos: name_asc, name_desc");
        }
    }

    @Override
    public MensajeDto unFollow(Integer userId, Integer userIdToUnFollow) {
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

        return new MensajeDto("El usuario " + userId + " a dejado de seguir al usuario " + userIdToUnFollow);
    }
}
