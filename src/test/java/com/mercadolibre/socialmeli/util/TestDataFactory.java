package com.mercadolibre.socialmeli.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.Follow;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import com.mercadolibre.socialmeli.entity.User;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;

public class TestDataFactory {

        private static final ObjectMapper mapper = new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);

        public static List<PostDto> parsePostDtoList(String json) throws IOException {
                return mapper.readValue(json, new TypeReference<>() {
                });
        }

        public static User createUserWithFollowers() {
                List<Post> posts = createSixPosts();

                User mainUser = new User(100, "Mariano Lopez", 2, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(0), posts.get(1)));

                User follower1 = new User(200, "Guillermo Lopez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(2)));

                User follower2 = new User(201, "Mariana Gimenez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(3)));

                User following1 = new User(300, "Franco Fernandez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(4)));

                User following2 = new User(301, "Miranda Miranda", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(5)));

                Follow follow1 = new Follow(follower1.getUserId(), follower1.getUserName());
                Follow follow2 = new Follow(follower2.getUserId(), follower2.getUserName());
                Follow followingFollow1 = new Follow(following1.getUserId(), following1.getUserName());
                Follow followingFollow2 = new Follow(following2.getUserId(), following2.getUserName());

                Set<Follow> followers = new HashSet<>(Arrays.asList(follow1, follow2));
                Set<Follow> followings = new HashSet<>(Arrays.asList(followingFollow1, followingFollow2));

                mainUser.setFollower(followers);
                mainUser.setFollowing(followings);

                return mainUser;
        }

        public static List<Product> createSixProducts() {
                List<Product> products = new ArrayList<Product>();

                Product product1 = new Product(1, "Laptop Inspiron", "Electronics", "Dell", "Negro",
                                "Laptop para gaming");
                products.add(product1);

                Product product2 = new Product(2, "Smartphone Galaxy", "Electronics", "Samsung", "Azul",
                                "Smartphone de alta gama");
                products.add(product2);

                Product product3 = new Product(3, "Tablet iPad", "Electronics", "Apple", "Plata",
                                "Tablet con tecnología avanzada");
                products.add(product3);

                Product product4 = new Product(4, "Auriculares Noise Cancelling", "Audio", "Sony", "Negro",
                                "Auriculares con cancelación de ruido");
                products.add(product4);

                Product product5 = new Product(5, "Smartwatch Series", "Wearables", "Apple", "Blanco",
                                "Reloj inteligente con múltiples funciones");
                products.add(product5);

                Product product6 = new Product(6, "Libro 'El Quijote'", "Literatura", "Planeta", "Rojo",
                                "Edición ilustrada");
                products.add(product6);

                return products;
        }

        public static List<Post> createSixPosts() {
                List<Product> products = createSixProducts();
                List<Post> posts = new ArrayList<Post>();

                posts.add(new Post(100, 1, "2023-10-10", products.get(0), 1, 1000.0, false, 0.0));
                posts.add(new Post(100, 2, "2025-04-29", products.get(1), 1, 500.0, true, 5.0));
                posts.add(new Post(200, 3, "2025-04-29", products.get(2), 2, 700.0, false, 0.0));
                posts.add(new Post(201, 4, "2023-10-13", products.get(3), 1, 200.0, false, 0.0));
                posts.add(new Post(300, 5, "2025-04-29", products.get(4), 3, 300.0, true, 10.0));
                posts.add(new Post(301, 6, "2023-10-15", products.get(5), 2, 60.0, false, 0.0));
                return posts;
        }

        public static boolean isRecent(String dateStr) {
                try {
                        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                        LocalDate today = LocalDate.now();
                        LocalDate twoWeeksAgo = today.minusWeeks(2);
                        return !date.isBefore(twoWeeksAgo) && !date.isAfter(today);
                } catch (DateTimeParseException e) {
                        return false;
                }
        }

        public static List<User> getSomeUsers() {
                List<Post> posts = createSixPosts();

                User mainUser = new User(100, "Mariano Lopez", 2, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(0), posts.get(1)));

                User follower1 = new User(200, "Guillermo Lopez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(2)));

                User follower2 = new User(201, "Mariana Gimenez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(3)));

                User following1 = new User(300, "Franco Fernandez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(4)));

                User following2 = new User(301, "Miranda Miranda", 0, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(5)));

                Follow follow1 = new Follow(follower1.getUserId(), follower1.getUserName());
                Follow follow2 = new Follow(follower2.getUserId(), follower2.getUserName());
                Follow followingFollow1 = new Follow(following1.getUserId(), following1.getUserName());
                Follow followingFollow2 = new Follow(following2.getUserId(), following2.getUserName());

                Set<Follow> followers = new HashSet<>(Arrays.asList(follow1, follow2));
                Set<Follow> followings = new HashSet<>(Arrays.asList(followingFollow1, followingFollow2));

                mainUser.setFollower(followers);
                mainUser.setFollowing(followings);

                return List.of(mainUser, follower1, follower2, following1, following2);
        }

        public static class FakeUserRepositoryImpl extends UserRepositoryImpl {
                public FakeUserRepositoryImpl() throws IOException {
                        super();
                        findAll().clear();
                }

                public void preloadUsers(List<User> users) {
                        findAll().addAll(users);
                }
        }

        public static User getUserFromId(int userId) {
                List<Post> posts = createSixPosts();
                return new User(userId, "Mariano Lopez", 2, new HashSet<>(), new HashSet<>(),
                                Set.of(posts.get(0), posts.get(1)));
        }

        public static class FakeProductRepositoryImpl extends ProductRepositoryImpl {
                public FakeProductRepositoryImpl() throws IOException {
                        super();
                        findAllProducts().clear();
                        findAllPosts().clear();
                }
        }

        public static void preloadUserFollowingSellerWithPost(UserRepositoryImpl userRepository,
                        ProductRepositoryImpl productRepository, Integer buyerId, Integer sellerId,
                        Integer categoryId) {
                Product testProduct = new Product(
                                99, "Producto Test", "Electronics",
                                "Marca Test", "Negro", "Descripción Test");
                productRepository.saveProduct(testProduct);

                Post testPost = new Post(
                                sellerId,
                                999,
                                LocalDate.now().toString(),
                                testProduct,
                                categoryId,
                                1500.0,
                                true,
                                10.0);
                productRepository.savePost(testPost);

                User seller = new User(
                                sellerId,
                                "Seller User",
                                0,
                                new HashSet<>(),
                                new HashSet<>(),
                                new HashSet<>(Set.of(testPost)));
                userRepository.findAll().add(seller);

                User buyer = new User(
                                buyerId,
                                "Test Buyer",
                                2,
                                new HashSet<>(),
                                new HashSet<>(Set.of(new Follow(sellerId, "Seller User"))),
                                new HashSet<>());
                userRepository.findAll().add(buyer);
        }

        public static Set<Follow> getFollowList() {
                Follow follower1 = new Follow(100, "Tralalero Tralala");
                Follow follower2 = new Follow(200, "Armando Casas");
                Follow follower3 = new Follow(300, "Esteban Quito");
                Follow follower4 = new Follow(400, "Jacky Sieras");

                return new HashSet<>(Arrays.asList(follower1, follower2, follower3, follower4));
        }

        public static User createUserWithoutFollowersOrFollowing() {
                List<Post> posts = createSixPosts();

                User mainUser = new User(100, "Mariano Lopez", 2, new HashSet<Follow>(), new HashSet<Follow>(),
                                Set.of(posts.get(0), posts.get(1)));

                mainUser.setFollower(new HashSet<>());
                mainUser.setFollowing(new HashSet<>());

                return mainUser;
        }
}
