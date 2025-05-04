package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTests {

    private UserRepositoryImpl repository;

    @BeforeEach
    void setUp() throws IOException {
        repository = new UserRepositoryImpl();
    }

    @Test
    void testRemoveFollow() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        User userToUnfollow = TestDataFactory.getUserFromId(200);
        Follow follower = new Follow(100, "Mariano Lopez");
        Follow unfollow = new Follow(200, "Guillermo Lopez");

        userToUnfollow.getFollower().add(follower);

        int userFollowersCount = userToUnfollow.getFollowersCount();

        // Act
        repository.removeFollow(user, userToUnfollow);

        // Assertions
        Assertions.assertFalse(userToUnfollow.getFollower().contains(follower));
        Assertions.assertEquals(userFollowersCount - 1, userToUnfollow.getFollowersCount());
        Assertions.assertFalse(user.getFollowing().contains(unfollow));

    }
}
