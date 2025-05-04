package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.MensajeDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.ConflictException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepositoryImpl repository;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void testFollow() {
        // Arrange
        User user1 = new User(1, "Mariano Lopez", 0, new HashSet<>(),
                new HashSet<>(), null);
        int userId2 = 2;

        MensajeDto expected = new MensajeDto("El usuario " + user1.getUserId() + " siguió al usuario " + userId2);

        // Act
        when(repository.findUserById(user1.getUserId())).thenReturn(user1);
        when(repository.isUserAlreadyFollowing(user1.getUserId(), userId2)).thenReturn(false);

        MensajeDto response = service.follow(user1.getUserId(), userId2);

        // Assert
        verify(repository).findUserById(user1.getUserId());
        verify(repository).isUserAlreadyFollowing(user1.getUserId(), userId2);
        Assertions.assertEquals(expected, response);
    }

    @Test
    @DisplayName("This test ensures that a `ConflictException` is thrown when a user attempts to follow themselves")
    void testCannotFollowHimself() {
        // Arrange
        int user1 = 1;
        String expected = "Un usuario no puede seguirse a sí mismo";

        // Act
        ConflictException thrown = Assertions.assertThrows(ConflictException.class,
                () -> service.follow(user1, user1));

        // Assert
        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    @DisplayName("This test ensures that a `NotFoundException` is thrown when a user is not found or does not exist")
    void testFollowUserIsNull() {
        // Arrange
        int userId = 999;
        int userId2 = 2;
        String expected = "No se encontró al seguidor";

        // Act
        when(repository.findUserById(userId)).thenReturn(null);
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class,
                () -> service.follow(userId, userId2));

        // Assert
        verify(repository).findUserById(userId);
        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    @DisplayName("This test ensures that a `ConflictException` is thrown when trying to re-follow an user")
    void testFollowUserAlreadyFollows() {
        // Arrange
        User user = new User(1, "Mariano Lopez", 0, new HashSet<>(),
                new HashSet<>(), null);
        int userId2 = 2;

        String expected = "El usuario ya sigue al otro";

        // Act
        when(repository.findUserById(user.getUserId())).thenReturn(user);
        when(repository.isUserAlreadyFollowing(user.getUserId(), userId2)).thenReturn(true);

        ConflictException thrown = Assertions.assertThrows(ConflictException.class,
                () -> service.follow(user.getUserId(), userId2));

        // Assert
        Assertions.assertEquals(expected, thrown.getMessage());
    }

    @Test
    @DisplayName("getFollowersCount .should return the number of followers of the user when the input data is valid")
    void getFollowersCount_shouldReturnCorrectFollowerCount_whenInputIsValid() {
        // Arrange
        User userExpected = TestDataFactory.createUserWithFollowers();
        int userId = userExpected.getUserId();
        int expectedCountFromField = userExpected.getFollowersCount();
        int expectedCountFromList = userExpected.getFollower().size();

        when(repository.findFollowersCount(userId)).thenReturn(userExpected);

        // Act
        FollowerCountDto result = service.getFollowersCount(userId);
        int actualCount = result.getFollowersCount();

        // Assert
        assertEquals(expectedCountFromField, actualCount);
        assertEquals(expectedCountFromList, actualCount);
        verify(repository).findFollowersCount(userId);

    }

}
