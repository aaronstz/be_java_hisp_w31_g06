package com.mercadolibre.socialmeli.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;

import com.mercadolibre.socialmeli.exception.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    public Set<Follow> findFollowersList(Integer userId) {
        User user = findUserById(userId);
        if (user == null) {
            return null;
        }
        Set<Follow> foundFollowers = user.getFollower();
        if (foundFollowers.isEmpty()) {
            return null;
        }
        return foundFollowers;
    }

    @Override
    public Set<Follow> findFollowingList(Integer userId) {
        User foundUser = findUserById(userId);
        if (foundUser == null) {
            return null;
        }
        Set<Follow> foundFollowing = foundUser.getFollowing();
        if (foundFollowing.isEmpty()) {
            return null;
        }
        return foundFollowing;
    }

    @Override
    public void removeFollow(User user, User toUnFollow) {
        toUnFollow.getFollower().removeIf(f -> f.getUserId().equals(user.getUserId()));
        toUnFollow.setFollowersCount(toUnFollow.getFollowersCount() - 1);

        user.getFollowing().removeIf(f -> f.getUserId().equals(toUnFollow.getUserId()));
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
        return filterByDates(user);

    }

    private Set<Post> filterByDates(User user) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return user.getPost().stream()
                .filter(p -> LocalDate.parse(p.getDate(), formatter).isAfter(
                        (currentDate.minusDays(14)))).collect(Collectors.toSet());
    }


    @Override
    public boolean addPostToUser(Post post) {
        User user = listOfUsers.stream().filter(u -> u.getUserId().equals(post.getUserId())).findFirst()
                .orElse(null);

        if (user == null)
            return false;

        user.getPost().add(post);

        return true;
    }

    @Override
    public boolean isFollowing(Integer userId, Integer userIdToUnFollow) {
        User user = findUserById(userId);
        if (user == null) {
            return false;
        }
        return user.getFollowing().stream()
                .anyMatch(f -> f.getUserId().equals(userIdToUnFollow));
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

    @Override
    public Set<Post> findPostsByKeyword(Integer userId, String keyword) {
        User user = listOfUsers.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if (user == null) {
            return null;
        }

        return user.getPost().stream()
                .filter(p -> p.getProduct().getProductName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Post> findPostsByFollowedUsersAndCategory(Integer userId, Integer categoryId) {
        User user = this.findUserById(userId);
        if (user == null || user.getFollowing() == null) return Collections.emptySet();

        return user.getFollowing().stream()
                .map(follow -> findPostsForUserByCategory(follow.getUserId(), categoryId))
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Set<Post> findPostsForUserByCategory(Integer sellerId, Integer categoryId) {
        User seller = this.findUserById(sellerId);
        if (seller == null || seller.getPost() == null) return Collections.emptySet();

        return seller.getPost().stream()
                .filter(post -> Objects.equals(post.getCategory(), categoryId))
                .collect(Collectors.toSet());
    }

    @Override
    public void clearRepository() {
        listOfUsers.clear();
    }
}
