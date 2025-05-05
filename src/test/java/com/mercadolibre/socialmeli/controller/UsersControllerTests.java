package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.MensajeDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.service.UserServiceImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UsersControllerTests {

    @Mock
    private UserServiceImpl service;

    @InjectMocks
    private UserController controller;

    @Test
    void testFollow() {
        int userId = 1;
        int userIdToFollow = 2;
        MensajeDto expected = new MensajeDto("El usuario " + userId + " siguió al usuario " + userIdToFollow);

        when(service.follow(userId, userIdToFollow)).thenReturn(expected);

        // Act
        ResponseEntity<?> response = controller.follow(userId, userIdToFollow);
        MensajeDto body = (MensajeDto) response.getBody();

        // Assert
        verify(service).follow(userId, userIdToFollow);
        Assertions.assertEquals(expected, body);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("Should return a success confirmation message when a user unfollows another")
    void testUnfollow_shouldReturnConfirmationMessage_whenInputsCorrect() {
        int userId = 1;
        int userIdToUnFollow = 2;
        MensajeDto expected = new MensajeDto("El usuario " + userId + " a dejado de seguir al usuario " + userIdToUnFollow);

        when(service.unFollow(userId, userIdToUnFollow)).thenReturn(expected);

        // Act
        ResponseEntity<?> response = controller.unfollowUser(userId, userIdToUnFollow);
        MensajeDto body = (MensajeDto) response.getBody();

        // Assert
        verify(service).unFollow(userId, userIdToUnFollow);
        Assertions.assertEquals(expected, body);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("getFollowersCount .should return the number of followers of the user when the input data is valid")
    void getFollowersCount_shouldReturnCorrectFollowerCount_whenInputIsValid() {
        // Arrange
        User userExpected = TestDataFactory.createUserWithFollowers();
        int userId = userExpected.getUserId();
        int expectedCountFromField = userExpected.getFollowersCount();
        int expectedCountFromList = userExpected.getFollower().size();

        FollowerCountDto result =  new FollowerCountDto();
        result.setFollowersCount(2);

        when(service.getFollowersCount(userId)).thenReturn(result);

        // Act
        ResponseEntity<?> response = controller.getFollowersCount(userId);
        FollowerCountDto body = (FollowerCountDto) response.getBody();
        int actualCount = body.getFollowersCount();

        // Assert
        assertEquals(expectedCountFromField, actualCount);
        assertEquals(expectedCountFromList, actualCount);
        verify(service).getFollowersCount(userId);
        assertNotNull(response);
    }




}
