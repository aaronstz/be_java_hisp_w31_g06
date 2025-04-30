package com.mercadolibre.socialmeli.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.utils.Util;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserRepositoryTests {

    @Autowired
    UserRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        Util.setUserRepositoryForTest(repository);
    }

    @Test
    void findRecentPostsForUserTest() {
        // Arrange
        List<User> users = Util.getSomeUsers();
        users.forEach(u -> repository.addUser(u));
        Set<Post> expectedResponse = users.get(0).getPost().stream()
                .filter(p -> Util.isRecent(p.getDate()))
                .collect(Collectors.toSet());

        // Act
        Set<Post> response = repository.findRecentPostsForUser(users.get(0).getUserId());

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.size(), response.size());
        for (Post post : expectedResponse) {
            assertTrue(response.contains(post));
        }
    }

    @Test
    void findUserByIdTest() {
        // Arrange
        List<User> users = Util.getSomeUsers();
        users.stream().forEach(u -> repository.addUser(u));
        User expectedResponse = users.get(0);

        // Act
        User response = repository.findUserById(expectedResponse.getUserId());

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void findPostsByKeywordTest() {
        // Arrange
        List<User> users = Util.getSomeUsers();
        users.forEach(u -> repository.addUser(u));
        String keyword = "Laptop";
        Set<Post> expectedResponse = users.get(0).getPost().stream()
                .filter(p -> p.getProduct().getProductName().contains(keyword))
                .collect(Collectors.toSet());

        // Act
        Set<Post> result = repository.findPostsByKeyword(users.get(0).getUserId(), keyword);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void findPostsByKeywordEmptyKeyword() {
        List<User> users = Util.getSomeUsers();
        users.forEach(u -> repository.addUser(u));
        String keyword = "";
        Set<Post> expectedResponse = users.get(0).getPost().stream()
                .filter(p -> p.getProduct().getProductName().contains(keyword))
                .collect(Collectors.toSet());

        // Act
        Set<Post> result = repository.findPostsByKeyword(users.get(0).getUserId(), keyword);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void findPostsByKeywordNulKeyword() {
        List<User> users = Util.getSomeUsers();
        users.forEach(u -> repository.addUser(u));
        String keyword = null;

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> repository.findPostsByKeyword(users.get(0).getUserId(), keyword));
    }
}
