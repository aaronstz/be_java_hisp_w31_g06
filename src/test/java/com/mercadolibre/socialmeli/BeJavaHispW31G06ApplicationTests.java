package com.mercadolibre.socialmeli;

import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.entity.Post;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.repository.UserRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BeJavaHispW31G06ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepositoryImpl productRepository;

	@Autowired
	private UserRepositoryImpl userRepository;

	@BeforeEach
	void setUp() {
		TestDataFactory.createSixPosts().forEach(productRepository::savePost);
	}

	@DisplayName("Should return only promotional posts when calling /products/promotions successfully")
	@Test
	void getAllPromotions_ShouldReturnOnlyPromoPosts_WhenCalledSuccessfully() throws Exception {
		String responseJson = mockMvc.perform(get("/products/promotions")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", not(empty())))
				.andExpect(jsonPath("$[*].hasPromo", everyItem(is(true))))
				.andExpect(jsonPath("$[*].discount", everyItem(greaterThan(0.0))))
				.andReturn()
				.getResponse()
				.getContentAsString();

		// Assert
		List<PostDto> responseList = TestDataFactory.parsePostDtoList(responseJson);
		assertFalse(responseList.isEmpty());
		assertTrue(responseList.stream().allMatch(p -> p.getHasPromo() && p.getDiscount() > 0));
	}

}
