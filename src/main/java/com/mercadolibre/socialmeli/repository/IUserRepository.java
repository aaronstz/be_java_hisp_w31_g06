package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.User;

import java.util.Optional;
import java.util.Set;

public interface IUserRepository {


    void saveFollow(Integer userId, Integer userIdToFollow);

    Integer findFollowersCount(Integer userId);

    Set<User> findFollowersList(Integer userId);

    Set<User> findFollowingList(Integer userId);

    void  removeFollow(User user, User toUnFollow) ;

    Optional<User> findUserById(Integer userId);

    boolean existsById(Integer userId);

    boolean isFollowing(Integer userId, Integer userIdToUnfollow);
}
