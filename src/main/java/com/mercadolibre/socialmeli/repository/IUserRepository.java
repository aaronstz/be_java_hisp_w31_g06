package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;

import java.util.List;
import java.util.Set;

public interface IUserRepository {
    List<User> findAll();
    void saveFollow(Integer userId, Integer userIdToFollow);
    boolean isUserAlreadyFollowing(Integer userId, Integer userIdToFollow);
    User findFollowersCount(Integer userId);
    Set<Follow> findFollowersList(Integer userId);
    Set<Follow> findFollowingList(Integer userId);
    void removeFollow(User user, User toUnFollow);
    User findUserById(Integer userId);
    boolean isFollowing(Integer userId, Integer userIdToUnfollow);
    Set<Post> findRecentPostsForUser(Integer userId);
    boolean addPostToUser(Post post);
    Set<Post> findPostsByKeyword(Integer userId, String keyword);
    Set<Post> findPostsByFollowedUsersAndCategory(Integer userId, Integer categoryId);

}
