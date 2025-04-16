package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.UserListDto;

public interface IUserService {

    void follow(Integer userId, Integer userIdToFollow);

    FollowerCountDto getFollowersCount(Integer userId);

    UserListDto getFollowersList(Integer userId);

    UserListDto getFollowedList(Integer userId);

    String unFollow(Integer userId, Integer userIdToUnFollow);

}
