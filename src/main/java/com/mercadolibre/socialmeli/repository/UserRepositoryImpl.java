package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.User;

import com.mercadolibre.socialmeli.exception.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;



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
    public Optional<User> findUserById(Integer userId) {//TODO ❤️
        return listOfUsers.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsById(Integer userId) {
        return listOfUsers.stream()
                .anyMatch(user -> user.getUserId().equals(userId));
    }

    @Override
    public boolean isFollowing(Integer userId, Integer userIdToUnFollow) {

        User user = findUserById(userId).orElseThrow();
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

