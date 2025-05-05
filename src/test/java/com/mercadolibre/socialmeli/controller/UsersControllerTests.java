package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.FollowingListDto;
import com.mercadolibre.socialmeli.dto.MensajeDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.NotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

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

    @Test
    @DisplayName("Should return the followed list sorted by name in ascending order")
    void testGetFollowedList_shouldReturnFollowedListAscOrder_whenInputsAreCorrect() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        Set<Follow> followSet = TestDataFactory.getFollowList();
        FollowingListDto expected = new FollowingListDto(user.getUserId(), user.getUserName(), followSet);
        String order = "name_asc";

        when(service.getFollowedList(user.getUserId(), order)).thenReturn(expected);

        // Act
        ResponseEntity<FollowingListDto> response = controller.getFollowedList(user.getUserId(), order);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
        verify(service).getFollowedList(user.getUserId(), order);
    }

    @Test
    @DisplayName("Should return the followed list sorted by name in descending order")
    void testGetFollowedList_shouldReturnFollowedListDescOrder_whenInputsAreCorrect() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        Set<Follow> followSet = TestDataFactory.getFollowList();
        FollowingListDto expected = new FollowingListDto(user.getUserId(), user.getUserName(), followSet);
        String order = "name_desc";

        when(service.getFollowedList(user.getUserId(), order)).thenReturn(expected);

        // Act
        ResponseEntity<FollowingListDto> response = controller.getFollowedList(user.getUserId(), order);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expected, response.getBody());
        verify(service).getFollowedList(user.getUserId(), order);
    }

    @Test
    @DisplayName("Should throw NotFound exception when user has an empty followed list")
    void testGetFollowedList_shouldThrowNotFoundException_whenUserHasEmptyList() {
        // Arrange
        Integer userId = 999;
        String order = "name_asc";

        when(service.getFollowedList(userId, order)).thenThrow(
                new NotFoundException("No se encontraron seguidos para el usuario con ID: " + userId));

        // Act & Assert
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class, () -> {
            controller.getFollowedList(userId, order);
        });

        assertEquals("No se encontraron seguidos para el usuario con ID: " + userId, thrown.getMessage());
    }
}
