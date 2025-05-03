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
import com.mercadolibre.socialmeli.util.TestDataFactory;

import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Should return posts filtered by category when valid userId and category are provided")
    @Test
    void getSellerPostsForUserByCategory_shouldReturnPosts_WhenUserIdAndCategoryValid() {
        // Arrange
        Integer userId = 100;
        Integer categoryId = 1;

        User user = TestDataFactory.createUserWithFollowers();
        Post matchingPost = TestDataFactory.createSixPosts().get(0);

        when(userRepository.findPostsByFollowedUsersAndCategory(userId, categoryId)).thenReturn(Set.of(matchingPost));

        // Act
        FollowingPostDto result = service.getSellerPostsForUserByCategory(userId, categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(1, result.getPostDto().size());
        assertEquals(categoryId, result.getPostDto().get(0).getCategory());

        verify(userRepository).findPostsByFollowedUsersAndCategory(userId, categoryId);
    }

    @DisplayName("Should throw BadRequestException when category is invalid (zero)")
    @Test
    void getSellerPostsForUserByCategory_shouldThrowBadRequest_WhenCategoryIsZero() {
        // Arrange
        Integer userId = 100;

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.getSellerPostsForUserByCategory(userId, 0));

        assertEquals("Debe proporcionar una categoría válida.", exception.getMessage());
    }

    @DisplayName("Should throw NotFoundException when no posts found for category")
    @Test
    void getSellerPostsForUserByCategory_shouldThrowNotFound_WhenNoPostsFound() {
        // Arrange
        Integer userId = 100;
        Integer categoryId = 99;

        User user = TestDataFactory.createUserWithFollowers();
        when(userRepository.findPostsByFollowedUsersAndCategory(userId, categoryId)).thenReturn(Set.of());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.getSellerPostsForUserByCategory(userId, categoryId));

        assertEquals("No se encontraron publicaciones para la categoría proporcionada.", exception.getMessage());

        verify(userRepository).findPostsByFollowedUsersAndCategory(userId, categoryId);
    }

    @DisplayName("Should return recent seller posts ordered when given valid userId and order")
    @Test
    void getRecentSellerPostsForUser_shouldReturnRecentPostsOrdered_WhenValidUserIdAndOrder() {
        // Arrange
        final Integer userId = 100;
        final String order = "date_desc";

        User user = TestDataFactory.createUserWithFollowers();
        Post recentPost = TestDataFactory.createSixPosts().get(1); // Post reciente con promo

        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userRepository.findRecentPostsForUser(anyInt())).thenReturn(Set.of(recentPost));

        // Act
        FollowingPostDto result = service.getRecentSellerPostsForUser(userId, order);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(2, result.getPostDto().size());
        assertEquals(recentPost.getPostId(), result.getPostDto().get(0).getPostId());

        verify(userRepository).findUserById(userId);
        verify(userRepository, atLeastOnce()).findRecentPostsForUser(anyInt());
    }
    @DisplayName("Should throw BadRequestException when order is invalid")
    @Test
    void getRecentSellerPostsForUser_InvalidOrder_ThrowsBadRequestException() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        lenient().when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.getRecentSellerPostsForUser(user.getUserId(), "invalid"));

        assertEquals("El orden solo puede ser 'date_asc' o 'date_desc'", exception.getMessage());

    }

    @DisplayName("Should throw BadRequestException when order is null")
    @Test
    void getRecentSellerPostsForUser_NullOrder_ThrowsBadRequestException() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        lenient().when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.getRecentSellerPostsForUser(user.getUserId(), null));

        assertEquals("El orden solo puede ser 'date_asc' o 'date_desc'", exception.getMessage());
    }
    @DisplayName("Should throw NotFoundException when userId is null")
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