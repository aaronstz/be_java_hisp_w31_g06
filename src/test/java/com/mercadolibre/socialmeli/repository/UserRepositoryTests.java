package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserRepositoryTests {

    private UserRepositoryImpl repository;

    @BeforeEach
    void setUp() throws IOException {
        repository = new TestDataFactory.FakeUserRepositoryImpl();
        TestDataFactory.getSomeUsers().forEach(user -> repository.findAll().add(user));
    }

    @DisplayName("Should return user's posts from the last two weeks when user has posts")
    @Test
    void findRecentPostsForUser_ShouldReturnPostsFromLastTwoWeeks_WhenUserHasPosts() {
        // Arrange
        User user = repository.findAll().get(0);
        Set<Post> expectedResponse = user.getPost().stream().filter(p -> TestDataFactory.isRecent(LocalDate.parse(
                p.getDate())))
                .collect(Collectors.toSet());

        // Act
        Set<Post> response = repository.findRecentPostsForUser(user.getUserId());

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.size(), response.size());
        for (Post post : expectedResponse) {
            assertTrue(response.contains(post));
        }
    }

    @DisplayName("Given a valid user ID, should return the user when the user exists")
    @Test
    void findUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User expectedResponse = repository.findAll().get(0);

        // Act
        User response = repository.findUserById(expectedResponse.getUserId());

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("This test confirms when a user did an unfollow to a user")
    void testRemoveFollow_shouldRemoveFollowFromUser_whenInputsCorrect() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        User userToUnfollow = TestDataFactory.getUserFromId(200);
        Follow follower = new Follow(100, "Mariano Lopez");
        Follow unfollow = new Follow(200, "Guillermo Lopez");

        userToUnfollow.getFollower().add(follower);

        int userFollowersCount = userToUnfollow.getFollowersCount();

        // Act
        repository.removeFollow(user, userToUnfollow);

        // Assertions
        Assertions.assertFalse(userToUnfollow.getFollower().contains(follower));
        Assertions.assertEquals(userFollowersCount - 1, userToUnfollow.getFollowersCount());
        Assertions.assertFalse(user.getFollowing().contains(unfollow));

    }

    @DisplayName("Given a valid user ID and keyword, should return the user's posts that contain the keyword")
    @Test
    void findPostsByKeyword_ShouldReturnUserPostsContainingKeyword_WhenUserIdAndKeywordAreValid() {
        // Arrange
        User user = repository.findAll().get(0);
        String keyword = "Laptop";
        Set<Post> expectedResponse = user.getPost().stream()
                .filter(p -> p.getProduct().getProductName().contains(keyword)).collect(Collectors.toSet());

        // Act
        Set<Post> result = repository.findPostsByKeyword(user.getUserId(), keyword);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @DisplayName("Given a valid user and a non-existent keyword, should return an empty set of posts")
    @Test
    void findPostsByKeyword_ShouldReturnEmptySet_WhenKeywordDoesNotExist() {
        // Arrange
        User user = repository.findAll().get(0);
        String keyword = "palabra clave inexistente!!!";
        Set<Post> expectedResponse = new HashSet<>();

        // Act
        Set<Post> result = repository.findPostsByKeyword(user.getUserId(), keyword);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @DisplayName("Given a valid user and null keyword, should throw NullPointerException")
    @Test
    void findPostsByKeyword_ShouldThrowNullPointerException_WhenKeywordIsNull() {
        // Arrange
        Integer userId = 100;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> repository.findPostsByKeyword(userId, null));
    }

    @Test
    @DisplayName("Should return false when user to follow doesn´t exist")
    void userNotExisting_shouldReturnFalse_whenTryingToFollowUser() {
        // Arrange
        int user = 1;
        int userToFollow = 9999;

        // Act
        boolean response   = repository.isUserAlreadyFollowing(user, userToFollow);

        // Asser
        assertFalse(response);
    }

    @Test
    void testAddPostToUser_shouldReturnTrue_whenUserExists() {
        Post post = TestDataFactory.getPost(100, 9999);

        boolean result = repository.addPostToUser(post);

        assertTrue(result);
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "3"
    })
    @DisplayName("Should return false when user not exist")
    void testAddPostToUser_shouldReturnFalse_whenUserNotExist(int userId) {
        // Arrange
        Post post = TestDataFactory.getPost(userId, 1);

        // Act
        boolean result = repository.addPostToUser(post);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Verifica que la lista de usuarios está limpia antes y despues de utilizar")
    void testClearRepository_shouldNotReturnAnything() {
        // Arrange
        assertFalse(repository.findAll().isEmpty());

        // Act
        repository.clearRepository();

        // Assert
        assertTrue(repository.findAll().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "3"
    })
    @DisplayName("Should return null when user does not exist")
    void testFindPostsByKeyWord_shouldReturnNull_whenUserDontExist(int userId) {

        // Act
        Set<Post> post = repository.findPostsByKeyword(userId, "");

        // Assert
        assertNull(post);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, 3",
    })
    @DisplayName("Should return false when user does not exist")
    void testIsFollowing_shouldReturnFalse_whenUserNotExist(int userId, int userIdToFollow){
        // Act
        boolean response = repository.isFollowing(userId, userIdToFollow);

        // Assert
        assertFalse(response);
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "3",
    })
    @DisplayName("Should return false when user does not exist")
    void testFindRecentPostsForUser_shouldReturnFalse_whenUserNotExist(int userId){
        // Act
        Set<Post> response = repository.findRecentPostsForUser(userId);

        // Assert
        assertNull(response);
    }
}
