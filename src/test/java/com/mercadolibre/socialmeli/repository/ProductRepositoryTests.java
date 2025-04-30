package com.mercadolibre.socialmeli.repository;

import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.entity.Product;
import com.mercadolibre.socialmeli.utils.Util;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private ProductRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        Util.setProductRepositoryForTest(repository);
        Util.createSixPosts().forEach(repository::savePost);
        Util.createSixProducts().forEach(repository::saveProduct);
    }

    @Test
    void getAllPromos_ReturnsOnlyValidPromos() {
        // Act
        List<Post> result = repository.getAllPromos();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(
                p -> Boolean.TRUE.equals(p.getHasPromo()) && p.getDiscount() != null && p.getDiscount() > 0
        ));
    }

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

    @Test
    void saveProduct_WhenProductExists_ShouldReturnFalse() {
        // Arrange
        Product product = Util.createSixProducts().get(0); // Already saved in @BeforeEach

        // Act
        boolean saved = repository.saveProduct(product);

        // Assert
        assertFalse(saved);
    }
}