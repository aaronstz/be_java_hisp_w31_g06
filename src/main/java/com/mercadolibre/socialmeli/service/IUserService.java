package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.*;

import java.util.List;

public interface IUserService {
    List<UserDto> getAll();
    MensajeDto follow(Integer userId, Integer userIdToFollow);
    FollowerCountDto getFollowersCount(Integer userId);
    UserListDto getFollowersList(Integer userId, String order);
    FollowingListDto getFollowedList(Integer userId, String order);
    MensajeDto unFollow(Integer userId, Integer userIdToUnFollow);
}
