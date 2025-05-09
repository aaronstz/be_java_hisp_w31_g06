package com.mercadolibre.socialmeli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.*;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.exception.BadRequestException;
import com.mercadolibre.socialmeli.exception.ConflictException;
import com.mercadolibre.socialmeli.exception.NotFoundException;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private ProductRepositoryImpl productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    @Mock
    private ObjectMapper mapper;

    private CreatePostDto createPostDto;
    private Post post;
    private PostDto postDto;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(1);
        product = new Product();
        product.setProductId(12345);

        createPostDto = new CreatePostDto();
        createPostDto.setUserId(user.getUserId());
        createPostDto.setProduct(product);
        createPostDto.setCategory(2);
        createPostDto.setPrice(1000.0);
        createPostDto.setHasPromo(false);
        createPostDto.setDiscount(5.0);

        post = new Post();
        post.setUserId(user.getUserId());
        post.setProduct(product);
        post.setCategory(2);
        post.setPrice(1000.0);
        post.setHasPromo(false);
        post.setDiscount(5.0);

        postDto = new PostDto();
        postDto.setUserId(user.getUserId());
        postDto.setPostId(99);
        postDto.setProduct(product);
        postDto.setCategory(2);
        postDto.setPrice(1000.0);
        postDto.setHasPromo(null);
        postDto.setDiscount(null);
    }

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
    void getRecentSellerPostsForUser_NullOrder_ThrowsNotFoundException() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        lenient().when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.getRecentSellerPostsForUser(user.getUserId(), null));

        assertEquals("El orden no puede estar vacío", exception.getMessage());
    }

    @DisplayName("Should throw BadRequestException when order is null")
    @Test
    void getRecentSellerPostsForUser_BadOrder_ThrowsBadRequestException() {
        // Arrange
        User user = TestDataFactory.createUserWithFollowers();
        String badOrder = "bad order";
        lenient().when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> service.getRecentSellerPostsForUser(user.getUserId(), badOrder));

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
                if (TestDataFactory.isRecent(LocalDate.parse(post.getDate()))) {
                    recentPostDtos
                            .add(new PostDto(post.getUserId(), post.getPostId(), LocalDate.parse(post.getDate()),
                                    post.getProduct(), post.getCategory(), post.getPrice(), post.getHasPromo(),
                                    post.getDiscount()));
                }
            }
        }

        Collections.sort(recentPostDtos, (p1, p2) -> {
            LocalDate date1 = p1.getDate();
            LocalDate date2 = p2.getDate();
            return date2.compareTo(date1);
        });

        FollowingPostDto expectedResponse = new FollowingPostDto(user.getUserId(), recentPostDtos);

        for (User followedUser : followedUsers) {
            when(userRepository.findRecentPostsForUser(followedUser.getUserId()))
                    .thenReturn(followedUser.getPost().stream()
                            .filter(p -> TestDataFactory.isRecent(LocalDate.parse(p.getDate())))
                            .collect(Collectors.toSet()));
        }
        when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act
        FollowingPostDto result = service.getRecentSellerPostsForUser(user.getUserId(), "date_desc");

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @DisplayName("Given a valid user and keyword, should return seller posts from followed sellers in the last two weeks that contain the keyword")
    @Test
    void getSellerPostsForUserByKeyword_ShouldReturnSellerPostsFromFollowedSellersInLastTwoWeeksContainingKeyword_WhenGivenValidUserAndKeyword() {
        // Arrange
        List<User> users = TestDataFactory.getSomeUsers();
        String keyword = "Smartwatch";

        Set<Integer> followedUserIds = users.get(0).getFollowing().stream().map(Follow::getUserId)
                .collect(Collectors.toSet());

        List<User> followedUsers = users.stream().filter(u -> followedUserIds.contains(u.getUserId())).toList();

        List<PostDto> postWithKeyword = new ArrayList<>();
        for (User seguido : followedUsers) {
            Set<Post> posts = new HashSet<>();
            for (Post post : seguido.getPost()) {
                if (post.getProduct().getProductName().contains(keyword)) {
                    postWithKeyword
                            .add(new PostDto(post.getUserId(), post.getPostId(), LocalDate.parse(post.getDate()), post.getProduct(),
                                    post.getCategory(), post.getPrice(), post.getHasPromo(), post.getDiscount()));
                    posts.add(post);
                }
            }
            when(userRepository.findPostsByKeyword(seguido.getUserId(), keyword)).thenReturn(posts);
        }

        FollowingPostDto expectedResponse = new FollowingPostDto(users.get(0).getUserId(), postWithKeyword);
        when(userRepository.findUserById(users.get(0).getUserId())).thenReturn(users.get(0));

        // Act
        FollowingPostDto response = service.getSellerPostsForUserByKeyword(users.get(0).getUserId(), keyword);

        // Assert
        assertNotNull(expectedResponse);
        assertEquals(expectedResponse, response);
    }

    @DisplayName("Given a valid user and a non-existent keyword, should throw NotFoundException when retrieving seller posts")
    @Test
    void getSellerPostsForUserByKeyword_ShouldThrowNotFoundException_WhenKeywordDoesNotExist() {
        // Arrange
        List<User> users = TestDataFactory.getSomeUsers();
        User user = users.get(0);
        String keyword = "Palabra clave inexistente!!!";

        Set<Integer> followedUserIds = user.getFollowing().stream().map(Follow::getUserId).collect(Collectors.toSet());
        List<User> followedUsers = users.stream().filter(u -> followedUserIds.contains(u.getUserId())).toList();

        followedUsers.forEach(seguido -> when(userRepository.findPostsByKeyword(seguido.getUserId(), keyword))
                .thenReturn(new HashSet<>()));

        when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> service.getSellerPostsForUserByKeyword(user.getUserId(), keyword));
    }

    @DisplayName("Given a valid user with no followed sellers and a valid keyword, should throw NotFoundException when retrieving seller posts")
    @Test
    void getSellerPostsForUserByKeyword_ShouldThrowNotFoundException_WhenUserDoesNotFollowAnySeller() {
        // Arrange
        List<User> users = new ArrayList<>(TestDataFactory.getSomeUsers());
        User user = TestDataFactory.createUserWithoutFollowersOrFollowing();
        users.add(user);
        String keyword = "Smartwatch";

        when(userRepository.findUserById(user.getUserId())).thenReturn(user);

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> service.getSellerPostsForUserByKeyword(user.getUserId(), keyword));
    }

    @Test
    @DisplayName("Should throw NotFoundException when recent seller posts are not found for a user.")
    void getRecentSellerPostsForUser_shouldReturn_NotFoundException() {
        Integer badId = 2391;
        String order = "date_desc";

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.getRecentSellerPostsForUser(badId, order));

        assertEquals("Usuario no encontrado con ID: " + badId, ex.getMessage());
    }

    @Test
    void getRecentSellerPostsForUser_shouldReturn_NotFoundExceptionByNotFollowingList() {
        // Arrange
        User user1 = TestDataFactory.getUserFromId(999);
        String order = "date_asc";

        String response = "No se encontraron seguidos para el usuario con ID: " + user1.getUserId();

        // Act
        when(userRepository.findUserById(user1.getUserId())).thenReturn(user1);
        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class,
                () -> service.getRecentSellerPostsForUser(user1.getUserId(), order));

        // Asser
        Assertions.assertEquals(response, thrown.getMessage());
    }



    @Test
    @DisplayName("Should throw NotFoundException when the user is not found while getting promo post count.")
    void testGetPromoPostCount_UserNotFound() {
        Integer userId = 1;
        when(userRepository.findUserById(userId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getPromoPostCount(userId);
        });
        assertEquals("No se encontró un usuario con ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw NotFoundException when there are no posts for the user while getting promo post count.")
    void testGetPromoPostCount_NoPosts() {
        Integer userId = 1;
        User user = new User();
        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userRepository.findRecentPostsForUser(userId)).thenReturn(Collections.emptySet());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getPromoPostCount(userId);
        });
        assertEquals("No se encontraron publicaciones para el usuario con ID 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should get promo post count correctly.")
    void testGetPromoPostCount_WithPromoPosts() {
        Integer userId = 1;
        User user = new User();
        user.setUserName("User1");
        Set<Post> posts = new HashSet<>();
        Post post1 = new Post();
        post1.setHasPromo(true);
        posts.add(post1);

        when(userRepository.findUserById(userId)).thenReturn(user);
        when(userRepository.findRecentPostsForUser(userId)).thenReturn(posts);

        PromoPostCountDto result = service.getPromoPostCount(userId);
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("User1", result.getUserName());
        assertEquals(1, result.getPromoProductsCount());
    }

    @Test
    @DisplayName("Should throw NotFoundException when the user is not found while getting promos by seller.")
    void testGetPromosBySeller_UserNotFound() {
        Integer userId = 1;
        when(userRepository.findUserById(userId)).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getPromosBySeller(userId);
        });
        assertEquals("No se encontró el usuario con id 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw NotFoundException when there are no promos for the seller.")
    void testGetPromosBySeller_NoPromos() {
        Integer userId = 1;
        User user = new User();
        when(userRepository.findUserById(userId)).thenReturn(user);
        when(productRepository.findPromosBySeller(userId)).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getPromosBySeller(userId);
        });
        assertEquals("No se encontraron promociones del vendedor con id 1", exception.getMessage());
    }

    @Test
    @DisplayName("Should get promos by seller correctly.")
    void testGetPromosBySeller_WithPromos() {
        Integer userId = 1;
        User user = new User();
        user.setUserName("Seller1");

        Post promoPost = new Post();
        List<Post> promoPosts = new ArrayList<>();
        promoPosts.add(promoPost);

        when(userRepository.findUserById(userId)).thenReturn(user);
        when(productRepository.findPromosBySeller(userId)).thenReturn(promoPosts);

        PromoPostDto result = service.getPromosBySeller(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("Seller1", result.getUserName());
        assertEquals(1, result.getPosts().size());
    }

    @Test
    @DisplayName("Should throw NotFoundException when no products are found while getting all.")
    void testGetAll_GetProducts() {
        List<ProductDto> expected = TestDataFactory.createSixProductsDto();
        List<Product> productList = TestDataFactory.createSixProducts();

        when(productRepository.findAllProducts()).thenReturn(productList);

        List<ProductDto> response = service.getAll();

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throw NotFoundException when no products are found while getting all.")
    void testGetAll_NoProducts() {
        when(productRepository.findAllProducts()).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.getAll();
        });
        assertEquals("No se encontraron productos.", exception.getMessage());
    }

    @Test
    @DisplayName("Should create a post correctly.")
    void testCreatePost_Success() {
        when(productRepository.saveProduct(post.getProduct())).thenReturn(true);
        when(userRepository.addPostToUser(any(Post.class))).thenReturn(true);
        doNothing().when(productRepository).savePost(post);
        when(productRepository.findAllPosts()).thenReturn(Collections.singletonList(post));

        PostDto result = service.createPost(createPostDto);
        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals(Integer.valueOf(2), result.getPostId());
        verify(productRepository, times(1)).saveProduct(post.getProduct());
        verify(userRepository, atLeastOnce()).addPostToUser(any(Post.class));
    }

    @Test
    @DisplayName("Should throw ConflictException if the product already exists while creating a post.")
    void testCreatePost_ProductAlreadyExists_ThrowsConflictException() {
        when(productRepository.saveProduct(post.getProduct())).thenReturn(false);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            service.createPost(createPostDto);
        });
        assertEquals("Ya existe un producto con el id 12345", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw NotFoundException if the user is not found while creating a post.")
    void testCreatePost_UserNotFound_ThrowsNotFoundException() {
        when(productRepository.saveProduct(post.getProduct())).thenReturn(true);
        when(userRepository.addPostToUser(post)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            service.createPost(createPostDto);
        });
        assertEquals("No se encontró al usuario", exception.getMessage());
    }

    @Test
    void testGetAllPromos_shouldReturnNotFoundException_whenPromosNotExist() {
        // Arrange
        String expected = "No se encontraron promociones";

        // Act
        when(productRepository.getAllPromos()).thenReturn(new ArrayList<>());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> service.getAllPromos());

        // Assert
        assertEquals(expected, thrown.getMessage());
        verify(productRepository, atLeast(1)).getAllPromos();
    }
}