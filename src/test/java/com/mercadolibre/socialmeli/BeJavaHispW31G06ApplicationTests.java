package com.mercadolibre.socialmeli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.socialmeli.dto.FollowerCountDto;
import com.mercadolibre.socialmeli.dto.PostDto;
import com.mercadolibre.socialmeli.repository.ProductRepositoryImpl;
import com.mercadolibre.socialmeli.util.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BeJavaHispW31G06ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepositoryImpl productRepository;

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


	@DisplayName("getFollowersCount should return the number of followers of the user when the input data is valid")
	@Test
	public void getFollowersCount_shouldReturnCorrectFollowerCount_whenInputIsValid() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/users/1/followers/count"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.followersCount").value(2))
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();
		assertNotNull(responseBody);

		ObjectMapper objectMapper = new ObjectMapper();
		FollowerCountDto responseDto = objectMapper.readValue(responseBody, FollowerCountDto.class);

		assertTrue(responseDto.getFollowersCount() >= 0);
		Assertions.assertEquals(2, responseDto.getFollowersCount());
	}


}
