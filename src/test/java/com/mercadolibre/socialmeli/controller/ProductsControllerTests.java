package com.mercadolibre.socialmeli.controller;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.service.ProductServiceImpl;
import com.mercadolibre.socialmeli.utils.Util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductsControllerTests {

        @Mock
        private ProductServiceImpl service;

        @InjectMocks
        private ProductsController controller;

        @Test
        void getRecentSellerPostsForUserTest() {
                // Arrange
                List<User> users = Util.getSomeUsers();
                User user = users.get(0);

                Set<Integer> followedUserIds = user.getFollowing().stream()
                                .map(Follow::getUserId)
                                .collect(Collectors.toSet());

                List<User> followedUsers = users.stream()
                                .filter(u -> followedUserIds.contains(u.getUserId()))
                                .collect(Collectors.toList());

                List<PostDto> recentPostDtos = new ArrayList<>();
                for (User usuario : followedUsers) {
                        for (Post post : usuario.getPost()) {
                                if (Util.isRecent(post.getDate())) {
                                        recentPostDtos
                                                        .add(new PostDto(post.getUserId(), post.getPostId(),
                                                                        post.getDate(), post.getProduct(),
                                                                        post.getCategory(), post.getPrice(),
                                                                        post.getHasPromo(), post.getDiscount()));
                                }
                        }
                }

                FollowingPostDto expectedResponse = new FollowingPostDto(user.getUserId(), recentPostDtos);

                Mockito.when(service.getRecentSellerPostsForUser(user.getUserId(), "date_asc"))
                                .thenReturn(expectedResponse);

                // Act
                ResponseEntity<?> response = controller.getRecentSellerPostsForUser(user.getUserId(), "date_asc");

                // Assert
                verify(service, atLeast(1)).getRecentSellerPostsForUser(user.getUserId(), "date_asc");
                assertEquals(response.getBody(), expectedResponse);
                assertTrue(response.getStatusCode().is2xxSuccessful());
        }

        @Test
        void getSellerPostsForUserByKeywordTest() {
                // Arrange
                List<User> users = Util.getSomeUsers();
                User user = users.get(0);
                String keyword = "Laptop";

                Set<Integer> followedUserIds = user.getFollowing().stream()
                                .map(Follow::getUserId)
                                .collect(Collectors.toSet());

                List<User> followedUsers = users.stream()
                                .filter(u -> followedUserIds.contains(u.getUserId()))
                                .collect(Collectors.toList());

                List<PostDto> filteredPostDto = new ArrayList<>();
                for (User usuario : followedUsers) {
                        for (Post post : usuario.getPost()) {
                                if (post.getProduct().getProductName().contains(keyword)) {
                                        filteredPostDto
                                                        .add(new PostDto(post.getUserId(), post.getPostId(),
                                                                        post.getDate(),
                                                                        post.getProduct(),
                                                                        post.getCategory(), post.getPrice(),
                                                                        post.getHasPromo(), post.getDiscount()));
                                }
                        }
                }

                FollowingPostDto expectedResponse = new FollowingPostDto(user.getUserId(), filteredPostDto);

                Mockito.when(service.getSellerPostsForUserByKeyword(user.getUserId(), keyword))
                                .thenReturn(expectedResponse);

                // Act
                ResponseEntity<?> response = controller.getSellerPostsForUserByKeyword(user.getUserId(), keyword);

                // Assert
                verify(service, atLeast(1)).getSellerPostsForUserByKeyword(user.getUserId(), keyword);
                assertEquals(response.getBody(), expectedResponse);
                assertTrue(response.getStatusCode().is2xxSuccessful());
        }

}
