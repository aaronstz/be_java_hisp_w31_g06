package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;

import java.util.List;

public interface IUserService {
    List<UserDto> getAll();

    String follow(Integer userId, Integer userIdToFollow);

    FollowerCountDto getFollowersCount(Integer userId);

    UserListDto getFollowersList(Integer userId);

    UserListDto getFollowedList(Integer userId);

    void unFollow(Integer userId, Integer userIdToUnFollow);

}
