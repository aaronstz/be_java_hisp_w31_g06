package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;

import com.mercadolibre.socialmeli.exception.NotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
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
    public List<User> findAll() {
        return listOfUsers;
    }

    @Override
    public void saveFollow(Integer userId, Integer userIdToFollow) {
        User userToFollow = listOfUsers.stream()
                .filter(utf -> utf.getUserId().equals(userIdToFollow))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No se encontró el usuario a seguir"));

        User user = listOfUsers.stream()
                .filter(ul -> ul.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("No se encontró al seguidor"));


        userToFollow.setFollowersCount(userToFollow.getFollowersCount() + 1);

        Follow newFollower = new Follow(user.getUserId(), user.getUserName());

        userToFollow.getFollower().add(newFollower);

        Follow newFollowing = new Follow(userToFollow.getUserId(), userToFollow.getUserName());

        user.getFollowing().add(newFollowing);
    }

    @Override
    public boolean isUserAlreadyFollowing(Integer userId, Integer userIdToFollow) {
        User userToFollow = listOfUsers.stream()
                .filter(utf -> utf.getUserId().equals(userIdToFollow))
                .findFirst()
                .orElse(null);

        if (userToFollow == null) {
            return false;
        }

        return userToFollow.getFollower().stream().anyMatch(utf -> utf.getUserId().equals(userId));
    }


    @Override
    public User findFollowersCount(Integer userId) {
        return findUserById(userId);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return user.getPost().stream()
                .filter(p -> LocalDate.parse(p.getDate(), formatter).isAfter(LocalDate.now().minusDays(14)))
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
