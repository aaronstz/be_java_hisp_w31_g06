package com.mercadolibre.socialmeli.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.utils.Util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        Util.setProductRepositoryForTest(productRepository);
        Util.createSixPosts().forEach(productRepository::savePost);
    }

    @Test
    void getAllPromotions_ShouldReturnPromoPostsOnly() throws Exception {
        String responseJson = mockMvc.perform(get("/products/promotions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].hasPromo", everyItem(is(true))))
                .andExpect(jsonPath("$[*].discount", everyItem(greaterThan(0.0))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PostDto> responseList = Util.parsePostDtoList(responseJson);

        assertFalse(responseList.isEmpty());
        assertTrue(responseList.stream().allMatch(p -> p.getHasPromo() && p.getDiscount() > 0));
    }
}