package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;

import com.mercadolibre.socialmeli.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Repository
public class UserRepositoryImpl implements IUserRepository {


    private List<User> listOfUsers = new ArrayList<>();

    public UserRepositoryImpl() throws IOException {
        loadDataBase();
    }

    @Override
    public void saveFollow(Integer userId, Integer userIdToFollow) { // TODO ❤️

    }

    @Override
    public Integer findFollowersCount(Integer userId) { // TODO ❤️
        return null;
    }

    @Override
    public Set<User> findFollowersList(Integer userId) { // TODO ❤️
        return null;
    }

    @Override
    public Set<User> findFollowingList(Integer userId) { // TODO ❤️
        return null;
    }

    @Override
    public void removeFollow(User user, User toUnFollow) { //TODO ❤️
        user.getFollowing().remove(toUnFollow);
        toUnFollow.getFollowing().remove(user);
    }

    @Override
    public User findUserById(Integer userId) {
        return listOfUsers.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Set<Post> findRecentPostsForUser(Integer userId) {
        User user = listOfUsers.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if (user == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return user.getPost().stream()
                .filter(p -> LocalDate.parse(p.getDate(), formatter).isAfter(LocalDate.now().minusDays(14)))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean existsById(Integer userId) {
        return listOfUsers.stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

    @Override
    public boolean isFollowing(Integer userId, Integer userIdToUnFollow) {

        User user = findUserById(userId);
        User toUnfollow = listOfUsers.get(userIdToUnFollow);

        return user.getFollowing().contains(toUnfollow);
    }


    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper mapper = new ObjectMapper();
        List<User> userList;

        file = ResourceUtils.getFile("classpath:user.json");
        userList = mapper.readValue(file, new TypeReference<List<User>>() {
        });

        this.listOfUsers = userList;
    }
}

