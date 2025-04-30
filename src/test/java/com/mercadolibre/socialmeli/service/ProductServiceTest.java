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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryImpl productRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void getRecentSellerPostsForUserTest() {
        List<User> users = Util.getSomeUsers();
        User user = users.get(0);

        Set<Integer> followedUserIds = user.getFollowing().stream()
                .map(Follow::getUserId)
                .collect(Collectors.toSet());

        List<User> followedUsers = users.stream()
                .filter(u -> followedUserIds.contains(u.getUserId()))
                .collect(Collectors.toList());

        List<PostDto> recentPostDtos = new ArrayList<>();
        for (User seguido : followedUsers) {
            for (Post post : seguido.getPost()) {
                if (Util.isRecent(post.getDate())) {
                    recentPostDtos
                            .add(new PostDto(post.getUserId(), post.getPostId(), post.getDate(), post.getProduct(),
                                    post.getCategory(), post.getPrice(), post.getHasPromo(), post.getDiscount()));
                }
            }
        }

        Collections.sort(recentPostDtos, (p1, p2) -> {
            LocalDate date1 = LocalDate.parse(p1.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate date2 = LocalDate.parse(p2.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            return date2.compareTo(date1);
        });

        FollowingPostDto expectedResponse = new FollowingPostDto(user.getUserId(), recentPostDtos);

        for (User followedUser : followedUsers) {
            Mockito.when(userRepository.findRecentPostsForUser(followedUser.getUserId()))
                    .thenReturn(followedUser.getPost().stream()
                            .filter(p -> Util.isRecent(p.getDate()))
                            .collect(Collectors.toSet()));
        }
        Mockito.when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act
        FollowingPostDto result = service.getRecentSellerPostsForUser(user.getUserId(), "date_desc");

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    void getRecentSellerPostsForUserSadPathI() {
        // Arrange
        List<User> users = Util.getSomeUsers();
        User user = users.get(0);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> service.getRecentSellerPostsForUser(user.getUserId(), null));
    }

    @Test
    void getRecentSellerPostsForUserSadPathII() {
        // Arrange
        List<User> users = Util.getSomeUsers();
        User user = users.get(0);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> service.getRecentSellerPostsForUser(user.getUserId(), "invalid"));
    }

    @Test
    void getRecentSellerPostsForUserSadPathIII() {
        // Arrange
        Integer userId = null;

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.getRecentSellerPostsForUser(userId, "date_desc"));
    }
}
