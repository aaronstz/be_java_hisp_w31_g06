package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

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
