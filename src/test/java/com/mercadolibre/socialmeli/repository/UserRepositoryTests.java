package com.mercadolibre.socialmeli.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.util.TestDataFactory;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserRepositoryTests {

    private UserRepositoryImpl repository;

    @BeforeEach
    void setUp() throws IOException {
        repository = new TestDataFactory.FakeUserRepositoryImpl();
        TestDataFactory.getSomeUsers().forEach(user -> repository.findAll().add(user));
    }

    @DisplayName("Should return user's posts from the last two weeks when user has posts")
    @Test
    void findRecentPostsForUser_ShouldReturnPostsFromLastTwoWeeks_WhenUserHasPosts() {
        // Arrange
        User user = repository.findAll().get(0);
        Set<Post> expectedResponse = user.getPost().stream().filter(p -> TestDataFactory.isRecent(p.getDate()))
                .collect(Collectors.toSet());

        // Act
        Set<Post> response = repository.findRecentPostsForUser(user.getUserId());

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.size(), response.size());
        for (Post post : expectedResponse) {
            assertTrue(response.contains(post));
        }
    }

    @DisplayName("Given a valid user ID, should return the user when the user exists")
    @Test
    void findUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User expectedResponse = repository.findAll().get(0);

        // Act
        User response = repository.findUserById(expectedResponse.getUserId());

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

}
