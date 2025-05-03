package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.service.ProductServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductsControllerTests {

    @Mock
    private ProductServiceImpl service;

    @InjectMocks
    private ProductsController controller;

    @Test
    @DisplayName("Debería devolver los posts ordenados de forma ascendente por fecha")
    void getRecentSellerPostsForUser_shouldReturnPostsInAscendingOrder_WhenOrderIsDateAsc() {
        // Arrange
        Integer userId = 100;
        String order = "date_asc";

        PostDto post1 = new PostDto();
        post1.setDate("2023-01-01");

        PostDto post2 = new PostDto();
        post2.setDate("2023-02-01");

        FollowingPostDto dto = new FollowingPostDto(userId, List.of(post1, post2));

        when(service.getRecentSellerPostsForUser(userId, order)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = controller.getRecentSellerPostsForUser(userId, order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowingPostDto body = (FollowingPostDto) response.getBody();
        assertNotNull(body);
        assertEquals(userId, body.getUserId());
        assertEquals(List.of("2023-01-01", "2023-02-01"),
                body.getPostDto().stream().map(PostDto::getDate).toList());

        verify(service).getRecentSellerPostsForUser(userId, order);
    }
    @Test
    @DisplayName("Debería devolver los posts ordenados de forma descendente por fecha")
    void getRecentSellerPostsForUser_shouldReturnPostsInDescendingOrder_WhenOrderIsDateDesc() {
        // Arrange
        Integer userId = 100;
        String order = "date_desc";

        PostDto post1 = new PostDto();
        post1.setDate("2023-02-01");

        PostDto post2 = new PostDto();
        post2.setDate("2023-01-01");

        FollowingPostDto dto = new FollowingPostDto(userId, List.of(post1, post2));

        when(service.getRecentSellerPostsForUser(userId, order)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = controller.getRecentSellerPostsForUser(userId, order);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        FollowingPostDto body = (FollowingPostDto) response.getBody();
        assertNotNull(body);
        assertEquals(userId, body.getUserId());
        assertEquals(List.of("2023-02-01", "2023-01-01"),
                body.getPostDto().stream().map(PostDto::getDate).toList());

        verify(service).getRecentSellerPostsForUser(userId, order);
    }


}
