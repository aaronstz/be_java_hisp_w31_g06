package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.dto.ProductDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.service.ProductServiceImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
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

    @Test
    @DisplayName("Should return the posts sorted in ascending order by date.")
    void getRecentSellerPostsForUser_shouldReturnPostsInAscendingOrder_WhenOrderIsDateAsc() {
        // Arrange
        Integer userId = 100;
        String order = "date_asc";

        PostDto post1 = new PostDto();
        LocalDate date1 = LocalDate.of(2023,1,1);
        post1.setDate(date1);

        PostDto post2 = new PostDto();
        LocalDate date2 = LocalDate.of(2023,2,1);
        post2.setDate(date2);

        FollowingPostDto dto = new FollowingPostDto(userId, List.of(post1, post2));

        when(productService.getRecentSellerPostsForUser(userId, order)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = controller.getRecentSellerPostsForUser(userId, order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowingPostDto body = (FollowingPostDto) response.getBody();
        assertNotNull(body);
        assertEquals(userId, body.getUserId());
        assertEquals(List.of(date1, date2),
                body.getPostDto().stream().map(PostDto::getDate).toList());

        verify(productService).getRecentSellerPostsForUser(userId, order);
    }

    @Test
    @DisplayName("Should return the posts sorted in descending order by date.")
    void getRecentSellerPostsForUser_shouldReturnPostsInDescendingOrder_WhenOrderIsDateDesc() {
        // Arrange
        Integer userId = 100;
        String order = "date_desc";

        PostDto post1 = new PostDto();
        LocalDate date1 = LocalDate.of(2023,2,1);
        post1.setDate(date1);

        PostDto post2 = new PostDto();
        LocalDate date2 = LocalDate.of(2023,1,1);
        post2.setDate(date2);

        FollowingPostDto dto = new FollowingPostDto(userId, List.of(post1, post2));

        when(productService.getRecentSellerPostsForUser(userId, order)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = controller.getRecentSellerPostsForUser(userId, order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowingPostDto body = (FollowingPostDto) response.getBody();
        assertNotNull(body);
        assertEquals(userId, body.getUserId());
        assertEquals(List.of(date1, date2),
                body.getPostDto().stream().map(PostDto::getDate).toList());

        verify(productService).getRecentSellerPostsForUser(userId, order);
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

    @DisplayName("Should call service to get seller posts filtered by that keyword when valid inputs are provided")
    @Test
    void getSellerPostsForUserByKeyword_ShouldInvokeServiceAndReturnFilteredPosts_WhenUserIdAndKeywordAreValid() {
        // Arrange
        Integer userId = 100;
        String keyword = "shoes";
        FollowingPostDto expectedResponse = new FollowingPostDto(userId, List.of());

        when(productService.getSellerPostsForUserByKeyword(userId, keyword)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<?> response = controller.getSellerPostsForUserByKeyword(userId, keyword);

        // Assert
        verify(productService, atLeast(1)).getSellerPostsForUserByKeyword(userId, keyword);
        assertNotNull(response);
    }
}