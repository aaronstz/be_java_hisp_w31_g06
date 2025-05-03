package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.service.ProductServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductsControllerTests {

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private ProductsController controller;


    @DisplayName("Should call service to get seller posts by category when valid inputs are provided")
    @Test
    void getSellerPostsByCategory_shouldCallServiceCorrectly_WhenUserIdAndCategoryValid() {
        // Arrange
        Integer userId = 100;
        Integer categoryId = 1;
        FollowingPostDto mockResponse = new FollowingPostDto(userId, List.of());

        when(productService.getSellerPostsForUserByCategory(userId, categoryId)).thenReturn(mockResponse);

        // Act
        ResponseEntity<?> response = controller.getSellerPostsByCategory(userId, categoryId);

        // Assert
        verify(productService).getSellerPostsForUserByCategory(userId, categoryId);
        assertNotNull(response);
    }
    @DisplayName("Should call service correctly when given valid userId and order")
    @Test
    void getRecentSellerPostsForUser_shouldCallServiceCorrectly_WhenUserIdAndOrderAreValid() {
        // Arrange
        final Integer validUserId = 100;
        final String sortOrder = "date_asc";
        final FollowingPostDto mockResponse = new FollowingPostDto(validUserId, List.of());

        when(productService.getRecentSellerPostsForUser(validUserId, sortOrder)).thenReturn(mockResponse);

        // Act
        ResponseEntity<?> response = controller.getRecentSellerPostsForUser(validUserId, sortOrder);

        // Assert
        verify(productService).getRecentSellerPostsForUser(validUserId, sortOrder);
        assertNotNull(response);
    }
}