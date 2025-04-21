package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.FollowingListDto;
import com.mercadolibre.socialmeli.dto.UserDto;
import com.mercadolibre.socialmeli.dto.UserListDto;

import java.util.List;


public interface IUserService {

    List<UserDto> getAll();

    String follow(Integer userId, Integer userIdToFollow);

    FollowerCountDto getFollowersCount(Integer userId);

    UserListDto getFollowersList(Integer userId, String order);

    FollowingListDto getFollowedList(Integer userId, String order);

    void unFollow(Integer userId, Integer userIdToUnFollow);

}
