package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTests {

    private ProductRepositoryImpl repository;

    @BeforeEach
    void setUp() throws IOException {
        repository = new TestDataFactory.FakeProductRepositoryImpl();
        TestDataFactory.createSixPosts().forEach(repository::savePost);
        TestDataFactory.createSixProducts().forEach(repository::saveProduct);
    }
    @DisplayName("Should return only posts with valid promotions when promotions exist")
    @Test
    void getAllPromos_shouldReturnValidPromos_WhenPostHavePromotions() {
        // Act
        List<Post> result = repository.getAllPromos();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(
                p -> Boolean.TRUE.equals(p.getHasPromo()) && p.getDiscount() != null && p.getDiscount() > 0
        ));
    }
    @DisplayName("Should return seller's promotional posts when promotions exist for given user")
    @Test
    void findPromosBySeller_ReturnsOnlySellerPromos() {
        // Arrange
        Integer userId = 100;

        // Act
        List<Post> result = repository.findPromosBySeller(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.stream().allMatch(
                p -> p.getUserId().equals(userId) && Boolean.TRUE.equals(p.getHasPromo()) && p.getDiscount() > 0
        ));
    }
    @DisplayName("Should return empty list when seller has no promotional posts")
    @Test
    void findPromosBySeller_NoPromos_ReturnsEmptyList() {
        // Arrange
        Integer userId = 201;

        // Act
        List<Post> result = repository.findPromosBySeller(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @DisplayName("Should save a new product and return true when product doesn't exist")
    @Test
    void saveProduct_WhenNewProduct_ShouldReturnTrue() {
        // Arrange
        Product product = new Product(99, "Test Product", "Category", "Brand", "Color", "Notes");

        // Act
        boolean saved = repository.saveProduct(product);

        // Assert
        assertTrue(saved);
        assertTrue(repository.findAllProducts().contains(product));
    }
    @DisplayName("Should return false when trying to save an already existing product")
    @Test
    void saveProduct_WhenProductExists_ShouldReturnFalse() {
        // Arrange
        Product product = TestDataFactory.createSixProducts().get(0);

        // Act
        boolean saved = repository.saveProduct(product);

        // Assert
        assertFalse(saved);
    }
}