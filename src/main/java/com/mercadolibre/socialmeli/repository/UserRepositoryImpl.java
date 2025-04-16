package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;

import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
    public void removeFollow(Integer userId, Integer userIdToUnFollow) { // TODO ❤️

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

        return user.getPost().stream()
                .filter(p -> p.getDate().isAfter(LocalDate.now().minusDays(14)))
                .collect(Collectors.toSet());
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
