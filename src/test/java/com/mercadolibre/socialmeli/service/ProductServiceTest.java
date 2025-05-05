package com.mercadolibre.socialmeli.service;

import com.mercadolibre.socialmeli.dto.FollowingPostDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.entity.Follow;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

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

    @Test
    @DisplayName("getRecentSellerPostsForUser Should return the posts sorted in ascending order by date.")
    void getRecentSellerPostsForUser_shouldReturnPostsInAscendingOrder_whenOrderIsAscending() {
        // Arrange
        String order = "date_asc";
        User userExpected = TestDataFactory.createUserWithFollowers();
        Set<Post> recentPosts = userExpected.getPost();
        List<Post> recentPostsList = new ArrayList<>(recentPosts);

        Integer userId = userExpected.getUserId();

        Set<Post> postsForUser300 = new LinkedHashSet<>();
        postsForUser300.add(recentPostsList.get(0));

        Set<Post> postsForUser301 = new LinkedHashSet<>();
        postsForUser301.add(recentPostsList.get(1));
        when(userRepository.findUserById(userId)).thenReturn(userExpected);
        when(userRepository.findRecentPostsForUser(300)).thenReturn(postsForUser300);
        when(userRepository.findRecentPostsForUser(301)).thenReturn(postsForUser301);

        // Act
        FollowingPostDto followingPostDto = service.getRecentSellerPostsForUser(userId, order);
        // Assert
        List<LocalDate> fechasExpected = recentPosts.stream()
                .map(Post::getDate)
                .map(LocalDate::parse)
                .sorted()
                .toList();

        List<LocalDate> fechas = followingPostDto.getPostDto().stream()
                .map(PostDto::getDate)
                .map(LocalDate::parse)
                .toList();

        assertFalse(fechas.isEmpty());
        assertEquals(fechasExpected.size(), fechas.size());
        assertEquals(fechasExpected, fechas);
        verify(userRepository).findUserById(userExpected.getUserId());
        verify(userRepository).findRecentPostsForUser(300);
        verify(userRepository).findRecentPostsForUser(301);
    }

    @Test
    @DisplayName("getRecentSellerPostsForUser Should return the posts sorted in descending order by date.")
    void getRecentSellerPostsForUser_shouldReturnPostsInDescendingOrder_whenOrderIsDescending() {
        // Arrange
        String order = "date_desc";
        User userExpected = TestDataFactory.createUserWithFollowers();
        Set<Post> recentPosts = userExpected.getPost();
        List<Post> recentPostsList = new ArrayList<>(recentPosts);

        Integer userId = userExpected.getUserId();

        Set<Post> postsForUser300 = new LinkedHashSet<>();
        postsForUser300.add(recentPostsList.get(0));

        Set<Post> postsForUser301 = new LinkedHashSet<>();
        postsForUser301.add(recentPostsList.get(1));
        when(userRepository.findUserById(userId)).thenReturn(userExpected);
        when(userRepository.findRecentPostsForUser(300)).thenReturn(postsForUser300);
        when(userRepository.findRecentPostsForUser(301)).thenReturn(postsForUser301);

        // Act
        FollowingPostDto followingPostDto = service.getRecentSellerPostsForUser(userId, order);
        // Assert
        List<LocalDate> fechasExpected = recentPosts.stream()
                .map(Post::getDate)
                .map(LocalDate::parse)
                .sorted(Comparator.reverseOrder())
                .toList();

        List<LocalDate> fechas = followingPostDto.getPostDto().stream()
                .map(PostDto::getDate)
                .map(LocalDate::parse)
                .toList();

        assertFalse(fechas.isEmpty());
        assertEquals(fechasExpected.size(), fechas.size());
        assertEquals(fechasExpected, fechas);
        verify(userRepository).findUserById(userExpected.getUserId());
        verify(userRepository).findRecentPostsForUser(300);
        verify(userRepository).findRecentPostsForUser(301);
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

    @DisplayName("Given a valid user, should return posts from followed sellers during the last two weeks")
    @Test
    void getRecentSellerPostsForUser_ShouldReturnSellerPostsFromLastTwoWeeks_WhenGivenValidUser() {
        List<User> users = TestDataFactory.getSomeUsers();
        User user = users.get(0);

        Set<Integer> followedUserIds = user.getFollowing().stream()
                .map(Follow::getUserId)
                .collect(Collectors.toSet());

        List<User> followedUsers = users.stream()
                .filter(u -> followedUserIds.contains(u.getUserId()))
                .toList();

        List<PostDto> recentPostDtos = new ArrayList<>();
        for (User seguido : followedUsers) {
            for (Post post : seguido.getPost()) {
                if (TestDataFactory.isRecent(post.getDate())) {
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
            when(userRepository.findRecentPostsForUser(followedUser.getUserId()))
                    .thenReturn(followedUser.getPost().stream()
                            .filter(p -> TestDataFactory.isRecent(p.getDate()))
                            .collect(Collectors.toSet()));
        }
        when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act
        FollowingPostDto result = service.getRecentSellerPostsForUser(user.getUserId(), "date_desc");

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }
}