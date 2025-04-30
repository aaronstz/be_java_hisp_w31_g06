package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import com.mercadolibre.socialmeli.utils.Util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepositoryImpl productRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void getRecentSellerPostsForUser_HappyPath_ReturnsOrderedRecentPosts() {
        // Arrange
        User user = Util.createUserWithFollowers();
        List<User> users = Util.getSomeUsers();

        Set<Integer> followedUserIds = user.getFollowing().stream()
                .map(Follow::getUserId)
                .collect(Collectors.toSet());

        List<User> followedUsers = users.stream()
                .filter(u -> followedUserIds.contains(u.getUserId()))
                .collect(Collectors.toList());

        List<PostDto> expectedRecentPosts = followedUsers.stream()
                .flatMap(f -> f.getPost().stream())
                .filter(p -> Util.isRecent(p.getDate()))
                .map(p -> new PostDto(p.getUserId(), p.getPostId(), p.getDate(), p.getProduct(),
                        p.getCategory(), p.getPrice(), p.getHasPromo(), p.getDiscount()))
                .sorted(Comparator.comparing(PostDto::getDate).reversed())
                .collect(Collectors.toList());

        for (User followed : followedUsers) {
            Set<Post> recentPosts = followed.getPost().stream()
                    .filter(p -> Util.isRecent(p.getDate()))
                    .collect(Collectors.toSet());

            lenient().when(userRepository.findRecentPostsForUser(followed.getUserId()))
                    .thenReturn(recentPosts);
        }

        when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act
        FollowingPostDto result = service.getRecentSellerPostsForUser(user.getUserId(), "date_desc");

        // Assert
        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(expectedRecentPosts.size(), result.getPostDto().size());

        for (int i = 0; i < expectedRecentPosts.size(); i++) {
            assertEquals(expectedRecentPosts.get(i).getPostId(), result.getPostDto().get(i).getPostId());
        }
    }
    @Test
    void getRecentSellerPostsForUser_InvalidOrder_ThrowsBadRequestException() {
        // Arrange
        User user = Util.createUserWithFollowers();
        lenient().when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.getRecentSellerPostsForUser(user.getUserId(), "invalid"));

        assertEquals("El orden solo puede ser 'date_asc' o 'date_desc'", exception.getMessage());
    }

    @Test
    void getRecentSellerPostsForUser_NullOrder_ThrowsBadRequestException() {
        // Arrange
        User user = Util.createUserWithFollowers();
        lenient().when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.getRecentSellerPostsForUser(user.getUserId(), null));

        assertEquals("El orden solo puede ser 'date_asc' o 'date_desc'", exception.getMessage());
    }

    @Test
    void getRecentSellerPostsForUser_NullUserId_ThrowsNotFoundException() {
        // Arrange
        Integer userId = null;

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.getRecentSellerPostsForUser(userId, "date_desc"));

        assertEquals("Usuario no encontrado con ID: null", exception.getMessage());
    }
}