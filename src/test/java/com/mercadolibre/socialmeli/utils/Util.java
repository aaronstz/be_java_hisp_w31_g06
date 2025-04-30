//package com.mercadolibre.socialmeli.utils;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import com.mercadolibre.socialmeli.entity.Follow;
//import com.mercadolibre.socialmeli.entity.Post;
//import com.mercadolibre.socialmeli.entity.Product;
//import com.mercadolibre.socialmeli.entity.User;
//import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
//import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
//
//public class Util {
//
//    public static void setUserRepositoryForTest(UserRepositoryImpl userRepository) {
//        userRepository.clearRepository();
//    }
//
//    public static void setProductRepositoryForTest(ProductRepositoryImpl productRepository) {
//        productRepository.clearRepository();
//    }
//
//    public static User createUserWithFollowers() {
//        List<Post> posts = createSixPosts();
//
//        User mainUser = new User(100, "Mariano Lopez", 2, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(0), posts.get(1)));
//
//        User follower1 = new User(200, "Guillermo Lopez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(2)));
//
//        User follower2 = new User(201, "Mariana Gimenez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(3)));
//
//        User following1 = new User(300, "Franco Fernandez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(4)));
//
//        User following2 = new User(301, "Miranda Miranda", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(5)));
//
//        Follow follow1 = new Follow(follower1.getUserId(), follower1.getUserName());
//        Follow follow2 = new Follow(follower2.getUserId(), follower2.getUserName());
//        Follow followingFollow1 = new Follow(following1.getUserId(), following1.getUserName());
//        Follow followingFollow2 = new Follow(following2.getUserId(), following2.getUserName());
//
//        Set<Follow> followers = new HashSet<>(Arrays.asList(follow1, follow2));
//        Set<Follow> followings = new HashSet<>(Arrays.asList(followingFollow1, followingFollow2));
//
//        mainUser.setFollower(followers);
//        mainUser.setFollowing(followings);
//
//        return mainUser;
//    }
//
//    public static List<Product> createSixProducts() {
//        List<Product> products = new ArrayList<Product>();
//
//        Product product1 = new Product(1, "Laptop Inspiron", "Electronics", "Dell", "Negro", "Laptop para gaming");
//        products.add(product1);
//
//        Product product2 = new Product(2, "Smartphone Galaxy", "Electronics", "Samsung", "Azul",
//                "Smartphone de alta gama");
//        products.add(product2);
//
//        Product product3 = new Product(3, "Tablet iPad", "Electronics", "Apple", "Plata",
//                "Tablet con tecnología avanzada");
//        products.add(product3);
//
//        Product product4 = new Product(4, "Auriculares Noise Cancelling", "Audio", "Sony", "Negro",
//                "Auriculares con cancelación de ruido");
//        products.add(product4);
//
//        Product product5 = new Product(5, "Smartwatch Series", "Wearables", "Apple", "Blanco",
//                "Reloj inteligente con múltiples funciones");
//        products.add(product5);
//
//        Product product6 = new Product(6, "Libro 'El Quijote'", "Literatura", "Planeta", "Rojo", "Edición ilustrada");
//        products.add(product6);
//
//        return products;
//    }
//
//    public static List<Post> createSixPosts() {
//        List<Product> products = createSixProducts();
//        List<Post> posts = new ArrayList<Post>();
//
//        posts.add(new Post(100, 1, "2023-10-10", products.get(0), 1, 1000.0, false, 0.0));
//        posts.add(new Post(100, 2, "2025-04-29", products.get(1), 1, 500.0, true, 5.0));
//        posts.add(new Post(200, 3, "2025-04-29", products.get(2), 2, 700.0, false, 0.0));
//        posts.add(new Post(201, 4, "2023-10-13", products.get(3), 1, 200.0, false, 0.0));
//        posts.add(new Post(300, 5, "2025-04-29", products.get(4), 3, 300.0, true, 10.0));
//        posts.add(new Post(301, 6, "2023-10-15", products.get(5), 2, 60.0, false, 0.0));
//
//        return posts;
//    }
//
//    public static boolean isRecent(String dateStr) {
//        try {
//            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
//            LocalDate today = LocalDate.now();
//            LocalDate twoWeeksAgo = today.minusWeeks(2);
//            return !date.isBefore(twoWeeksAgo) && !date.isAfter(today);
//        } catch (DateTimeParseException e) {
//            return false;
//        }
//    }
//
//    public static List<User> getSomeUsers() {
//        List<Post> posts = createSixPosts();
//
//        User mainUser = new User(100, "Mariano Lopez", 2, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(0), posts.get(1)));
//
//        User follower1 = new User(200, "Guillermo Lopez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(2)));
//
//        User follower2 = new User(201, "Mariana Gimenez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(3)));
//
//        User following1 = new User(300, "Franco Fernandez", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(4)));
//
//        User following2 = new User(301, "Miranda Miranda", 0, new HashSet<Follow>(), new HashSet<Follow>(),
//                Set.of(posts.get(5)));
//
//        Follow follow1 = new Follow(follower1.getUserId(), follower1.getUserName());
//        Follow follow2 = new Follow(follower2.getUserId(), follower2.getUserName());
//        Follow followingFollow1 = new Follow(following1.getUserId(), following1.getUserName());
//        Follow followingFollow2 = new Follow(following2.getUserId(), following2.getUserName());
//
//        Set<Follow> followers = new HashSet<>(Arrays.asList(follow1, follow2));
//        Set<Follow> followings = new HashSet<>(Arrays.asList(followingFollow1, followingFollow2));
//
//        mainUser.setFollower(followers);
//        mainUser.setFollowing(followings);
//
//        return List.of(mainUser, follower1, follower2, following1, following2);
//    }
//}